package io.github.mobodev.heartbeatfixerforgcm;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;

import io.github.mobodev.heartbeatfixerforgcm.utils.AndroidVersionUtils;

/**
 * Created by shaobin on 3/8/15.
 */
public class HeartbeatFixerUtils {
    private static final String PREF_KEY_FIXER_STATE = "fixer_state";

    private HeartbeatFixerUtils() {}

    public static void scheduleHeartbeatRequest(Context context) {
        if (!isHeartbeatFixerEnabled(context)) {
            cancelHeartbeatRequest(context);
            return;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            int intervalMillis;
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                intervalMillis = getHeartbeatIntervalMillisForWifi(context);
            } else {
                intervalMillis = getHeartbeatIntervalMillisForMobile(context);
            }
            setNextHeartbeatRequest(context, intervalMillis);
        } else {
            cancelHeartbeatRequest(context);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void setNextHeartbeatRequest(Context context, int intervalMillis) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long triggerAtMillis = System.currentTimeMillis() + intervalMillis;
        PendingIntent broadcastPendingIntent = getBroadcastPendingIntent(context);
        int rtcWakeup = AlarmManager.RTC_WAKEUP;
        if (AndroidVersionUtils.hasKitkat()) {
            alarmManager.setExact(rtcWakeup, triggerAtMillis, broadcastPendingIntent);
        } else {
            alarmManager.set(rtcWakeup, triggerAtMillis, broadcastPendingIntent);
        }
    }

    private static void cancelHeartbeatRequest(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getBroadcastPendingIntent(context));
    }

    private static PendingIntent getBroadcastPendingIntent(Context context) {
        return PendingIntent.getBroadcast(context, 0, new Intent(context, HeartbeatReceiver.class), 0);
    }

    public static void setHeartbeatFixerEnabled(Context context, boolean enabled) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putBoolean(PREF_KEY_FIXER_STATE, enabled).commit();
        if (enabled) {
            sendHeartbeatRequest(context);
        } else {
            cancelHeartbeatRequest(context);
        }
    }

    public static boolean isHeartbeatFixerEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_KEY_FIXER_STATE, false);
    }

    public static int getHeartbeatIntervalMillisForWifi(Context context) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("pref_gcm_heartbeat_interval_wifi", "5")) * 60000;
    }

    public static int getHeartbeatIntervalMillisForMobile(Context context) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("pref_gcm_heartbeat_interval_mobile", "5")) * 60000;
    }

    public static void sendHeartbeatRequest(Context context) {
        context.sendBroadcast(new Intent(context, HeartbeatReceiver.class));
    }

}
