package com.nhnacademy.group6Project.responseMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ResponseMessageHead {
    String responseMessageHead;
    String stateCode = "200 OK";
    String contentType = "application/json";
    String date;
    int contentLength;

    public String getResponseMessageHead() {
        return responseMessageHead;
    }

    public void makeResponseMessageHead(String responseBodyJson) {
        contentLength = responseBodyJson.getBytes().length;
        date = getDate();

        responseMessageHead =
            "HTTP/1.1 " + stateCode + System.lineSeparator()
                + "Date: " + date + System.lineSeparator()
                + "content-length: " + contentLength + System.lineSeparator()
                + "content-type: " + contentType + System.lineSeparator()
                + "connection: keep-alive" + System.lineSeparator()
                + "server: 6Group" + System.lineSeparator()
                + "Access-Control-Allow-Origin: *" + System.lineSeparator()
                + "Access-Control-Allow-Credentials: true" + System.lineSeparator();

    }

    private String getDate() {
        Date currentTime = new Date();
        SimpleDateFormat dateFormat =
            new SimpleDateFormat("EEE, d MMM yyyy hh:mm:ss z", Locale.ENGLISH);

        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return dateFormat.format(currentTime);
    }

}
