package com.aeiric.thumb.lib;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.aeiric.thumb.lib.ThumbConstant.PREFIX_THUMB;
import static com.aeiric.thumb.lib.ThumbConstant.PREFIX_TRACK;
import static com.aeiric.thumb.lib.ThumbSpUtil.DEFAULT_STR;


/**
 * @author xujian
 * @desc ThumbDirUtil
 * @from v1.0.0
 */
class ThumbDirUtil {

    @Nullable
    static String createThumbDirSuffix(@NonNull Context context, @NonNull String videoPath) {
        if (getThumbDirSuffix(context, videoPath) != null) {
            return getThumbDirSuffix(context, videoPath);
        } else {
            return buildThumbSuffix();
        }
    }

    @Nullable
    static String createTrackDirSuffix(@NonNull Context context, @NonNull String videoPath) {
        if (getTrackDirSuffix(context, videoPath) != null) {
            return getTrackDirSuffix(context, videoPath);
        } else {
            return buildTrackSuffix();
        }
    }

    @Nullable
    private static String getThumbDirSuffix(@NonNull Context context, @NonNull String videoPath) {
        if (isThumbSuffixExist(context, videoPath)) {
            return ThumbSpUtil.getThumbSuffix(context, videoPath);
        }
        return null;
    }

    public static boolean isThumbSuffixExist(@NonNull Context context, @NonNull String videoPath) {
        return !DEFAULT_STR.equals(ThumbSpUtil.getThumbSuffix(context, videoPath));
    }

    @NonNull
    private static String buildThumbSuffix() {
        return PREFIX_THUMB + System.currentTimeMillis();
    }

    @Nullable
    private static String getTrackDirSuffix(@NonNull Context context, @NonNull String videoPath) {
        if (isTrackSuffixExist(context, videoPath)) {
            return ThumbSpUtil.getTrackSuffix(context, videoPath);
        }
        return null;
    }

    private static boolean isTrackSuffixExist(@NonNull Context context, @NonNull String videoPath) {
        return !DEFAULT_STR.equals(ThumbSpUtil.getTrackSuffix(context, videoPath));
    }

    @NonNull
    private static String buildTrackSuffix() {
        return PREFIX_TRACK + System.currentTimeMillis();
    }

}
