package com.nhnacademy.group6Project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseMessageBody {
    private Map<String, String> args = new HashMap<>();
    private Map<String, String> data = new HashMap<>();
    private Map<String, String> files = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> json = new HashMap<>();
    private String origin;
    private String url;

    public Map<String, String> getArgs() {
        return args;
    }

    public Map<String, String> getData() {
        return data;
    }

    public Map<String, String> getFiles() {
        return files;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getJson() {
        return json;
    }

    public String getOrigin() {
        return origin;
    }

    public String getUrl() {
        return url;
    }

    public void setJson(Map<String, String> json) {
        this.json = json;
    }
}
