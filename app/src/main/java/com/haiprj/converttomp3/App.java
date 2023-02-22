package com.haiprj.converttomp3;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Environment;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.haiprj.android_app_lib.mvp.view.ViewResult;
import com.haiprj.converttomp3.databases.rooms.AppRoomDatabase;
import com.haiprj.converttomp3.models.FileModel;
import com.haiprj.converttomp3.mvp.model.AppDataModel;
import com.haiprj.converttomp3.mvp.presenter.AppDataPresenter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class App extends Application {

    private static String appPath;
    public static final List<FileModel> list = new ArrayList<>();
    public static AppRoomDatabase getAppRoomDatabase(Context context) {
        return AppRoomDatabase.getInstance(context);
    }
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
