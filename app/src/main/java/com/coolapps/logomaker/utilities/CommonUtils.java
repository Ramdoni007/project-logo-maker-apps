package com.coolapps.logomaker.utilities;

public class CommonUtils {
    private static final int KEY_PREVENT_TS = -20000;
    private static String TAG = "CommonUtils";
    private static long lastClickTime;
    private static int lastClickViewId;

    public static long calculateLength(CharSequence c) {
        double len = 0.0d;
        for (int i = 0; i < c.length(); i++) {
            int tmp = c.charAt(i);
            if (tmp <= 0 || tmp >= 127) {
                len += 1.0d;
            } else {
                len += 0.5d;
            }
        }
        return Math.round(len);
    }
}
