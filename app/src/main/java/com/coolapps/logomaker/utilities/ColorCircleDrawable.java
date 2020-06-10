package com.coolapps.logomaker.utilities;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class ColorCircleDrawable extends Drawable {
    private final Paint mPaint = new Paint(1);
    private int mRadius = 0;

    public ColorCircleDrawable(int color) {
        this.mPaint.setColor(color);
    }

    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        canvas.drawCircle((float) bounds.centerX(), (float) bounds.centerY(), (float) this.mRadius, this.mPaint);
    }

    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        this.mRadius = Math.min(bounds.width(), bounds.height()) / 2;
    }

    public void setAlpha(int alpha) {
        this.mPaint.setAlpha(alpha);
    }

    public void setColorFilter(ColorFilter cf) {
        this.mPaint.setColorFilter(cf);
    }

    @SuppressLint("WrongConstant")
    public int getOpacity() {
        return -3;
    }
}
