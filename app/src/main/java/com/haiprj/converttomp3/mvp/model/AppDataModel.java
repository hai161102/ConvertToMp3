package com.haiprj.converttomp3.mvp.model;

import com.haiprj.android_app_lib.mvp.model.DataModel;
import com.haiprj.android_app_lib.mvp.model.DataResult;

import java.util.ArrayList;

public class AppDataModel extends DataModel {
    public AppDataModel(DataResult dataResult) {
        super(dataResult);
    }

    public void loadFile(String tag) {

        dataResult.onDataResultSuccess("loadFile", new ArrayList<>());
    }
}
