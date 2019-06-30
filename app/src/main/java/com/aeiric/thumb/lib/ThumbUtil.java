package com.aeiric.thumb.lib;

import android.os.Looper;

/**
 * @author xujian
 * @desc ThumbUtil
 * @from v1.0.0
 */
class ThumbUtil {

    static boolean isInMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

}
