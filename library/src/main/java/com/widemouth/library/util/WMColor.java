package com.widemouth.library.util;

public enum WMColor {
    TRANSPARENT(0,"透明"),
    BLACK(0xFF333333, "雅黑色"),
    GARY(0xFFC0C0C0, "灰色"),
    CA8A8A8(0xFFA8A8A8, "浅灰色"),
    RED(0xFFFF2400, "橙红色"),
    ORANGE(0xFFFF7F00, "橙色"),
    YELLOW(0xFFFFFF00, "黄色"),
    C99CC32(0xFF99CC32, "黄绿色"),
    C238E23(0xFF238E23, "森林绿"),
    GREEN(0xFF238E68, "海绿色"),
    CYAN(0xFF00FFFF, "青色"),
    CC0D9D9(0xFFC0D9D9, "浅蓝色"),
    BLUE(0xFF3299CC, "天蓝色"),
    C3232CD(0xFF3232CD, "中蓝色"),
    CE47833(0xFFE47833, "桔黄色"),
    BROWN(0xFFA67D3D, "棕色"),
    CFC9D99(0xFFFC9D99, "粉色"),
    CFF1CAE(0xFFFF1CAE, "艳粉红色"),
    DB70DBC(0xFFDB70DB, "淡紫色"),
    C9932CD(0xFF9932CD, "深兰花色");


    public int ColorInt;
    public String ColorName;

    WMColor(int ColorInt, String ColorName) {
        this.ColorInt = ColorInt;
        this.ColorName = ColorName;
    }
}