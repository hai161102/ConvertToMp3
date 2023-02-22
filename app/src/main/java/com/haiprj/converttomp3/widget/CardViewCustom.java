package com.haiprj.converttomp3.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class CardViewCustom extends CardView {

    private final int FPS = 100;
    private boolean spin = true;

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (spin) {
                update();
            }
            postDelayed(this, 1000 / FPS);

        }
    };

    private void update() {
        float rotate = this.getRotation();
        if (rotate == 360) {
            rotate = 0;
        }
        this.setRotation(rotate + 1f);
        invalidate();
    }


    public CardViewCustom(@NonNull Context context) {
        super(context);
    }

    public CardViewCustom(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CardViewCustom(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    public void startSpin() {
        post(runnable);
    }

    public void stopSpin() {
        spin = false;
    }
}
