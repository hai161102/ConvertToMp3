package com.haiprj.converttomp3.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;

import com.haiprj.android_app_lib.ui.BaseActivity;
import com.haiprj.converttomp3.R;
import com.haiprj.converttomp3.databinding.ActivityWatchBinding;
import com.haiprj.converttomp3.utils.AppUtils;
import com.haiprj.converttomp3.utils.FilePath;
import com.haiprj.converttomp3.widget.VideoControllerView;

import java.io.File;
import java.io.IOException;

public class WatchVideoActivity extends BaseActivity<ActivityWatchBinding> implements SurfaceHolder.Callback,
        MediaPlayer.OnPreparedListener,
        VideoControllerView.MediaPlayerControl
{
    private String uriPath = "";
    private Uri videoUri;

    private MediaPlayer player;
    private VideoControllerView controller;
    public static void start(Context context, String uriPath) {
        Intent starter = new Intent(context, WatchVideoActivity.class);
        starter.putExtra("path", uriPath);
        context.startActivity(starter);
    }

    @Override
    protected void initView() {
        uriPath = getIntent().getStringExtra("path");
        videoUri = Uri.parse(uriPath);

        SurfaceHolder videoHolder = binding.videoSurface.getHolder();
        videoHolder.setFixedSize(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        videoHolder.addCallback(this);

        player = new MediaPlayer();
        controller = new VideoControllerView(this);

        try {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(this, videoUri);
            player.setOnPreparedListener(this);
        } catch (IllegalArgumentException | SecurityException | IllegalStateException |
                 IOException e) {
            e.printStackTrace();
        }
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
        controller.show();
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
        player.start();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        player.setDisplay(holder);
        player.prepareAsync();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void start() {
        player.start();
    }

    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public int getDuration() {
        return player.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        player.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
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

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void toggleFullScreen() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.pause();
    }
}
