package com.widemouth.library.wmview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.widemouth.library.R;
import com.widemouth.library.util.WMUtil;


public class WMHorizontalScrollView extends FrameLayout {
    private Context context;

    private boolean arrowShow = true;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout linearLayout;

    public WMHorizontalScrollView(@NonNull Context context) {
        super(context);
        this.context = context;
        InitView();
    }

    public WMHorizontalScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        InitView();
    }

    public WMHorizontalScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        InitView();
    }

    public WMHorizontalScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        InitView();
    }

    public void InitView() {
        horizontalScrollView = new HorizontalScrollView(context);
        linearLayout = new LinearLayout(context);
        linearLayout.setGravity(Gravity.CENTER);
        horizontalScrollView.addView(linearLayout);
        horizontalScrollView.setHorizontalScrollBarEnabled(false);
        this.addView(horizontalScrollView);

    }


    @SuppressLint("NewApi")
    public void showArrow() {
        int w = WMUtil.getPixelByDp(context, 40);
        final LayoutParams l = new LayoutParams(w, ViewGroup.LayoutParams.MATCH_PARENT);
        l.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        LayoutParams r = new LayoutParams(w, ViewGroup.LayoutParams.MATCH_PARENT);
        r.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        final ImageView arrowLeft = getArrowView(true);
        final ImageView arrowRight = getArrowView(false);
        arrowLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalScrollView.arrowScroll(View.FOCUS_LEFT);
            }
        });
        arrowRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalScrollView.arrowScroll(View.FOCUS_RIGHT);
            }
        });
        horizontalScrollView.post(new Runnable() {
            @Override
            public void run() {
                int maxScrollX = linearLayout.getMeasuredWidth() - horizontalScrollView.getMeasuredWidth();
                if (maxScrollX > 0) {
                    arrowRight.setVisibility(VISIBLE);
                }
            }
        });
        horizontalScrollView.setOnScrollChangeListener(new OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int maxScrollX = linearLayout.getMeasuredWidth() - horizontalScrollView.getMeasuredWidth();
                if (scrollX == 0) {
                    arrowLeft.setVisibility(View.GONE);
                } else if (scrollX == maxScrollX) {
                    arrowRight.setVisibility(View.GONE);
                } else {
                    arrowLeft.setVisibility(View.VISIBLE);
                    arrowRight.setVisibility(View.VISIBLE);
                }
            }
        });
        this.addView(arrowLeft, l);
        this.addView(arrowRight, r);
    }

    public void addItemView(View view) {
        linearLayout.addView(view);
    }

    public ImageView getArrowView(boolean isLeft) {
        ImageView arrowView = new ImageView(context);
        int p = WMUtil.getPixelByDp(context, 12);
        arrowView.setPadding(p, p, p, p);
        if (isLeft) {
            arrowView.setImageResource(R.drawable.icon_arrow_left);
        } else {
            arrowView.setImageResource(R.drawable.icon_arrow_right);
        }
        arrowView.setBackgroundColor(0xAAFFFFFF);
        arrowView.setVisibility(GONE);
        return arrowView;
    }
}