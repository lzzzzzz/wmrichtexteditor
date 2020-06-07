package com.widemouth.library.span;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;

import com.widemouth.library.util.WMUtil;


public class WMListNumberSpan implements WMLeadingMarginSpan {

    public static Context context;
    private static final String TAG = "WMListNumberSpan";
    private Paint paint = new Paint();

    private int Number = 1;
    private int mFirst = 120, mRest = 50;


    public WMListNumberSpan() {
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(WMUtil.getPixelByDp(context, 16));
    }

    public WMListNumberSpan(int first, int rest) {
        mFirst = first;
        mRest = rest;
    }

    @Override
    public int getLeadingMargin(boolean first) {
        return first ? mFirst : mRest;
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p,
                                  int x, int dir, int top, int baseline, int bottom,
                                  CharSequence text,
                                  int start, int end, boolean first,
                                  Layout layout) {
        if (first) {
            if (start > 1) {
                WMListNumberSpan[] listNumberSpans = ((SpannableStringBuilder) text).getSpans(start - 2, start - 1, WMListNumberSpan.class);
                if (listNumberSpans != null && listNumberSpans.length > 0) {
                    WMListNumberSpan listNumberSpan = listNumberSpans[listNumberSpans.length - 1];
                    this.Number = listNumberSpan.getNumber() + 1;
                }
            }
            int spanStart = ((SpannableStringBuilder) text).getSpanStart(this);
            int spanEnd = ((SpannableStringBuilder) text).getSpanEnd(this);
            int currentLine = getLineForOffset(layout, start);
            int textLead = getTextLead(layout, text, currentLine), textEnd = getTextEnd(layout, text, currentLine);

            if (textLead != spanStart || textEnd != spanEnd) {
                ((SpannableStringBuilder) text).removeSpan(this);
                if (text.charAt(start) != '\u200B') {
                    SpannableStringBuilder stringBuilder = new SpannableStringBuilder("\u200B");
                    stringBuilder.setSpan(new WMListNumberSpan(), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ((SpannableStringBuilder) text).insert(start, stringBuilder);
                } else {
                    ((SpannableStringBuilder) text).setSpan(new WMListNumberSpan(), textLead, textEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }


            if (text.length() == 0 || text.charAt(start) != '\u200B') {
                ((SpannableStringBuilder) text).removeSpan(this);
            }


            CharacterStyle[] styleSpans = ((SpannableStringBuilder) text).getSpans(start + 1, start + 2, CharacterStyle.class);
            for (CharacterStyle styleSpan : styleSpans) {
                if (((SpannableStringBuilder) text).getSpanStart(styleSpan) ==
                        ((SpannableStringBuilder) text).getSpanEnd(styleSpan)) {
                    continue;
                }
                if (styleSpan instanceof StyleSpan) {
                    if (((StyleSpan) styleSpan).getStyle() == Typeface.BOLD) {

                        paint.setFakeBoldText(true);
                    } else if (((StyleSpan) styleSpan).getStyle() == Typeface.ITALIC) {
                        paint.setTextSkewX(-0.3f);
                    }
                } else if (styleSpan instanceof AbsoluteSizeSpan) {
                    paint.setTextSize(WMUtil.getPixelByDp(context, ((AbsoluteSizeSpan) styleSpan).getSize()));
                } else if (styleSpan instanceof ForegroundColorSpan) {
                    paint.setColor(((ForegroundColorSpan) styleSpan).getForegroundColor());
                }
            }

            float left = layout.getLineLeft(layout.getLineForOffset(start));
            float right = layout.getLineRight(layout.getLineForOffset(start));
            float position;
            if (left == 0) {
                position = x + dir + left + mRest;
            } else {
                position = x + dir + left + mRest - mFirst + mRest;
            }
            c.drawText(
                    Number + ".",
                    position,
                    baseline,
                    paint);


        }

    }

    public int getTextLead(Layout layout, CharSequence text, int currentLine) {
        int lead = 0;
        while (currentLine != 0) {
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
        int end = text.length();
        while (currentLine < layout.getLineCount()) {
            end = layout.getLineEnd(currentLine);
            if (end > 0) {
                char Char = text.charAt(end - 1);
                if (Char == '\n') {
                    break;
                }
            }
            currentLine++;
        }
        return end;
    }

    private int getLineForOffset(Layout layout, int offset) {
        if (null == layout) {
            return -1;
        }

        return layout.getLineForOffset(offset);
    }

    private int getNumber() {
        return Number;
    }
}