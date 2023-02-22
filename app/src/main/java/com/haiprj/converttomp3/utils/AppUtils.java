package com.haiprj.converttomp3.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.google.gson.Gson;
import com.haiprj.converttomp3.BuildConfig;
import com.haiprj.converttomp3.ui.dialog.RenameDialog;
import com.haiprj.converttomp3.widget.CustomSeekBar;

import java.io.File;
import java.util.Formatter;
import java.util.Locale;
import java.util.Objects;

public class AppUtils {

    public static void shareFile(Context context, File file) {
        Uri uri;
        uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("*/*");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(share, "Share file"));
    }
    public static void shareFile(Context context, Uri uri) {
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("*/*");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(share, "Share file"));
    }

    public static void renameFile(Context context, Uri fileUri) {
        File file = new File(FilePath.getPath(context, fileUri));

        Log.d("rename", "renameFile: " + file.getPath());
    }

    public static void deleteFile(Uri fileUri) {

    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static float getPixels(Context context, float i) {
        return pxFromDp(context, i);
    }
    public static float getDp(Context context,float i) {
        return dpFromPx(context, i);
    }

    public static String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours   = totalSeconds / 3600;

        StringBuilder mFormatBuilder = new StringBuilder();
        mFormatBuilder.setLength(0);
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        if (hours > 0) {
            return mFormatter.format("%d : %02d : %02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d : %02d", minutes, seconds).toString();
        }
    }

    public static String convertToJson(Object object){
        Gson gson = new Gson();
        String json = gson.toJson(object);
        return json;
    }

    public static <T> T convertFromJson(String json, Class<T> anonymous) {
        Gson gson = new Gson();
        return gson.fromJson(json, anonymous);
    }
}
