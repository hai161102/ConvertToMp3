package com.haiprj.converttomp3.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomSeekBar extends androidx.appcompat.widget.AppCompatSeekBar {

    private int length = 100;
    private boolean isPlay = false;
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            runProgress();
            postDelayed(this, 1000);
        }
    };

    private void runProgress() {
        if (isPlay) {
            this.setProgress(this.getProgress() + 1, true);
            if (this.getProgress() == length) {
                this.setProgress(0, true);
            }
        }

    }


    public CustomSeekBar(@NonNull Context context) {
        super(context);
        init();
    }

    public CustomSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setMax(length);
        post(runnable);
    }

    public void setLength(int length) {
        this.length = length;
        this.setMax(length);
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void removeRunnable(){
        this.removeCallbacks(runnable);
    }
}
