package io.github.mobodev.heartbeatfixerforgcm;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;

import org.solovyev.android.checkout.ActivityCheckout;
import org.solovyev.android.checkout.Checkout;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import io.github.mobodev.heartbeatfixerforgcm.ui.activities.ActivityBase;
import io.github.mobodev.heartbeatfixerforgcm.utils.DeviceUtils;
import io.github.mobodev.heartbeatfixerforgcm.utils.PackageUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends ActivityBase implements CompoundButton.OnCheckedChangeListener {
    private SwitchCompat mSwitch;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestCheckout();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_actionbar));
        buildTranslucentStatusBar(findViewById(R.id.capture_insets_frame_layout));

        if (savedInstanceState == null) {
            SettingsFragment fragment = new SettingsFragment();
            fragment.setRetainInstance(false);
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    @TargetApi(android.os.Build.VERSION_CODES.LOLLIPOP)
    protected void buildTranslucentStatusBar(View contentRoot) {
        if (DeviceUtils.hasKitkat() && !DeviceUtils.hasLollipop()) {
            setTranslucentStatusFlag(true);
        }

        if (DeviceUtils.hasKitkat()) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
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

    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
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
                sendHearbeatRequestIfAllows();
            } else if (PREF_GCM_HEARTBEAT_INTERVAL_MOBILE.equals(key)) {
                updateIntervalSummaryMobile();
                sendHearbeatRequestIfAllows();
            }
        }

        private void updateIntervalSummaryWifi() {
            mIntervalWifi.setSummary(mIntervalWifi.getEntry());
        }

        private void updateIntervalSummaryMobile() {
            mIntervalMobile.setSummary(mIntervalMobile.getEntry());
        }

        private void sendHearbeatRequestIfAllows() {
            Activity activity = getActivity();
            if (HeartbeatFixerUtils.isHeartbeatFixerEnabled(activity)) {
                HeartbeatFixerUtils.sendHeartbeatRequest(activity);
            }
        }
    }
}
