package io.github.mobodev.heartbeatfixerforgcm;

import android.app.Application;

import org.solovyev.android.checkout.Billing;
import org.solovyev.android.checkout.Cache;
import org.solovyev.android.checkout.Checkout;
import org.solovyev.android.checkout.ProductTypes;
import org.solovyev.android.checkout.Products;

import java.util.Arrays;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by shaobin on 3/8/15.
 */
public class HeartbeatFixerForGcmApp extends Application {
    static final String TAG = "HeartbeatFixerForGCM";
//    private final Billing billing = new Billing(this, new Billing.DefaultConfiguration() {
//        @Override
//        public String getPublicKey() {
//            return null;
//        }
//    });
//    private final Checkout checkout = Checkout.forApplication(billing, Products.create().add(ProductTypes.IN_APP, Arrays.asList("product")));

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault("fonts/GothamRnd-Book.otf", R.attr.fontPath);
    }
}
