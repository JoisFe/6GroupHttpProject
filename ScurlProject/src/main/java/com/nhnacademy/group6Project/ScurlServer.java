package com.nhnacademy.group6Project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.group6Project.responseMessage.GetResponseMessageBody;
import com.nhnacademy.group6Project.responseMessage.PostResponseMessageBody;
import com.nhnacademy.group6Project.responseMessage.ResponseMessageHead;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ScurlServer {
    public static void main(String[] args) {

        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(80);

            socket = serverSocket.accept(); // 나중에 소켓 반납 ?

            Sender sender = new Sender(socket);
            Receiver receiver = new Receiver(socket);

            byte[] byteArr = new byte[2048];
            int readByteCount = receiver.getIn().read(byteArr);
            String data = new String(byteArr, 0, readByteCount, "UTF-8");

            String[] requestMessage = data.split("\r\n\r\n");
            String requestMessageHead = requestMessage[0];
            String requestMessageBody = null;
            if (requestMessage.length > 1) {
                requestMessageBody = "";
                for (int i = 1; i < requestMessage.length; ++i) {
                    requestMessageBody += requestMessage[i];
                }
            }

            PostResponseMessageBody postResponseMessageBody = new PostResponseMessageBody();
            GetResponseMessageBody getResponseMessageBody = new GetResponseMessageBody();

            String[] requestMessageLine = requestMessageHead.split(System.lineSeparator());

            RequestMessageContent requestMessageContent= new RequestMessageContent();
            
            requestMessageContent.analyzeRequest(requestMessageLine);

            // 헤더넣기
            injectHead(requestMessageLine, requestMessageBody, getResponseMessageBody, postResponseMessageBody);

            String responseBodyJson;

            int contentLength = 0;

            if (requestMessageBody != null) {
                Map<String, String> jsonMap = null;

                if (requestMessageContent.getRequestContentType().equals("application/json")) {
                    JsonToMap jsonToMap = new JsonToMap(requestMessageBody);
                    jsonMap = jsonToMap.parsing();


                    postResponseMessageBody.setData(jsonMap);
                    postResponseMessageBody.setJson(jsonMap);
                }

                if (requestMessageContent.getRequestContentType().equals("multipart/form-data")) {
                    String[] fileDataList = requestMessageBody.split("\r\n\r\n")[0].split(System.lineSeparator());

                    String fileNameLine = fileDataList[1];
                    String fileNameMap = fileNameLine.split(";")[2];
                    String fileName = fileNameMap.substring(fileNameMap.indexOf("=") + 2, fileNameMap.length() - 2);

                    String fileDataLine = fileDataList[2];
                    String fileData = fileDataLine.substring(fileDataLine.indexOf("{"));

                    Map<String, String> map = new HashMap<>();
                    map.put("upload", fileData);

                    postResponseMessageBody.setFiles(map);

                    // File 만들기
                    File file = new File("/Users/jo/desktop/file/" + fileName);
                    String fileContets = fileData;
                    byte[] bytes = fileContets.getBytes();

                    try (FileOutputStream fileOutputStream = new FileOutputStream(file);) {
                        fileOutputStream.write(bytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                postResponseMessageBody.setOrigin(socket.getInetAddress().getHostAddress());
                postResponseMessageBody.setUrl(socket.getInetAddress().getHostName() + requestMessageContent.getPath());

                JsonSerialization jsonSerialization = new JsonSerialization(postResponseMessageBody);;
                responseBodyJson = jsonSerialization.serialize();
            } else {
                String queryResource = requestMessageHead.split("\n")[0];
                String queryString = queryResource.substring(queryResource.lastIndexOf('?') + 1, queryResource.lastIndexOf(' '));


                String[] queryParameters = queryString.split("&");

                for (String queryParameter : queryParameters) {
                    String[] parameter = queryParameter.split("=");

                    if (parameter.length == 2) {
                        getResponseMessageBody.getArgs().put(parameter[0], parameter[1]);
                    }
                }

                getResponseMessageBody.setOrigin(socket.getInetAddress().getHostAddress());
                getResponseMessageBody.setUrl(socket.getInetAddress().getHostName() + requestMessageContent.getPath());

                JsonSerialization jsonSerialization = new JsonSerialization(getResponseMessageBody);;
                responseBodyJson = jsonSerialization.serialize();

            }
            ResponseMessageHead responseMessageHead = new ResponseMessageHead();

            responseMessageHead.makeResponseMessageHead(responseBodyJson);

            sender.getOut().println(responseMessageHead.getResponseMessageHead());

            if (requestMessageContent.getPath().equals("/ip")) {
                Map<String, String> map = new HashMap<>();
                map.put("origin", socket.getInetAddress().getHostAddress());
                ObjectMapper objectMapper = new ObjectMapper();

                sender.getOut().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map));
            } else {
                sender.getOut().println(responseBodyJson);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void injectHead(String[] requestMessageLine, String requestMessageBody,
                                   GetResponseMessageBody getResponseMessageBody,
                                   PostResponseMessageBody postResponseMessageBody) {
        for (String s : requestMessageLine) {
            String[] headResource = s.split(":");

            if (headResource.length == 2) {
                if (requestMessageBody != null) {
                    if (!headResource[1].contains("\r")) {
                        postResponseMessageBody.getHeaders().put(headResource[0],
                            headResource[1].substring(1));
                    } else {
                        postResponseMessageBody.getHeaders().put(headResource[0],
                            headResource[1].substring(1, headResource[1].length() - 1));
                    }
                } else {
                    if (!headResource[1].contains("\r")) {
                        getResponseMessageBody.getHeaders().put(headResource[0],
                            headResource[1].substring(1));
                    } else {
                        getResponseMessageBody.getHeaders().put(headResource[0],
                            headResource[1].substring(1, headResource[1].length() - 1));
                    }
                }
            }
        }
    }
}
