package com.haiprj.converttomp3.databases.rooms;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.haiprj.converttomp3.databases.dao.FavouriteDao;
import com.haiprj.converttomp3.models.FileModel;

@Database(entities = {FileModel.class}, version = 3)
public abstract class AppRoomDatabase extends RoomDatabase {

    public abstract FavouriteDao favouriteDao();

    private static AppRoomDatabase instance;

    public static synchronized AppRoomDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppRoomDatabase.class, "FileModelDatabase")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
