package com.aeiric.thumb.lib;

import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.text.TextUtils;


/**
 * @author xujian
 * @desc ThumbVideoUtil
 * @from 5.43
 */
class ThumbVideoUtil {

    static int getVideoWidth(@NonNull MediaMetadataRetriever retriever) {
        String width = getVideoWidthS(retriever);
        if (TextUtils.isEmpty(width))
            return 0;
        try {
            return Integer.parseInt(width);
        } catch (Exception e) {
            return 0;
        }
    }

    static int getVideoHeight(@NonNull MediaMetadataRetriever retriever) {
        String height = getVideHeightS(retriever);
        if (TextUtils.isEmpty(height))
            return 0;

        try {
            return Integer.parseInt(height);
        } catch (Exception e) {
            return 0;
        }
    }

    static long getVideoDuration(@NonNull MediaMetadataRetriever retriever) {
        String duration = getVideoDurationS(retriever);
        if (TextUtils.isEmpty(duration))
            return 0;
        try {
            return Long.parseLong(duration);
        } catch (Exception e) {
            return 0;
        }
    }

    private static int getVideoAngle(@NonNull MediaMetadataRetriever retriever) {
        String rotation = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            rotation = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
        }
        try {
            if (rotation != null) {
                return Integer.parseInt(rotation);
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }

    //获取视频时长
    private static String getVideoDurationS(@NonNull MediaMetadataRetriever retriever) {

        return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
    }

    //获取视频宽
    private static String getVideoWidthS(@NonNull MediaMetadataRetriever retriever) {
        int rotation = getVideoAngle(retriever);
        String width = null;
        switch (rotation) {
            case 0:
            case 180:
                width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
                break;
            case 90:
            case 270:
                width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
                break;
        }
        return width;

    }

    //获取视频高
    private static String getVideHeightS(@NonNull MediaMetadataRetriever retriever) {
        int rotation = getVideoAngle(retriever);
        switch (rotation) {
            case 0:
            case 180:
                return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
            case 90:
            case 270:
                return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        }
        return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
    }

    /**
     * 根据{@link MediaMetadataRetriever}获取视频高宽比
     *
     * @param retriever MediaMetadataRetriever
     * @return 视频高宽比
     */
    static float getPercent(@NonNull MediaMetadataRetriever retriever) {
        if (isRetrieverNormal(retriever)) {
            return ThumbVideoUtil.getVideoHeight(retriever) / (float) ThumbVideoUtil.getVideoWidth(retriever);
        }
        return 0;
    }

    /**
     * 判断{@link MediaMetadataRetriever}是否正常
     *
     * @param retriever MediaMetadataRetriever
     * @return 是否正常
     */
    private static boolean isRetrieverNormal(@NonNull MediaMetadataRetriever retriever) {
        return ThumbVideoUtil.getVideoWidth(retriever) != 0 && ThumbVideoUtil.getVideoHeight(retriever) != 0;
    }


}
