package com.coolapps.logomaker.utilities;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.coolapps.logomaker.R;


/**
 * Created by sam on 14-8-14.
 */
public class StickerViewOld extends View {

    private float mScaleSize;

    public static final float MAX_SCALE_SIZE = 4.0f;
    public static final float MIN_SCALE_SIZE = 0.1f;


    private float[] mOriginPoints;
    private float[] mPoints;
    private RectF mOriginContentRect;
    private RectF mContentRect;
    private RectF mViewRect;

    private float mLastPointX, mLastPointY;

    private Bitmap mBitmap;
    private Bitmap mControllerBitmap, mDeleteBitmap;
    private Bitmap mReversalHorBitmap,mReversalVerBitmap;//水平反转和垂直反转bitmap
    private Matrix mMatrix;
    private Paint mPaint, mBorderPaint;
    private float mControllerWidth, mControllerHeight, mDeleteWidth, mDeleteHeight;
    private float mReversalHorWidth,mReversalHorHeight,mReversalVerWidth,mReversalVerHeight;
    private boolean mInController, mInMove;
    private boolean mInReversalHorizontal,mInReversalVertical;

    private boolean mDrawController = true;
    //private boolean mCanTouch;
    private float mStickerScaleSize = 1.0f;

    private int align;
    private int circle;
    private String color;
    private boolean drawedit;
    private String font;
    private String text;


    private OnStickerDeleteListener mOnStickerDeleteListener;
    private OnStickerEditListener mOnStickerEditListener;

    public StickerViewOld(Context context) {
        this(context, null);
    }

    public StickerViewOld(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickerViewOld(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(MAX_SCALE_SIZE);
        mPaint.setColor(Color.WHITE);

        mBorderPaint = new Paint(mPaint);
        mBorderPaint.setColor(Color.parseColor("#B2ffffff"));
        mBorderPaint.setShadowLayer(DisplayUtil.dip2px(getContext(), 2.0f), 0, 0, Color.parseColor("#33000000"));
        int width = DisplayUtil.getDisplayWidthPixels(getContext());

        mControllerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_resize);
        mControllerBitmap = BitmapProcessing.resizeBitmap(this.mControllerBitmap, width / 12, width / 12);
        mControllerWidth = (float) mControllerBitmap.getWidth();
        mControllerHeight = (float) mControllerBitmap.getHeight();


        mDeleteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_deleteicon);
        mDeleteBitmap = BitmapProcessing.resizeBitmap(this.mDeleteBitmap, width / 12, width / 12);
        mDeleteWidth = (float) mDeleteBitmap.getWidth();
        mDeleteHeight = (float) mDeleteBitmap.getHeight();


        mReversalHorBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_flipicon);
        mReversalHorBitmap = BitmapProcessing.resizeBitmap(this.mReversalHorBitmap, width / 12, width / 12);
        mReversalHorWidth = (float) mReversalHorBitmap.getWidth();
        mReversalHorHeight = (float) mReversalHorBitmap.getHeight();


        mReversalVerBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_editicon);
        mReversalVerBitmap = BitmapProcessing.resizeBitmap(this.mReversalVerBitmap, width / 12, width / 12);
        mReversalVerWidth = (float) mReversalVerBitmap.getWidth();
        mReversalVerHeight = (float) mReversalVerBitmap.getHeight();

    }

    public void setWaterMark(@NonNull Bitmap bitmap) {
        mBitmap = bitmap;
        mStickerScaleSize = 1.0f;


        setFocusable(true);
        try {


            float px = mBitmap.getWidth();
            float py = mBitmap.getHeight();


            //mOriginPoints = new float[]{px, py, px + bitmap.getWidth(), py, bitmap.getWidth() + px, bitmap.getHeight() + py, px, py + bitmap.getHeight()};
            mOriginPoints = new float[]{0, 0, px, 0, px, py, 0, py, px / 2, py / 2};
            mOriginContentRect = new RectF(0, 0, px, py);
            mPoints = new float[10];
            mContentRect = new RectF();

            mMatrix = new Matrix();
            float transtLeft = ((float)DisplayUtil.getDisplayWidthPixels(getContext()) - mBitmap.getWidth()) / 2;
            float transtTop = ((float)DisplayUtil.getDisplayWidthPixels(getContext()) - mBitmap.getHeight()) / 2;

            mMatrix.postTranslate(transtLeft, transtTop);

        } catch (Exception e) {
            e.printStackTrace();
        }
        postInvalidate();

    }

    public Matrix getMarkMatrix() {
        return mMatrix;
    }

    @Override
    public void setFocusable(boolean focusable) {
        super.setFocusable(focusable);
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap == null || mMatrix == null) {
            return;
        }

        mMatrix.mapPoints(mPoints, mOriginPoints);

        mMatrix.mapRect(mContentRect, mOriginContentRect);
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
        if (mDrawController && isFocusable()) {
            canvas.drawLine(mPoints[0], mPoints[1], mPoints[2], mPoints[3], mBorderPaint);
            canvas.drawLine(mPoints[2], mPoints[3], mPoints[4], mPoints[5], mBorderPaint);
            canvas.drawLine(mPoints[4], mPoints[5], mPoints[6], mPoints[7], mBorderPaint);
            canvas.drawLine(mPoints[6], mPoints[7], mPoints[0], mPoints[1], mBorderPaint);
            canvas.drawBitmap(mControllerBitmap, mPoints[4] - mControllerWidth / 2, mPoints[5] - mControllerHeight / 2, mBorderPaint);
            canvas.drawBitmap(mDeleteBitmap, mPoints[0] - mDeleteWidth / 2, mPoints[1] - mDeleteHeight / 2, mBorderPaint);
            canvas.drawBitmap(mReversalHorBitmap,mPoints[2]-mReversalHorWidth/2,mPoints[3]-mReversalVerHeight/2,mBorderPaint);
            canvas.drawBitmap(mReversalVerBitmap,mPoints[6]-mReversalVerWidth/2,mPoints[7]-mReversalVerHeight/2,mBorderPaint);
        }
    }

    public Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mDrawController = false;
        draw(canvas);
        mDrawController = true;
        canvas.save();
        return bitmap;
    }

    public void setShowDrawController(boolean show) {
        mDrawController = show;
    }

    private boolean isInController(float x, float y) {
        int position = 4;
        //while (position < 8) {
        float rx = mPoints[position];
        float ry = mPoints[position + 1];
        RectF rectF = new RectF(rx - mControllerWidth / 2,
                ry - mControllerHeight / 2,
                rx + mControllerWidth / 2,
                ry + mControllerHeight / 2);
        return rectF.contains(x, y);
        //   position += 2;
        //}

    }

    private boolean isInDelete(float x, float y) {
        int position = 0;
        //while (position < 8) {
        float rx = mPoints[position];
        float ry = mPoints[position + 1];
        RectF rectF = new RectF(rx - mDeleteWidth / 2,
                ry - mDeleteHeight / 2,
                rx + mDeleteWidth / 2,
                ry + mDeleteHeight / 2);
        return rectF.contains(x, y);
        //   position += 2;
        //}

    }


    //判断点击区域是否在水平反转按钮区域内
    private boolean isInReversalHorizontal(float x,float y){
        Log.d("Horizental called", "Horize Called");
        int position = 2;
        float rx = mPoints[position];
        float ry = mPoints[position+1];

        RectF rectF = new RectF(rx - mReversalHorWidth/2,ry-mReversalHorHeight/2,rx+mReversalHorWidth/2,ry+mReversalHorHeight/2);
        return rectF.contains(x, y);

    }

    //判断点击区域是否在垂直反转按钮区域内
    private boolean isInReversalVertical(float x,float y){
        Log.d("Vertical called", "Vertical Called");
        int position = 6;
        float rx = mPoints[position];
        float ry = mPoints[position+1];

        RectF rectF = new RectF(rx - mReversalVerWidth/2,ry - mReversalVerHeight/2,rx + mReversalVerWidth/2,ry+mReversalVerHeight/2);
        return rectF.contains(x, y);
    }

    private boolean mInDelete = false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (!isFocusable()) {

            return super.dispatchTouchEvent(event);
        }
        if (mViewRect == null) {
            mViewRect = new RectF(0f, 0f, getMeasuredWidth(), getMeasuredHeight());
        }
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isInController(x, y)) {
                    mInController = true;
                    mLastPointY = y;
                    mLastPointX = x;
                    break;
                }

                if (isInDelete(x, y)) {
                    mInDelete = true;
                    break;
                }

                if(isInReversalHorizontal(x,y)){
                    mInReversalHorizontal = true;
                    break;
                }

                if(isInReversalVertical(x,y)){
                    mInReversalVertical = true;
                    break;
                }

                if (mContentRect.contains(x, y)) {
                    mLastPointY = y;
                    mLastPointX = x;
                    mInMove = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isInDelete(x, y) && mInDelete) {
                    doDeleteSticker();
                    break;
                }
                if(isInReversalHorizontal(x,y) && mInReversalHorizontal){
                    doReversalHorizontal();
                    break;
                }
                if (isInReversalVertical(x,y) && mInReversalVertical){
//                    doReversalVertical();
//                    doEditSticker();

//                    if(getTag().equals("text")) {
//
//                        LogoMakerActivity.showTextSticker();
//                    }else{
//                        LogoMakerActivity.showImageSticker();
//                    }

                    break;
                }


            case MotionEvent.ACTION_CANCEL:
                mLastPointX = 0;
                mLastPointY = 0;
                mInController = false;
                mInMove = false;
                mInDelete = false;
                mInReversalHorizontal = false;
                mInReversalVertical = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mInController) {

                    mMatrix.postRotate(rotation(event), mPoints[8], mPoints[9]);
                    float nowLenght = caculateLength(mPoints[0], mPoints[1]);
                    float touchLenght = caculateLength(event.getX(), event.getY());
                    if ((float)Math.sqrt((nowLenght - touchLenght) * (nowLenght - touchLenght)) > 0.0f) {
                        float scale = touchLenght / nowLenght;
                        float nowsc = mStickerScaleSize * scale;
                        if (nowsc >= MIN_SCALE_SIZE && nowsc <= MAX_SCALE_SIZE) {
                            mMatrix.postScale(scale, scale, mPoints[8], mPoints[9]);
                            mStickerScaleSize = nowsc;
                        }
                    }

                    invalidate();
                    mLastPointX = x;
                    mLastPointY = y;
                    break;

                }

                if (mInMove == true) { //拖动的操作
                    float cX = x - mLastPointX;
                    float cY = y - mLastPointY;
                    mInController = false;
                    //Log.i("MATRIX_OK", "ma_jiaodu:" + a(cX, cY));

                    if ((float)Math.sqrt(cX * cX + cY * cY) > 2.0f  && canStickerMove(cX, cY)) {
                        //Log.i("MATRIX_OK", "is true to move");
                        mMatrix.postTranslate(cX, cY);
                        postInvalidate();
                        mLastPointX = x;
                        mLastPointY = y;
                    }
                    break;
                }


                return true;

        }
        return true;
    }

    private void doDeleteSticker() {
        setWaterMark(null);
        if (mOnStickerDeleteListener != null) {
            mOnStickerDeleteListener.onDelete();
        }
    }

    //图片水平反转
    private void doReversalHorizontal(){
        float[] floats = new float[] { -1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f };
        Matrix tmpMatrix = new Matrix();
        tmpMatrix.setValues(floats);
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
                mBitmap.getHeight(), tmpMatrix, true);
        invalidate();
        mInReversalHorizontal = false;
    }

    //图片垂直反转
    private void doReversalVertical(){
        float[] floats = new float[] { 1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f };
        Matrix tmpMatrix = new Matrix();
        tmpMatrix.setValues(floats);
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
                mBitmap.getHeight(), tmpMatrix, true);
        invalidate();
        mInReversalVertical = true;
    }

    private boolean canStickerMove(float cx, float cy) {
        float px = cx + mPoints[8];
        float py = cy + mPoints[9];
        return mViewRect.contains(px, py);
    }

    private float caculateLength(float x, float y) {
        float ex = x - mPoints[8];
        float ey = y - mPoints[9];
        return (float)Math.sqrt(ex*ex + ey*ey);
    }

    private float rotation(MotionEvent event) {
        float  originDegree = calculateDegree(mLastPointX, mLastPointY);
        float nowDegree = calculateDegree(event.getX(), event.getY());
        return nowDegree - originDegree;
    }

    private float calculateDegree(float x, float y) {
        double delta_x = x - mPoints[8];
        double delta_y = y - mPoints[9];
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    public interface OnStickerDeleteListener {
        void onDelete();
    }

    public void setOnStickerDeleteListener(OnStickerDeleteListener listener) {
        mOnStickerDeleteListener = listener;
    }

    public interface OnStickerEditListener {
        void onEdit();
    }

    public void setOnStickerEditListener(OnStickerEditListener listener) {
        mOnStickerEditListener = listener;
    }


    private void doEditSticker() {

        Log.d("Edit called", "Edit Called");
        if (mOnStickerEditListener != null) {
            mOnStickerEditListener.onEdit();
        }
    }

    public void changeColor(int color) {
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        new Canvas(this.mBitmap).drawBitmap(this.mBitmap, 0.0f, 0.0f, paint);
        postInvalidate();
    }

    public RectF getContentRect() {
        return this.mContentRect;
    }

    public String getFont() {
        return this.font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    public int getAlign() {
        return this.align;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    public int getCircle() {
        return this.circle;
    }

    public void setCircle(int circle) {
        this.circle = circle;
    }

    public boolean isDrawedit() {
        return this.drawedit;
    }

    public void setDrawedit(boolean drawedit) {
        this.drawedit = drawedit;
    }


}
