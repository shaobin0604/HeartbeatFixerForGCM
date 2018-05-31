package io.github.mobodev.heartbeatfixerforgcm;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.solovyev.android.checkout.Inventory;
import org.solovyev.android.checkout.ProductTypes;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import io.github.mobodev.heartbeatfixerforgcm.ad.AdvertisementManager;
import io.github.mobodev.heartbeatfixerforgcm.billing.IabProducts;
import io.github.mobodev.heartbeatfixerforgcm.ui.activities.ActivityBase;
import io.github.mobodev.heartbeatfixerforgcm.utils.DeviceUtils;
import io.github.mobodev.heartbeatfixerforgcm.utils.PackageUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends ActivityBase implements CompoundButton.OnCheckedChangeListener {
    public static final String TAG = "MainActivity";
    public static final String ONCE_TAG_CLICK_AD = "click_ad";
    private final InventoryLoadedListener mInventoryLoadedListener = new InventoryLoadedListener();
    private SwitchCompat mSwitch;
    private Inventory mInventory;
    private boolean mShowAd;
    private AdView mAdView;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestCheckout();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_actionbar));

        if (savedInstanceState == null) {
            SettingsFragment fragment = new SettingsFragment();
            fragment.setRetainInstance(false);
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }

        mInventory = mCheckout.loadInventory();

        MobileAds.initialize(this, "ca-app-pub-5964196502067291~1489720161");

        AdvertisementManager.getInstance(this).showAdIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mInventory.load().whenLoaded(mInventoryLoadedListener);
    }

    protected ViewGroup getRootViewGroup() {
        return (ViewGroup) findViewById(R.id.root_view);
    }

    @TargetApi(android.os.Build.VERSION_CODES.LOLLIPOP)
    protected void buildTranslucentStatusBar(View contentRoot) {
        if (DeviceUtils.hasKitkat() && !DeviceUtils.hasLollipop()) {
            setTranslucentStatusFlag(true);
        }

        if (DeviceUtils.hasKitkat()) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (DeviceUtils.hasLollipop()) {
            setTranslucentStatusFlag(false);
            //getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        contentRoot.setPadding(0, getResources().getDimensionPixelSize(R.dimen.tool_bar_top_padding), 0, 0);
    }

    private void setTranslucentStatusFlag(boolean on) {
        if (DeviceUtils.hasKitkat()) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_switch);
        mSwitch = (SwitchCompat) item.getActionView();
        mSwitch.setOnCheckedChangeListener(this);
        mSwitch.setChecked(HeartbeatFixerUtils.isHeartbeatFixerEnabled(this));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_switch) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        HeartbeatFixerUtils.setHeartbeatFixerEnabled(this, isChecked);
        PackageUtils.setComponentEnabledSetting(this, NetworkStateReceiver.class, isChecked);
        toastHeartbeatFixerState(isChecked);
    }

    private void toastHeartbeatFixerState(boolean enabled) {
        final int msgResId = enabled ? R.string.toast_heartbeat_fixer_on : R.string.toast_heartbeat_fixer_off;
        Crouton.makeText(MainActivity.this, msgResId, Style.INFO, R.id.container).show();
    }

    private void showAd() {
        Log.v(TAG, "showAd");
        if (!mShowAd) {
            mShowAd = true;
            mAdView = new AdView(this);
            mAdView.setAdSize(AdSize.SMART_BANNER);
            mAdView.setAdUnitId("ca-app-pub-5964196502067291/5460955376");
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    Log.v(TAG, "onAdClosed");
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    Log.v(TAG, "onAdFailedToLoad, errorCode: " + errorCode);
                }

                @Override
                public void onAdLeftApplication() {
                    Log.v(TAG, "onAdLeftApplication");
                }

                @Override
                public void onAdOpened() {
                    Log.v(TAG, "onAdOpened");
                }

                @Override
                public void onAdLoaded() {
                    Log.v(TAG, "onAdLoaded");
                }

                @Override
                public void onAdClicked() {
                    Log.v(TAG, "onAdClicked");
                }

                @Override
                public void onAdImpression() {
                    Log.v(TAG, "onAdImpression");
                }
            });
            getRootViewGroup().addView(mAdView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mAdView.loadAd(new AdRequest.Builder().build());
        }
    }

    private void hideAd() {
        Log.v(TAG, "hideAd");
        if (mShowAd) {
            mShowAd = false;
            if (mAdView != null) {
                getRootViewGroup().removeView(mAdView);
            }
        }
    }

    public static class SettingsFragment extends PreferenceFragment implements
            SharedPreferences.OnSharedPreferenceChangeListener {
        public static final String PREF_GCM_HEARTBEAT_INTERVAL_WIFI = "pref_gcm_heartbeat_interval_wifi";
        public static final String PREF_GCM_HEARTBEAT_INTERVAL_MOBILE = "pref_gcm_heartbeat_interval_mobile";
        private ListPreference mIntervalWifi;
        private ListPreference mIntervalMobile;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            mIntervalWifi = (ListPreference) findPreference(PREF_GCM_HEARTBEAT_INTERVAL_WIFI);
            mIntervalMobile = (ListPreference) findPreference(PREF_GCM_HEARTBEAT_INTERVAL_MOBILE);
        }

        @Override
        public void onResume() {
            super.onResume();
            updateIntervalSummaryWifi();
            updateIntervalSummaryMobile();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (PREF_GCM_HEARTBEAT_INTERVAL_WIFI.equals(key)) {
                updateIntervalSummaryWifi();
                sendHeartbeatRequestIfAllows();
            } else if (PREF_GCM_HEARTBEAT_INTERVAL_MOBILE.equals(key)) {
                updateIntervalSummaryMobile();
                sendHeartbeatRequestIfAllows();
            }
        }

        private void updateIntervalSummaryWifi() {
            mIntervalWifi.setSummary(mIntervalWifi.getEntry());
        }

        private void updateIntervalSummaryMobile() {
            mIntervalMobile.setSummary(mIntervalMobile.getEntry());
        }

        private void sendHeartbeatRequestIfAllows() {
            Activity activity = getActivity();
            if (HeartbeatFixerUtils.isHeartbeatFixerEnabled(activity)) {
                HeartbeatFixerUtils.sendHeartbeatRequest(activity);
            }
        }
    }

    private class InventoryLoadedListener implements Inventory.Listener {

        @Override
        public void onLoaded(@NonNull Inventory.Products products) {
            final Inventory.Product product = products.get(ProductTypes.IN_APP);

            if (isPurchased(product)) {
                hideAd();
            } else {
                showAd();
            }
        }

        private boolean isPurchased(Inventory.Product product) {
            if (!product.supported) {
                return false;
            }

            for (String sku : IabProducts.PRODUCT_LIST) {
                if (product.isPurchased(sku)) {
                    return true;
                }
            }

            return false;
        }

    }
}
