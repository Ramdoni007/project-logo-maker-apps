package com.coolapps.logomaker.utilities;

import android.content.Context;

public class LOADERCACHE {
    private static LOADERCACHE INSTANCE;
    public static ImageLoader imageLoader;

    public static ImageLoader getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LOADERCACHE();
            imageLoader = new ImageLoader(context);
        }
        return imageLoader;
    }
}
