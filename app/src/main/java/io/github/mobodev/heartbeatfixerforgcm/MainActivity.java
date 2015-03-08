package io.github.mobodev.heartbeatfixerforgcm;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends ActionBarActivity implements CompoundButton.OnCheckedChangeListener {
    private SwitchCompat mSwitch;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_actionbar));

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new SettingsFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_switch);
        mSwitch = (SwitchCompat) item.getActionView();
        mSwitch.setOnCheckedChangeListener(this);
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
        toastHeartbeatFixerState(isChecked);
    }

    private void toastHeartbeatFixerState(boolean enabled) {
        final int msgResId = enabled ? R.string.toast_heartbeat_fixer_on : R.string.toast_heartbeat_fixer_off;
        Crouton.makeText(MainActivity.this, msgResId, Style.INFO, R.id.container).show();
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
