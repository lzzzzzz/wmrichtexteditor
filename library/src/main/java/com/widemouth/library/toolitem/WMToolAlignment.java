package com.widemouth.library.toolitem;

import android.content.Context;
import android.text.Editable;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.view.View;


import com.widemouth.library.R;
import com.widemouth.library.span.WMLeadingMarginSpan;
import com.widemouth.library.util.WMUtil;
import com.widemouth.library.wmview.WMImageButton;

import java.util.ArrayList;
import java.util.List;

public class WMToolAlignment extends WMToolItem implements View.OnClickListener {
    private static final String TAG = "WMToolAlignment";
    private WMImageButton imageButtonLeft;
    private WMImageButton imageButtonCenter;
    private WMImageButton imageButtonRight;
    private Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;


    @Override
    public void applyStyle(int start, int end) {

    }

    @Override
    public List<View> getView(Context context) {
        imageButtonLeft = new WMImageButton(context);
        imageButtonCenter = new WMImageButton(context);
        imageButtonRight = new WMImageButton(context);

        imageButtonLeft.setImageResource(R.drawable.icon_text_align_left);
        imageButtonCenter.setImageResource(R.drawable.icon_text_align_center);
        imageButtonRight.setImageResource(R.drawable.icon_text_align_right);


        imageButtonLeft.setOnClickListener(this);
        imageButtonCenter.setOnClickListener(this);
        imageButtonRight.setOnClickListener(this);

        List<View> views = new ArrayList<>();
        views.add(imageButtonLeft);
        views.add(imageButtonCenter);
        views.add(imageButtonRight);


        return views;

    }

    @Override
    public void onSelectionChanged(int selStart, int selEnd) {
        Editable s = getEditText().getEditableText();
        int lineStart = WMUtil.getLineStart(getEditText(), selStart);
        AlignmentSpan.Standard[] currentSpan = s.getSpans(lineStart, lineStart + 1, AlignmentSpan.Standard.class);
        for (AlignmentSpan.Standard span : currentSpan) {
            alignment = span.getAlignment();
        }

        if (selStart == selEnd) {
            if (selStart > 0 && selStart < s.length() && s.charAt(selStart - 1) != '\n') {
                int textLead = WMUtil.getTextLead(selStart - 1, getEditText());
                int textEnd = WMUtil.getTextEnd(selStart - 1, getEditText());

                AlignmentSpan.Standard[] spansLast = s.getSpans(selStart - 1, selStart, AlignmentSpan.Standard.class);
                if (spansLast != null && spansLast.length > 0) {
                    AlignmentSpan.Standard spanLast = spansLast[spansLast.length - 1];
                    int spanStart = s.getSpanStart(spanLast);
                    int spanEnd = s.getSpanEnd(spanLast);
                    if (spanStart > textLead || spanEnd < textEnd) {
                        removeSpan(s, textLead, textEnd);
                        s.setSpan(new AlignmentSpan.Standard(spanLast.getAlignment()), textLead, textEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    }
                } else {
                    removeSpan(s, textLead, textEnd);
                }
            }
            int lineEnd = WMUtil.getLineEnd(getEditText(), selStart);
            if (lineEnd > 0 && lineEnd <= s.length() && s.charAt(lineEnd - 1) == '\u200B') {
                if (lineEnd - 1 != lineStart) {
                    WMLeadingMarginSpan[] listSpans = s.getSpans(lineEnd - 1, lineEnd, WMLeadingMarginSpan.class);
                    if (listSpans != null && listSpans.length > 0) {
                        s.delete(lineEnd - 1, lineEnd);
                        return;
                    }
                }
            }
            if (selStart > 0) {
                char Char = s.charAt(selStart - 1);
                if (Char == '\u200B') {
                    WMLeadingMarginSpan[] leadingMarginSpans = s.getSpans(selStart - 1, selStart, WMLeadingMarginSpan.class);
                    if (leadingMarginSpans == null || leadingMarginSpans.length == 0) {
                        getEditText().setSelection(selStart - 1);
                        return;
                    }
                }
            }

            if ((selStart == 0 || s.charAt(selStart - 1) == '\n') && selStart == s.length() && s.length() != 0) {
                s.replace(selStart, selStart, "\u200B");
            }
        }

    }

    public void removeSpan(Editable s, int textLead, int textEnd) {
        AlignmentSpan.Standard[] spans = s.getSpans(textLead, textEnd, AlignmentSpan.Standard.class);
        for (AlignmentSpan.Standard span : spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            if (start < textLead) {
                s.setSpan(new AlignmentSpan.Standard(span.getAlignment()), start, textLead, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }
            if (end > textEnd) {
                s.setSpan(new AlignmentSpan.Standard(span.getAlignment()), textEnd, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }
        }
    }

    @Override
    public void onCheckStateUpdate() {

    }

    @Override
    public void onClick(View v) {
        if (null == getEditText()) {
            return;
        }

        if (v == imageButtonLeft) {
            alignment = Layout.Alignment.ALIGN_NORMAL;
        } else if (v == imageButtonCenter) {
            alignment = Layout.Alignment.ALIGN_CENTER;
        } else if (v == imageButtonRight) {
            alignment = Layout.Alignment.ALIGN_OPPOSITE;
        }
        Editable s = getEditText().getEditableText();
        int selStart = getEditText().getSelectionStart();
        int selEnd = getEditText().getSelectionEnd();
        int textLead = WMUtil.getTextLead(selStart, getEditText());
        int textEnd = WMUtil.getTextEnd(selEnd, getEditText());


        if (textLead == textEnd) {
            s.insert(textLead, "\u200B");
            s.setSpan(new AlignmentSpan.Standard(alignment), textLead, textLead + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }


        for (int i = textLead; i < textEnd; ) {
            int end = WMUtil.getTextEnd(i, getEditText());
            AlignmentSpan.Standard[] alignmentSpans = s.getSpans(i, end, AlignmentSpan.Standard.class);
            for (AlignmentSpan.Standard alignmentSpan : alignmentSpans) {
                int spanStart = s.getSpanStart(alignmentSpan);
                int spanEnd = s.getSpanEnd(alignmentSpan);
                s.removeSpan(alignmentSpan);
                if (spanStart < i) {
                    s.setSpan(new AlignmentSpan.Standard(alignmentSpan.getAlignment()), spanStart, i, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                }
                if (spanEnd > end) {
                    s.setSpan(new AlignmentSpan.Standard(alignmentSpan.getAlignment()), end, spanEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                }
            }
            WMLeadingMarginSpan[] leadingMarginSpans = s.getSpans(i, end, WMLeadingMarginSpan.class);
            if (leadingMarginSpans == null || leadingMarginSpans.length == 0) {
                s.setSpan(new AlignmentSpan.Standard(alignment), i, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }
            i = end;
        }
    }
}