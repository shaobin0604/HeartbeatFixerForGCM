package io.github.mobodev.heartbeatfixerforgcm;

import android.app.Application;
import android.support.v4.util.ArrayMap;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.solovyev.android.checkout.ProductTypes;
import org.solovyev.android.checkout.Products;

import java.util.Map;

import io.github.mobodev.heartbeatfixerforgcm.billing.CheckoutInternal;
import io.github.mobodev.heartbeatfixerforgcm.billing.IabProducts;
import jonathanfinerty.once.Once;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by shaobin on 3/8/15.
 */
public class HeartbeatFixerForGcmApp extends Application {
    static final String TAG = "HeartbeatFixerForGCM";

    private CheckoutInternal mCheckoutInternal;

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/GothamRnd-Book.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        mCheckoutInternal = new CheckoutInternal(this, Products.create().add(ProductTypes.IN_APP,
                IabProducts.PRODUCT_LIST));
        Once.initialise(this);
        initFirebaseRemoteConfig();
    }

    public CheckoutInternal getCheckoutInternal() {
        return mCheckoutInternal;
    }


    private void initFirebaseRemoteConfig() {
        final FirebaseRemoteConfig config = FirebaseRemoteConfig.getInstance();
        config.setConfigSettings(new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build());

        Map<String, Object> defaults = new ArrayMap<>(1);
        defaults.put("ad", "{\"title\":\"Insgot - Repost for Instagram\",\"message\":\"Save, repost and share public "
                + "Instagram Photos & Videos in just one click!!!\","
                + "\"largeIconUrl\":\"https://scontent-iad3-1.cdninstagram"
                + ".com/vp/3d38d0a116646360fe0ea55b3653e594/5B9E635B/t51.2885-19/s150x150"
                + "/14719833_310540259320655_1605122788543168512_a.jpg\","
                + "\"bigPictureUrl\":\"https://scontent-iad3-1.cdninstagram"
                + ".com/vp/5209be0209350d926e79098b259158ff/5B887074/t51.2885-15/e35"
                + "/31928266_361115554410794_7648764144841129984_n.jpg\",\"version\":1,"
                + "\"landingPageUrl\":\"market://details?id=com.insgot.ins\"}");

        config.setDefaults(defaults);
    }
}