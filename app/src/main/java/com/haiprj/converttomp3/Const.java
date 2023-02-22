package com.haiprj.converttomp3;

public class Const {

    public static final String PERMISSION_NOT_GRANTED = "PERMISSION_NOT_GRANTED";
    public static final int REQUEST_MANAGE_STORAGE_ID = 1;
    public static final int REQUEST_WRITE_STORAGE_ID = 2;
    public static final int REQUEST_READ_STORAGE_ID = 3;

    public static final String LOAD_MP3 = "LOAD_MP3";
    public static final String LOAD_MP4 = "LOAD_MP4";

    public static final String REPLAY_STATE_CURRENT = "REPLAY_STATE_CURRENT";
    public static final String MVP_CONVERT = "MVP_CONVERT";

    public static String commandConvertVideoToAudio(String mp4Path, String mp3Path) {
        return "-y -i " + mp4Path + " -vn " + mp3Path;
    }
}
