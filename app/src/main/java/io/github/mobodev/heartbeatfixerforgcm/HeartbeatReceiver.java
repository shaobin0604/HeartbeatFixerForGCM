package io.github.mobodev.heartbeatfixerforgcm;

import static io.github.mobodev.heartbeatfixerforgcm.HeartbeatFixerForGcmApp.TAG;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by shaobin on 3/8/15.
 */
public class HeartbeatReceiver extends BroadcastReceiver {
    private static final Intent GTALK_HEART_BEAT_INTENT = new Intent("com.google.android.intent.action.GTALK_HEARTBEAT");
    private static final Intent MCS_MCS_HEARTBEAT_INTENT = new Intent("com.google.android.intent.action.MCS_HEARTBEAT");

    @Override
    public void onReceive(Context context, Intent intent) {
        context.sendBroadcast(GTALK_HEART_BEAT_INTENT);
        context.sendBroadcast(MCS_MCS_HEARTBEAT_INTENT);
        Log.d(TAG, "HeartbeatReceiver sent heartbeat request");
        HeartbeatFixerUtils.scheduleHeartbeatRequest(context, false);
    }
}
