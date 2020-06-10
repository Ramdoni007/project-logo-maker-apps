package com.coolapps.logomaker.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import androidx.core.view.MotionEventCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.ImageView;


import com.coolapps.logomaker.R;

import yuku.ambilwarna.BuildConfig;
@SuppressLint("AppCompatCustomView")
public class BubbleTextView extends ImageView {
    private static final float BITMAP_SCALE = 0.7f;
    private static final String TAG = "BubbleTextView";
    private float MAX_SCALE;
    private float MIN_SCALE;
    private float baseline;
    private final long bubbleId;
    private Canvas canvasText;
    Context context;
    private final String defaultStr;
    private Bitmap deleteBitmap;
    private int deleteBitmapHeight;
    private int deleteBitmapWidth;
    private DisplayMetrics dm;
    private final long doubleClickTimeLimit;
    private Rect dst_delete;
    private Rect dst_flipV;
    private Rect dst_resize;
    private Rect dst_size_down;
    private Rect dst_top;
    private Bitmap flipVBitmap;
    private int flipVBitmapHeight;
    private int flipVBitmapWidth;
    private FontMetrics fm;
    private final int fontColor;
    private double halfDiagonalLength;
    private boolean isDown;
    private boolean isInBitmap;
    private boolean isInEdit;
    private boolean isInResize;
    private boolean isInSide;
    boolean isInit;
    private boolean isMove;
    private boolean isPointerDown;
    private boolean isTop;
    private boolean isUp;
    private float lastLength;
    private float lastRotateDegree;
    private float lastX;
    private float lastY;
    private Paint localPaint;
    private Bitmap mBitmap;
    private final float mDefaultMargin;
    private final float mDefultSize;
    private TextPaint mFontPaint;
    private float mFontSize;
    private float mMargin;
    private final float mMaxFontSize;
    private final float mMinFontSize;
    private int mScreenHeight;
    private int mScreenwidth;
    private String mStr;
    private Matrix matrix;
    private PointF mid;
    private final float moveLimitDis;
    private float oldDis;
    private OperationListener operationListener;
    private Bitmap originBitmap;
    private float oringinWidth;
    private final float pointerLimitDis;
    private final float pointerZoomCoeff;
    private long preClicktime;
    private Bitmap resizeBitmap;
    private int resizeBitmapHeight;
    private int resizeBitmapWidth;
    private Bitmap topBitmap;
    private int topBitmapHeight;
    private int topBitmapWidth;

    public interface OperationListener {
        void onClick(BubbleTextView bubbleTextView);

        void onDeleteClick();

        void onEdit(BubbleTextView bubbleTextView);

        void onTop(BubbleTextView bubbleTextView);
    }

