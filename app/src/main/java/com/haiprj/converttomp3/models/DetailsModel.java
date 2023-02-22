package com.haiprj.converttomp3.models;

public class DetailsModel {

    private String header;
    private String body;

    public DetailsModel(String header, String body) {
        this.header = header;
        this.body = body;
    }

    public DetailsModel() {
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
