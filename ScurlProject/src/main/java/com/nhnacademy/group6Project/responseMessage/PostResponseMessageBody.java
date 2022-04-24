package com.nhnacademy.group6Project.responseMessage;

import java.util.HashMap;
import java.util.Map;

public class PostResponseMessageBody implements ResponseMessageBody {
    private Map<String, String> args = new HashMap<>();
    private Map<String, String> data = new HashMap<>();
    private Map<String, String> files = new HashMap<>();
    private Map<String, String> form = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> json = new HashMap<>();
    private String origin ="";
    private String url = "";

    public Map<String, String> getArgs() {
        return args;
    }

    public Map<String, String> getData() {
        return data;
    }

    public Map<String, String> getFiles() {
        return files;
    }

    public Map<String, String> getForm() {
        return form;
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

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setJson(Map<String, String> json) {
        this.json = json;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public void setFiles(Map<String, String> files) {
        this.files = files;
    }
}
