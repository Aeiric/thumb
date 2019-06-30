package com.aeiric.thumb.lib;


import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import static com.aeiric.thumb.lib.ThumbGenerator.S_THUMB_SAVE_MAX_LENGTH;

/**
 * @author xujian
 * @desc ThumbCalculate
 * @from v1.0.0
 */
class ThumbCalculate {

    /**
     * 获取视频缩略图的数量
     *
     * @param duration 视频时长
     * @return 小图总数
     */
    static int getThumbCount(long duration) {
        int seconds = (int) duration / 1000;
        int thumbPerSec = getThumbPerSec(duration);
        int count = seconds / thumbPerSec;
        if (duration - count * thumbPerSec * 1000 == 0) {
            return count;
        } else {
            return count + 1;
        }
    }

    /**
     * 每张小缩略图的时长
     *
     * @param duration 视频时长
     * @return 秒每桢
     */
    static int getThumbPerSec(long duration) {
        if (duration / 1000 < 30) {
            return 1;
        } else if (duration / 1000 >= 30 && duration / 1000 <= 60) {
            return 2;
        } else if (duration / 1000 > 60 && duration / 1000 <= 300) {
            return 3;
        } else if (duration / 1000 > 300 && duration / 1000 <= 600) {
            return 4;
        } else {
            long seconds = duration / 1000;
            if (seconds % 150 == 0) {
                return ((int) seconds / 150);
            } else {
                return ((int) seconds / 150 + 1);
            }
        }
    }

    /**
     * 是否有余
     *
     * @param duration 视频时长
     * @return 最后一张图是否需要裁
     */
    static boolean isNeedCut(long duration) {
        int seconds = (int) duration / 1000;
        int thumbPerSec = getThumbPerSec(duration);
        int count = seconds / thumbPerSec;
        return duration - count * thumbPerSec * 1000 != 0;
    }


    /**
     * @param duration 视频时长
     * @return 最后一张图宽度占比
     */
    static float getPercentWidth(long duration) {
        int seconds = (int) duration / 1000;
        int thumbPerSec = getThumbPerSec(duration);
        int count = seconds / thumbPerSec;
        float lastDuration = (float) duration - (thumbPerSec * 1000) * count;
        return lastDuration / (thumbPerSec * 1000);
    }

    /**
     * 根据解码出的桢图片获取新图片的宽度
     *
     * @param bitmap  原图片
     * @param percent 高宽比
     * @return 目标图片宽度
     */
    static int getTargetWidth(@NonNull Bitmap bitmap, float percent) {
        if (bitmap.getWidth() < bitmap.getHeight()) {
            return S_THUMB_SAVE_MAX_LENGTH;
        } else {
            return (int) (S_THUMB_SAVE_MAX_LENGTH / percent);
        }
    }

    /**
     * 根据解码出的桢图片获取目标图片的高度
     *
     * @param bitmap  原图片
     * @param percent 高宽比
     * @return 目标图片高度
     */
    static int getTargetHeight(@NonNull Bitmap bitmap, float percent) {
        if (bitmap.getWidth() < bitmap.getHeight()) {
            return (int) (S_THUMB_SAVE_MAX_LENGTH * percent);
        } else {
            return S_THUMB_SAVE_MAX_LENGTH;
        }
    }
}
