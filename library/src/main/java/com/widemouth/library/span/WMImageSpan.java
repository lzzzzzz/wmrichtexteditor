package com.widemouth.library.span;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.style.ImageSpan;

import androidx.annotation.NonNull;


public class WMImageSpan extends ImageSpan {

    public enum ImageType {
        URI,
        URL,
        RES,
    }

    private Context mContext;

    private Uri mUri;

    private String mUrl;

    private int mResId;

    public WMImageSpan(Context context, Bitmap bitmapDrawable, Uri uri) {
        super(context, bitmapDrawable,ImageSpan.ALIGN_CENTER);
        this.mContext = context;
        this.mUri = uri;
    }

    public WMImageSpan(Context context, Bitmap bitmapDrawable, String url) {
        super(context, bitmapDrawable,ImageSpan.ALIGN_CENTER);
        this.mContext = context;
        this.mUrl = url;
    }

    public WMImageSpan(Context context, int resId) {
        super(context, resId,ImageSpan.ALIGN_CENTER);
        this.mContext = context;
        this.mResId = resId;
    }

    public WMImageSpan(Drawable drawable, String source) {
        super(drawable, source,ImageSpan.ALIGN_CENTER);
    }


    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        super.draw(canvas, text, start, end, x, top, y, bottom, paint);
//        Drawable d = getDrawable();
//        canvas.save();
////            // 注意如果这样实现会有问题：TextView 有 lineSpacing 时，这里 bottom 偏大，导致偏下
////            int transY = bottom - d.getBounds().bottom; // 底对齐
////            transY -= (paint.getFontMetricsInt().bottom - paint.getFontMetricsInt().top) / 2 - d.getBounds().bottom / 2; // 居中对齐
////            canvas.translate(x, transY);
////            d.draw(canvas);
////            canvas.restore();
//
//        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
//        int fontTop = y + fontMetricsInt.top;
//        int fontMetricsHeight = fontMetricsInt.bottom - fontMetricsInt.top;
//        int iconHeight = d.getBounds().bottom - d.getBounds().top;
//        int iconTop = fontTop + (fontMetricsHeight - iconHeight) / 2;
//        canvas.translate(x, iconTop);
//        d.draw(canvas);
//        canvas.restore();
    }


    @Override
    public String getSource() {
        if (this.mUri != null) {
            return this.mUri.toString();
        } else if (this.mUrl != null) {
            return this.mUrl;
        } else {
            return String.valueOf(this.mResId);
        }
    }

}
