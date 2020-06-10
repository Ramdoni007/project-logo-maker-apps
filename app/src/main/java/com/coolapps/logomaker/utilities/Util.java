package com.coolapps.logomaker.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import yuku.ambilwarna.BuildConfig;

public class Util {
    static int i = 0;

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static boolean assetExists(Context context, String path) {
        try {
            context.getAssets().open(path).close();
            return true;
        } catch (FileNotFoundException e) {
            Log.w("IOUtilities", "assetExists failed: " + e.toString());
            return false;
        } catch (IOException e2) {
            Log.w("IOUtilities", "assetExists failed: " + e2.toString());
            return false;
        }
    }

    public static boolean checkExitFile(String sfile) {
        return new File(sfile).exists();
    }

    public static TextView createTextView(Context context, int x, int y, int w, int h) {
        TextView layoutSave = new TextView(context);
        LayoutParams par1 = new LayoutParams(w, h);
        par1.leftMargin = x;
        par1.topMargin = y;
        par1.gravity = 3;
        layoutSave.setLayoutParams(par1);
        return layoutSave;
    }

    public static TextView createTextViewLEFTCENTER(Context context, int x, int y, int w, int h) {
        TextView layoutSave = new TextView(context);
        LayoutParams par1 = new LayoutParams(w, h);
        par1.leftMargin = x;
        par1.topMargin = y;
        par1.gravity = 19;
        layoutSave.setLayoutParams(par1);
        return layoutSave;
    }

    public static TextView createTextViewCenter(Context context, int x, int y, int w, int h) {
        TextView layoutSave = new TextView(context);
        LayoutParams par1 = new LayoutParams(w, h);
        par1.leftMargin = x;
        par1.topMargin = y;
        par1.gravity = 17;
        layoutSave.setLayoutParams(par1);
        return layoutSave;
    }

    public static ImageView createImageViewBotton(Context context, int x, int y, int w, int h) {
        ImageView layoutSave = new ImageView(context);
        LayoutParams par1 = new LayoutParams(w, h);
        par1.leftMargin = x;
        par1.topMargin = y;
        par1.gravity = 80;
        layoutSave.setLayoutParams(par1);
        return layoutSave;
    }

    public static ImageView createImageView(Context context, int x, int y, int w, int h) {
        ImageView layoutSave = new ImageView(context);
        LayoutParams par1 = new LayoutParams(w, h);
        par1.leftMargin = x;
        par1.topMargin = y;
        par1.gravity = 17;
        layoutSave.setLayoutParams(par1);
        return layoutSave;
    }

    public static ImageView createImageViewNOCENTER(Context context, int x, int y, int w, int h) {
        ImageView layoutSave = new ImageView(context);
        LayoutParams par1 = new LayoutParams(w, h);
        par1.leftMargin = x;
        par1.topMargin = y;
        layoutSave.setLayoutParams(par1);
        return layoutSave;
    }

    public static ImageView createImageViewRightTop(Context context, int x, int y, int w, int h) {
        ImageView layoutSave = new ImageView(context);
        LayoutParams par1 = new LayoutParams(w, h);
        par1.rightMargin = x;
        par1.topMargin = y;
        par1.gravity = 53;
        layoutSave.setLayoutParams(par1);
        return layoutSave;
    }

    public static ImageView createImageViewRight(Context context, int x, int y, int w, int h) {
        ImageView layoutSave = new ImageView(context);
        LayoutParams par1 = new LayoutParams(w, h);
        par1.rightMargin = x;
        par1.topMargin = y;
        par1.gravity = 21;
        layoutSave.setLayoutParams(par1);
        return layoutSave;
    }

    public static ImageView createImageViewLeft(Context context, int x, int y, int w, int h) {
        ImageView layoutSave = new ImageView(context);
        LayoutParams par1 = new LayoutParams(w, h);
        par1.leftMargin = x;
        par1.topMargin = y;
        par1.gravity = 19;
        layoutSave.setLayoutParams(par1);
        return layoutSave;
    }

    public static VideoView createVideoView(Context context, int x, int y, int w, int h) {
        VideoView layoutSave = new VideoView(context);
        LayoutParams par1 = new LayoutParams(w, h);
        par1.leftMargin = x;
        par1.topMargin = y;
        par1.gravity = 17;
        layoutSave.setLayoutParams(par1);
        return layoutSave;
    }

