package com.widemouth.library.toolitem;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.View;


import com.widemouth.library.R;
import com.widemouth.library.wmview.WMEditText;
import com.widemouth.library.wmview.WMImageButton;

import java.util.ArrayList;
import java.util.List;

public class WMToolStrikethrough extends WMToolItem {
    private static final String TAG = "WMToolStrikethrough";


    public void setStyle(int start, int end) {
        int start_fact = start;
        int end_fact = end;
        Editable s = getEditText().getEditableText();
        StrikethroughSpan[] styleSpans = s.getSpans(start - 1, end + 1, StrikethroughSpan.class);
        for (StrikethroughSpan styleSpan : styleSpans) {

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
        s.setSpan(new StrikethroughSpan(), start_fact, end_fact, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public void removeStyle(int start, int end) {
        Editable s = getEditText().getEditableText();
        StrikethroughSpan[] styleSpans = s.getSpans(start, end, StrikethroughSpan.class);
        for (StrikethroughSpan styleSpan : styleSpans) {

            int spanStart = s.getSpanStart(styleSpan);
            int spanEnd = s.getSpanEnd(styleSpan);
            if (spanStart != spanEnd) {
                if (spanStart <= start && spanEnd >= end) {
                    s.removeSpan(styleSpan);
                    s.setSpan(new StrikethroughSpan(), spanStart, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    s.setSpan(new StrikethroughSpan(), end, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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

        imageButton.setImageResource(R.drawable.icon_text_strikethrough);

        view = imageButton;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == getEditText()) {
                    return;
                }
                WMEditText editText = getEditText();
                Editable s = editText.getEditableText();
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
        boolean Strikethrough_flag = false;
        WMEditText editText = this.getEditText();
        Editable s = editText.getEditableText();
        if (selStart > 0 && selStart == selEnd) {
            StrikethroughSpan[] styleSpans = s.getSpans(selStart - 1, selStart, StrikethroughSpan.class);
            for (StrikethroughSpan styleSpan : styleSpans) {
                if (s.getSpanStart(styleSpan) != s.getSpanEnd(styleSpan)) {
                    Strikethrough_flag = true;
                    StrikethroughSpan[] styleSpans_next = s.getSpans(selStart, selStart + 1, StrikethroughSpan.class);
                    for (StrikethroughSpan styleSpan_next : styleSpans_next) {

                        if (s.getSpanStart(styleSpan_next) != s.getSpanEnd(styleSpan_next)) {
                            if (styleSpan_next != styleSpan) {
                                setStyle(selStart - 1, selStart + 1);
                            }
                        }

                    }
                }

            }
        } else if (selStart != selEnd) {
            StrikethroughSpan[] styleSpans = s.getSpans(selStart, selEnd, StrikethroughSpan.class);
            for (StrikethroughSpan styleSpan : styleSpans) {

                if (s.getSpanStart(styleSpan) <= selStart
                        && s.getSpanEnd(styleSpan) >= selEnd) {
                    if (s.getSpanStart(styleSpan) != s.getSpanEnd(styleSpan)) {
                        Strikethrough_flag = true;
                    }
                }

            }
        }
        setStyle_state(Strikethrough_flag);
    }

    @Override
    public void onCheckStateUpdate() {
        if (getStyle_state() == true) {
            view.setBackgroundColor(Color.GRAY);
        } else {
            view.setBackgroundColor(0);
        }
    }


}