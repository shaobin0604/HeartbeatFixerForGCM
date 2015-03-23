package io.github.mobodev.heartbeatfixerforgcm.utils;

import android.os.Build;

/**
 * Created by bshao on 2015/2/27.
 */
public class DeviceUtils {
    private DeviceUtils() {}

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasKitkat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
