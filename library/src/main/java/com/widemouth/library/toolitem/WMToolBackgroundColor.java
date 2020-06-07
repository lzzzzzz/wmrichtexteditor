package com.widemouth.library.toolitem;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;


import com.widemouth.library.R;
import com.widemouth.library.util.WMColor;
import com.widemouth.library.util.WMUtil;
import com.widemouth.library.wmview.WMEditText;
import com.widemouth.library.wmview.WMHorizontalScrollView;
import com.widemouth.library.wmview.WMImageButton;

import java.util.ArrayList;
import java.util.List;

public class WMToolBackgroundColor extends WMToolItem implements View.OnClickListener {


    private static final String TAG = "WMToolBackgroundColor";
    private PopupWindow popupView;

    private int backgroundColor = WMColor.TRANSPARENT.ColorInt;

    public void setStyle(int start, int end) {
        int start_fact = start;
        int end_fact = end;
        Editable s = getEditText().getEditableText();
        BackgroundColorSpan[] styleSpans = s.getSpans(start - 1, end + 1, BackgroundColorSpan.class);
        for (BackgroundColorSpan styleSpan : styleSpans) {
            int spanStart = s.getSpanStart(styleSpan);
            int spanEnd = s.getSpanEnd(styleSpan);
            if (styleSpan.getBackgroundColor() == backgroundColor) {
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
            } else {
                if (spanEnd <= start || spanStart >= end) {

                } else {
                    s.removeSpan(styleSpan);
                    if (spanStart < start) {
                        s.setSpan(new BackgroundColorSpan(styleSpan.getBackgroundColor()), spanStart, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    if (spanEnd > end) {
                        s.setSpan(new BackgroundColorSpan(styleSpan.getBackgroundColor()), end, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }
        s.setSpan(new BackgroundColorSpan(backgroundColor), start_fact, end_fact, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public void removeStyle(int start, int end) {
        Editable s = getEditText().getEditableText();
        BackgroundColorSpan[] styleSpans = s.getSpans(start, end, BackgroundColorSpan.class);
        for (BackgroundColorSpan styleSpan : styleSpans) {
            int spanStart = s.getSpanStart(styleSpan);
            int spanEnd = s.getSpanEnd(styleSpan);
            if (spanStart != spanEnd) {
                s.removeSpan(styleSpan);
                if (spanStart < start) {
                    s.setSpan(new BackgroundColorSpan(styleSpan.getBackgroundColor()), spanStart, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                if (spanEnd > end) {
                    s.setSpan(new BackgroundColorSpan(styleSpan.getBackgroundColor()), end, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
    }

    @Override
    public void applyStyle(int start, int end) {

        if (backgroundColor == WMColor.TRANSPARENT.ColorInt) {
            removeStyle(start, end);
        } else {
            setStyle(start, end);
        }

    }

    @Override
    public List<View> getView(final Context context) {
        WMImageButton imageButton = new WMImageButton(context);

        imageButton.setImageResource(R.drawable.icon_text_backgroundcolor);

        view = imageButton;


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == getEditText()) {
                    return;
                }
                popupView = new PopupWindow(context);
                WMHorizontalScrollView c = new WMHorizontalScrollView(context);
                for (WMColor type : WMColor.values()) {
                    WMImageButton i = new WMImageButton(context);
                    LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    int m = WMUtil.getPixelByDp(context, 10);
                    l.setMargins(m / 2, m, m / 2, m);
                    int p = WMUtil.getPixelByDp(context, 5);
                    i.setPadding(p, p, p, p);
                    i.setLayoutParams(l);
                    i.setBackgroundColor(type.ColorInt);
                    if (type == WMColor.TRANSPARENT) {
                        i.setBackgroundColor(0x80FFFFFF);
                    }
                    if (type.ColorInt == backgroundColor) {
                        i.setImageResource(R.drawable.icon_selected);
                    }
                    i.setId(type.ColorInt);
                    i.setOnClickListener(WMToolBackgroundColor.this);
                    c.addItemView(i);
                }
                popupView.setContentView(c);
                int h = WMUtil.getPixelByDp(context, 45);
                popupView.setHeight(h);
                popupView.setBackgroundDrawable(new ColorDrawable(0x50FFFFFF));
                popupView.setOutsideTouchable(true);
                int offsetY = WMUtil.getPixelByDp(context, -90);
                popupView.showAsDropDown(v, 0, offsetY);


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
            BackgroundColorSpan[] styleSpans = s.getSpans(selStart - 1, selStart, BackgroundColorSpan.class);
            for (BackgroundColorSpan styleSpan : styleSpans) {
                if (s.getSpanStart(styleSpan) != s.getSpanEnd(styleSpan)) {
                    backgroundColor = styleSpan.getBackgroundColor();
                }
            }
            if (styleSpans.length == 0) {
                backgroundColor = WMColor.TRANSPARENT.ColorInt;
            }
        } else if (selStart != selEnd) {
            BackgroundColorSpan[] styleSpans = s.getSpans(selStart, selEnd, BackgroundColorSpan.class);
            for (BackgroundColorSpan styleSpan : styleSpans) {

                if (s.getSpanStart(styleSpan) <= selStart
                        && s.getSpanEnd(styleSpan) >= selEnd) {
                    if (s.getSpanStart(styleSpan) != s.getSpanEnd(styleSpan)) {
                        backgroundColor = styleSpan.getBackgroundColor();
                    }
                }

            }
        }
        onCheckStateUpdate();
    }

    @Override
    public void onCheckStateUpdate() {
        view.setBackgroundColor(backgroundColor);
        view.invalidate();
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        onCheckStateUpdate();
        WMEditText editText = getEditText();
        int selStart = editText.getSelectionStart();
        int selEnd = editText.getSelectionEnd();
        if (selStart < selEnd) {
            if (backgroundColor == 0) {
                removeStyle(selStart, selEnd);
            } else {
                setStyle(selStart, selEnd);
            }
        }
    }

    @Override
    public void onClick(View v) {
        setBackgroundColor(v.getId());
        popupView.dismiss();
    }
}