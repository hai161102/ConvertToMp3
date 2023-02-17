package com.haiprj.converttomp3.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;


import com.haiprj.android_app_lib.ui.BaseActivity;
import com.haiprj.converttomp3.R;
import com.haiprj.converttomp3.databinding.ActivityPlayMusicBinding;
import com.haiprj.converttomp3.models.MusicManager;
import com.haiprj.converttomp3.utils.AppUtils;

import java.util.concurrent.TimeUnit;

public class PlayMusicActivity extends BaseActivity<ActivityPlayMusicBinding> {

    private static MusicManager musicManager;
    private MediaPlayer mediaPlayer;

    public static void start(Context context, MusicManager music) {
        musicManager = music;
        Intent starter = new Intent(context, PlayMusicActivity.class);
        context.startActivity(starter);
    }
    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        binding.fileName.setText(musicManager.getFileName());
        binding.endTime.setText(AppUtils.convertDuration(musicManager.getDuration()));
        mediaPlayer = MediaPlayer.create(this, musicManager.getUri());
        mediaPlayer.setOnPreparedListener(mp -> {
            long seconds = TimeUnit.SECONDS.convert(musicManager.getDuration(), TimeUnit.MILLISECONDS);
            binding.timeLine.setMax((int) seconds);
        });

    }

    @Override
    protected void addEvent() {
        binding.play.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()){
                pauseMusic();
            }
            else {
                startMusic();

            }
            runProgress(mediaPlayer.isPlaying());

        });

        binding.back.setOnClickListener(v -> {
            finish();
        });

        binding.share.setOnClickListener(v -> {
            AppUtils.shareFile(this, musicManager.getUri());
        });

    }

    private void startMusic() {
        mediaPlayer.start();
        binding.play.setImageResource(R.drawable.baseline_pause_circle_filled_24);
        binding.spinView.startSpin();
    }

    private void pauseMusic() {
        mediaPlayer.pause();
        binding.play.setImageResource(R.drawable.baseline_play_circle_filled_24);
        binding.spinView.stopSpin();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseMusic();
    }



    private void runProgress(boolean isPlay) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (isPlay)
                    binding.timeLine.setProgress(binding.timeLine.getProgress() + 1, true);
                binding.timeLine.postDelayed(this, 1000);
                if (binding.timeLine.getProgress() == binding.timeLine.getMax()) {
                    binding.timeLine.removeCallbacks(this);
                    mediaPlayer.reset();
                    binding.timeLine.setProgress(0, true);
                }
            }
        };
        binding.timeLine.post(runnable);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_play_music;
    }
}
