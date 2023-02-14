package com.haiprj.converttomp3;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.File;

public class App extends Application {

    private static String appPath;
    public App() {

    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String getAppPath(Context context) {
        String path;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_AUDIOBOOKS) + File.separator + context.getString(R.string.app_name);
        }
        else {
            path = Environment.getExternalStorageDirectory() + File.separator + context.getString(R.string.app_name);
        }
        if (!new File(path).exists()){
            new File(path).mkdirs();
        }
        return path;
    }
}
