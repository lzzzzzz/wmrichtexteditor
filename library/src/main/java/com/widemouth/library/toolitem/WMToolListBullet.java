package com.widemouth.library.toolitem;

import android.content.Context;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.ParagraphStyle;
import android.view.View;


import com.widemouth.library.R;
import com.widemouth.library.span.WMListBulletSpan;
import com.widemouth.library.util.WMUtil;
import com.widemouth.library.wmview.WMImageButton;

import java.util.ArrayList;
import java.util.List;

public class WMToolListBullet extends WMToolItem {

    private static final String TAG = "WMToolListBullet";

    @Override
    public void applyStyle(int start, int end) {
        Editable s = getEditText().getEditableText();
        if (start > 0 && s.charAt(start - 1) == '\n') {
            return;
        }

        WMListBulletSpan[] styles = s.getSpans(start - 1, start, WMListBulletSpan.class);
        if (styles != null && styles.length > 0) {
            WMListBulletSpan style = styles[styles.length - 1];
            int spanStart = s.getSpanStart(style);
            int spanEnd = s.getSpanEnd(style);
            if (s.subSequence(start, end).toString().equals("\n")) {
                if (spanEnd == end - 1) {
                    if (spanEnd == spanStart + 1) {
                        s.delete(end - 2, end);
                    } else {
                        s.insert(end, "\u200B");
                        s.setSpan(new WMListBulletSpan(), end, end + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }
    }

    @Override
    public List<View> getView(Context context) {
        WMListBulletSpan.context = context;
        WMImageButton imageButton = new WMImageButton(context);

        imageButton.setImageResource(R.drawable.icon_text_listbullet);

        view = imageButton;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == getEditText()) {
                    return;
                }
                Editable s = getEditText().getEditableText();
                int selStart = getEditText().getSelectionStart();
                int selEnd = getEditText().getSelectionEnd();

                int textLead = WMUtil.getTextLead(selStart, getEditText());
                int textEnd = WMUtil.getTextEnd(selEnd, getEditText());

                WMListBulletSpan[] spans = s.getSpans(textLead, textEnd, WMListBulletSpan.class);
                if (null != spans && spans.length == WMUtil.getParagraphCount(getEditText(), selStart, selEnd)) {
                    for (WMListBulletSpan span : spans) {
                        int spanStart = s.getSpanStart(span);
                        if (s.charAt(spanStart) == '\u200B') {
                            s.delete(spanStart, spanStart + 1);
                            s.removeSpan(span);
                        }
                    }

                } else {
                    for (int i = textLead; i <= textEnd; ) {
                        int end = WMUtil.getTextEnd(i, getEditText());
                        WMListBulletSpan[] spans1 = s.getSpans(i, end, WMListBulletSpan.class);
                        if (spans1 == null || spans1.length == 0) {
                            ParagraphStyle[] paragraphStyles = s.getSpans(i, end, ParagraphStyle.class);
                            for (ParagraphStyle paragraphStyle : paragraphStyles) {
                                int spanStart = s.getSpanStart(paragraphStyle);
                                int spanEnd = s.getSpanEnd(paragraphStyle);
                                int flag = s.getSpanFlags(paragraphStyle);
                                s.removeSpan(paragraphStyle);
                                if (spanStart < i) {
                                    s.setSpan(paragraphStyle, spanStart, i, flag);
                                }
                                if (spanEnd > end) {
                                    s.setSpan(paragraphStyle, end, spanEnd, flag);
                                }
                            }
                            if (i == s.length() || s.charAt(i) != '\u200B') {
                                s.insert(i, "\u200B");
                                textEnd++;
                                end = WMUtil.getTextEnd(i, getEditText());
                            }
                            s.setSpan(new WMListBulletSpan(), i, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                        i = end;
                        if (i == textEnd) {
                            break;
                        }
                    }
                    onSelectionChanged(getEditText().getSelectionStart(), getEditText().getSelectionEnd());

                }

            }
        });
        List<View> views = new ArrayList<>();
        views.add(view);
        return views;
    }

    @Override
    public void onSelectionChanged(int selStart, int selEnd) {
        Editable s = getEditText().getEditableText();
        if (selStart < s.length()) {
            char Char = s.charAt(selStart);
            if (Char == '\u200B') {
                WMListBulletSpan[] listBulletSpans = s.getSpans(selStart, selStart + 1, WMListBulletSpan.class);
                if (listBulletSpans != null && listBulletSpans.length > 0) {
                    if (selStart + 1 > selEnd) {
                        selEnd = selStart + 1;
                    }
                    getEditText().setSelection(selStart + 1, selEnd);
                }
            }
        }
    }

    @Override
    public void onCheckStateUpdate() {

    }
}