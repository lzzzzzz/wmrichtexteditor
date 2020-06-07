package com.widemouth.library.toolitem;

import android.content.Context;
import android.view.View;


import com.widemouth.library.wmview.WMEditText;

import java.util.List;

public abstract class WMToolItem {
    private WMEditText editText;
    protected View view;
    private boolean style_state = false;
    public abstract void applyStyle(int start,int end);

    public abstract List<View> getView(Context context);

    public abstract void onSelectionChanged(int selStart, int selEnd);

    public abstract void onCheckStateUpdate() ;

    public boolean getStyle_state() {
        return style_state;
    }

    public void setStyle_state(boolean style_state) {
        this.style_state = style_state;
        onCheckStateUpdate();
        view.invalidate();
    }

    public WMEditText getEditText() {
        return editText;
    }

    public void setEditText(WMEditText editText) {
        this.editText = editText;
    }


}