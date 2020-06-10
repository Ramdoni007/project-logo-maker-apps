package com.coolapps.logomaker.utilities;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Build.VERSION;
import java.io.IOException;
import java.io.InputStream;

public class BitmapLoader {
    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static InputStream istr;
    public static Bitmap load(Context r11, int[] r12, String r13) {
        /*
        r0 = new android.graphics.BitmapFactory$Options;	 Catch:{ Exception -> 0x0051, OutOfMemoryError -> 0x004e }
        r0.<init>();	 Catch:{ Exception -> 0x0051, OutOfMemoryError -> 0x004e }
        r9 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0051, OutOfMemoryError -> 0x004e }
        r10 = 11;
        if (r9 >= r10) goto L_0x0019;
    L_0x000b:
        r9 = r0.getClass();	 Catch:{ Exception -> 0x0053, OutOfMemoryError -> 0x004e }
        r10 = "inNativeAlloc";
        r1 = r9.getField(r10);	 Catch:{ Exception -> 0x0053, OutOfMemoryError -> 0x004e }
        r9 = 1;
        r1.setBoolean(r0, r9);	 Catch:{ Exception -> 0x0053, OutOfMemoryError -> 0x004e }
    L_0x0019:
        r9 = 1;
        r0.inJustDecodeBounds = r9;	 Catch:{ Exception -> 0x0051, OutOfMemoryError -> 0x004e }
        r9 = 0;
        r0.inScaled = r9;	 Catch:{ Exception -> 0x0051, OutOfMemoryError -> 0x004e }
        android.graphics.BitmapFactory.decodeFile(r13, r0);	 Catch:{ Exception -> 0x0051, OutOfMemoryError -> 0x004e }
        r6 = 1;
        r8 = r0.outWidth;	 Catch:{ Exception -> 0x0051, OutOfMemoryError -> 0x004e }
        r7 = r0.outHeight;	 Catch:{ Exception -> 0x0051, OutOfMemoryError -> 0x004e }
        r9 = 0;
        r5 = r12[r9];	 Catch:{ Exception -> 0x0051, OutOfMemoryError -> 0x004e }
        r9 = 1;
        r4 = r12[r9];	 Catch:{ Exception -> 0x0051, OutOfMemoryError -> 0x004e }
        if (r7 > r4) goto L_0x0031;
    L_0x002f:
        if (r8 <= r5) goto L_0x0040;
    L_0x0031:
        r3 = r8 / 2;
        r2 = r7 / 2;
    L_0x0035:
        r9 = r2 / r6;
        if (r9 <= r4) goto L_0x0040;
    L_0x0039:
        r9 = r3 / r6;
        if (r9 <= r5) goto L_0x0040;
    L_0x003d:
        r6 = r6 * 2;
        goto L_0x0035;
    L_0x0040:
        r0.inSampleSize = r6;	 Catch:{ Exception -> 0x0051, OutOfMemoryError -> 0x004e }
        r9 = 0;
        r0.inJustDecodeBounds = r9;	 Catch:{ Exception -> 0x0051, OutOfMemoryError -> 0x004e }
        r9 = android.graphics.BitmapFactory.decodeFile(r13, r0);	 Catch:{ Exception -> 0x0051, OutOfMemoryError -> 0x004e }
        r9 = com.pavahainc.doodlecrown.bitmap.BitmapProcessing.modifyOrientation(r9, r13);	 Catch:{ Exception -> 0x0051, OutOfMemoryError -> 0x004e }
    L_0x004d:
        return r9;
    L_0x004e:
        r9 = move-exception;
    L_0x004f:
        r9 = 0;
        goto L_0x004d;
    L_0x0051:
        r9 = move-exception;
        goto L_0x004f;
    L_0x0053:
        r9 = move-exception;
        goto L_0x0019;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.pavahainc.doodlecrown.bitmap.BitmapLoader.load(android.content.Context, int[], java.lang.String):android.graphics.Bitmap");
    }

    public static Bitmap loadFromAsset(Context context, int[] holderDimension, String image_url) {
        Bitmap bm = null;
        Options options = new Options();
        if (VERSION.SDK_INT < 11) {
            try {
                options.getClass().getField("inNativeAlloc").setBoolean(options, true);
            } catch (Exception e) {
            }
        }
        options.inJustDecodeBounds = true;
        options.inScaled = false;
        AssetManager assetManager = context.getAssets();
        try {
            istr = assetManager.open(image_url);
            BitmapFactory.decodeStream(istr, null, options);
            istr.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (OutOfMemoryError e3) {
        }
        int inSampleSize = 1;
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        int holderWidth = holderDimension[0];
        int holderHeight = holderDimension[1];
        if (outHeight > holderHeight || outWidth > holderWidth) {
            int halfWidth = outWidth / 2;
            int halfHeight = outHeight / 2;
            while (halfHeight / inSampleSize > holderHeight && halfWidth / inSampleSize > holderWidth) {
                inSampleSize *= 2;
            }
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        try {
            istr = assetManager.open(image_url);
            bm = BitmapFactory.decodeStream(istr, null, options);
            istr.close();
            return bm;
        } catch (IOException e4) {
            return bm;
        } catch (OutOfMemoryError e5) {
            return bm;
        }
    }

    public static Bitmap loadFromResource(Context context, int[] holderDimension, int drawable) {
        Bitmap bm = null;
        Options options = new Options();
        if (VERSION.SDK_INT < 11) {
            try {
                options.getClass().getField("inNativeAlloc").setBoolean(options, true);
            } catch (Exception e) {
            }
        }
        options.inJustDecodeBounds = true;
        options.inScaled = false;
        try {
            BitmapFactory.decodeResource(context.getResources(), drawable, options);
        } catch (OutOfMemoryError e2) {
        }
        int inSampleSize = 1;
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        int holderWidth = holderDimension[0];
        int holderHeight = holderDimension[1];
        if (outHeight > holderHeight || outWidth > holderWidth) {
            int halfWidth = outWidth / 2;
            int halfHeight = outHeight / 2;
            while (halfHeight / inSampleSize > holderHeight && halfWidth / inSampleSize > holderWidth) {
                inSampleSize *= 2;
            }
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        try {
            bm = BitmapFactory.decodeResource(context.getResources(), drawable, options);
        } catch (OutOfMemoryError e3) {
        }
        return bm;
    }

    public static Bitmap load(Context context, String image_url) throws Exception {
        Options bitmapOptions = new Options();
        if (VERSION.SDK_INT < 11) {
            try {
                bitmapOptions.getClass().getField("inNativeAlloc").setBoolean(bitmapOptions, true);
            } catch (Exception e) {
            }
        }
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(image_url, bitmapOptions);
        bitmapOptions.inSampleSize = (int) Math.pow(2.0d, Math.floor((double) (((((((float) bitmapOptions.outWidth) * ((float) bitmapOptions.outHeight)) * StickerViewOld.MAX_SCALE_SIZE) / 1024.0f) / 1024.0f) / MemoryManagement.free(context))));
        bitmapOptions.inJustDecodeBounds = false;
        return BitmapProcessing.modifyOrientation(BitmapFactory.decodeFile(image_url, bitmapOptions), image_url);
    }
}
