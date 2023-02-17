package com.haiprj.converttomp3.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CircleView extends View {

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


    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void startSpin() {
        post(runnable);
    }

    public void stopSpin() {
        spin = false;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }


}
