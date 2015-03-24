package io.github.mobodev.heartbeatfixerforgcm.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

/**
 * Created by shaobin on 3/24/15.
 */
public class PackageUtils {
    private PackageUtils() {
    }

    public static void setComponentEnabledSetting(@NonNull Context context, @NonNull Class<?> cls, boolean enabled) {
        PackageManager packageManager = context.getPackageManager();
        packageManager.setComponentEnabledSetting(new ComponentName(context, cls),
                enabled ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
