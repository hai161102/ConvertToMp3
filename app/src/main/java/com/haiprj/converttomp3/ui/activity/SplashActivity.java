package com.haiprj.converttomp3.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;

import com.haiprj.android_app_lib.mvp.view.ViewResult;
import com.haiprj.android_app_lib.ui.BaseActivity;
import com.haiprj.converttomp3.App;
import com.haiprj.converttomp3.R;
import com.haiprj.converttomp3.databinding.ActivitySplashBinding;
import com.haiprj.converttomp3.models.FileModel;
import com.haiprj.converttomp3.mvp.presenter.AppDataPresenter;
import com.haiprj.converttomp3.ui.fragment.Mp3Fragment;

import java.util.Collection;
import java.util.Objects;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends BaseActivity<ActivitySplashBinding> {

    @Override
    protected void initView() {
        new Handler().postDelayed(() -> {
            MainActivity.start(SplashActivity.this);
            finish();
        }, 2000);
    }

    @Override
    protected void addEvent() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

}
