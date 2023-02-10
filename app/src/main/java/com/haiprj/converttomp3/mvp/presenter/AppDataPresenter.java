package com.haiprj.converttomp3.mvp.presenter;


import android.content.Context;


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
}
