package com.haiprj.converttomp3.mvp.model;

import android.content.Context;


import com.haiprj.android_app_lib.mvp.model.DataModel;
import com.haiprj.android_app_lib.mvp.model.DataResult;
import com.haiprj.converttomp3.ui.fragment.Mp3Fragment;
import com.haiprj.converttomp3.ui.fragment.Mp4Fragment;
import com.haiprj.converttomp3.utils.AudioExtractor;
import com.haiprj.converttomp3.utils.LoadFileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AppDataModel extends DataModel {
    public AppDataModel(DataResult dataResult) {
        super(dataResult);
    }

    public void loadFile(Context context, String tag) {

        String extension = "";
        if (Objects.equals(tag, Mp4Fragment.TAG))
            extension = ".mp4";
        else if (Objects.equals(tag, Mp3Fragment.TAG)) extension = ".mp3";
        new LoadFileUtils(context, dataResult, "loadFile").execute(extension);
    }

    public void convertMp4ToMp3(String srcPath, String dstPath, int startMs, int endMs, boolean useAudio, boolean useVideo) {
        AudioExtractor.genVideoUsingMuxer(srcPath, dstPath, startMs, endMs, useAudio, useVideo, dataResult);
    }
}
