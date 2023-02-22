package com.haiprj.converttomp3.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaController2;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.MediaController;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;

import com.haiprj.android_app_lib.ui.BaseActivity;
import com.haiprj.converttomp3.R;
import com.haiprj.converttomp3.databinding.ActivityWatchBinding;
import com.haiprj.converttomp3.utils.AppUtils;
import com.haiprj.converttomp3.utils.FilePath;

import java.io.File;
import java.io.IOException;

public class WatchVideoActivity extends BaseActivity<ActivityWatchBinding> implements
        MediaPlayer.OnPreparedListener,
        MediaController.MediaPlayerControl
{
    private String uriPath = "";
    private Uri videoUri;

    private MediaController controller;

    public static void start(Context context, String uriPath) {
        Intent starter = new Intent(context, WatchVideoActivity.class);
        starter.putExtra("path", uriPath);
        context.startActivity(starter);
    }

    @Override
    protected void initView() {
        uriPath = getIntent().getStringExtra("path");
        videoUri = Uri.parse(uriPath);
        //noinspection ConstantConditions
        File file = new File(FilePath.getPath(this, videoUri));
        controller = new MediaController(this);
        binding.videoSurface.setVideoURI(videoUri);
        binding.videoSurface.setOnPreparedListener(this);
        binding.fileName.setText(file.getName().split("\\.")[0]);

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!controller.isShowing())
            controller.show(0);
        else controller.hide();
        return false;
    }

    /**
     * Called when the media file is ready for playback.
     *
     * @param mp the MediaPlayer that is ready for playback
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        controller.setMediaPlayer(this);
        controller.setAnchorView(binding.videoSurfaceContainer);
        binding.videoSurface.start();
    }


    @Override
    public void start() {
        binding.videoSurface.start();
    }

    @Override
    public void pause() {
        binding.videoSurface.pause();
    }

    @Override
    public int getDuration() {
        return binding.videoSurface.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return binding.videoSurface.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        binding.videoSurface.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return binding.videoSurface.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    /**
     * Get the audio session id for the player used by this VideoView. This can be used to
     * apply audio effects to the audio track of a video.
     *
     * @return The audio session, or 0 if there was an error.
     */
    @Override
    public int getAudioSessionId() {
        return binding.videoSurface.getAudioSessionId();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //binding.videoSurface.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.videoSurface.pause();
    }
}
