package com.widemouth.library.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.widemouth.library.wmview.WMDrawable;


public class WMImageGetter implements WMHtml.ImageGetter {

    private Context mContext;

    private TextView mTextView;



    public WMImageGetter(Context context, TextView textView) {
        mContext = context;
        mTextView = textView;
    }

    @Override
    public Drawable getDrawable(String source) {
        //   content://media/external/images/media/846589
        WMDrawable drawable = new WMDrawable(mContext);
        BitmapTarget bitmapTarget = new BitmapTarget(drawable, mTextView);
        try {
            Uri uri = Uri.parse(source);
            Glide.with(mContext).asBitmap().load(uri).centerCrop().into(bitmapTarget);
            return drawable;
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return null;
    }

    private  class BitmapTarget extends SimpleTarget<Bitmap> {
        private final WMDrawable areUrlDrawable;
        private TextView textView;

        private BitmapTarget(WMDrawable urlDrawable, TextView textView) {
            this.areUrlDrawable = urlDrawable;
            this.textView = textView;
        }

        @Override
        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
            int sWidth = WMUtil.getScreenWidthAndHeight(mContext)[0]-textView.getPaddingLeft()-textView.getPaddingRight();
            bitmap = WMUtil.scaleBitmapToFitWidth(bitmap,sWidth);
            int bw = bitmap.getWidth();
            int bh = bitmap.getHeight();
            Rect rect = new Rect(0, 0, bw, bh);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
            bitmapDrawable.setBounds(rect);
            areUrlDrawable.setBounds(rect);
            areUrlDrawable.setDrawable(bitmapDrawable);
            textView.setText(textView.getText());
            textView.invalidate();
        }
    }
}

