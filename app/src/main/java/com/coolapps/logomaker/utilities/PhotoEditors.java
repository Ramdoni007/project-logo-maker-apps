package com.coolapps.logomaker.utilities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import android.util.Log;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Burhanuddin Rashid on 18/01/2017.
 */

public class PhotoEditors {

    private static final String TAG = PhotoEditors.class.getSimpleName();
    private Context context;
    private RelativeLayout parentView;

    public PhotoEditors(Context context, LogoEditorView parentView) {
        this.context = context;
        this.parentView = parentView;

    }



    public interface OnSaveListener {
        void onSuccess(@NonNull String imagePath);

        void onFailure(@NonNull Exception exception);
    }

    @SuppressLint("StaticFieldLeak")
    @RequiresPermission(allOf = {Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void saveImage(@NonNull final String imagePath, @NonNull final OnSaveListener onSaveListener) {
        Log.d(TAG, "Image Path: " + imagePath);
        new AsyncTask<String, String, Exception>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                parentView.setDrawingCacheEnabled(false);
            }

            @SuppressLint("MissingPermission")
            @Override
            protected Exception doInBackground(String... strings) {
                // Create a media file name
                File file = new File(imagePath);
                try {
                    FileOutputStream out = new FileOutputStream(file, false);
                    if (parentView != null) {
                        parentView.setDrawingCacheEnabled(true);
                        Bitmap drawingCache = parentView.getDrawingCache();
                        drawingCache.compress(Bitmap.CompressFormat.PNG, 100, out);
                    }
                    out.flush();
                    out.close();
                    Log.d(TAG, "Filed Saved Successfully");
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "Failed to save File");
                    return e;
                }
            }

            @Override
            protected void onPostExecute(Exception e) {
                super.onPostExecute(e);
                if (e == null) {
                    onSaveListener.onSuccess(imagePath);
                } else {
                    onSaveListener.onFailure(e);
                }
            }

        }.execute();
    }

    private boolean isSDCARDMounted() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    private static String convertEmoji(String emoji) {
        String returnedEmoji;
        try {
            int convertEmojiToInt = Integer.parseInt(emoji.substring(2), 16);
            returnedEmoji = getEmojiByUnicode(convertEmojiToInt);
        } catch (NumberFormatException e) {
            returnedEmoji = "";
        }
        return returnedEmoji;
    }

    private static String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    public void clearAllViews() {

        parentView.removeAllViews();
    }

}
