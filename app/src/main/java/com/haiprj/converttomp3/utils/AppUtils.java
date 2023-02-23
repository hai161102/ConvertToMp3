package com.haiprj.converttomp3.utils;

import static com.arthenica.mobileffmpeg.Config.MOBILE_FFMPEG_PIPE_PREFIX;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.FileProvider;

import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.arthenica.mobileffmpeg.FFprobe;
import com.arthenica.mobileffmpeg.MediaInformation;
import com.google.gson.Gson;
import com.haiprj.android_app_lib.mvp.model.DataResult;
import com.haiprj.android_app_lib.ui.BaseDialog;
import com.haiprj.converttomp3.App;
import com.haiprj.converttomp3.BuildConfig;
import com.haiprj.converttomp3.Const;
import com.haiprj.converttomp3.R;
import com.haiprj.converttomp3.mvp.presenter.AppDataPresenter;
import com.haiprj.converttomp3.ui.dialog.DeleteDialog;
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

    @SuppressLint("DefaultLocale")
    public static long convertVideoToAudio(Context context,final String mp4Path, DataResult dataResult){

        String mp3Path = new File(mp4Path).getName();
        mp3Path = App.getAppPath(context) + File.separator + mp3Path.split("\\.")[0] + ".mp3";
        String finalMp3Path = mp3Path;
        return FFmpeg.executeAsync(Const.commandConvertVideoToAudio(mp4Path, mp3Path, 192000), (executionId, returnCode) -> {
            switch (returnCode) {
                case RETURN_CODE_SUCCESS:
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(finalMp3Path))));
                    new Handler().postDelayed(() -> dataResult.onDataResultSuccess(Const.MVP_CONVERT, new File(finalMp3Path)), 1000);

                    break;
                case RETURN_CODE_CANCEL:
                    dataResult.onDataResultFailed("Async command execution cancelled by user.");
                    break;
                default:
                    String mp4 = mp4Path;
                    File currentFile = new File(mp4);
                    String newName = currentFile.getName().replace(" ", "%20");
                    newName = newName.replace("-", "_");
                    File currentParent = currentFile.getParentFile();
                    File renameFile = new File(App.getAppPath(context), newName);
                    mp4 = mp4.replace(" ", "%20");
                    copyMp4(context, mp4, renameFile.getAbsolutePath(), new CopyMp4Listener() {
                        @Override
                        public void onSuccess(Object... objects) {
                            convertVideoToAudio(context, renameFile.getAbsolutePath(), dataResult);
                        }

                        @Override
                        public void onFailed(String message) {
                            dataResult.onDataResultFailed(message);
                        }
                    });
                    break;
            }
        });
    }

    @SuppressLint("DefaultLocale")
    public static long copyMp4(Context context, String oldPath, String copyPath, CopyMp4Listener copyMp4Listener){


        return FFmpeg.executeAsync(Const.commandCopyVideoTo(oldPath, copyPath, 50, 50, 0, 0), (executionId, returnCode) -> {
            switch (returnCode) {
                case RETURN_CODE_SUCCESS:
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(copyPath))));
                    new Handler().postDelayed(() -> copyMp4Listener.onSuccess(new File(copyPath)), 300);
                    break;
                case RETURN_CODE_CANCEL:
                    copyMp4Listener.onFailed("Async command execution cancelled by user.");
                    break;
                default:
                    copyMp4Listener.onFailed(String.format("Async command execution failed with returnCode=%d.", returnCode));
                    break;
            }
        });
    }
    public static void rename(Context context, Activity activity, String filePath, AppDataPresenter dataPresenter) {
        RenameDialog.getInstance(context, activity, new BaseDialog.OnActionDialogCallback() {
            @Override
            public void callback(String key, Object... objects) {
                if (Objects.equals(key, "rename")) {
                    String newName = ((String) objects[0]) + "." +  new File(filePath).getName().split("\\.")[1];
                    dataPresenter.rename(context, filePath, newName);
                }
            }
        }).setFilePath(filePath);
        RenameDialog.showUI();
    }

    public static void showPopupMenu(Context context, int layoutId, View anchor, ItemClick itemClick){
        androidx.appcompat.widget.PopupMenu popup = new androidx.appcompat.widget.PopupMenu(context, anchor);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(layoutId, popup.getMenu());
        popup.setForceShowIcon(true);
        popup.setOnMenuItemClickListener(item -> {
            itemClick.onClick(item);
            popup.dismiss();
            return true;
        });
        popup.show();
    }

    public static void delete(Context context, Activity activity, String filePath, AppDataPresenter appDataPresenter) {

        if (new File(filePath).exists()) {
            DeleteDialog deleteDialog = new DeleteDialog(context, activity, new BaseDialog.OnActionDialogCallback() {
                @Override
                public void callback(String key, Object... objects) {
                    if (Objects.equals(key, DeleteDialog.DELETE)) {
                        appDataPresenter.delete(context, filePath);
                    }
                }
            });
            deleteDialog.show();
        }

    }
    public interface ItemClick {
        void onClick(MenuItem menuItem);
    }

    public interface CopyMp4Listener {
        void onSuccess(Object... objects);
        void onFailed(String message);
    }
}
