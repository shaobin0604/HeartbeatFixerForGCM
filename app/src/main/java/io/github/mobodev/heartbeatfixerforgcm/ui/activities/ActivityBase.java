package io.github.mobodev.heartbeatfixerforgcm.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.solovyev.android.checkout.ActivityCheckout;
import org.solovyev.android.checkout.Checkout;

import io.github.mobodev.heartbeatfixerforgcm.HeartbeatFixerForGcmApp;
import io.github.mobodev.heartbeatfixerforgcm.billing.CheckoutInternal;
import io.github.mobodev.heartbeatfixerforgcm.tests.Check;


/**
 * Created by Artem Chepurnoy on 28.12.2014.
 */
public class ActivityBase extends AppCompatActivity {
    protected ActivityCheckout mCheckout;

    private boolean mCheckoutRequest;

    public void requestCheckout() {
        Check.getInstance().isFalse(mCheckoutRequest);
        mCheckoutRequest = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (mCheckoutRequest) mCheckout = Checkout.forActivity(this, getCheckoutInternal().getCheckout());
        super.onCreate(savedInstanceState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (mCheckout != null) {
            getCheckoutInternal().requestConnect();
            mCheckout.start();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onStop() {
        if (mCheckout != null) {
            mCheckout.stop();
            getCheckoutInternal().requestDisconnect();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mCheckout = null;
        super.onDestroy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mCheckout != null) {
            boolean handled = mCheckout.onActivityResult(requestCode, resultCode, data);
            if (handled) return;
        }

        // Pass to parent.
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Nullable
    public ActivityCheckout getCheckout() {
        return mCheckout;
    }

    private CheckoutInternal getCheckoutInternal() {
        HeartbeatFixerForGcmApp app = (HeartbeatFixerForGcmApp) getApplication();
        return app.getCheckoutInternal();
    }
}
