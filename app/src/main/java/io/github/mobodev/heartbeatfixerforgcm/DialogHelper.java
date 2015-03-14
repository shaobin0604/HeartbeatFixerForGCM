package io.github.mobodev.heartbeatfixerforgcm;

import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by shaobin on 3/14/15.
 */
public class DialogHelper {
    public static final String TAG_FRAGMENT_DONATION = "dialog_donate";

    public static void showDonateDialog(@NonNull ActionBarActivity activity) {
        showDialog(activity, DonateDialogFragment.class, TAG_FRAGMENT_DONATION);
    }

    private static void showDialog(@NonNull ActionBarActivity activity,
                                   @NonNull Class clazz,
                                   @NonNull String tag) {
        try {
            showDialog(activity, (DialogFragment) clazz.newInstance(), tag);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void showDialog(@NonNull ActionBarActivity activity,
                                   @NonNull DialogFragment fragment,
                                   @NonNull String tag) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new RuntimeException("Should be called on the main thread");
        }

        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(tag);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        fragment.show(ft, tag);
    }
}