    public BubbleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mid = new PointF();
        this.isPointerDown = false;
        this.pointerLimitDis = 20.0f;
        this.pointerZoomCoeff = 0.09f;
        this.moveLimitDis = 0.5f;
        this.isInResize = false;
        this.matrix = new Matrix();
        this.isInEdit = true;
        this.MIN_SCALE = 0.5f;
        this.MAX_SCALE = 1.5f;
        this.oringinWidth = 0.0f;
        this.mStr = BuildConfig.FLAVOR;
        this.mDefultSize = 25.0f;
        this.mFontSize = 14.0f;
        this.mMaxFontSize = 30.0f;
        this.mMinFontSize = 20.0f;
        this.mDefaultMargin = 20.0f;
        this.mMargin = 20.0f;
        this.isInit = true;
        this.isDown = false;
        this.isMove = false;
        this.isUp = false;
        this.isTop = true;
        this.doubleClickTimeLimit = 200;
        this.defaultStr = getContext().getString(R.string.double_click_input_text);
        this.fontColor = -16777216;
        this.bubbleId = 0;
        init();
    }

    public BubbleTextView(Context context) {
        super(context);
        this.mid = new PointF();
        this.isPointerDown = false;
        this.pointerLimitDis = 20.0f;
        this.pointerZoomCoeff = 0.09f;
        this.moveLimitDis = 0.5f;
        this.isInResize = false;
        this.matrix = new Matrix();
        this.isInEdit = true;
        this.MIN_SCALE = 0.5f;
        this.MAX_SCALE = 1.5f;
        this.oringinWidth = 0.0f;
        this.mStr = BuildConfig.FLAVOR;
        this.mDefultSize = 25.0f;
        this.mFontSize = 14.0f;
        this.mMaxFontSize = 30.0f;
        this.mMinFontSize = 20.0f;
        this.mDefaultMargin = 20.0f;
        this.mMargin = 20.0f;
        this.isInit = true;
        this.isDown = false;
        this.isMove = false;
        this.isUp = false;
        this.isTop = true;
        this.doubleClickTimeLimit = 200;
        this.defaultStr = getContext().getString(R.string.double_click_input_text);
        this.fontColor = -16777216;
        this.bubbleId = 0;
        init();
    }

    public BubbleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mid = new PointF();
        this.isPointerDown = false;
        this.pointerLimitDis = 20.0f;
        this.pointerZoomCoeff = 0.09f;
        this.moveLimitDis = 0.5f;
        this.isInResize = false;
        this.matrix = new Matrix();
        this.isInEdit = true;
        this.MIN_SCALE = 0.5f;
        this.MAX_SCALE = 1.5f;
        this.oringinWidth = 0.0f;
        this.mStr = BuildConfig.FLAVOR;
        this.mDefultSize = 25.0f;
        this.mFontSize = 14.0f;
        this.mMaxFontSize = 30.0f;
        this.mMinFontSize = 20.0f;
        this.mDefaultMargin = 20.0f;
        this.mMargin = 20.0f;
        this.isInit = true;
        this.isDown = false;
        this.isMove = false;
        this.isUp = false;
        this.isTop = true;
        this.doubleClickTimeLimit = 200;
        this.defaultStr = getContext().getString(R.string.double_click_input_text);
        this.fontColor = -16777216;
        this.bubbleId = 0;
        init();
    }

    public BubbleTextView(Context context, int fontColor, long bubbleId) {
        super(context);
        this.mid = new PointF();
        this.isPointerDown = false;
        this.pointerLimitDis = 20.0f;
        this.pointerZoomCoeff = 0.09f;
        this.moveLimitDis = 0.5f;
        this.isInResize = false;
        this.matrix = new Matrix();
        this.isInEdit = true;
        this.MIN_SCALE = 0.5f;
        this.MAX_SCALE = 1.5f;
        this.oringinWidth = 0.0f;
        this.mStr = BuildConfig.FLAVOR;
        this.mDefultSize = 25.0f;
        this.mFontSize = 14.0f;
        this.mMaxFontSize = 30.0f;
        this.mMinFontSize = 20.0f;
        this.mDefaultMargin = 20.0f;
        this.mMargin = 20.0f;
        this.isInit = true;
        this.isDown = false;
        this.isMove = false;
        this.isUp = false;
        this.isTop = true;
        this.doubleClickTimeLimit = 200;
        this.defaultStr = getContext().getString(R.string.double_click_input_text);
        this.fontColor = fontColor;
        this.bubbleId = bubbleId;
        init();
    }

    private void init() {
        this.dm = getResources().getDisplayMetrics();
        this.dst_delete = new Rect();
        this.dst_resize = new Rect();
        this.dst_flipV = new Rect();
        this.dst_top = new Rect();
        this.dst_size_down = new Rect();
        this.localPaint = new Paint();
        this.localPaint.setColor(getResources().getColor(R.color.red_e73a3d));
        this.localPaint.setAntiAlias(true);
        this.localPaint.setDither(true);
        this.localPaint.setStyle(Style.STROKE);
        this.localPaint.setStrokeWidth(2.0f);
        this.mScreenwidth = this.dm.widthPixels;
        this.mScreenHeight = this.dm.heightPixels;
        this.mFontSize = 25.0f;
        this.mFontPaint = new TextPaint();
        this.mFontPaint.setTextSize(TypedValue.applyDimension(2, this.mFontSize, this.dm));
        this.mFontPaint.setColor(this.fontColor);
        this.mFontPaint.setTextAlign(Align.CENTER);
        this.mFontPaint.setAntiAlias(true);
        this.fm = this.mFontPaint.getFontMetrics();
        this.baseline = this.fm.descent - this.fm.ascent;
        this.isInit = true;
        this.mStr = this.defaultStr;
    }

    protected void onDraw(Canvas canvas) {
        if (this.mBitmap != null) {
            float[] arrayOfFloat = new float[9];
            this.matrix.getValues(arrayOfFloat);
            float f1 = ((0.0f * arrayOfFloat[0]) + (0.0f * arrayOfFloat[1])) + arrayOfFloat[2];
            float f2 = ((0.0f * arrayOfFloat[3]) + (0.0f * arrayOfFloat[4])) + arrayOfFloat[5];
            float f3 = ((arrayOfFloat[0] * ((float) this.mBitmap.getWidth())) + (0.0f * arrayOfFloat[1])) + arrayOfFloat[2];
            float f4 = ((arrayOfFloat[3] * ((float) this.mBitmap.getWidth())) + (0.0f * arrayOfFloat[4])) + arrayOfFloat[5];
            float f5 = ((0.0f * arrayOfFloat[0]) + (arrayOfFloat[1] * ((float) this.mBitmap.getHeight()))) + arrayOfFloat[2];
            float f6 = ((0.0f * arrayOfFloat[3]) + (arrayOfFloat[4] * ((float) this.mBitmap.getHeight()))) + arrayOfFloat[5];
            float f7 = ((arrayOfFloat[0] * ((float) this.mBitmap.getWidth())) + (arrayOfFloat[1] * ((float) this.mBitmap.getHeight()))) + arrayOfFloat[2];
            float f8 = ((arrayOfFloat[3] * ((float) this.mBitmap.getWidth())) + (arrayOfFloat[4] * ((float) this.mBitmap.getHeight()))) + arrayOfFloat[5];
            canvas.save();
            this.mBitmap = this.originBitmap.copy(Config.ARGB_8888, true);
            this.canvasText.setBitmap(this.mBitmap);
            this.canvasText.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
            float left = TypedValue.applyDimension(1, 15.0f, this.dm);
            float scalex = arrayOfFloat[0];
            float skewy = arrayOfFloat[3];
            float rScale = (float) Math.sqrt((double) ((scalex * scalex) + (skewy * skewy)));
            this.mFontPaint.setTextSize(TypedValue.applyDimension(2, this.mFontSize, this.dm));
            String[] texts = autoSplit(this.mStr, this.mFontPaint, ((float) this.mBitmap.getWidth()) - (3.0f * left));
            float top = ((((float) this.mBitmap.getHeight()) - ((((float) texts.length) * (this.baseline + this.fm.leading)) + this.baseline)) / 2.0f) + this.baseline;
            for (String text : texts) {
                if (!TextUtils.isEmpty(text)) {
                    this.canvasText.drawText(text, (float) (this.mBitmap.getWidth() / 2), top, this.mFontPaint);
                    top += this.baseline + this.fm.leading;
                }
            }
            canvas.drawBitmap(this.mBitmap, this.matrix, null);
            this.dst_delete.left = (int) (f3 - ((float) (this.deleteBitmapWidth / 2)));
            this.dst_delete.right = (int) (((float) (this.deleteBitmapWidth / 2)) + f3);
            this.dst_delete.top = (int) (f4 - ((float) (this.deleteBitmapHeight / 2)));
            this.dst_delete.bottom = (int) (((float) (this.deleteBitmapHeight / 2)) + f4);
            this.dst_resize.left = (int) (f7 - ((float) (this.resizeBitmapWidth / 2)));
            this.dst_resize.right = (int) (((float) (this.resizeBitmapWidth / 2)) + f7);
            this.dst_resize.top = (int) (f8 - ((float) (this.resizeBitmapHeight / 2)));
            this.dst_resize.bottom = (int) (((float) (this.resizeBitmapHeight / 2)) + f8);
            this.dst_top.left = (int) (f1 - ((float) (this.topBitmapWidth / 2)));
            this.dst_top.right = (int) (((float) (this.topBitmapWidth / 2)) + f1);
            this.dst_top.top = (int) (f2 - ((float) (this.topBitmapHeight / 2)));
            this.dst_top.bottom = (int) (((float) (this.topBitmapHeight / 2)) + f2);
            this.dst_flipV.left = (int) (f5 - ((float) (this.flipVBitmapWidth / 2)));
            this.dst_flipV.right = (int) (((float) (this.flipVBitmapWidth / 2)) + f5);
            this.dst_flipV.top = (int) (f6 - ((float) (this.flipVBitmapHeight / 2)));
            this.dst_flipV.bottom = (int) (((float) (this.flipVBitmapHeight / 2)) + f6);
            this.dst_size_down.left = (this.mBitmap.getWidth() / 2) - (this.topBitmapWidth / 2);
            this.dst_size_down.right = (this.mBitmap.getWidth() / 2) + (this.topBitmapWidth / 2);
            this.dst_size_down.top = (int) (f6 - ((float) (this.topBitmapHeight / 2)));
            this.dst_size_down.bottom = (int) (((float) (this.topBitmapHeight / 2)) + f6);
            if (this.isInEdit) {
                canvas.drawLine(f1, f2, f3, f4, this.localPaint);
                canvas.drawLine(f3, f4, f7, f8, this.localPaint);
                canvas.drawLine(f5, f6, f7, f8, this.localPaint);
                canvas.drawLine(f5, f6, f1, f2, this.localPaint);
                canvas.drawBitmap(this.deleteBitmap, null, this.dst_delete, null);
                canvas.drawBitmap(this.resizeBitmap, null, this.dst_resize, null);
                canvas.drawBitmap(this.flipVBitmap, null, this.dst_flipV, null);
                canvas.drawBitmap(this.topBitmap, null, this.dst_top, null);
            }
            canvas.restore();
        }
    }

    public void setText(String text) {
        this.mStr = text;
        invalidate();
    }

    public void setFont(Typeface font) {
        this.mFontPaint.setTypeface(font);
    }

    public void setImageResource(int resId) {
        this.matrix.reset();
        setBitmap(BitmapFactory.decodeResource(getResources(), resId));
    }

    public void setImageBitmap(Bitmap b) {
        this.matrix.reset();
        setBitmap(b);
    }

    public void setImageResource(int resId, BubblePropertyModel model) {
        this.matrix.reset();
        setBitmap(BitmapFactory.decodeResource(getResources(), resId), model);
    }

    public void setBitmap(Bitmap bitmap, BubblePropertyModel model) {
        this.mFontSize = 25.0f;
        this.originBitmap = bitmap;
        this.mBitmap = this.originBitmap.copy(Config.ARGB_8888, true);
        this.canvasText = new Canvas(this.mBitmap);
        setDiagonalLength();
        initBitmaps();
        int w = this.mBitmap.getWidth();
        int h = this.mBitmap.getHeight();
        this.oringinWidth = (float) w;
        this.mStr = model.getText();
        float scale = (model.getScaling() * ((float) this.mScreenwidth)) / ((float) this.mBitmap.getWidth());
        if (scale > this.MAX_SCALE) {
            scale = this.MAX_SCALE;
        } else if (scale < this.MIN_SCALE) {
            scale = this.MIN_SCALE;
        }
        this.matrix.postRotate(-((float) Math.toDegrees((double) model.getDegree())), (float) (w >> 1), (float) (h >> 1));
        this.matrix.postScale(scale, scale, (float) (w >> 1), (float) (h >> 1));
        float midX = model.getxLocation() * ((float) this.mScreenwidth);
        float midY = model.getyLocation() * ((float) this.mScreenwidth);
        float offset = TypedValue.applyDimension(1, 22.0f, this.dm);
        this.matrix.postTranslate((midX - ((((float) w) * scale) / 2.0f)) - offset, (midY - ((((float) h) * scale) / 2.0f)) - offset);
        invalidate();
    }

    public void setBitmap(Bitmap bitmap) {
        this.mFontSize = 25.0f;
        this.originBitmap = bitmap;
        this.mBitmap = this.originBitmap.copy(Config.ARGB_8888, true);
        this.canvasText = new Canvas(this.mBitmap);
        setDiagonalLength();
        initBitmaps();
        int w = this.mBitmap.getWidth();
        int h = this.mBitmap.getHeight();
        this.oringinWidth = (float) w;
        float topbarHeight = (float) DensityUtils.dip2px(getContext(), 50.0f);
        this.matrix.postTranslate((float) ((this.mScreenwidth / 2) - (w / 2)), (float) ((this.mScreenwidth / 2) - (h / 2)));
        invalidate();
    }

    private void setDiagonalLength() {
        this.halfDiagonalLength = Math.hypot((double) this.mBitmap.getWidth(), (double) this.mBitmap.getHeight()) / 2.0d;
    }

    private void initBitmaps() {
        float minWidth = (float) (this.mScreenwidth / 8);
        if (((float) this.mBitmap.getWidth()) < minWidth) {
            this.MIN_SCALE = 1.0f;
        } else {
            this.MIN_SCALE = (1.0f * minWidth) / ((float) this.mBitmap.getWidth());
        }
        if (this.mBitmap.getWidth() > this.mScreenwidth) {
            this.MAX_SCALE = 1.0f;
        } else {
            this.MAX_SCALE = (((float) this.mScreenwidth) * 1.0f) / ((float) this.mBitmap.getWidth());
        }
        this.topBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_top_enable);
        this.deleteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_deleteicon);
        this.flipVBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_flipicon);
        this.resizeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_resize);
        this.deleteBitmapWidth = (int) (((float) this.deleteBitmap.getWidth()) * BITMAP_SCALE);
        this.deleteBitmapHeight = (int) (((float) this.deleteBitmap.getHeight()) * BITMAP_SCALE);
        this.resizeBitmapWidth = (int) (((float) this.resizeBitmap.getWidth()) * BITMAP_SCALE);
        this.resizeBitmapHeight = (int) (((float) this.resizeBitmap.getHeight()) * BITMAP_SCALE);
        this.flipVBitmapWidth = (int) (((float) this.flipVBitmap.getWidth()) * BITMAP_SCALE);
        this.flipVBitmapHeight = (int) (((float) this.flipVBitmap.getHeight()) * BITMAP_SCALE);
        this.topBitmapWidth = (int) (((float) this.topBitmap.getWidth()) * BITMAP_SCALE);
        this.topBitmapHeight = (int) (((float) this.topBitmap.getHeight()) * BITMAP_SCALE);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        boolean handled = true;
        this.isInBitmap = false;
        switch (action) {
            case 0:
                if (!isInButton(event, this.dst_delete)) {
                    if (!isInResize(event)) {
                        if (!isInButton(event, this.dst_flipV)) {
                            if (!isInButton(event, this.dst_top)) {
                                if (!isInBitmap(event)) {
                                    handled = false;
                                    break;
                                }
                                this.isInSide = true;
                                this.lastX = event.getX(0);
                                this.lastY = event.getY(0);
                                this.isDown = true;
                                this.isMove = false;
                                this.isPointerDown = false;
                                this.isUp = false;
                                this.isInBitmap = true;
                                long currentTime = System.currentTimeMillis();
                                Log.d(TAG, (currentTime - this.preClicktime) + BuildConfig.FLAVOR);
                                if (currentTime - this.preClicktime <= 200 && this.isInEdit && this.operationListener != null) {
                                    this.operationListener.onClick(this);
                                    break;
                                }
                                this.preClicktime = currentTime;
                                break;
                            }
                            bringToFront();
                            if (this.operationListener != null) {
                                this.operationListener.onTop(this);
                            }
                            this.isDown = false;
                            break;
                        }
                        this.mFontSize += 1.0f;
                        invalidate();
                        break;
                    }
                    this.isInResize = true;
                    this.lastRotateDegree = rotationToStartPoint(event);
                    midPointToStartPoint(event);
                    this.lastLength = diagonalLength(event);
                    this.isDown = false;
                    break;
                }
                if (this.operationListener != null) {
                    this.operationListener.onDeleteClick();
                }
                this.isDown = false;
                break;
            case 1:
            case 3:
                this.isInResize = false;
                this.isInSide = false;
                this.isPointerDown = false;
                this.isUp = true;
                break;
            case 2:
                float scale;
                if (!this.isPointerDown) {
                    if (!this.isInResize && this.isInSide) {
                        float x = event.getX(0);
                        float y = event.getY(0);
                        this.isMove = this.isMove || Math.abs(x - this.lastX) >= 0.5f || Math.abs(y - this.lastY) >= 0.5f;
                        this.matrix.postTranslate(x - this.lastX, y - this.lastY);
                        this.lastX = x;
                        this.lastY = y;
                        invalidate();
                        break;
                    }
                    this.matrix.postRotate((rotationToStartPoint(event) - this.lastRotateDegree) * 2.0f, this.mid.x, this.mid.y);
                    this.lastRotateDegree = rotationToStartPoint(event);
                    scale = diagonalLength(event) / this.lastLength;
                    if ((((double) diagonalLength(event)) / this.halfDiagonalLength > ((double) this.MIN_SCALE) || scale >= 1.0f) && (((double) diagonalLength(event)) / this.halfDiagonalLength < ((double) this.MAX_SCALE) || scale <= 1.0f)) {
                        this.lastLength = diagonalLength(event);
                    } else {
                        scale = 1.0f;
                        if (!isInResize(event)) {
                            this.isInResize = false;
                        }
                    }
                    this.matrix.postScale(scale, scale, this.mid.x, this.mid.y);
                    invalidate();
                    break;
                }
                float disNew = spacing(event);
                if (disNew == 0.0f || disNew < 20.0f) {
                    scale = 1.0f;
                } else {
                    scale = (((disNew / this.oldDis) - 1.0f) * 0.09f) + 1.0f;
                }
                float scaleTemp = (((float) Math.abs(this.dst_flipV.left - this.dst_resize.left)) * scale) / this.oringinWidth;
                if ((scaleTemp > this.MIN_SCALE || scale >= 1.0f) && (scaleTemp < this.MAX_SCALE || scale <= 1.0f)) {
                    this.lastLength = diagonalLength(event);
                } else {
                    scale = 1.0f;
                }
                this.matrix.postScale(scale, scale, this.mid.x, this.mid.y);
                invalidate();
                break;
            case 5:
                if (spacing(event) > 20.0f) {
                    this.oldDis = spacing(event);
                    this.isPointerDown = true;
                    midPointToStartPoint(event);
                } else {
                    this.isPointerDown = false;
                }
                this.isInSide = false;
                this.isInResize = false;
                break;
        }
        if (handled && this.operationListener != null) {
            this.operationListener.onEdit(this);
        }
        return handled;
    }

    public BubblePropertyModel calculate(BubblePropertyModel model) {
        float[] v = new float[9];
        this.matrix.getValues(v);
        Log.d(TAG, "tx : " + v[2] + " ty : " + v[5]);
        float scalex = v[0];
        float skewy = v[3];
        float rScale = (float) Math.sqrt((double) ((scalex * scalex) + (skewy * skewy)));
        Log.d(TAG, "rScale : " + rScale);
        float rAngle = (float) Math.round(Math.atan2((double) v[1], (double) v[0]) * 57.29577951308232d);
        Log.d(TAG, "rAngle : " + rAngle);
        float minX = (float) ((this.dst_top.centerX() + this.dst_resize.centerX()) / 2);
        float minY = (float) ((this.dst_top.centerY() + this.dst_resize.centerY()) / 2);
        Log.d(TAG, "midX : " + minX + " midY : " + minY);
        model.setDegree((float) Math.toRadians((double) rAngle));
        model.setBubbleId(this.bubbleId);
        model.setScaling((((float) this.mBitmap.getWidth()) * rScale) / ((float) this.mScreenwidth));
        Log.d(TAG, " x " + (minX / ((float) this.mScreenwidth)) + " y " + (minY / ((float) this.mScreenwidth)));
        model.setxLocation(minX / ((float) this.mScreenwidth));
        model.setyLocation(minY / ((float) this.mScreenwidth));
        model.setText(this.mStr);
        return model;
    }

    private boolean isInBitmap(MotionEvent event) {
        float[] arrayOfFloat1 = new float[9];
        this.matrix.getValues(arrayOfFloat1);
        float f1 = ((0.0f * arrayOfFloat1[0]) + (0.0f * arrayOfFloat1[1])) + arrayOfFloat1[2];
        float f3 = ((arrayOfFloat1[0] * ((float) this.mBitmap.getWidth())) + (0.0f * arrayOfFloat1[1])) + arrayOfFloat1[2];
        float f4 = ((arrayOfFloat1[3] * ((float) this.mBitmap.getWidth())) + (0.0f * arrayOfFloat1[4])) + arrayOfFloat1[5];
        float f5 = ((0.0f * arrayOfFloat1[0]) + (arrayOfFloat1[1] * ((float) this.mBitmap.getHeight()))) + arrayOfFloat1[2];
        float f6 = ((0.0f * arrayOfFloat1[3]) + (arrayOfFloat1[4] * ((float) this.mBitmap.getHeight()))) + arrayOfFloat1[5];
        float f7 = ((arrayOfFloat1[0] * ((float) this.mBitmap.getWidth())) + (arrayOfFloat1[1] * ((float) this.mBitmap.getHeight()))) + arrayOfFloat1[2];
        float f8 = ((arrayOfFloat1[3] * ((float) this.mBitmap.getWidth())) + (arrayOfFloat1[4] * ((float) this.mBitmap.getHeight()))) + arrayOfFloat1[5];
        float[] arrayOfFloat2 = new float[4];
        float[] arrayOfFloat3 = new float[]{f1, f3, f7, f5};
        arrayOfFloat3[0] = ((0.0f * arrayOfFloat1[3]) + (0.0f * arrayOfFloat1[4])) + arrayOfFloat1[5];
        arrayOfFloat3[1] = f4;
        arrayOfFloat3[2] = f8;
        arrayOfFloat3[3] = f6;
        return pointInRect(arrayOfFloat2, arrayOfFloat3, event.getX(0), event.getY(0));
    }

    private boolean pointInRect(float[] xRange, float[] yRange, float x, float y) {
        double a1 = Math.hypot((double) (xRange[0] - xRange[1]), (double) (yRange[0] - yRange[1]));
        double a2 = Math.hypot((double) (xRange[1] - xRange[2]), (double) (yRange[1] - yRange[2]));
        double a3 = Math.hypot((double) (xRange[3] - xRange[2]), (double) (yRange[3] - yRange[2]));
        double a4 = Math.hypot((double) (xRange[0] - xRange[3]), (double) (yRange[0] - yRange[3]));
        double b1 = Math.hypot((double) (x - xRange[0]), (double) (y - yRange[0]));
        double b2 = Math.hypot((double) (x - xRange[1]), (double) (y - yRange[1]));
        double b3 = Math.hypot((double) (x - xRange[2]), (double) (y - yRange[2]));
        double b4 = Math.hypot((double) (x - xRange[3]), (double) (y - yRange[3]));
        double u1 = ((a1 + b1) + b2) / 2.0d;
        double u2 = ((a2 + b2) + b3) / 2.0d;
        double u3 = ((a3 + b3) + b4) / 2.0d;
        double u4 = ((a4 + b4) + b1) / 2.0d;
        return Math.abs((a1 * a2) - (((Math.sqrt((((u1 - a1) * u1) * (u1 - b1)) * (u1 - b2)) + Math.sqrt((((u2 - a2) * u2) * (u2 - b2)) * (u2 - b3))) + Math.sqrt((((u3 - a3) * u3) * (u3 - b3)) * (u3 - b4))) + Math.sqrt((((u4 - a4) * u4) * (u4 - b4)) * (u4 - b1)))) < 0.5d;
    }

    private boolean isInButton(MotionEvent event, Rect rect) {
        int left = rect.left;
        int right = rect.right;
        int top = rect.top;
        int bottom = rect.bottom;
        return !(event.getX(0) < ((float) left)) && !(event.getX(0) > ((float) right)) && !(event.getY(0) < ((float) top)) && !(event.getY(0) > ((float) bottom));
    }

    private boolean isInResize(MotionEvent event) {
        int top = this.dst_resize.top - 20;
        int right = this.dst_resize.right + 20;
        int bottom = this.dst_resize.bottom + 20;
        return !(event.getX(0) < ((float) (this.dst_resize.left - 20))) && !(event.getX(0) > ((float) right)) && !(event.getY(0) < ((float) top)) && !(event.getY(0) > ((float) bottom));
    }

    private void midPointToStartPoint(MotionEvent event) {
        float[] arrayOfFloat = new float[9];
        this.matrix.getValues(arrayOfFloat);
        this.mid.set(((((arrayOfFloat[0] * 0.0f) + (arrayOfFloat[1] * 0.0f)) + arrayOfFloat[2]) + event.getX(0)) / 2.0f, ((((arrayOfFloat[3] * 0.0f) + (arrayOfFloat[4] * 0.0f)) + arrayOfFloat[5]) + event.getY(0)) / 2.0f);
    }

    private void midDiagonalPoint(PointF paramPointF) {
        float[] arrayOfFloat = new float[9];
        this.matrix.getValues(arrayOfFloat);
        paramPointF.set(((((arrayOfFloat[0] * 0.0f) + (arrayOfFloat[1] * 0.0f)) + arrayOfFloat[2]) + (((arrayOfFloat[0] * ((float) this.mBitmap.getWidth())) + (arrayOfFloat[1] * ((float) this.mBitmap.getHeight()))) + arrayOfFloat[2])) / 2.0f, ((((arrayOfFloat[3] * 0.0f) + (arrayOfFloat[4] * 0.0f)) + arrayOfFloat[5]) + (((arrayOfFloat[3] * ((float) this.mBitmap.getWidth())) + (arrayOfFloat[4] * ((float) this.mBitmap.getHeight()))) + arrayOfFloat[5])) / 2.0f);
    }

    private float rotationToStartPoint(MotionEvent event) {
        float[] arrayOfFloat = new float[9];
        this.matrix.getValues(arrayOfFloat);
        return (float) Math.toDegrees(Math.atan2((double) (event.getY(0) - (((arrayOfFloat[3] * 0.0f) + (arrayOfFloat[4] * 0.0f)) + arrayOfFloat[5])), (double) (event.getX(0) - (((arrayOfFloat[0] * 0.0f) + (arrayOfFloat[1] * 0.0f)) + arrayOfFloat[2]))));
    }

    private float diagonalLength(MotionEvent event) {
        return (float) Math.hypot((double) (event.getX(0) - this.mid.x), (double) (event.getY(0) - this.mid.y));
    }

    private float spacing(MotionEvent event) {
        if (event.getPointerCount() != 2) {
            return 0.0f;
        }
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt((double) ((x * x) + (y * y)));
    }

    public void setOperationListener(OperationListener operationListener) {
        this.operationListener = operationListener;
    }

    public void setInEdit(boolean isInEdit) {
        this.isInEdit = isInEdit;
        invalidate();
    }

    private String[] autoSplit(String content, Paint p, float width) {
        int length = content.length();
        float textWidth = p.measureText(content);
        if (textWidth <= width) {
            return new String[]{content};
        }
        int start = 0;
        int end = 1;
        String[] lineTexts = new String[((int) Math.ceil((double) (textWidth / width)))];
        int i = 0;
        while (start < length) {
            int i2;
            if (p.measureText(content, start, end) > width) {
                i2 = i + 1;
                lineTexts[i] = (String) content.subSequence(start, end);
                start = end;
            } else {
                i2 = i;
            }
            if (end == length) {
                lineTexts[i2] = (String) content.subSequence(start, end);
                return lineTexts;
            }
            end++;
            i = i2;
        }
        return lineTexts;
    }

    public String getmStr() {
        return this.mStr;
    }
}
