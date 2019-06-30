package com.aeiric.thumb.lib;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import static com.aeiric.thumb.lib.ThumbUtil.isInMainThread;

/**
 * @author xujian
 * @desc build ThumbGenerator
 * @from v1.0.0
 */
class ThumbGeneratorBuilder {

    private static final int S_MAX_CACHE_SIZE = 10 * 1024 * 1024;
    @NonNull
    ThumbExecutor thumbExecutor;
    @NonNull
    ThumbCache thumbCache;
    @Nullable
    Context context;
    @NonNull
    MediaMetadataRetriever retriever;
    @Nullable
    String thumbDirSuffix;
    @Nullable
    String trackDirSuffix;
    int secPerThumb;
    float percent;

    @UiThread
    ThumbGeneratorBuilder(@NonNull MediaMetadataRetriever retriever) {
        this.retriever = retriever;
        thumbExecutor = new ThumbExecutor();
        thumbCache = new ThumbCache(S_MAX_CACHE_SIZE);
        if (!isInMainThread()) {
            throw new NoMainThreadException("Not in Main Thread error!");
        }
    }

    ThumbGeneratorBuilder context(@Nullable Context context) {
        this.context = context;
        return this;
    }

    ThumbGeneratorBuilder thumbDirSuffix(@Nullable String thumbDirSuffix) {
        this.thumbDirSuffix = thumbDirSuffix;
        return this;
    }

    ThumbGeneratorBuilder trackDirSuffix(@Nullable String trackDirSuffix) {
        this.trackDirSuffix = trackDirSuffix;
        return this;
    }

    ThumbGeneratorBuilder secPerThumb(int secPerThumb) {
        this.secPerThumb = secPerThumb;
        return this;
    }

    ThumbGeneratorBuilder percent(float percent) {
        this.percent = percent;
        return this;
    }

    ThumbGenerator build() {
        return new ThumbGenerator(this);
    }
}
