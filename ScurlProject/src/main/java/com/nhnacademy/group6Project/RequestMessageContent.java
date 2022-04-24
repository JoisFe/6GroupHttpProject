package com.nhnacademy.group6Project;

public class RequestMessageContent {
    private String method;
    private String path;
    private String requestContentType;

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getRequestContentType() {
        return requestContentType;
    }

    public void analyzeRequest(String[] requestMessageLine) {
        for (String s : requestMessageLine) {
            String[] headResource = s.split(":");
            if (headResource[0].equals("Content-Type")) {
                requestContentType = headResource[1].split(";")[0].substring(1);
            }
            if (headResource.length != 2) {
                String[] requestMethodAndPath = s.split(" ");

                method = requestMethodAndPath[0];
                path = requestMethodAndPath[1];
                continue;
            }
        }
    }
}
