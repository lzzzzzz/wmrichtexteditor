package com.widemouth.library.toolitem;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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

public class WMToolTextColor extends WMToolItem implements View.OnClickListener {

    private static final String TAG = "WMToolBackgroundColor";

    private PopupWindow popupView;

    private int textColor = WMColor.BLACK.ColorInt;

    public void setStyle(int start, int end) {
        int start_fact = start;
        int end_fact = end;
        Editable s = getEditText().getEditableText();
        ForegroundColorSpan[] styleSpans = s.getSpans(start - 1, end + 1, ForegroundColorSpan.class);
        for (ForegroundColorSpan styleSpan : styleSpans) {
            int spanStart = s.getSpanStart(styleSpan);
            int spanEnd = s.getSpanEnd(styleSpan);
            if (styleSpan.getForegroundColor() == textColor) {
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
                        s.setSpan(new ForegroundColorSpan(styleSpan.getForegroundColor()), spanStart, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    if (spanEnd > end) {
                        s.setSpan(new ForegroundColorSpan(styleSpan.getForegroundColor()), end, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }

        }
        s.setSpan(new ForegroundColorSpan(textColor), start_fact, end_fact, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


    @Override
    public void applyStyle(int start, int end) {
        setStyle(start, end);
    }

    @Override
    public List<View> getView(final Context context) {
        WMImageButton imageButton = new WMImageButton(context);

        imageButton.setImageResource(R.drawable.icon_text_textcolor);

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
                    if (type.ColorInt == 0) {
                        continue;
                    }
                    WMImageButton i = new WMImageButton(context);
                    LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    int m = WMUtil.getPixelByDp(context, 10);
                    l.setMargins(m / 2, m, m / 2, m);
                    int p = WMUtil.getPixelByDp(context, 5);
                    i.setPadding(p, p, p, p);
                    i.setLayoutParams(l);
                    i.setBackgroundColor(type.ColorInt);
                    if (type.ColorInt == textColor) {
                        i.setImageResource(R.drawable.icon_selected);
                    }
                    i.setId(type.ColorInt);
                    i.setOnClickListener(WMToolTextColor.this);
                    c.addItemView(i);
                }
                int h = WMUtil.getPixelByDp(context, 45);
                popupView.setHeight(h);
                popupView.setContentView(c);
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
            ForegroundColorSpan[] styleSpans = s.getSpans(selStart - 1, selStart, ForegroundColorSpan.class);
            for (ForegroundColorSpan styleSpan : styleSpans) {
                if (s.getSpanStart(styleSpan) != s.getSpanEnd(styleSpan)) {
                    textColor = styleSpan.getForegroundColor();
                }
            }
        } else if (selStart != selEnd) {
            ForegroundColorSpan[] styleSpans = s.getSpans(selStart, selEnd, ForegroundColorSpan.class);
            for (ForegroundColorSpan styleSpan : styleSpans) {

                if (s.getSpanStart(styleSpan) <= selStart
                        && s.getSpanEnd(styleSpan) >= selEnd) {
                    if (s.getSpanStart(styleSpan) != s.getSpanEnd(styleSpan)) {
                        textColor = styleSpan.getForegroundColor();
                    }
                }

            }
        }
        onCheckStateUpdate();
    }

    @Override
    public void onCheckStateUpdate() {
        ((WMImageButton) view).setColorFilter(textColor);
        view.invalidate();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
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
        setTextColor(v.getId());
        popupView.dismiss();
    }


}