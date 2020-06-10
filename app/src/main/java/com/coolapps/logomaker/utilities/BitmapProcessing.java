package com.coolapps.logomaker.utilities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.exifinterface.media.ExifInterface;

/**
 * Created by apple on 2/20/18.
 */

public class BitmapProcessing {

    public static Bitmap resizeBitmap(Bitmap base, int width, int height) {
        try {
            Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Paint p = new Paint();
            p.setAntiAlias(true);
            p.setFilterBitmap(true);
            p.setDither(true);
            new Canvas(result).drawBitmap(base, new Rect(0, 0, base.getWidth(), base.getHeight()), new Rect(0, 0, width, height), p);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return base;
        }
    }
    public static Bitmap modifyOrientation(Bitmap bitmap, String image_url) {
        try {
            switch (new ExifInterface(image_url).getAttributeInt("Orientation", 1)) {
//                case R.styleable.View_paddingEnd /*2*/:
//                    return flip(bitmap, true, false);
//                case R.styleable.View_paddingStart /*3*/:
//                    return rotate(bitmap, 180.0f);
//                case R.styleable.View_theme /*4*/:
//                    return flip(bitmap, false, true);
//                case R.styleable.Toolbar_contentInsetEndWithActions /*6*/:
//                    return rotate(bitmap, 90.0f);
//                case R.styleable.Toolbar_contentInsetRight /*8*/:
//                    return rotate(bitmap, 270.0f);
                default:
                    return bitmap;
            }
        } catch (Exception e) {
            return bitmap;
        } catch (OutOfMemoryError e2) {
            return bitmap;
        }
    }


}
