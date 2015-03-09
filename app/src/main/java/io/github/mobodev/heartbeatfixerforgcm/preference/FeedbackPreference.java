package io.github.mobodev.heartbeatfixerforgcm.preference;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.DialogPreference;
import android.text.Html;
import android.util.AttributeSet;

import io.github.mobodev.heartbeatfixerforgcm.R;
import io.github.mobodev.heartbeatfixerforgcm.utils.ActivityUtils;

/**
 * Created by shaobin on 1/24/15.
 */
public class FeedbackPreference extends DialogPreference {


    public FeedbackPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public FeedbackPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        final Context context = getContext();
        PackageManager packageManager = context.getPackageManager();
        String versionName;
        try {
            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "1.0";
        }

        setTitle(R.string.dlg_feedback_title);
        setSummary(new StringBuilder(getContext().getString(R.string.app_name)).append(" v").append(versionName));
        setDialogTitle(R.string.dlg_feedback_title);
        setDialogMessage(Html.fromHtml(getContext().getString(R.string.dlg_feedback_content)));
        setPositiveButtonText(R.string.dlg_feedback_positive_text);
        setNegativeButtonText(R.string.dlg_feedback_negative_text);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        super.onClick(dialog, which);
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE: {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + getContext().getPackageName()));
                ActivityUtils.startActivitySafe(getContext(), intent);
                break;
            }
            case DialogInterface.BUTTON_NEGATIVE: {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:yutouji0917@gmail.com"));
                ActivityUtils.startActivitySafe(getContext(), Intent.createChooser(intent, getContext().getString(R.string.dlg_feedback_negative_text)));
                break;
            }
            default:
                break;
        }
    }
}
