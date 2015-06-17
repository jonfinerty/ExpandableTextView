package com.jonathanfinerty.expandabletextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.jonathanfinerty.lib.R;

public class ExpandableTextViewToggle extends TextView implements com.jonathanfinerty.expandabletextview.ExpandableTextView.Listener {

    private String expandText;
    private Drawable expandDrawable;

    private String collapseText;
    private Drawable collapseDrawable;

    private int collapsibleTextViewId;

    public ExpandableTextViewToggle(Context context) {
        super(context);
        init(null);
    }

    public ExpandableTextViewToggle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ExpandableTextViewToggle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableTextViewToggle);
        collapsibleTextViewId = typedArray.getResourceId(R.styleable.ExpandableTextViewToggle_collapsibleTextView, -1);
        expandText = typedArray.getString(R.styleable.ExpandableTextViewToggle_expandText);
        expandDrawable = typedArray.getDrawable(R.styleable.ExpandableTextViewToggle_expandDrawable);
        collapseText = typedArray.getString(R.styleable.ExpandableTextViewToggle_collapseText);
        collapseDrawable = typedArray.getDrawable(R.styleable.ExpandableTextViewToggle_collapseDrawable);
        typedArray.recycle();

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        final ExpandableTextView expandableTextView = (ExpandableTextView) this.getRootView().findViewById(collapsibleTextViewId);
        expandableTextView.setListener(this);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableTextView.toggleExpansion();
            }
        });
    }

    @Override
    public void expandableChanged(boolean expandable) {
        if (expandable) {
            this.setVisibility(View.VISIBLE);
        } else {
            this.setVisibility(View.GONE);
        }
    }

    @Override
    public void expandedChanged(boolean expanded) {
        if (expanded) {
            setText(collapseText);
            setCompoundDrawablesWithIntrinsicBounds(null, null, collapseDrawable, null);
        } else {
            setText(expandText);
            setCompoundDrawablesWithIntrinsicBounds(null, null, expandDrawable, null);
        }
    }
}