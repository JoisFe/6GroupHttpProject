package com.nhnacademy.group6Project;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//< HTTP/1.1 200 OK
//< Date: Thu, 21 Apr 2022 08:41:37 GMT
//< Content-Type: application/json
//< Content-Length: 254
//< Connection: keep-alive
//< Server: gunicorn/19.9.0
//< Access-Control-Allow-Origin: *
//< Access-Control-Allow-Credentials: true
//<
//{
//  "args": {},
//  "headers": {
//    "Accept": "*/*",
//    "Host": "httpbin.org",
//    "User-Agent": "curl/7.79.1",
//    "X-Amzn-Trace-Id": "Root=1-626118c1-04208d911d9c9f45249dd141"
//    },
//    "origin": "112.216.11.34",
//    "url


public class ScurlServer {
    public static void main(String[] args) {

        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(80); //

            socket = serverSocket.accept(); // 나중에 소켓 반납 ?

            PrintStream out = new PrintStream(socket.getOutputStream());

            InputStream in = socket.getInputStream();

//            BufferedReader in = new BufferedReader(new InputStreamReader(new DataInputStream(inputStream)));


//            String data = "";
//            String str = null;


//            List<Byte> listByte = new ArrayList<>();
//
//            int x = 0;
//            while ((x = inputStream.read()) != 0) {
//                listByte.add((byte) x);
//                System.out.println(x);
//            }

            byte[] byteArr = new byte[2048];
            int readByteCount = in.read(byteArr);
            String data = new String(byteArr, 0, readByteCount, "UTF-8");

            String[] requestMessage = data.split("\r\n\r\n");
            String requestMessageHead = requestMessage[0];
            String requestMessageBody = null;
            if (requestMessage.length == 2) {
                requestMessageBody = requestMessage[1];
            }

// -------------------- Response Message를 만듬
            String stateCode = "200 OK"; // redirection 및 400 터트려야할 때는 바꾸어야 하니 나중에 조건문 달아서 바꿔주자

            String contentType = "application/json";

            String responseMessageHead;
            PostResponseMessageBody postResponseMessageBody = new PostResponseMessageBody();
            GetResponseMessageBody getResponseMessageBody = new GetResponseMessageBody();

            //// 헤더 넣기
            String[] requestMassageLine = requestMessageHead.split(System.lineSeparator());

            for (String s : requestMassageLine) {
                String[] headResource = s.split(":");
                if (headResource.length != 2) {
                    continue;
                }

                if (requestMessageBody != null) {
                    postResponseMessageBody.getHeaders().put(headResource[0], headResource[1].substring(1, headResource[1].length() - 1));
                } else {
                    getResponseMessageBody.getHeaders().put(headResource[0], headResource[1].substring(1, headResource[1].length() - 1));
                }
            }
            
            // Serialize 해보자-------------------------------------------------

            String responseBodyJson;

            int contentLength = 0;

            if (requestMessageBody != null) {
                JsonToMap jsonToMap = new JsonToMap(requestMessageBody);
                Map<String, String> jsonMap = jsonToMap.parsing();

                postResponseMessageBody.setData(jsonMap);
                postResponseMessageBody.setJson(jsonMap);

                JsonSerialization jsonSerialization = new JsonSerialization(postResponseMessageBody);;
                responseBodyJson = jsonSerialization.serialize();
            } else {
                String queryResource = requestMessageHead.split("\n")[0];
                String queryString = queryResource.substring(queryResource.lastIndexOf('?') + 1, queryResource.lastIndexOf(' '));


                String[] queryParameters = queryString.split("&");

                for (String queryParameter : queryParameters) {
                    String[] parameter = queryParameter.split("=");

                    getResponseMessageBody.getArgs().put(parameter[0], parameter[1]);
                }

                JsonSerialization jsonSerialization = new JsonSerialization(getResponseMessageBody);;
                responseBodyJson = jsonSerialization.serialize();
            }


            responseMessageHead =
                "HTTP/1.1 " + stateCode + System.lineSeparator()
                + "content-length: " + contentLength + System.lineSeparator()
                + "content-type: " + contentType + System.lineSeparator()
                + "connection: keep-alive" + System.lineSeparator()
                + "server: 6Group" + System.lineSeparator()
                + "Access-Control-Allow-Origin: *" + System.lineSeparator()
                + "Access-Control-Allow-Credentials: true" + System.lineSeparator();

            out.print(responseMessageHead);
            out.println(responseBodyJson);




            System.out.println("-----------------------------------");
            System.out.println("여기는 requestMessage Head 부분입니다.");
            System.out.println(requestMessageHead);
            System.out.println();
            if (requestMessageBody != null) {
                System.out.println("여기는 requestMessage Body 부분 입니다.");
                System.out.println(requestMessageBody);
                System.out.println();
            }
            System.out.println("여기는 responseMessage Head 부분 입니다.");
            System.out.println(responseMessageHead);
            System.out.println();

            if (responseBodyJson != null) {
                System.out.println("여기는 responseMessage Body 부분 입니다.");
                System.out.println(responseBodyJson);
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
