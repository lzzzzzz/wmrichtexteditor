package com.widemouth.library.toolitem;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;


import com.widemouth.library.R;
import com.widemouth.library.util.WMUtil;
import com.widemouth.library.wmview.WMButton;
import com.widemouth.library.wmview.WMEditText;
import com.widemouth.library.wmview.WMHorizontalScrollView;

import java.util.ArrayList;
import java.util.List;

public class WMToolTextSize extends WMToolItem implements View.OnClickListener {

    private static final String TAG = "WMToolTextSize";

    private PopupWindow popupWindow;

    private int textSize = 16;

    private void setStyle(int start, int end) {
        int start_fact = start;
        int end_fact = end;
        Editable s = getEditText().getEditableText();

        AbsoluteSizeSpan[] styleSpans = s.getSpans(start - 1, end + 1, AbsoluteSizeSpan.class);

        for (AbsoluteSizeSpan styleSpan : styleSpans) {
            int spanStart = s.getSpanStart(styleSpan);
            int spanEnd = s.getSpanEnd(styleSpan);
            if (styleSpan.getSize() == textSize) {
                if (spanStart < start) {
                    start_fact = spanStart;
                }
                if (spanEnd > end) {
                    end_fact = spanEnd;
                }
                if (spanStart != spanEnd) {
                    if (spanStart <= start && spanEnd >= end) {
                        return;
                    } else {
                        s.removeSpan(styleSpan);
                    }
                }
            } else {   //非当前字体大小
                if (spanEnd <= start || spanStart >= end) {   //不在选中区间内

                } else {      //在选中区间内
                    s.removeSpan(styleSpan);   //移除此style

                    if (spanStart < start) {
                        //恢复
                        s.setSpan(new AbsoluteSizeSpan(styleSpan.getSize(), true), spanStart, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    if (spanEnd > end) {
                        //恢复
                        s.setSpan(new AbsoluteSizeSpan(styleSpan.getSize(), true), end, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }

        }

        s.setSpan(new AbsoluteSizeSpan(textSize, true), start_fact, end_fact, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


    @Override
    public void applyStyle(int start, int end) {
        setStyle(start, end);
    }

    @Override
    public List<View> getView(final Context context) {
        WMButton button = new WMButton(context);
        button.setTextSize(18);
        button.setText(String.valueOf(textSize));

        view = button;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == getEditText()) {
                    return;
                }
                popupWindow = new PopupWindow(context);
                WMHorizontalScrollView c = new WMHorizontalScrollView(context);
                for (int i = 12; i < 30; i += 2) {
                    WMButton b = new WMButton(context);
                    LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    b.setLayoutParams(l);
                    b.setGravity(Gravity.CENTER);
                    b.setText(String.valueOf(i));
                    b.setTextColor(0xFF333333);
                    b.setBackgroundColor(0);
                    if (textSize == i) {
                        b.setBackgroundResource(R.drawable.icon_circle);
                    }
                    b.setId(i);
                    b.setOnClickListener(WMToolTextSize.this);
                    c.addItemView(b);
                }
                popupWindow.setContentView(c);
                int h = WMUtil.getPixelByDp(context, 45);
                popupWindow.setHeight(h);
                popupWindow.setBackgroundDrawable(new ColorDrawable(0x50FFFFFF));
                popupWindow.setOutsideTouchable(true);
                int offsetY = WMUtil.getPixelByDp(context, -90);
                popupWindow.showAsDropDown(v, 0, offsetY);
            }
        });
        List<View> views = new ArrayList<>();
        views.add(view);
        return views;
    }

    @Override
    public void onSelectionChanged(int selStart, int selEnd) {
        if (null == getEditText()) {
            return;
        }
        WMEditText editText = this.getEditText();
        Editable s = editText.getEditableText();
        if (selStart > 0 && selStart == selEnd) {
            AbsoluteSizeSpan[] styleSpans = s.getSpans(selStart - 1, selStart, AbsoluteSizeSpan.class);
            for (AbsoluteSizeSpan styleSpan : styleSpans) {
                if (s.getSpanStart(styleSpan) != s.getSpanEnd(styleSpan)) {
                    textSize = styleSpan.getSize();
                }
            }
        } else if (selStart != selEnd) {
            AbsoluteSizeSpan[] styleSpans = s.getSpans(selStart, selEnd, AbsoluteSizeSpan.class);
            for (AbsoluteSizeSpan styleSpan : styleSpans) {

                if (s.getSpanStart(styleSpan) <= selStart
                        && s.getSpanEnd(styleSpan) >= selEnd) {
                    if (s.getSpanStart(styleSpan) != s.getSpanEnd(styleSpan)) {
                        textSize = styleSpan.getSize();
                    }
                }
            }
        }
        onCheckStateUpdate();
    }

    @Override
    public void onCheckStateUpdate() {
        ((WMButton) view).setText(String.valueOf(textSize));
    }

    private void setTextSize(int textSize) {
        this.textSize = textSize;
        onCheckStateUpdate();
        WMEditText editText = getEditText();
        int selStart = editText.getSelectionStart();
        int selEnd = editText.getSelectionEnd();
        if (selStart < selEnd) {
            setStyle(selStart, selEnd);
        }
    }

    @Override
    public void onClick(View v) {
        setTextSize(v.getId());
        popupWindow.dismiss();
    }
}