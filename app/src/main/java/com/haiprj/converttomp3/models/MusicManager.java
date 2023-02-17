package com.haiprj.converttomp3.models;

import android.net.Uri;

import java.io.Serializable;

public class MusicManager implements Serializable {

    private String fileName;
    private Uri uri;
    private int size;
    private int duration;

    public MusicManager(String fileName, Uri uri, int size, int duration) {
        this.fileName = fileName;
        this.uri = uri;
        this.size = size;
        this.duration = duration;
    }

    public MusicManager() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
