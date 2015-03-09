package io.github.mobodev.heartbeatfixerforgcm.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import io.github.mobodev.heartbeatfixerforgcm.R;

/**
 * Created by shaobin on 12/22/13.
 */
public class PlayStoreUtils {

    private static final String MARKET_DETAILS_PREFIX = "market://details?id=";
    private static final String WEB_DETAIL_PREFIX = "https://play.google.com/store/apps/details?id=";

    public static String generateAppDetailPageLink(String packageName, boolean isWebPage) {
        return (isWebPage ? WEB_DETAIL_PREFIX : MARKET_DETAILS_PREFIX) + packageName;
    }

    public static void launchAppDetailPage(Context context, String packageName, boolean isWebPage) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(generateAppDetailPageLink(packageName, isWebPage)));
        startActivitySafe(context, intent);
    }

    public static void launchAuthorAppsPage(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:MoboDev"));
        startActivitySafe(context, intent);
    }

    private static void startActivitySafe(Context context, Intent intent) {
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, R.string.toast_play_store_unavailable, Toast.LENGTH_SHORT).show();
        }
    }
}
