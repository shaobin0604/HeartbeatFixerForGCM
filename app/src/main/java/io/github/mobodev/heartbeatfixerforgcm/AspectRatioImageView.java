package io.github.mobodev.heartbeatfixerforgcm;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by bshao on 9/20/17.
 */

public class AspectRatioImageView extends android.support.v7.widget.AppCompatImageView {
    private float mAspectRatio;

    public AspectRatioImageView(Context context) {
        super(context);
        mAspectRatio = 1.0F;
    }

    public AspectRatioImageView(Context context,
            AttributeSet attrs) {
        super(context, attrs);
        mAspectRatio = context.obtainStyledAttributes(attrs,
                R.styleable.AspectRatioImageView).getFraction(0, 1, 1, 1.0F);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            super.onMeasure(
                    widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(
                            Math.round(MeasureSpec.getSize(widthMeasureSpec) / this.mAspectRatio),
                            MeasureSpec.EXACTLY));
            return;
        }
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            super.onMeasure(
                    MeasureSpec.makeMeasureSpec(
                            Math.round(MeasureSpec.getSize(heightMeasureSpec) * this.mAspectRatio),
                            MeasureSpec.EXACTLY), heightMeasureSpec);
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setAspectRatio(float aspectRatio) {
        this.mAspectRatio = aspectRatio;
    }
}
