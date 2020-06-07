package com.widemouth.library.span;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

public class WMQuoteSpan implements WMLeadingMarginSpan {

    private static final String TAG = "WMQuoteSpan";

    public static Context context;

    @Override
    public int getLeadingMargin(boolean first) {
        return 100;
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
        int spanStart = ((SpannableStringBuilder) text).getSpanStart(this);
        int spanEnd = ((SpannableStringBuilder) text).getSpanEnd(this);
        if (first) {
            int currentLine = getLineForOffset(layout, start);
            int textLead = getTextLead(layout, text, currentLine), textEnd = getTextEnd(layout, text, currentLine);

            if (textLead != spanStart || textEnd != spanEnd) {
                ((SpannableStringBuilder) text).removeSpan(this);
                if (text.charAt(start) != '\u200B') {
                    SpannableStringBuilder stringBuilder = new SpannableStringBuilder("\u200B");
                    stringBuilder.setSpan(new WMQuoteSpan(), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ((SpannableStringBuilder) text).insert(start, stringBuilder);
                } else {
                    ((SpannableStringBuilder) text).setSpan(new WMQuoteSpan(), textLead, textEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }

//
            if (text.length() > 0) {
                if (text.charAt(start) != '\u200B') {
                    ((SpannableStringBuilder) text).removeSpan(this);
                }
            }
        }

//        Paint.FontMetricsInt fontMetricsInt = p.getFontMetricsInt();
//        int fontTop = baseline + fontMetricsInt.top;
//        int fontMetricsHeight = fontMetricsInt.bottom - fontMetricsInt.top;
//        int rectHeight = bottom - top;
//        int rectTop = fontTop + (fontMetricsHeight - rectHeight) / 2;
//        int rectBottom = rectTop + rectHeight;

        Paint paint = new Paint();
        paint.setColor(0xFFC4C8D0);
        c.drawRect(x + 60, top, x + 60 + 5, bottom, paint);
    }

    public static int getTextLead(Layout layout, CharSequence text, int currentLine) {
        int lead = 0;
        while (currentLine > 0) {
            lead = layout.getLineStart(currentLine);
            char Char = text.charAt(lead - 1);
            if (Char == '\n') {
                break;
            } else {
                lead = 0;
            }
            currentLine--;
        }
        return lead;
    }

    public int getTextEnd(Layout layout, CharSequence text, int currentLine) {
        int lead = text.length();
        while (currentLine < layout.getLineCount()) {
            lead = layout.getLineEnd(currentLine);
            char Char = text.charAt(lead - 1);
            if (Char == '\n') {
                break;
            }
            currentLine++;
        }
        return lead;
    }

    public int getLineForOffset(Layout layout, int offset) {
        if (null == layout) {
            return -1;
        }

        return layout.getLineForOffset(offset);
    }

}