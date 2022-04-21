package com.nhnacademy.group6Project;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResponseMessageBody {
    private List<String> args;
    private List<Map<String, String>> data;
    private List<String> files;
    private List<Map<String, String>> headers;
    private List<Map<String, String>> json;
    private String origin;
    private String url;

    public List<String> getArgs() {
        return args;
    }

    public List<Map<String, String>> getData() {
        return data;
    }

    public List<String> getFiles() {
        return files;
    }

    public List<Map<String, String>> getHeaders() {
        return headers;
    }

    public List<Map<String, String>> getJson() {
        return json;
    }

    public String getOrigin() {
        return origin;
    }

    public String getUrl() {
        return url;
    }

    public void addJson(Map<String, String> map) {
        json = new ArrayList<>();
        json.add(map);
    }
}
