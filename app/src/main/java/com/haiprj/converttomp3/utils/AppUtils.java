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
