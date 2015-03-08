package io.github.mobodev.heartbeatfixerforgcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

/**
 * Created by shaobin on 3/9/15.
 */
public class NetworkStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            Log.d(HeartbeatFixerForGcmApp.TAG, "NetworkStateReceiver, intent: " + intent);
            HeartbeatFixerUtils.scheduleHeartbeatRequest(context);
        }
    }
}
