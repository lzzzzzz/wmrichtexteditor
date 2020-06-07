package com.widemouth.library.span;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.MotionEvent;

import com.widemouth.library.R;


public class WMListClickToSwitchSpan implements WMLeadingMarginSpan {

    private static final String TAG = "WMListClickToSwitchSpan";
    public static Context context;
    private int size = 36;
    private WMDrawableType type ;
    private boolean isCheck ;
    private boolean downFlag = false;
    private float touchDownX = 0f, touchDownY = 0f;
    private float drawableX = 0, drawableY = 0;

    public WMListClickToSwitchSpan(WMDrawableType type, boolean isCheck) {
        this.type = type;
        this.isCheck = isCheck;
    }

    public WMDrawableType getType() {
        return type;
    }

    public void setType(WMDrawableType type) {
        this.type = type;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }


    public enum WMDrawableType {
        CHECK(R.drawable.icon_no_checked, R.drawable.icon_checked),
        STAR(R.drawable.icon_star_no_checked, R.drawable.icon_star_checked);

        public int noCheckedDrawableResId;
        public int checkedDrawableResId;

        public int getDrawableResId(boolean isCheck) {
            if (isCheck) {
                return checkedDrawableResId;
            } else {
                return noCheckedDrawableResId;
            }
        }

        WMDrawableType(int noCheckedDrawableResId, int checkedDrawableResId) {
            this.noCheckedDrawableResId = noCheckedDrawableResId;
            this.checkedDrawableResId = checkedDrawableResId;
        }
    }

    @Override
    public int getLeadingMargin(boolean first) {
        return 70;
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
        if (first) {
            int spanStart = ((SpannableStringBuilder) text).getSpanStart(this);
            int spanEnd = ((SpannableStringBuilder) text).getSpanEnd(this);
            int currentLine = getLineForOffset(layout, start);
            int textLead = getTextLead(layout, text, currentLine), textEnd = getTextEnd(layout, text, currentLine);

            if (textLead != spanStart || textEnd != spanEnd) {
                ((SpannableStringBuilder) text).removeSpan(this);
                if (textLead != spanStart) {
                    isCheck = false;
                }
                WMListClickToSwitchSpan listCheckViewSpan = new WMListClickToSwitchSpan(type, isCheck);
                if (text.charAt(start) != '\u200B') {
                    SpannableStringBuilder stringBuilder = new SpannableStringBuilder("\u200B");
                    stringBuilder.setSpan(listCheckViewSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ((SpannableStringBuilder) text).insert(start, stringBuilder);
                } else {
                    ((SpannableStringBuilder) text).setSpan(listCheckViewSpan, textLead, textEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }

//
            if (text.length() > 0) {
                if (text.charAt(start) != '\u200B') {
                    ((SpannableStringBuilder) text).removeSpan(this);
                }
            }

            Drawable d = context.getDrawable(type.getDrawableResId(isCheck));
            d.setBounds(0, 0, size, size);
            c.save();
//            // 注意如果这样实现会有问题：TextView 有 lineSpacing 时，这里 bottom 偏大，导致偏下
//            int transY = bottom - d.getBounds().bottom; // 底对齐
//            transY -= (paint.getFontMetricsInt().bottom - paint.getFontMetricsInt().top) / 2 - d.getBounds().bottom / 2; // 居中对齐
//            canvas.translate(x, transY);
//            d.draw(canvas);
//            canvas.restore();

            Paint.FontMetricsInt fontMetricsInt = p.getFontMetricsInt();
            int fontTop = baseline + fontMetricsInt.top;
            int fontMetricsHeight = fontMetricsInt.bottom - fontMetricsInt.top;
            int iconHeight = d.getBounds().bottom - d.getBounds().top;
            int iconTop = fontTop + (fontMetricsHeight - iconHeight) / 2;
            c.translate(x + 14, iconTop);
            drawableX = x + 14;
            drawableY = iconTop;
            d.draw(c);
            c.restore();
        }
    }

    public boolean onTouchEvent(MotionEvent event, float x, float y) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (x > drawableX && x < drawableX + size && y > drawableY && y < drawableY + size) {
                    touchDownX = x;
                    touchDownY = y;
                    downFlag = true;
                } else {
                    downFlag = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (x == touchDownX && y == touchDownY) {
                    isCheck = !isCheck;
                }
        }
        return downFlag;
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

    private  int getLineForOffset(Layout layout, int offset) {
        if (null == layout) {
            return -1;
        }

        return layout.getLineForOffset(offset);
    }

}