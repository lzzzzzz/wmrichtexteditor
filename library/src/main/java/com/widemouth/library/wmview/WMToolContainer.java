package com.widemouth.library.wmview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;


import com.widemouth.library.toolitem.WMToolItem;
import com.widemouth.library.util.WMUtil;

import java.util.ArrayList;
import java.util.List;

public class WMToolContainer extends WMHorizontalScrollView {

    private Context context;
    private List<WMToolItem> tools = new ArrayList<>();

    public WMToolContainer(Context context) {
        this(context, null);
    }

    public WMToolContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WMToolContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public WMToolContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    public void addToolItem(WMToolItem toolItem) {
        tools.add(toolItem);
        List<View> views = toolItem.getView(context);
        if (views != null) {
            for (View view : views) {
                view.setBackgroundColor(0);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                int margin = WMUtil.getPixelByDp(context, 1);
                layoutParams.setMargins(margin, margin, margin, margin);
                view.setLayoutParams(layoutParams);
                int padding = WMUtil.getPixelByDp(context, 5);
                view.setPadding(padding, padding, padding, padding);
                this.addItemView(view);
            }
        }
    }


    public List<WMToolItem> getTools() {
        return tools;
    }

}