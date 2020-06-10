package com.coolapps.logomaker.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.provider.MediaStore.Images.Media;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;

import java.util.ArrayList;

import yuku.ambilwarna.BuildConfig;

public class CustomView extends View {
    static final int DRAG = 1;
    static final int DRAW = 2;
    private static final int INVALID_POINTER_ID = -1;
    static final int NONE = 0;
    static final int ZOOM = 3;
    Activity actit;
    public Bitmap bitmap;
    ArrayList<Point> listPoint = new ArrayList();
    private int mActivePointerId = INVALID_POINTER_ID;
    private float mLastTouchX;
    private float mLastTouchY;
    Paint mPiePaint;
    private float mPosX;
    private float mPosY;
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.0f;
    int mode = NONE;
    int rota = NONE;
    public int type = DRAG;

    private class ScaleListener extends SimpleOnScaleGestureListener {
        private ScaleListener() {
        }

        public boolean onScale(ScaleGestureDetector detector) {
            CustomView.this.mScaleFactor = CustomView.this.mScaleFactor * detector.getScaleFactor();
            Log.i("xxx", CustomView.this.mScaleFactor + BuildConfig.FLAVOR);
            CustomView.this.invalidate();
            return true;
        }
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        this.mPiePaint = new Paint();
    }

    public void setActivity(Activity activity) {
        this.actit = activity;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setType(Activity activity, int type) {
        this.type = type;
        this.actit = activity;
    }

    public void addImage(Bitmap bit) {
        if (this.bitmap != null) {
            this.bitmap.recycle();
        }
        this.bitmap = bit;
        this.listPoint.add(new Point(OneFrame.width / DRAW, OneFrame.height / DRAW));
    }

    private Bitmap copyBitmap(Bitmap src) {
        int w = src.getWidth();
        int h = src.getHeight();
        return Bitmap.createBitmap(src);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        if (this.bitmap != null) {
            canvas.scale(this.mScaleFactor, this.mScaleFactor, (float) (this.bitmap.getWidth() / DRAW), (float) (this.bitmap.getHeight() / DRAW));
            Matrix matrix = new Matrix();
            matrix.preRotate((float) this.rota, this.mPosX + ((float) (this.bitmap.getWidth() / DRAW)), this.mPosY + ((float) (this.bitmap.getHeight() / DRAW)));
            matrix.preTranslate(this.mPosX, this.mPosY);
            canvas.drawBitmap(this.bitmap, matrix, null);
        }
        canvas.restore();
    }

    public void rotateBitmap(int i) {
        if (i > 0) {
            this.rota += DRAW;
        } else {
            this.rota -= 2;
        }
        invalidate();
    }

    public void rotateBitmap90(int i) {
        if (i > 0) {
            this.rota += 90;
        } else {
            this.rota -= 90;
        }
        invalidate();
    }

    public Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.setRotate(angle, (float) (source.getWidth() / DRAW), (float) (source.getHeight() / DRAW));
        return Bitmap.createBitmap(source, NONE, NONE, source.getWidth(), source.getHeight(), matrix, true);
    }
    public boolean onTouchEvent(MotionEvent ev) {
        if (OneFrame.mCurrentView != null) {
            OneFrame.mCurrentView.setInEdit(false);
        }
        if (OneFrame.mCurrentEditTextView != null) {
            OneFrame.mCurrentEditTextView.setInEdit(false);
        }
        if (PhotoEditor.mCurrentView != null) {
            PhotoEditor.mCurrentView.setInEdit(false);
        }
        if (PhotoEditor.mCurrentEditTextView != null) {
            PhotoEditor.mCurrentEditTextView.setInEdit(false);
        }
        this.mScaleDetector.onTouchEvent(ev);
        float x;
        float y;
        switch (ev.getAction() & 255) {
            case 0:
                OneFrame.type = this.type;
                setBackgroundColor(Color.rgb(255, 182, 7));
                x = ev.getX() / this.mScaleFactor;
                y = ev.getY() / this.mScaleFactor;
                this.mLastTouchX = x;
                this.mLastTouchY = y;
                this.mode = DRAW;
                Log.i("xxx", x + ":" + y);
                break;
            case 1:
                this.mActivePointerId = INVALID_POINTER_ID;
                setBackgroundColor(INVALID_POINTER_ID);
                this.mode = NONE;
                break;
            case 2:
                x = ev.getX() / this.mScaleFactor;
                y = ev.getY() / this.mScaleFactor;
                if (!this.mScaleDetector.isInProgress()) {
                    this.mode = DRAG;
                    float dy = y - this.mLastTouchY;
                    this.mPosX += x - this.mLastTouchX;
                    this.mPosY += dy;
                    invalidate();
                }
                this.mLastTouchX = x;
                this.mLastTouchY = y;
                break;
            case 3:
                this.mActivePointerId = INVALID_POINTER_ID;
                break;
        }
        return true;
    }


    /*public boolean onTouchEvent(MotionEvent ev) {
        if (OneFrame.mCurrentView != null) {
            OneFrame.mCurrentView.setInEdit(false);
        }
        if (OneFrame.mCurrentEditTextView != null) {
            OneFrame.mCurrentEditTextView.setInEdit(false);
        }
        if (PhotoEditors.mCurrentView != null) {
            PhotoEditors.mCurrentView.setInEdit(false);
        }
        if (PhotoEditors.mCurrentEditTextView != null) {
            PhotoEditors.mCurrentEditTextView.setInEdit(false);
        }
        this.mScaleDetector.onTouchEvent(ev);
        float x;
        float y;
        switch (ev.getAction() & NalUnitUtil.EXTENDED_SAR) {
            case 0:
                OneFrame.type = this.type;
                setBackgroundColor(Color.rgb(NalUnitUtil.EXTENDED_SAR, 182, 7));
                x = ev.getX() / this.mScaleFactor;
                y = ev.getY() / this.mScaleFactor;
                this.mLastTouchX = x;
                this.mLastTouchY = y;
                this.mode = DRAW;
                Log.i("xxx", x + ":" + y);
                break;
            case 1:
                this.mActivePointerId = INVALID_POINTER_ID;
                setBackgroundColor(INVALID_POINTER_ID);
                this.mode = NONE;
                break;
            case 2:
                x = ev.getX() / this.mScaleFactor;
                y = ev.getY() / this.mScaleFactor;
                if (!this.mScaleDetector.isInProgress()) {
                    this.mode = DRAG;
                    float dy = y - this.mLastTouchY;
                    this.mPosX += x - this.mLastTouchX;
                    this.mPosY += dy;
                    invalidate();
                }
                this.mLastTouchX = x;
                this.mLastTouchY = y;
                break;
            case 3:
                this.mActivePointerId = INVALID_POINTER_ID;
                break;
        }
        return true;
    }*/

    public void loadImage() {
        this.actit.startActivityForResult(Intent.createChooser(new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI), "Select Picture"), DRAG);
    }
}
