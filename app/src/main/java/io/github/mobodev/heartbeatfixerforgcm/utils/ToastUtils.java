package io.github.mobodev.heartbeatfixerforgcm.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Toast;

public class ToastUtils {

    /**
     * Shows toast message with given message shortly.
     *
     * @param text message to show
     * @see #showLong(android.content.Context, CharSequence)
     */
    @NonNull
    public static Toast showShort(@NonNull Context context, @NonNull CharSequence text) {
        return show(context, text, Toast.LENGTH_SHORT);
    }

    @NonNull
    public static Toast showShort(@NonNull Context context, @StringRes int stringRes) {
        return showShort(context, context.getString(stringRes));
    }

    /**
     * Shows toast message with given message for a long time.
     *
     * @param text message to show
     * @see #showShort(android.content.Context, CharSequence)
     */
    @NonNull
    public static Toast showLong(@NonNull Context context, @NonNull CharSequence text) {
        return show(context, text, Toast.LENGTH_LONG);
    }

    @NonNull
    public static Toast showLong(@NonNull Context context, @StringRes int stringRes) {
        return showLong(context, context.getString(stringRes));
    }

    @NonNull
    private static Toast show(@NonNull Context context, CharSequence text, int duration) {
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        return toast;
    }

    /**
     * A class for showing a sequence of toasts.
     *
     * @author Artem Chepurnoy
     */
    public static class SingleToast {

        private final Context mContext;
        private Toast mToast;

        public SingleToast(@NonNull Context context) {
            mContext = context;
        }

        public void show(CharSequence text, int duration) {
            if (mToast != null) {
                mToast.cancel();
            }

            mToast = ToastUtils.show(mContext, text, duration);
        }

    }

}
