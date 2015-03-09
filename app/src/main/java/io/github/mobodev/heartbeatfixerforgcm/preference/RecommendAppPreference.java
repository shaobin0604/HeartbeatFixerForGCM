package io.github.mobodev.heartbeatfixerforgcm.preference;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import io.github.mobodev.heartbeatfixerforgcm.R;
import io.github.mobodev.heartbeatfixerforgcm.utils.PlayStoreUtils;

/**
 * Created by shaobin on 1/18/15.
 */
public class RecommendAppPreference extends Preference {
    public RecommendAppPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RecommendAppPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecommendAppPreference(Context context) {
        super(context);
        init();
    }

    private void init() {
        setTitle(R.string.pref_recommend_apps_title);
        setSummary(R.string.pref_recommend_apps_description);
    }

    @Override
    protected void onClick() {
        PlayStoreUtils.launchAuthorAppsPage(getContext());
    }
}
