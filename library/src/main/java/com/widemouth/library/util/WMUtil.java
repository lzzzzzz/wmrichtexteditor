package com.widemouth.library.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author WideMouth
 */
public class WMUtil {

    /**
     * Gets the pixels by the given number of dp.
     *
     * @param context
     * @param dp
     * @return
     */
    public static int getPixelByDp(Context context, int dp) {
        int pixels = dp;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((AppCompatActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        pixels = (int) (displayMetrics.density * dp + 0.5);
        return pixels;
    }

    /**
     * Returns the screen width and height.
     *
     * @param context
     * @return
     */
    public static int[] getScreenWidthAndHeight(Context context) {
        Point outSize = new Point();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        display.getSize(outSize);

        int[] widthAndHeight = new int[2];
        widthAndHeight[0] = outSize.x;
        widthAndHeight[1] = outSize.y;
        return widthAndHeight;
    }

    public static Bitmap scaleBitmapToFitWidth(Bitmap bitmap, int maxWidth) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int newWidth = maxWidth;
        int newHeight = maxWidth * h / w;
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) newWidth / w);
        float scaleHeight = ((float) newHeight / h);
        if (w < maxWidth * 0.2) {
            return bitmap;
        }
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }

    public static int getParagraphCount(TextView textView, int start, int end) {
        int count = 1;
        Layout layout = textView.getLayout();
        String text = textView.getText().toString();
        for (int i = getLineForOffset(textView, start) + 1; i <= getLineForOffset(textView, end); i++) {
            int lineStart = layout.getLineStart(i);
            char c = text.charAt(lineStart - 1);
            if (c == '\n') {
                count++;
            }
        }
        return count;
    }


    public static int getTextLead(TextView textView, int currentLine) {
        Layout layout = textView.getLayout();
        String text = textView.getText().toString();
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

    public static int getTextLead(int offset, TextView textView) {
        int currentLine = getLineForOffset(textView, offset);
        Layout layout = textView.getLayout();
        String text = textView.getText().toString();
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

    public static int getTextEnd(TextView textView, int currentLine) {
        Layout layout = textView.getLayout();
        String text = textView.getText().toString();
        int end = text.length();
        while (currentLine != textView.getLineCount()) {
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

    public static int getTextEnd(int offset, TextView textView) {
        int currentLine = getLineForOffset(textView, offset);
        Layout layout = textView.getLayout();
        String text = textView.getText().toString();
        int end = text.length();
        while (currentLine < textView.getLineCount()) {
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

    public static int getLineStart(TextView textView, int offset) {
        return textView.getLayout().getLineStart(getLineForOffset(textView, offset));
    }

    public static int getLineStart(int currentLine, TextView textView) {
        return textView.getLayout().getLineStart(currentLine);
    }

    public static int getLineEnd(TextView textView, int offset) {
        return textView.getLayout().getLineEnd(getLineForOffset(textView, offset));
    }

    public static int getLineEnd(int currentLine, TextView textView) {
        return textView.getLayout().getLineEnd(currentLine);
    }

    public static int getLineForOffset(TextView textView, int offset) {
        Layout layout = textView.getLayout();
        if (null == layout) {
            return -1;
        }
        return layout.getLineForOffset(offset);
    }


}
