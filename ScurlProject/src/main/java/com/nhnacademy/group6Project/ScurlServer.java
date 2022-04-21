package com.nhnacademy.group6Project;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

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
            byte[] byteArr = new byte[2048];

            int readByteCount = in.read(byteArr);

            String data = new String(byteArr, 0, readByteCount, "UTF-8");
            String[] requestMessage = data.split("\r\n\r\n");
            String requestMessageHead = requestMessage[0];
            String requestMessageBody = null;
            if (requestMessage.length == 2) {
                requestMessageBody = requestMessage[1];
            }

            Date currentTime = new Date();

            SimpleDateFormat dateFormat =
                new SimpleDateFormat("EEE, d MMM yyyy hh:mm:ss z", Locale.ENGLISH);

            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            System.out.println(dateFormat.format(currentTime));

            //String respondMessage[] = data.split("\r\n\r\n");

            System.out.println(data);
            String date = "Date: " + data;

            String responseHeadMessage = "HTTP/1.1 200 OK\n"
                + "Content-Type: application/json\n" + "Content-Length: 254\n" + "Connection: keep-alive\n"
                + "Server: gunicorn/19.9.0\n" + "Access-Control-Allow-Origin: *\n" + "Access-Control-Allow-Credentials: true\n"
                +"\n";
            responseHeadMessage +=
                "{\"browsers\": {\"firefox\": {\"name\": \"Firefox\",\"pref_url\": \"about:config\",\"releases\": {\"1\": {\"release_date\": \"2004-11-09\",\"status\": \"retired\",\"engine\": \"Gecko\",\"engine_version\": \"1.7\"}}}}}\n";


            out.println(responseHeadMessage);
//            out.println(responseHeadMessage.getBytes(StandardCharsets.UTF_8));
            System.out.println("----------");
            System.out.println(data);
            String[] arr = requestMessageHead.split("\n");

            String stateCode = "200 OK"; // redirection 및 400 터트려야할 때는 바꾸어야 하니 나중에 조건문 달아서 바꿔주자
            String contentLength = null;
            String contentType = null;

            for (String s : arr) {
                if (s.contains("Content-Type")) {
                    System.out.println("xxxx");
                    contentType = s.substring(14, s.length() - 1);
                }

                if (s.contains("Content-Length")) {
                    contentLength = s.substring(16, s.length() - 1);
                }
            }
            ResponseMessageBody responseMessageBody = new ResponseMessageBody();

            if (requestMessageBody != null) {
                JsonToMap jsonToMap = new JsonToMap(requestMessageBody);
                Map<String, String> jsonMap = jsonToMap.parsing();

                responseMessageBody.addJson(jsonMap);
            }

            // Serialize 해보자-------------------------------------------------

            JsonSerialization jsonSerialization = new JsonSerialization(responseMessageBody);
            String responseBodyJson = jsonSerialization.serialize().replace("null", "{}");
            System.out.println(responseBodyJson);

            System.out.println(contentType + " " + contentLength);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 우리가 해야할 일
    // 1. 서버 소켓을 만든다
    // 2. 커맨드 창에서 우리 ip와 포트로 curl을 쏜다
    // 3. 잘 받아오는지 출력해본다

    // ip : 192.168.71.30
}
;