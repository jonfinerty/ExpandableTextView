package com.jonathanfinerty.expandabletextview;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

class TextViewHeightAnimation extends Animation {

    private final static int animationDuration = 400;

    private final TextView textView;
    private final int startHeight;
    private final int heightDelta;

    public TextViewHeightAnimation(TextView textView, int targetHeight) {
        this.textView = textView;
        startHeight = textView.getHeight();
        this.heightDelta = targetHeight - startHeight;
        setDuration(animationDuration);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final int newHeight = (int) (heightDelta * interpolatedTime + startHeight);
        textView.setMaxHeight(newHeight);
        textView.getLayoutParams().height = newHeight;
        textView.requestLayout();
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
