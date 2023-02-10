package com.haiprj.converttomp3.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;

import com.haiprj.android_app_lib.ui.BaseActivity;
import com.haiprj.converttomp3.R;
import com.haiprj.converttomp3.databinding.ActivityWatchBinding;
import com.haiprj.converttomp3.utils.AppUtils;
import com.haiprj.converttomp3.utils.FilePath;

import java.io.File;

public class WatchVideoActivity extends BaseActivity<ActivityWatchBinding> {

    private String uriPath = "";
    private Uri videoUri;

    public static void start(Context context, String uriPath) {
        Intent starter = new Intent(context, WatchVideoActivity.class);
        starter.putExtra("path", uriPath);
        context.startActivity(starter);
    }

    @Override
    protected void initView() {
        uriPath = getIntent().getStringExtra("path");
        videoUri = Uri.parse(uriPath);
        binding.videoView.setVideoURI(videoUri);
        binding.videoView.start();
    }

    @Override
    protected void addEvent() {
        binding.backImage.setOnClickListener(v -> {
            this.finish();
        });
        binding.share.setOnClickListener(v -> {
//            String selectedFilePath = FilePath.getPath(this, Uri.parse(uriPath));
//            assert selectedFilePath != null;
//            final File file = new File(selectedFilePath);
            AppUtils.shareFile(this, videoUri);
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_watch;
    }
}
