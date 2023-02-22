package com.haiprj.converttomp3.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;

import androidx.annotation.Nullable;

import com.haiprj.converttomp3.R;
import com.haiprj.converttomp3.utils.AppUtils;

import java.io.IOException;

public class TimeLineView extends View{



    public void setPrepared() {
        paint = new Paint();
        paint.setColor(getContext().getColor(R.color.app_color));
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        post(runnable);

    }

    public void setComplete() {
    }

    private long lineLength = 0;

    private Paint paint;
    private final Runnable runnable = this::onProgressRun;

    private final String TAG = "TimeLine";

    private TimeLineListener timeLineListener;

    private void onProgressRun() {
        if (timeLineListener != null) {
            if (timeLineListener.getDuration() != 0){
                lineLength = timeLineListener.getCurrentPosition() * 360L / timeLineListener.getDuration();
                invalidate();
            }
        }
        postDelayed(runnable, 1);
    }

    public TimeLineView(Context context) {
        super(context);
    }

    public TimeLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TimeLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ((Activity) getContext()).runOnUiThread(() -> onDrawView(canvas));
    }

    private void onDrawView(Canvas canvas) {
        if (paint != null) {
            paintCircle(canvas);
        }
    }

    private void paintCircle(Canvas canvas) {
        Paint bgPaint = new Paint();
        bgPaint.setColor(getContext().getColor(R.color.time_line_bg));
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeWidth(5);
        canvas.drawRoundRect(10, 10, this.getWidth() - 10, this.getHeight() - 10, this.getWidth(), this.getHeight(), bgPaint);
        canvas.drawArc(10, 10, this.getWidth() - 10, this.getHeight() - 10, -90, lineLength, false, paint);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void init(TimeLineListener timeLineListener) {
        this.timeLineListener = timeLineListener;

        //this.setBackgroundColor(getContext().getColor(R.color.time_line_bg));
    }

    public void destroy() {
        removeCallbacks(runnable);
    }
    public interface TimeLineListener {
        int getCurrentPosition();
        int getDuration();
    }
}
