package com.haiprj.converttomp3.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.haiprj.converttomp3.BuildConfig;
import com.haiprj.converttomp3.ui.dialog.RenameDialog;

import java.io.File;

public class AppUtils {

    public static String convertDuration(long duration) {
        String out = null;
        long hours=0;
        try {
            hours = (duration / 3600000);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return out;
        }
        long remaining_minutes = (duration - (hours * 3600000)) / 60000;
        String minutes = String.valueOf(remaining_minutes);
        if (minutes.equals(0)) {
            minutes = "00";
        }
        else if (remaining_minutes < 10) {
            minutes = "0" + String.valueOf(remaining_minutes);
        }
        long remaining_seconds = (duration - (hours * 3600000) - (remaining_minutes * 60000));
        String seconds = String.valueOf(remaining_seconds);
        if (seconds.length() < 2) {
            seconds = "00";
        } else {
            seconds = seconds.substring(0, 2);
        }

        if (hours > 0) {
            out = hours + " : " + minutes + " : " + seconds;
        } else {
            out = minutes + " : " + seconds;
        }

        return out;

    }
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
}
