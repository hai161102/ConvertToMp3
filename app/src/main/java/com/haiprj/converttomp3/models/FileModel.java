package com.haiprj.converttomp3.models;

import android.net.Uri;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "file_model")
public class FileModel {
    @PrimaryKey(autoGenerate = true)
    public int id;
    private String displayName;
    private String fileUriPath;

    private int duration;
    private int size;

    public FileModel() {
    }

    public FileModel(String displayName, String fileUriPath, int duration, int size) {
        this.displayName = displayName;
        this.fileUriPath = fileUriPath;
        this.duration = duration;
        this.size = size;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFileUriPath() {
        return fileUriPath;
    }

    public void setFileUriPath(String fileUriPath) {
        this.fileUriPath = fileUriPath;
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

    public Uri getFileUri() {
        return Uri.parse(fileUriPath);
    }
}
