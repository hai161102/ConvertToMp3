package com.haiprj.converttomp3.mvp.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;


import com.haiprj.android_app_lib.mvp.model.DataModel;
import com.haiprj.android_app_lib.mvp.model.DataResult;
import com.haiprj.converttomp3.App;
import com.haiprj.converttomp3.Const;
import com.haiprj.converttomp3.utils.AppUtils;
import com.haiprj.converttomp3.utils.AudioExtractor;
import com.haiprj.converttomp3.utils.LoadAudio;
import com.haiprj.converttomp3.utils.LoadFileUtils;

import java.io.File;
import java.util.Objects;

public class AppDataModel extends DataModel {
    public AppDataModel(DataResult dataResult) {
        super(dataResult);
    }

    public void loadFile(Context context, String tag) {
        if (Objects.equals(tag, Const.LOAD_MP4))
            new LoadFileUtils(context, dataResult, "loadFile").execute();
        else if (Objects.equals(tag, Const.LOAD_MP3)) new LoadAudio(context, dataResult, "loadFile").execute();
    }

    public void convertMp4ToMp3(String srcPath, String dstPath, int startMs, int endMs, boolean useAudio, boolean useVideo) {
        AudioExtractor.genVideoUsingMuxer(srcPath, dstPath, startMs, endMs, useAudio, useVideo, dataResult);
    }
    public void convertMp4ToMp3(Context context, String mp4Path) {
        AppUtils.convertVideoToAudio(context, mp4Path, dataResult);
    }
    public void loadFavouriteMp3(Context requireContext) {
        dataResult.onDataResultSuccess("loadFavouriteMp3", App.getAppRoomDatabase(requireContext).favouriteDao().getAllFavourite());
    }

    public void rename(Context context, String filePath, String newName) {
        File currentFile = new File(filePath);
        File currentParent = currentFile.getParentFile();
        File renameFile = new File(currentParent, newName);
        if (currentFile.renameTo(renameFile)){
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(renameFile)));
            new Handler().postDelayed(() -> dataResult.onDataResultSuccess(Const.RENAME, renameFile), 300);
        }
        else {
            dataResult.onDataResultFailed("Cannot rename this file");
        }

    }

    public void delete(Context context, String filePath) {
        File deleteFile = new File(filePath);
        if (deleteFile.delete()) {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE));
            new Handler().postDelayed(() -> dataResult.onDataResultSuccess(Const.DELETE), 300);
        }
        else {
            dataResult.onDataResultFailed("Cannot delete this file");
        }
    }
}
