package io.github.mobodev.heartbeatfixerforgcm.utils;

import android.content.Context;
import android.content.Intent;

/**
 * Created by shaobin on 1/24/15.
 */
public class ActivityUtils {
    private ActivityUtils() {}

    public static void startActivitySafe(Context context, Intent intent) {
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            // ignore
            e.printStackTrace();
        }
    }
}
