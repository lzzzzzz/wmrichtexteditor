package com.widemouth.library.span;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.widemouth.library.R;


public class WMSplitLineSpan extends ReplacementSpan {

    public enum LineType {
        SOLID(R.drawable.icon_line_solid),
        DOTTED(R.drawable.icon_line_dotted);

        public int redId;

        LineType(int resId) {
            this.redId = resId;
        }
    }

    private int mWidth;
    private int mHeight = 1;
    private LineType lineType;

    public WMSplitLineSpan(int mWidth, LineType lineType) {
        this.mWidth = mWidth;
        this.lineType = lineType;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        return mWidth;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        paint.setStrokeWidth(mHeight);
        if (lineType == LineType.DOTTED) {
            paint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
        }

        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        int fontTop = y + fontMetricsInt.top;
        int fontMetricsHeight = fontMetricsInt.bottom - fontMetricsInt.top;
        int iconTop = fontTop + (fontMetricsHeight - mHeight) / 2;
        canvas.drawLine(x, iconTop, x + mWidth, iconTop, paint);
    }


    public int getWidth() {
        return mWidth;
    }

    public LineType getLineType() {
        return lineType;
    }
}
