package io.github.mobodev.heartbeatfixerforgcm.utils;

import android.os.Build;

/**
 * Created by bshao on 2015/2/27.
 */
public class AndroidVersionUtils {
    private AndroidVersionUtils() {}
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }
    public static boolean hasKitkat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

}
