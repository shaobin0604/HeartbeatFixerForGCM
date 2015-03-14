package io.github.mobodev.heartbeatfixerforgcm;

import android.app.Application;

import org.solovyev.android.checkout.Billing;
import org.solovyev.android.checkout.Checkout;
import org.solovyev.android.checkout.ProductTypes;
import org.solovyev.android.checkout.Products;

import java.util.Arrays;
import java.util.List;

import io.github.mobodev.heartbeatfixerforgcm.billing.CheckoutInternal;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by shaobin on 3/8/15.
 */
public class HeartbeatFixerForGcmApp extends Application {
    static final String TAG = "HeartbeatFixerForGCM";

    private static final List<String> PRODUCT_LIST = Arrays.asList("coffee", "beer", "hamburger", "cake");

    private CheckoutInternal mCheckoutInternal;

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault("fonts/GothamRnd-Book.otf", R.attr.fontPath);
        mCheckoutInternal = new CheckoutInternal(this, Products.create().add(ProductTypes.IN_APP, PRODUCT_LIST));
    }

    public CheckoutInternal getCheckoutInternal() {
        return mCheckoutInternal;
    }
}
