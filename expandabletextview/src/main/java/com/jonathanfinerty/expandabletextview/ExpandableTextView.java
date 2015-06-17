package com.jonathanfinerty.expandabletextview;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.widget.TextView;

public class ExpandableTextView extends TextView {

    private Listener listener;

    public interface Listener {
        void expandableChanged(boolean isExpandable);

        void expandedChanged(boolean expanded);
    }

    private final static int collapsedLineCount = 2;

    private boolean expandable = false;

    private int collapsedHeight;
    private int expandedHeight;

    private boolean expanded = false;

    private boolean textMeasured = false;
    private static final String expandedKey = "expandedKey";
    private static final String instanceStateKey = "instanceStateKey";

    public ExpandableTextView(Context context) {
        super(context);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            expanded = bundle.getBoolean(expandedKey);
            state = bundle.getParcelable(instanceStateKey);
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(instanceStateKey, super.onSaveInstanceState());
        bundle.putBoolean(expandedKey, expanded);
        return bundle;
    }

    public void toggleExpansion() {
        Animation heightAnimation;

        if (expanded) {
            heightAnimation = new TextViewHeightAnimation(this, collapsedHeight);
        } else {
            heightAnimation = new TextViewHeightAnimation(this, expandedHeight);
        }

        heightAnimation.setFillAfter(true);
        clearAnimation();
        startAnimation(heightAnimation);

        expanded = !expanded;

        if (listener != null) {
            listener.expandedChanged(expanded);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (textMeasured) {
            return;
        }

        setMaxLines(Integer.MAX_VALUE);

        boolean wasExpandable = expandable;

        expandable = getLineCount() > collapsedLineCount;

        collapsedHeight = getCollapsedHeight();
        expandedHeight = getExpandedHeight();

        textMeasured = true;

        if (listener != null && wasExpandable != expandable) {
            listener.expandableChanged(expandable);
        }

        if (!expanded) {
            setMaxLines(collapsedLineCount);
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        textMeasured = false;
        if (text.length() == 0) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
        }

        super.setText(text, type);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
        listener.expandedChanged(expanded);
        listener.expandableChanged(expandable);
    }

    private int getCollapsedHeight() {
        int textHeight = getLayout().getLineTop(collapsedLineCount);
        int padding = getCompoundPaddingTop() + getCompoundPaddingBottom();
        return textHeight + padding;
    }

    private int getExpandedHeight() {
        int textHeight = getLayout().getHeight();
        int padding = getCompoundPaddingTop() + getCompoundPaddingBottom();
        return textHeight + padding;
    }
}