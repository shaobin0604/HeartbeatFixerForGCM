package io.github.mobodev.heartbeatfixerforgcm.ad;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by bshao on 6/1/18.
 */

public class AnalyticsReceiver extends BroadcastReceiver {
    static final String ACTION_CLICK = "action_click";
    static final String ACTION_DELETE = "action_delete";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }

        final FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
        final String action = intent.getAction();

        Advertisement advertisement = intent.getParcelableExtra("ad");
        Bundle bundle = new Bundle();
        bundle.putInt("version", advertisement.version);

        if (ACTION_CLICK.equals(action)) {
            analytics.logEvent("push_ad_click", bundle);
            final Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(advertisement.landingPageUrl));
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        } else if (ACTION_DELETE.equals(action)) {
            analytics.logEvent("push_ad_delete", bundle);
        }
    }
}
