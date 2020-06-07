package com.widemouth.library.toolitem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.widemouth.library.R;
import com.widemouth.library.span.WMImageSpan;
import com.widemouth.library.util.WMUtil;
import com.widemouth.library.wmview.WMImageButton;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.listener.OnCheckedListener;
import com.zhihu.matisse.listener.OnSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class WMToolImage extends WMToolItem {
    private static final String TAG = "WMToolImage";

    public static final int ALBUM_CHOOSE = 23;

    private Context context;

    @Override
    public void applyStyle(int start, int end) {

    }

    @Override
    public List<View> getView(final Context context) {
        this.context = context;
        WMImageButton imageButton = new WMImageButton(context);

        imageButton.setImageResource(R.drawable.icon_text_picture);

        view = imageButton;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matisse.from((Activity) context)
                        .choose(MimeType.ofImage(), false)
                        .countable(true)
                        .capture(true)
                        .captureStrategy(
                                new CaptureStrategy(true, "com.widemouth.wmrichtexteditor.fileprovider", "test"))
                        .maxSelectable(1)
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .setOnSelectedListener(new OnSelectedListener() {
                            @Override
                            public void onSelected(@NonNull List<Uri> uriList, @NonNull List<String> pathList) {
                                Log.e("onSelected", "onSelected: pathList=" + pathList);
                            }
                        })
                        .showSingleMediaType(true)
                        .originalEnable(true)
                        .maxOriginalSize(10)
                        .autoHideToolbarOnSingleTap(true)
                        .setOnCheckedListener(new OnCheckedListener() {
                            @Override
                            public void onCheck(boolean isChecked) {
                                Log.e("isChecked", "onCheck: isChecked=" + isChecked);
                            }
                        })
                        .forResult(ALBUM_CHOOSE);

            }
        });
        List<View> views = new ArrayList<>();
        views.add(view);
        return views;
    }

    public void onActivityResult(Intent data) {
        Uri uri = Matisse.obtainResult(data).get(0);
        insertImage(uri, WMImageSpan.ImageType.URI);
    }

    @Override
    public void onSelectionChanged(int selStart, int selEnd) {

    }

    @Override
    public void onCheckStateUpdate() {

    }

    public void insertImage(final Object src, final WMImageSpan.ImageType type) {
        // Note for a possible bug:
        // There may be a possible bug here, it is related to:
        //   https://issuetracker.google.com/issues/67102093
        // But I forget what the real use case is, just remember that
        // When working on the feature, there was a crash bug
        //
        // That's why I introduce the method below:
        // this.mEditText.useSoftwareLayerOnAndroid8();
        //
        // However, with this setting software layer, there is another
        // bug which is when inserting a few (2~3) images, there will
        // be a warning:
        //
        // AREditText not displayed because it is too large to fit into a software layer (or drawing cache), needs 17940960 bytes, only 8294400 available
        //
        // And then the EditText becomes an entire white nothing displayed
        //
        // So in temporary, I commented out this method invoke to prevent this known issue
        // When someone run into the crash bug caused by this on Android 8
        // I can then find out a solution to cover both cases

        SimpleTarget myTarget = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                if (bitmap == null) {
                    return;
                }

                int sWidth = WMUtil.getScreenWidthAndHeight(context)[0] - getEditText().getPaddingLeft() - getEditText().getPaddingRight();

                bitmap = WMUtil.scaleBitmapToFitWidth(bitmap, sWidth);

                ImageSpan imageSpan = null;
                if (type == WMImageSpan.ImageType.URI) {
                    imageSpan = new WMImageSpan(context, bitmap, ((Uri) src));
                } else if (type == WMImageSpan.ImageType.URL) {
                    imageSpan = new WMImageSpan(context, bitmap, ((String) src));
                }
                if (imageSpan == null) {
                    return;
                }
                insertSpan(imageSpan);
            }
        };

        if (type == WMImageSpan.ImageType.URI) {
            Glide.with(context).asBitmap().load((Uri) src).centerCrop().into(myTarget);
        } else if (type == WMImageSpan.ImageType.URL) {
            Glide.with(context).asBitmap().load((String) src).centerCrop().into(myTarget);
        } else if (type == WMImageSpan.ImageType.RES) {
            ImageSpan imageSpan = new WMImageSpan(context, ((int) src));
            insertSpan(imageSpan);
        }
    }

    private void insertSpan(ImageSpan imageSpan) {
        Editable editable = this.getEditText().getEditableText();
        int start = this.getEditText().getSelectionStart();
        int end = this.getEditText().getSelectionEnd();

        SpannableStringBuilder text = new SpannableStringBuilder("\n[image]\n\n");

        text.setSpan(imageSpan, 1, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        editable.replace(start, end, text);

    }
}