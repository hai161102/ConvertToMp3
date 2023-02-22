package com.haiprj.converttomp3.mvp.model;

import android.content.Context;


import com.haiprj.android_app_lib.mvp.model.DataModel;
import com.haiprj.android_app_lib.mvp.model.DataResult;
import com.haiprj.converttomp3.App;
import com.haiprj.converttomp3.Const;
import com.haiprj.converttomp3.utils.AppUtils;
import com.haiprj.converttomp3.utils.AudioExtractor;
import com.haiprj.converttomp3.utils.LoadAudio;
import com.haiprj.converttomp3.utils.LoadFileUtils;

import java.util.ArrayList;
import java.util.List;
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
}
