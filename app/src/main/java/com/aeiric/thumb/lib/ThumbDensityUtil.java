/*
 * Copyright (c) 2015-2017 BiliBili Inc.
 */

package com.aeiric.thumb.lib;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

/**
 * @author xujian
 * @desc ThumbDensityUtil
 * @from v1.0.0
 */
@SuppressWarnings("JavaDoc")
class ThumbDensityUtil {
    private static int sWidthPixels;
    private static int sHeightPixels;


    /**
     * 根据手机分辨率获取设备的宽度
     *
     * @param context
     * @return
     */
    static int getDevicesWidthPixels(Context context) {
        if (sWidthPixels <= 0) {
            int x, y, orientation = context.getResources().getConfiguration().orientation;
            WindowManager wm = ((WindowManager)
                    context.getSystemService(Context.WINDOW_SERVICE));
            Display display = wm.getDefaultDisplay();
            Point screenSize = new Point();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                display.getRealSize(screenSize);
            } else {
                display.getSize(screenSize);
            }
            x = screenSize.x;
            y = screenSize.y;
            return sWidthPixels = getWidth(x, y, orientation);
        }
        return sWidthPixels;
    }

    /**
     * 根据手机分辨率获取设备的高度
     *
     * @param context
     * @return
     */
    static int getDevicesHeightPixels(Context context) {
        if (sHeightPixels <= 0) {
            int x, y, orientation = context.getResources().getConfiguration().orientation;
            WindowManager wm = ((WindowManager)
                    context.getSystemService(Context.WINDOW_SERVICE));
            Display display = wm.getDefaultDisplay();
            Point screenSize = new Point();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                display.getRealSize(screenSize);
            } else {
                display.getSize(screenSize);
            }
            x = screenSize.x;
            y = screenSize.y;
            return sHeightPixels = getHeight(x, y, orientation);
        }
        return sHeightPixels;
    }

    private static int getWidth(int x, int y, int orientation) {
        return orientation == Configuration.ORIENTATION_PORTRAIT ? x : y;
    }

    private static int getHeight(int x, int y, int orientation) {
        return orientation == Configuration.ORIENTATION_PORTRAIT ? y : x;
    }

}