    public static String savePhotoBitmap(Bitmap bmp, String fileSaveNoRoot, String name) {
        Exception e;
        createDirIfNotExists(fileSaveNoRoot);
        File imageFileFolder = new File(Environment.getExternalStorageDirectory(), "/" + fileSaveNoRoot);
        imageFileFolder.mkdir();
        File imageFileName = new File(imageFileFolder, name);
        try {
            FileOutputStream out = new FileOutputStream(imageFileName);
            try {
                bmp.compress(CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
                return imageFileName.toString();
            } catch (Exception e2) {
                e = e2;
                FileOutputStream fileOutputStream = out;
                e.printStackTrace();
                return BuildConfig.FLAVOR;
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return BuildConfig.FLAVOR;
        }
    }

    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getAssets().open(filePath));
        } catch (IOException e) {
        }
        return bitmap;
    }

    public static Button createImageViewButton(Context context, int x, int y, int w, int h) {
        Button layoutSave = new Button(context);
        LayoutParams par1 = new LayoutParams(w, h);
        par1.leftMargin = x;
        par1.topMargin = y;
        par1.gravity = 17;
        layoutSave.setLayoutParams(par1);
        return layoutSave;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        if (bm == null) {
            return null;
        }
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / ((float) width);
        float scaleHeight = ((float) newHeight) / ((float) height);
        if (scaleHeight == 1.0f && scaleWidth == 1.0f) {
            return bm;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        if (bm.isRecycled()) {
            return null;
        }
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        if (bm != null) {
            try {
                if (!bm.isRecycled()) {
                    bm.recycle();
                }
            } catch (Exception e) {
                e.getMessage();
                return null;
            }
        }
        return resizedBitmap;
    }

    public static void recyleBitmap(Bitmap map) {
        if (map != null) {
            try {
                if (!map.isRecycled()) {
                    map.recycle();
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(-1);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    public static Bitmap getBitmapFromAsset(Context context, String filePath, int REQUIRED_SIZE_IMAGE) {
        AssetManager assetManager = context.getAssets();
        try {
            InputStream istr = assetManager.open(filePath);
            Options o = new Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(istr, null, o);
            istr.close();
            int width_tmp = o.outWidth;
            int height_tmp = o.outHeight;
            int scale = 1;
            while (width_tmp / 2 >= REQUIRED_SIZE_IMAGE && height_tmp / 2 >= REQUIRED_SIZE_IMAGE) {
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
            InputStream istr2 = assetManager.open(filePath);
            Options o2 = new Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(istr2, null, o2);
        } catch (IOException e) {
            return null;
        }
    }

    public static Bitmap getBitmapFromViewTranparen(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(0);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    public static LinearLayout createLayerLinerofHO(Context context, int x, int y, int w, int h) {
        LinearLayout layoutSave = new LinearLayout(context);
        LayoutParams par1 = new LayoutParams(w, h);
        par1.leftMargin = x;
        par1.topMargin = y;
        par1.gravity = 19;
        layoutSave.setLayoutParams(par1);
        return layoutSave;
    }

    public static LinearLayout createLayerLineroButton(Context context, int x, int y, int w, int h) {
        LinearLayout layoutSave = new LinearLayout(context);
        LayoutParams par1 = new LayoutParams(w, h);
        par1.leftMargin = x;
        par1.bottomMargin = y;
        par1.gravity = 83;
        layoutSave.setLayoutParams(par1);
        return layoutSave;
    }

    public static FrameLayout createLayer(Context context, int x, int y, int w, int h) {
        FrameLayout layoutSave = new FrameLayout(context);
        LayoutParams par1 = new LayoutParams(w, h);
        par1.leftMargin = x;
        par1.topMargin = y;
        par1.gravity = 17;
        layoutSave.setLayoutParams(par1);
        return layoutSave;
    }

    public static FrameLayout createLayerBottom(Context context, int x, int y, int w, int h) {
        FrameLayout layoutSave = new FrameLayout(context);
        LayoutParams par1 = new LayoutParams(w, h);
        par1.leftMargin = x;
        par1.bottomMargin = y;
        par1.gravity = 80;
        layoutSave.setLayoutParams(par1);
        return layoutSave;
    }

    public static FrameLayout createLayerBottomCenter(Context context, int x, int y, int w, int h) {
        FrameLayout layoutSave = new FrameLayout(context);
        LayoutParams par1 = new LayoutParams(w, h);
        par1.leftMargin = x;
        par1.bottomMargin = y;
        par1.gravity = 81;
        layoutSave.setLayoutParams(par1);
        return layoutSave;
    }

    public static FrameLayout createLayerTop(Context context, int x, int y, int w, int h) {
        FrameLayout layoutSave = new FrameLayout(context);
        LayoutParams par1 = new LayoutParams(w, h);
        par1.leftMargin = x;
        par1.topMargin = y;
        par1.gravity = 48;
        layoutSave.setLayoutParams(par1);
        return layoutSave;
    }

    public static String createNameTime() {
        StringBuilder append = new StringBuilder().append(new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date())).append("_");
        int i=0;
        i = i + 1;
        return append.append(i).toString();
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String file : children) {
                if (!deleteDir(new File(dir, file))) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public static String getPath(Uri uri, Activity activity) {
        String res = null;
        Cursor cursor = activity.getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
        if (cursor.moveToFirst()) {
            res = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
        }
        cursor.close();
        return res;
    }

    public static void copyFile(String inputPath, String outputPath, String nameOut) {
        FileNotFoundException fnfe1;
        OutputStream outputStream;
        Exception e;
        InputStream inputStream;
        try {
            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            InputStream in = new FileInputStream(inputPath);
            try {
                OutputStream out = new FileOutputStream(outputPath + "/" + nameOut);
                try {
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int read = in.read(buffer);
                        if (read != -1) {
                            out.write(buffer, 0, read);
                        } else {
                            in.close();
                            try {
                                out.flush();
                                out.close();
                                return;
                            } catch (FileNotFoundException e2) {
                                fnfe1 = e2;
                                outputStream = out;
                                Log.e("tag", fnfe1.getMessage());
                            } catch (Exception e3) {
                                e = e3;
                                outputStream = out;
                                Log.e("tag", e.getMessage());
                            }
                        }
                    }
                } catch (FileNotFoundException e4) {
                    fnfe1 = e4;
                    outputStream = out;
                    inputStream = in;
                    Log.e("tag", fnfe1.getMessage());
                } catch (Exception e5) {
                    e = e5;
                    outputStream = out;
                    inputStream = in;
                    Log.e("tag", e.getMessage());
                }
            } catch (FileNotFoundException e6) {
                fnfe1 = e6;
                inputStream = in;
                Log.e("tag", fnfe1.getMessage());
            } catch (Exception e7) {
                e = e7;
                inputStream = in;
                Log.e("tag", e.getMessage());
            }
        } catch (FileNotFoundException e8) {
            fnfe1 = e8;
            Log.e("tag", fnfe1.getMessage());
        } catch (Exception e9) {
            e = e9;
            Log.e("tag", e.getMessage());
        }
    }

    public static boolean createDirIfNotExists(String path) {
        File file = new File(Environment.getExternalStorageDirectory(), path);
        if (file.exists() || file.mkdirs()) {
            return true;
        }
        Log.e("TravellerLog :: ", "Problem creating Image folder");
        return false;
    }

    public static boolean createDirIfNotExistsFull(String path) {
        File file = new File(path);
        if (file.exists() || file.mkdirs()) {
            return true;
        }
        Log.e("TravellerLog :: ", "Problem creating Image folder");
        return false;
    }

    public static void closeQuietly(OutputStream output) {
        if (output != null) {
            try {
                output.close();
            } catch (IOException e) {
            }
        }
    }

    public static void closeQuietly(InputStream input) {
        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {
            }
        }
    }

    public static void copyAndClose(InputStream input, OutputStream output) throws IOException {
        copy(input, output);
        closeQuietly(input);
        closeQuietly(output);
    }

    public static void copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
            int n = input.read(buffer);
            if (-1 != n) {
                output.write(buffer, 0, n);
            } else {
                return;
            }
        }
    }

    public static void copyFileFromAssets(Context context, String file, String dest) throws Exception {
        Exception e;
        Throwable th;
        InputStream in = null;
        OutputStream outputStream = null;
        try {
            in = context.getAssets().open(file);
            OutputStream fout = new FileOutputStream(new File(dest));
            try {
                byte[] data = new byte[1024];
                while (true) {
                    int count = in.read(data, 0, 1024);
                    if (count == -1) {
                        break;
                    }
                    fout.write(data, 0, count);
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e2) {
                    }
                }
                if (fout != null) {
                    try {
                        fout.close();
                        outputStream = fout;
                        return;
                    } catch (IOException e3) {
                        outputStream = fout;
                        return;
                    }
                }
            } catch (Exception e4) {
                e = e4;
                outputStream = fout;
                try {
                    e.printStackTrace();
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e5) {
                        }
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e6) {
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e7) {
                        }
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e8) {
                        }
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                outputStream = fout;
                if (in != null) {
                    in.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (Exception e9) {
            e = e9;
            e.printStackTrace();
            if (in != null) {
                in.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
}
