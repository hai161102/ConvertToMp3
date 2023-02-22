package com.haiprj.converttomp3.mvp.presenter;


import android.content.Context;


import com.haiprj.android_app_lib.mvp.model.DataModel;
import com.haiprj.android_app_lib.mvp.presenter.DataPresenter;
import com.haiprj.android_app_lib.mvp.view.ViewResult;
import com.haiprj.converttomp3.mvp.model.AppDataModel;

public class AppDataPresenter extends DataPresenter {
    public AppDataPresenter(ViewResult viewResult) {
        super(viewResult);
        dataModel = new AppDataModel(this);
    }

    public void loadFile(Context context, String tag) {
        ((AppDataModel)dataModel).loadFile(context, tag);
    }

    public void convertMp4ToMp3(String srcPath, String dstPath, int startMs, int endMs, boolean useAudio, boolean useVideo) {
        ((AppDataModel) dataModel).convertMp4ToMp3(srcPath, dstPath, startMs, endMs, useAudio, useVideo);
    }
    public void convertMp4ToMp3(Context context, String mp4Path) {
        ((AppDataModel) dataModel).convertMp4ToMp3(context, mp4Path);
    }
    public void loadFavouriteMp3(Context requireContext) {
        ((AppDataModel) dataModel).loadFavouriteMp3(requireContext);
    }
}
