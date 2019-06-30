/*
 * Copyright (c) 2015-2016 BiliBili Inc.
 */

package com.aeiric.thumb.lib;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;

import static com.aeiric.thumb.lib.ThumbConstant.PREFIX_THUMB;
import static com.aeiric.thumb.lib.ThumbConstant.PREFIX_TRACK;


/**
 * @author xujian
 * @desc ThumbFileUtil
 * @from v1.0.0
 */
@SuppressWarnings("JavaDoc")
class ThumbFileUtil {
    private static final String THUMB_DIR_NAME = "thumb";

    /**
     * 获取轨道图片缓存文件（小图面)
     *
     * @param context context
     * @param time    桢图对应时间
     * @return
     */
    @Nullable
    static File getCacheTrackFile(@NonNull Context context, @NonNull String trackDirSuffix, float time) {
        File dir = getTrackDir(context, trackDirSuffix);
        if (dir != null) {
            return getFile(dir, PREFIX_TRACK, time);
        }
        return null;
    }

    /**
     * 获取预览图片封面缓存文件（大封面图片)
     *
     * @param context context
     * @param time    桢图对应时间
     * @return
     */
    @Nullable
    static File getCacheThumbFile(@NonNull Context context, @NonNull String thumbDirSuffix, float time) {
        File dir = getThumbDir(context, thumbDirSuffix);
        if (dir != null) {
            return getFile(dir, PREFIX_THUMB, time);
        }
        return null;
    }


    /**
     * 获取缓存文件
     *
     * @param dir  存储图片的文件夹
     * @param time 桢图对应时间
     * @return
     */
    @Nullable
    private static File getFile(@NonNull File dir, @NonNull String prefix, float time) {
        String child = prefix + (long) (time * 1000000) + ".jpg";
        File imgFile = new File(dir, child);
        if (imgFile.exists()) {
            return imgFile;
        }
        return null;
    }

    /**
     * 获取缓存轨道图路径
     *
     * @param context context
     * @param time    桢图对应时间
     * @return
     */
    @Nullable
    static String getTrackFilePath(@NonNull Context context, @NonNull String trackDirSuffix, float time) {
        File dir = getTrackDir(context, trackDirSuffix);
        if (dir != null) {
            return dir.getAbsolutePath() + File.separator + PREFIX_TRACK + (long) (time * 1000000) + ".jpg";
        }
        return null;
    }

    /**
     * 获取缓存封面图片路径
     *
     * @param context context
     * @param time    桢图对应时间
     * @return
     */
    @Nullable
    static String getThumbFilePath(@NonNull Context context, @NonNull String thumbDirSuffix, float time) {
        File dir = getThumbDir(context, thumbDirSuffix);
        if (dir != null) {
            return dir.getAbsolutePath() + File.separator + PREFIX_THUMB + (long) (time * 1000000) + ".jpg";
        }
        return null;
    }


    /**
     * 创建轨道图片文件
     *
     * @param context 存储图片的文件夹
     * @param time    桢图对应时间
     * @return
     */
    @Nullable
    static File makeTrackFile(@NonNull Context context, @NonNull String trackDirSuffix, float time) {
        File dir = getTrackDir(context, trackDirSuffix);
        if (dir != null) {
            return makeFile(dir, PREFIX_TRACK, time);
        } else {
            if (makeTrackDir(context, trackDirSuffix)) {
                dir = getTrackDir(context, trackDirSuffix);
                if (dir != null) {
                    return makeFile(dir, PREFIX_TRACK, time);
                }
            }
        }
        return null;
    }

    /**
     * 创建大封面图片文件
     *
     * @param context 存储图片的文件夹
     * @param time    桢图对应时间
     * @return
     */
    @Nullable
    static File makeThumbFile(@NonNull Context context, @NonNull String thumbDirSuffix, float time) {
        File dir = getThumbDir(context, thumbDirSuffix);
        if (dir != null) {
            return makeFile(dir, PREFIX_THUMB, time);
        } else {
            if (makeThumbDir(context, thumbDirSuffix)) {
                dir = getThumbDir(context, thumbDirSuffix);
                if (dir != null) {
                    return makeFile(dir, PREFIX_THUMB, time);
                }
            }
        }
        return null;
    }

