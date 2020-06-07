package com.widemouth.library.toolitem;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.Spanned;
import android.view.View;


import com.widemouth.library.R;
import com.widemouth.library.span.WMUnderlineSpan;
import com.widemouth.library.wmview.WMEditText;
import com.widemouth.library.wmview.WMImageButton;

import java.util.ArrayList;
import java.util.List;

public class WMToolUnderline extends WMToolItem {
    private static final String TAG = "WMToolUnderline";

    private void setStyle(int start, int end) {
        int start_fact = start;
        int end_fact = end;
        Editable s = getEditText().getEditableText();
        WMUnderlineSpan[] styleSpans = s.getSpans(start - 1, end + 1, WMUnderlineSpan.class);
        for (WMUnderlineSpan styleSpan : styleSpans) {

            int spanStart = s.getSpanStart(styleSpan);
            int spanEnd = s.getSpanEnd(styleSpan);
            if (spanStart != spanEnd) {
                if (spanStart < start) {
                    start_fact = spanStart;
                }
                if (spanEnd > end) {
                    end_fact = spanEnd;
                }

                if (spanStart <= start && spanEnd >= end) {
                    return;
                } else {
                    s.removeSpan(styleSpan);
                }
            }

        }
        s.setSpan(new WMUnderlineSpan(), start_fact, end_fact, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    }

    private void removeStyle(int start, int end) {
        Editable s = getEditText().getEditableText();
        WMUnderlineSpan[] styleSpans = s.getSpans(start, end, WMUnderlineSpan.class);
        for (WMUnderlineSpan styleSpan : styleSpans) {

            int spanStart = s.getSpanStart(styleSpan);
            int spanEnd = s.getSpanEnd(styleSpan);
            if (spanStart != spanEnd) {
                if (spanStart <= start && spanEnd >= end) {
                    s.removeSpan(styleSpan);
                    s.setSpan(new WMUnderlineSpan(), spanStart, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    s.setSpan(new WMUnderlineSpan(), end, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }

        }
    }

    @Override
    public void applyStyle(int start, int end) {
        if (getStyle_state()) {
            setStyle(start, end);
        } else {
            removeStyle(start, end);
        }
    }

    @Override
    public List<View> getView(Context context) {
        WMImageButton imageButton = new WMImageButton(context);

        imageButton.setImageResource(R.drawable.icon_text_underline);

        view = imageButton;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == getEditText()) {
                    return;
                }
                WMEditText editText = getEditText();
                int selStart = editText.getSelectionStart();
                int selEnd = editText.getSelectionEnd();
                if (selStart < selEnd) {
                    if (getStyle_state()) {
                        removeStyle(selStart, selEnd);
                    } else {
                        setStyle(selStart, selEnd);
                    }
                }
                setStyle_state(!getStyle_state());
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
        boolean Underline_flag = false;
        WMEditText editText = this.getEditText();
        Editable s = editText.getEditableText();
        if (selStart > 0 && selStart == selEnd) {
            WMUnderlineSpan[] styleSpans = s.getSpans(selStart - 1, selStart, WMUnderlineSpan.class);
            for (WMUnderlineSpan styleSpan : styleSpans) {
                if (s.getSpanStart(styleSpan) != s.getSpanEnd(styleSpan)) {
                    Underline_flag = true;
                    WMUnderlineSpan[] styleSpans_next = s.getSpans(selStart, selStart + 1, WMUnderlineSpan.class);
                    for (WMUnderlineSpan styleSpan_next : styleSpans_next) {
                        if (s.getSpanStart(styleSpan_next) != s.getSpanEnd(styleSpan_next)) {
                            if (styleSpan_next != styleSpan) {
                                setStyle(selStart - 1, selStart + 1);
                            }
                        }
                    }
                }

            }
        } else if (selStart != selEnd) {
            WMUnderlineSpan[] styleSpans = s.getSpans(selStart, selEnd, WMUnderlineSpan.class);
            for (WMUnderlineSpan styleSpan : styleSpans) {

                if (s.getSpanStart(styleSpan) <= selStart
                        && s.getSpanEnd(styleSpan) >= selEnd) {
                    if (s.getSpanStart(styleSpan) != s.getSpanEnd(styleSpan)) {
                        Underline_flag = true;
                    }
                }

            }
        }
        setStyle_state(Underline_flag);
    }

    @Override
    public void onCheckStateUpdate() {
        if (getStyle_state()) {
            view.setBackgroundColor(Color.GRAY);
        } else {
            view.setBackgroundColor(0);
        }
    }

}