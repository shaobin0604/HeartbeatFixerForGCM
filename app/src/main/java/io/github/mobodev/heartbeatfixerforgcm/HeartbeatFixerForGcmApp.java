package io.github.mobodev.heartbeatfixerforgcm;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by shaobin on 3/8/15.
 */
public class HeartbeatFixerForGcmApp extends Application {
    static final String TAG = "HeartbeatFixerForGCM";
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault("fonts/GothamRnd-Book.otf", R.attr.fontPath);
    }
}
