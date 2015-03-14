package io.github.mobodev.heartbeatfixerforgcm;

import android.support.annotation.NonNull;

/**
 * Created by shaobin on 3/15/15.
 */
public final class Build {

    /**
     * Is the current build <b>debug</b> or not.
     */
    public static final boolean DEBUG =
            BuildConfig.MY_DEBUG;

    /**
     * The timestamp of build in {@code EEE MMMM dd HH:mm:ss zzz yyyy} format.
     */
    @NonNull
    public static final String TIME_STAMP =
            BuildConfig.MY_TIME_STAMP;

    /**
     * Uncrypted Google Play's public key.
     *
     * @see #GOOGLE_PLAY_PUBLIC_KEY_SALT
     */
    @NonNull
    public static final String GOOGLE_PLAY_PUBLIC_KEY_ENCRYPTED =
            BuildConfig.MY_GOOGLE_PLAY_PUBLIC_KEY;

    /**
     * Salt for {@link #GOOGLE_PLAY_PUBLIC_KEY_ENCRYPTED}
     *
     * @see #GOOGLE_PLAY_PUBLIC_KEY_ENCRYPTED
     */
    @NonNull
    public static final String GOOGLE_PLAY_PUBLIC_KEY_SALT =
            BuildConfig.MY_GOOGLE_PLAY_PUBLIC_KEY_SALT;

    /**
     * The oficial e-mail for tons of complains, billions of
     * "How to uninistall?" screams and one or two useful emails.
     */
    @NonNull
    public static final String SUPPORT_EMAIL =
            "yutouji0917@gmail.com";

}