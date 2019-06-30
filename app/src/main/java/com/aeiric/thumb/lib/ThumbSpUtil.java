/*
 * Copyright (c) 2015-2019 BiliBili Inc.
 */

package com.aeiric.thumb.lib;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


/**
 * @author xujian
 * @desc ThumbSpUtil
 * @from v1.0.0
 */
class ThumbSpUtil {
    private static final String S_SP_THUMB_TAG = "Thumb";
    private static final String S_SP_THUMB_SEEK_X_SUFFIX = "seekX_";
    private static final String S_SP_THUMB_ERROR_SUFFIX = "error_";
    private static final String S_SP_THUMB_PREFIX = "thumb_";
    private static final String S_SP_TRACK_PREFIX = "track_";
    private static final boolean DEFAULT_BOOLEAN = false;
    private static final int DEFAULT_INT = 0;
    static final String DEFAULT_STR = "";

    static int getSeekX(@NonNull Context context, @NonNull String videoPath) {
        return getInt(context, S_SP_THUMB_SEEK_X_SUFFIX + videoPath);
    }

    static void putSeekX(@NonNull Context context, @NonNull String videoPath, int value) {
        putInt(context, S_SP_THUMB_SEEK_X_SUFFIX + videoPath, value);
    }

    static boolean getError(@NonNull Context context, @NonNull String videoPath) {
        return getBoolean(context, S_SP_THUMB_ERROR_SUFFIX + videoPath);
    }

    static void putError(@NonNull Context context, @NonNull String videoPath) {
        putBoolean(context, S_SP_THUMB_ERROR_SUFFIX + videoPath);
    }

    static void putThumbSuffix(@NonNull Context context, @NonNull String videoPath, @NonNull String suffix) {
        putString(context, S_SP_THUMB_PREFIX + videoPath, suffix);
    }

    @Nullable
    static String getThumbSuffix(@NonNull Context context, @NonNull String videoPath) {
        return getString(context, S_SP_THUMB_PREFIX + videoPath);
    }

    static void putTrackSuffix(@NonNull Context context, @NonNull String videoPath, @NonNull String suffix) {
        putString(context, S_SP_TRACK_PREFIX + videoPath, suffix);
    }

    @Nullable
    static String getTrackSuffix(@NonNull Context context, @NonNull String videoPath) {
        return getString(context, S_SP_TRACK_PREFIX + videoPath);
    }

    private static void putInt(@NonNull Context context, @NonNull String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences(S_SP_THUMB_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private static int getInt(@NonNull Context context, @NonNull String key) {
        SharedPreferences preferences = context.getSharedPreferences(S_SP_THUMB_TAG, Context.MODE_PRIVATE);
        return preferences.getInt(key, DEFAULT_INT);
    }

    private static void putBoolean(@NonNull Context context, @NonNull String key) {
        SharedPreferences preferences = context.getSharedPreferences(S_SP_THUMB_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, true);
        editor.apply();
    }

    private static boolean getBoolean(@NonNull Context context, @NonNull String key) {
        SharedPreferences preferences = context.getSharedPreferences(S_SP_THUMB_TAG, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, DEFAULT_BOOLEAN);
    }

    private static void putString(@NonNull Context context, @NonNull String key, @NonNull String value) {
        SharedPreferences preferences = context.getSharedPreferences(S_SP_THUMB_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    @Nullable
    private static String getString(@NonNull Context context, @NonNull String key) {
        SharedPreferences preferences = context.getSharedPreferences(S_SP_THUMB_TAG, Context.MODE_PRIVATE);
        return preferences.getString(key, DEFAULT_STR);
    }


}
