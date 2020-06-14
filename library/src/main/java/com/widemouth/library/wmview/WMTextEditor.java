package com.widemouth.library.wmview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.widemouth.library.toolitem.WMToolAlignment;
import com.widemouth.library.toolitem.WMToolBackgroundColor;
import com.widemouth.library.toolitem.WMToolBold;
import com.widemouth.library.toolitem.WMToolImage;
import com.widemouth.library.toolitem.WMToolItalic;
import com.widemouth.library.toolitem.WMToolItem;
import com.widemouth.library.toolitem.WMToolListBullet;
import com.widemouth.library.toolitem.WMToolListClickToSwitch;
import com.widemouth.library.toolitem.WMToolListNumber;
import com.widemouth.library.toolitem.WMToolQuote;
import com.widemouth.library.toolitem.WMToolSplitLine;
import com.widemouth.library.toolitem.WMToolStrikethrough;
import com.widemouth.library.toolitem.WMToolTextColor;
import com.widemouth.library.toolitem.WMToolTextSize;
import com.widemouth.library.toolitem.WMToolUnderline;
import com.widemouth.library.util.WMUtil;


public class WMTextEditor extends LinearLayout {


    WMEditText editText;
    WMToolContainer toolContainer;

    private WMToolItem toolBold = new WMToolBold();
    private WMToolItem toolItalic = new WMToolItalic();
    private WMToolItem toolUnderline = new WMToolUnderline();
    private WMToolItem toolStrikethrough = new WMToolStrikethrough();
    private WMToolItem toolImage = new WMToolImage();
    private WMToolItem toolTextColor = new WMToolTextColor();
    private WMToolItem toolBackgroundColor = new WMToolBackgroundColor();
    private WMToolItem toolTextSize = new WMToolTextSize();
    private WMToolItem toolListNumber = new WMToolListNumber();
    private WMToolItem toolListBullet = new WMToolListBullet();
    private WMToolItem toolAlignment = new WMToolAlignment();
    private WMToolItem toolQuote = new WMToolQuote();
    private WMToolItem toolListClickToSwitch = new WMToolListClickToSwitch();
    private WMToolItem toolSplitLine = new WMToolSplitLine();

    public WMTextEditor(Context context) {
        this(context, null);
    }

    public WMTextEditor(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WMTextEditor(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public WMTextEditor(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    public void initView() {
        ScrollView scrollView = new ScrollView(getContext());
        editText = new WMEditText(getContext());
        scrollView.addView(editText, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.setOrientation(VERTICAL);
        this.addView(scrollView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        toolContainer = new WMToolContainer(getContext());
        int h = WMUtil.getPixelByDp(getContext(), 45);
        this.addView(toolContainer, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, h));
        toolContainer.addToolItem(toolImage);
        toolContainer.addToolItem(toolTextColor);
        toolContainer.addToolItem(toolBackgroundColor);
        toolContainer.addToolItem(toolTextSize);
        toolContainer.addToolItem(toolBold);
        toolContainer.addToolItem(toolItalic);
        toolContainer.addToolItem(toolUnderline);
        toolContainer.addToolItem(toolStrikethrough);
        toolContainer.addToolItem(toolListNumber);
        toolContainer.addToolItem(toolListBullet);
        toolContainer.addToolItem(toolAlignment);
        toolContainer.addToolItem(toolQuote);
        toolContainer.addToolItem(toolListClickToSwitch);
        toolContainer.addToolItem(toolSplitLine);
        editText.setupWithToolContainer(toolContainer);
    }

    public void onActivityResult(Intent data) {
        ((WMToolImage) toolImage).onActivityResult(data);
    }

    public WMTextEditor setEditable(boolean editable) {
        editText.setEditable(editable);
        if (editable) {
            toolContainer.setVisibility(VISIBLE);
        } else {
            toolContainer.setVisibility(GONE);
        }
        return this;
    }

    public WMEditText getEditText() {
        return editText;
    }

    public WMToolContainer getToolContainer() {
        return toolContainer;
    }

    public WMTextEditor setEditTextMaxLines(int maxLines) {
        editText.setMaxLines(maxLines);
        return this;
    }

    public WMTextEditor setEditTextPadding(int left, int top, int right, int bottom) {
        editText.setPadding(left, top, right, bottom);
        return this;
    }

    public WMTextEditor setEditTextLineSpacing(float add, float mult) {
        editText.setLineSpacing(add, mult);
        return this;
    }

    public WMTextEditor fromHtml(String html) {
        editText.fromHtml(html);
        return this;
    }


    public WMTextEditor fromHtml(String html, int textSizeOffset) {
        editText.fromHtml(html, textSizeOffset);
        return this;
    }

    public String getHtml() {
        return editText.getHtml();
    }

    public WMTextEditor setupWithFragment(Fragment fragment) {
        ((WMToolImage) toolImage).setupWithFragment(fragment);
        return this;
    }

}