package io.github.mobodev.heartbeatfixerforgcm.utils;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by shaobin on 3/14/15.
 */
public class ViewUtils {
    private ViewUtils() {}

    public static void setVisible(@NonNull View view, boolean visible) {
        setVisible(view, visible, View.GONE);
    }

    public static void setVisible(@NonNull View view, boolean visible, int invisibleFlag) {
        int visibility = view.getVisibility();
        int visibilityNew = visible ? View.VISIBLE : invisibleFlag;

        if (visibility != visibilityNew) {
            view.setVisibility(visibilityNew);
        }
    }
}
