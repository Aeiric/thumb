package com.aeiric.thumb.lib;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;

import static com.aeiric.thumb.lib.ThumbConstant.PREFIX_THUMB;
import static com.aeiric.thumb.lib.ThumbConstant.PREFIX_TRACK;
import static com.aeiric.thumb.lib.ThumbFileUtil.getTrackFilePath;

/**
 * @author xujian
 * @desc ThumbUtil
 * @from v1.0.0
 */
class ThumbUtil {

    static boolean isInMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    @NonNull
    static String getTrackTag(@NonNull Context context, @NonNull String trackDirSuffix, int time) {
        return getTag(context, PREFIX_TRACK, trackDirSuffix, time);
    }

    @NonNull
    static String getThumbTag(@NonNull Context context, @NonNull String thumbDirSuffix, int time) {
        return getTag(context, PREFIX_THUMB, thumbDirSuffix, time);
    }

    @NonNull
    private static String getTag(@NonNull Context context, @NonNull String filePreffix, @NonNull String dirSuffix, int time) {
        return filePreffix + getTrackFilePath(context, dirSuffix, time);
    }

}