    /**
     * 创建图片文件
     *
     * @param prefix 文件前缀
     * @param time   桢图对应时间
     * @return
     */
    @Nullable
    private static File makeFile(@NonNull File dir, @Nullable String prefix, float time) {
        String fileName = (long) (time * 1000000) + ".jpg";
        if (prefix != null) {
            fileName = prefix + fileName;
        }
        File file = new File(dir, fileName);
        if (file.exists()) {
            return file;
        } else {
            try {
                if (file.createNewFile()) {
                    return file;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 创建存储视频解码封面(大图片)的文件夹
     *
     * @param context context
     * @return
     */
    static boolean makeRootDir(@NonNull Context context) {
        if (getRootDir(context) != null) {
            return true;
        } else {
            if (context.getExternalCacheDir() != null) {
                return makeDir(context.getExternalCacheDir(), THUMB_DIR_NAME);
            }
        }
        return false;
    }

    /**
     * 创建存储视频解码图片的跟目录文件夹
     *
     * @param context context
     * @return
     */
    @Nullable
    private static File getRootDir(@NonNull Context context) {
        if (context.getExternalCacheDir() != null) {
            return getDir(context.getExternalCacheDir(), THUMB_DIR_NAME);
        }
        return null;
    }

    /**
     * 创建存储视频解码封面(大图片)的文件夹
     *
     * @param context        context
     * @param thumbDirSuffix 大图（封面）文件夹后缀
     * @return
     */
    static boolean makeThumbDir(@NonNull Context context, @NonNull String thumbDirSuffix) {
        File rootDir = getRootDir(context);
        if (rootDir != null) {
            return makeDir(rootDir, thumbDirSuffix);
        } else {
            if (makeRootDir(context)) {
                rootDir = getRootDir(context);
                if (rootDir != null) {
                    return makeDir(rootDir, thumbDirSuffix);
                }
            }
        }
        return false;
    }

    /**
     * 获取存储视频解码封面(大图片)的文件夹
     *
     * @param context        context
     * @param thumbDirSuffix 大图（封面）文件夹后缀
     * @return
     */
    @Nullable
    private static File getThumbDir(@NonNull Context context, @NonNull String thumbDirSuffix) {
        File rootDir = getRootDir(context);
        if (rootDir != null) {
            return getDir(rootDir, thumbDirSuffix);
        }
        return null;
    }


    /**
     * 创建存储视频解码小图片的文件夹
     *
     * @param context        context
     * @param trackDirSuffix 小图文件夹后缀
     * @return
     */
    static boolean makeTrackDir(@NonNull Context context, @NonNull String trackDirSuffix) {
        File rootDir = getRootDir(context);
        if (rootDir != null) {
            return makeDir(rootDir, trackDirSuffix);
        } else {
            if (makeRootDir(context)) {
                rootDir = getRootDir(context);
                if (rootDir != null) {
                    return makeDir(rootDir, trackDirSuffix);
                }
            }
        }
        return false;
    }

    /**
     * 获取存储视频解码封面(大图片)的文件夹
     *
     * @param context        context
     * @param trackDirSuffix 轨道图（小图）文件夹后缀
     * @return
     */
    @Nullable
    private static File getTrackDir(@NonNull Context context, @NonNull String trackDirSuffix) {
        File rootDir = getRootDir(context);
        if (rootDir != null && rootDir.exists()) {
            return getDir(rootDir, trackDirSuffix);
        }
        return null;
    }

    /**
     * 创建存储视频解码图片的文件夹
     *
     * @param dir     父文件夹
     * @param dirName 文件夹名称
     * @return
     */
    private static boolean makeDir(@NonNull File dir, @NonNull String dirName) {
        File resultDir = new File(dir, dirName);
        if (resultDir.exists()) {
            return true;
        } else {
            return resultDir.mkdir();
        }
    }

    /**
     * 创建存储视频解码图片的文件夹
     *
     * @param dir     父文件夹
     * @param dirName 文件夹名称
     * @return
     */
    @Nullable
    private static File getDir(@NonNull File dir, @NonNull String dirName) {
        File resultDir = new File(dir, dirName);
        if (resultDir.exists()) {
            return resultDir;
        }
        return null;
    }


}