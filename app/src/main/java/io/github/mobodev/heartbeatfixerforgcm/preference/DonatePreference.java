package io.github.mobodev.heartbeatfixerforgcm.preference;

import android.content.Context;
import android.preference.Preference;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;

import io.github.mobodev.heartbeatfixerforgcm.DialogHelper;
import io.github.mobodev.heartbeatfixerforgcm.R;

/**
 * Created by shaobin on 3/9/15.
 */
public class DonatePreference extends Preference {
    public DonatePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DonatePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DonatePreference(Context context) {
        super(context);
        init();
    }

    private void init() {
        setTitle(R.string.pref_donate_title);
        setSummary(R.string.pref_donate_summary);
    }


    @Override
    protected void onClick() {
        DialogHelper.showDonateDialog((ActionBarActivity)getContext());
    }
}
