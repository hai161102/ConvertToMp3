package com.haiprj.converttomp3.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.haiprj.android_app_lib.mvp.model.DataResult;
import com.haiprj.converttomp3.Const;
import com.haiprj.converttomp3.models.FileModel;
import com.haiprj.converttomp3.ui.fragment.Mp4Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("deprecation")
public class LoadFileUtils extends AsyncTask<String, Void, List<FileModel>> {

    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private final DataResult dataResult;
    private final String key;

    public LoadFileUtils(Context context, DataResult dataResult, String key) {
        this.context = context;
        this.dataResult = dataResult;
        this.key = key;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected List<FileModel> doInBackground(String... strings) {
        return getListFileModelVideo();
    }

    private List<FileModel> getListFileModelVideo() {
        List<FileModel> fileModels = new ArrayList<>();
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }



        String[] projection = new String[] {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE
        };
        String selection = MediaStore.Video.Media.DURATION +
                " >= ?";
        String[] selectionArgs = new String[] {
                String.valueOf(TimeUnit.MILLISECONDS.convert(0, TimeUnit.MINUTES))
        };
        String sortOrder = MediaStore.Video.Media.DISPLAY_NAME + " DESC";

        try (Cursor cursor = context.getApplicationContext().getContentResolver().query(
                collection,
                projection,
                selection,
                selectionArgs,
                sortOrder
        )) {
            // Cache column indices.
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            int durationColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                int duration = cursor.getInt(durationColumn);
                int size = cursor.getInt(sizeColumn);

                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);

                // Stores column values and the contentUri in a local object
                // that represents the media file.
                fileModels.add(new FileModel(name,contentUri.toString(), duration, size));
            }
        }
        return fileModels;
    }

    @Override
    protected void onPostExecute(List<FileModel> fileModels) {
        super.onPostExecute(fileModels);
        if (fileModels == null) {
            dataResult.onDataResultFailed(Const.PERMISSION_NOT_GRANTED);
            return;
        }
        Log.d(Mp4Fragment.TAG, "onFileAvailable: " + fileModels.size());
        dataResult.onDataResultSuccess(key, fileModels);
    }


}
