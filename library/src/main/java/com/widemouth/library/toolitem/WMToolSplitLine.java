package com.widemouth.library.toolitem;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.widemouth.library.R;
import com.widemouth.library.span.WMSplitLineSpan;
import com.widemouth.library.util.WMUtil;
import com.widemouth.library.wmview.WMHorizontalScrollView;
import com.widemouth.library.wmview.WMImageButton;

import java.util.ArrayList;
import java.util.List;

public class WMToolSplitLine extends WMToolItem implements View.OnClickListener {

    private static final String TAG = "WMToolSplitLine";
    private PopupWindow popupWindow;

    @Override
    public void applyStyle(int start, int end) {

    }

    @Override
    public List<View> getView(final Context context) {
        WMImageButton imageButton = new WMImageButton(context);

        imageButton.setImageResource(R.drawable.icon_text_splitline);


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == getEditText()) {
                    return;
                }

                popupWindow = new PopupWindow(context);
                WMHorizontalScrollView c = new WMHorizontalScrollView(context);
                for (WMSplitLineSpan.LineType type : WMSplitLineSpan.LineType.values()) {
                    WMImageButton i = new WMImageButton(context);
                    LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    int m = WMUtil.getPixelByDp(context, 5);
                    l.setMargins(m / 2, m, m / 2, m);
                    i.setLayoutParams(l);
                    i.setImageResource(type.redId);
                    i.setTag(type.name());
                    i.setOnClickListener(WMToolSplitLine.this);
                    c.addItemView(i);
                }
                int h = WMUtil.getPixelByDp(context, 45);
                popupWindow.setHeight(h);
                popupWindow.setContentView(c);
                popupWindow.setBackgroundDrawable(new ColorDrawable(0x50FFFFFF));
                popupWindow.setOutsideTouchable(true);
                int offsetY = WMUtil.getPixelByDp(context, -90);
                popupWindow.showAsDropDown(v, 0, offsetY);

            }
        });
        List<View> views = new ArrayList<>();
        views.add(imageButton);
        return views;
    }

    @Override
    public void onSelectionChanged(int selStart, int selEnd) {

    }

    @Override
    public void onCheckStateUpdate() {

    }

    @Override
    public void onClick(View v) {
        Editable editable = getEditText().getEditableText();
        int start = getEditText().getSelectionStart();
        int end = getEditText().getSelectionEnd();

        int size = getEditText().getWidth() - getEditText().getPaddingLeft() - getEditText().getPaddingRight();
        SpannableStringBuilder ssb = new SpannableStringBuilder("\n[splitLine]\n");
        ssb.setSpan(new WMSplitLineSpan(size, WMSplitLineSpan.LineType.valueOf(v.getTag().toString())), 1, 1 + "[splitLine]".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        editable.replace(start, end, ssb);
    }
}