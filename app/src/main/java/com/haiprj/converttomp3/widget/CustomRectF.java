package com.haiprj.converttomp3.widget;

import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import androidx.annotation.Nullable;

public class CustomRectF extends RectF {

    public CustomRectF() {
    }

    public CustomRectF(float left, float top, float right, float bottom) {
        super(left, top, right, bottom);
    }

    public CustomRectF(@Nullable RectF r) {
        super(r);
    }

    public CustomRectF(@Nullable Rect r) {
        super(r);
    }

    public boolean hasClick(PointF point) {
        return this.contains(point.x, point.y);
    }
}
