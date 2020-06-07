package com.widemouth.library.span;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

import com.widemouth.library.util.WMUtil;


public class WMListBulletSpan implements WMLeadingMarginSpan {
    public static Context context;
    private int mFirst = 100, mRest = 50;

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
            int spanStart = ((SpannableStringBuilder) text).getSpanStart(this);
            int spanEnd = ((SpannableStringBuilder) text).getSpanEnd(this);
            int currentLine = getLineForOffset(layout, start);
            int textLead = getTextLead(layout, text, currentLine), textEnd = getTextEnd(layout, text, currentLine);

            if (textLead != spanStart || textEnd != spanEnd) {
                ((SpannableStringBuilder) text).removeSpan(this);
                if (text.charAt(start) != '\u200B') {
                    SpannableStringBuilder stringBuilder = new SpannableStringBuilder("\u200B");
                    stringBuilder.setSpan(new WMListBulletSpan(), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ((SpannableStringBuilder) text).insert(start, stringBuilder);
                } else {
                    ((SpannableStringBuilder) text).setSpan(new WMListBulletSpan(), textLead, textEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }

//
            if (text.length() > 0) {
                if (text.charAt(start) != '\u200B') {
                    ((SpannableStringBuilder) text).removeSpan(this);
                }
            }

            Paint.Style style = p.getStyle();
            float textSize = p.getTextSize();
            p.setStyle(Paint.Style.FILL);
            p.setTextSize(WMUtil.getPixelByDp(context, 18));

            CharacterStyle[] styleSpans = ((SpannableStringBuilder) text).getSpans(start + 1, start + 2, CharacterStyle.class);
            for (CharacterStyle styleSpan : styleSpans) {
                if (((SpannableStringBuilder) text).getSpanStart(styleSpan) ==
                        ((SpannableStringBuilder) text).getSpanEnd(styleSpan)) {
                    continue;
                }
                if (styleSpan instanceof AbsoluteSizeSpan) {
                    p.setTextSize(WMUtil.getPixelByDp(context, ((AbsoluteSizeSpan) styleSpan).getSize()) + 2);
                } else if (styleSpan instanceof ForegroundColorSpan) {
                    p.setColor(((ForegroundColorSpan) styleSpan).getForegroundColor());
                }
            }

            float left = layout.getLineLeft(layout.getLineForOffset(start));
            float position;
            if (left == 0) {
                position = x + dir + left + mRest;
            } else {
                position = x + dir + left + mRest - mFirst + mRest;
            }
            c.drawText(
                    "\u2022",
                    position,
                    baseline,
                    p);
            p.setStyle(style);
            p.setTextSize(textSize);
        }


//        AlignmentSpan.Standard[] alignments = ((SpannableStringBuilder) text).getSpans(spanStart, spanEnd, AlignmentSpan.Standard.class);
//        if (alignments != null && alignments.length > 0) {
//            for (AlignmentSpan.Standard alignment : alignments) {
//                int alignmentStart = ((SpannableStringBuilder) text).getSpanStart(alignment);
//                int alignmentEnd = ((SpannableStringBuilder) text).getSpanEnd(alignment);
//                ((SpannableStringBuilder) text).removeSpan(alignment);
//                if (alignmentStart < spanStart) {
//                    ((SpannableStringBuilder) text).setSpan(new AlignmentSpan.Standard(alignment.getAlignment()), alignmentStart, spanStart, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//                }
//                if (alignmentEnd > spanEnd) {
//                    ((SpannableStringBuilder) text).setSpan(new AlignmentSpan.Standard(alignment.getAlignment()), spanEnd, alignmentEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//                }
//            }
//        }

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

    public int getLineForOffset(Layout layout, int offset) {
        if (null == layout) {
            return -1;
        }

        return layout.getLineForOffset(offset);
    }
}