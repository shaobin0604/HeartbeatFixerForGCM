package io.github.mobodev.heartbeatfixerforgcm.preference;

import android.content.Context;
import android.content.Intent;
import android.preference.Preference;
import android.util.AttributeSet;

import io.github.mobodev.heartbeatfixerforgcm.R;
import io.github.mobodev.heartbeatfixerforgcm.utils.PlayStoreUtils;

/**
 * Created by shaobin on 2/2/15.
 */
public class ShareAppPreference extends Preference {

    private String mTextToShare;

    public ShareAppPreference(Context context) {
        super(context);
        init();
    }

    public ShareAppPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShareAppPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setTitle(R.string.pref_share_app_title);
        setSummary(R.string.pref_share_app_description);
    }

    @Override
    protected void onClick() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, generateShareText());
        sendIntent.setType("text/plain");
        getContext().startActivity(Intent.createChooser(sendIntent, getContext().getText(R.string.send_to)));
    }

    private String generateShareText() {
        if (mTextToShare == null) {
            mTextToShare = new StringBuilder(getContext().getString(R.string.app_name))
                    .append(' ')
                    .append(PlayStoreUtils.generateAppDetailPageLink(getContext().getPackageName(), true))
                    .toString();
        }
        return mTextToShare;
    }
}
