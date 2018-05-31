package io.github.mobodev.heartbeatfixerforgcm.ad;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import io.github.mobodev.heartbeatfixerforgcm.R;

/**
 * Created by bshao on 5/13/18.
 */

public class AdvertisementManager {
    private static AdvertisementManager sInstance;

    private Context mContext;
    private SharedPreferences mPreferences;

    private int mLocalAdVersion;

    public static synchronized AdvertisementManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AdvertisementManager(context);
        }
        return sInstance;
    }

    private AdvertisementManager(@NonNull Context context) {
        mContext = context.getApplicationContext();
        mPreferences = mContext.getSharedPreferences("ad", Context.MODE_PRIVATE);

        mLocalAdVersion = mPreferences.getInt("version", 0);
    }


    public void showAdIfNeeded() {
        final Advertisement remoteAdvertisement = getRemoteAdvertisement();
        if (remoteAdvertisement.version > mLocalAdVersion) {
            showAd(remoteAdvertisement);
        }
    }

    private Advertisement getRemoteAdvertisement() {

    }

    private void showAd(Advertisement advertisement) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .setContentTitle(advertisement.title)
                .setContentText(advertisement.message)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(null)
                .setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(null))
                .setContentIntent(PendingIntent.getActivity(mContext, 0, new Intent(Intent.ACTION_VIEW), PendingIntent.FLAG_UPDATE_CURRENT));
        NotificationManagerCompat.from(mContext).notify(0, builder.build());

        mLocalAdVersion = advertisement.version;
        mPreferences.edit().putInt("version", mLocalAdVersion).apply();
    }
}
