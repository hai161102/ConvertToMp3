package com.haiprj.converttomp3.models;

import android.net.Uri;

public class FileModel {
    private String displayName;
    private Uri fileUri;

    private int duration;
    private int size;

    public FileModel(String displayName, Uri fileUri) {
        this.displayName = displayName;
        this.fileUri = fileUri;
    }

    public FileModel(String displayName, Uri fileUri, int duration, int size) {
        this.displayName = displayName;
        this.fileUri = fileUri;
        this.duration = duration;
        this.size = size;
    }

    public FileModel() {
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Uri getFileUri() {
        return fileUri;
    }

    public void setFileUri(Uri fileUri) {
        this.fileUri = fileUri;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
