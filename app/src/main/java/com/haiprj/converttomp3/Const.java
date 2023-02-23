package com.haiprj.converttomp3;

import android.annotation.SuppressLint;

public class Const {

    public static final String PERMISSION_NOT_GRANTED = "PERMISSION_NOT_GRANTED";
    public static final int REQUEST_MANAGE_STORAGE_ID = 1;
    public static final int REQUEST_WRITE_STORAGE_ID = 2;
    public static final int REQUEST_READ_STORAGE_ID = 3;

    public static final String LOAD_MP3 = "LOAD_MP3";
    public static final String LOAD_MP4 = "LOAD_MP4";

    public static final String REPLAY_STATE_CURRENT = "REPLAY_STATE_CURRENT";
    public static final String RANDOM_PLAY_STATE = "RANDOM_PLAY_STATE";
    public static final String MVP_CONVERT = "MVP_CONVERT";
    public static final String RENAME = "RENAME";
    public static final String DELETE = "DELETE";

    public static String commandConvertVideoToAudio(String mp4Path, String mp3Path) {
        return "-y -i " + mp4Path + " -vn " + mp3Path;
    }
    @SuppressLint("DefaultLocale")
    public static String commandConvertVideoToAudio(String mp4Path, String mp3Path, long kbps) {
        return String.format("-y -i %s -f mp3 -ab %d -vn %s", mp4Path, kbps, mp3Path);
    }
    @SuppressLint("DefaultLocale")
    public static String commandCopyVideoTo(String oldPath, String copyPath, int w, int h, int x, int y) {
        return String.format("-y -i %s -filter:v “crop=%d:%d:%d:%d” %s", oldPath, w, h, x, y, copyPath);
    }

//ffmpeg -i video.mp4 -f mp3 -ab 192000 -vn music.mp3
}
