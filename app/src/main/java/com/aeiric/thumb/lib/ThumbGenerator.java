package com.aeiric.thumb.lib;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aeiric.thumb.R;

import java.io.File;
import java.util.concurrent.Callable;

import static com.aeiric.thumb.lib.ThumbBitmapUtil.getThumbShowBitmap;
import static com.aeiric.thumb.lib.ThumbBitmapUtil.getTrackShowBitmap;
import static com.aeiric.thumb.lib.ThumbBitmapUtil.saveBitmapToFile;
import static com.aeiric.thumb.lib.ThumbCalculate.getTargetHeight;
import static com.aeiric.thumb.lib.ThumbCalculate.getTargetWidth;
import static com.aeiric.thumb.lib.ThumbFileUtil.getCacheThumbFile;
import static com.aeiric.thumb.lib.ThumbFileUtil.getCacheTrackFile;
import static com.aeiric.thumb.lib.ThumbFileUtil.makeThumbFile;
import static com.aeiric.thumb.lib.ThumbFileUtil.makeTrackFile;
import static com.aeiric.thumb.lib.ThumbUtil.getThumbTag;
import static com.aeiric.thumb.lib.ThumbUtil.getTrackTag;


/**
 * @author xujian
 * @desc ThumbGenerator
 * @from v1.0.0
 */
class ThumbGenerator {
    static final int S_THUMB_SAVE_MAX_LENGTH = 400;
    @NonNull
    private MediaMetadataRetriever mRetriever;
    @NonNull
    private ThumbExecutor mThumbExecutor;
    @NonNull
    private volatile ThumbCache mThumbCache;
    @Nullable
    private Context mContext;
    @Nullable
    private String mThumbDirSuffix;
    @Nullable
    private String mTrackDirSuffix;
    @Nullable
    private Bitmap mThumbBitmap;
    private int mSecPerThumb;
    private float mPercent;


    ThumbGenerator(@NonNull ThumbGeneratorBuilder builder) {
        mRetriever = builder.retriever;
        mThumbExecutor = builder.thumbExecutor;
        mThumbCache = builder.thumbCache;
        mContext = builder.context;
        mThumbDirSuffix = builder.thumbDirSuffix;
        mTrackDirSuffix = builder.trackDirSuffix;
        mSecPerThumb = builder.secPerThumb;
        mPercent = builder.percent;

    }

    @UiThread
    void genThumb(@NonNull final ImageView iv, final int pos) {
        if (mContext == null) {
            return;
        }
        if (mThumbDirSuffix == null) {
            return;
        }
        final String tag = getThumbTag(mContext, mThumbDirSuffix, pos * mSecPerThumb);
        if (iv.getTag() != null) {
            String lastTag = (String) iv.getTag();
            if (tag.equals(lastTag)) {
                return;
            }
        }
        File thumbFile = getCacheThumbFile(mContext, mThumbDirSuffix, mSecPerThumb * pos);
        if (thumbFile == null) {
            mThumbExecutor.execute(new Callable() {
                @Override
                public Object call() {
                    if (mContext == null) {
                        return null;
                    }
                    if (mThumbDirSuffix == null) {
                        return null;
                    }
                    Bitmap thumbBitmap = mRetriever.getFrameAtTime((long) (mSecPerThumb * pos * 1000000));
                    if (thumbBitmap == null) {
                        return null;
                    }
                    if (getCacheThumbFile(mContext, mThumbDirSuffix, mSecPerThumb * pos) == null) {
                        final File imgFile = makeThumbFile(mContext, mThumbDirSuffix, mSecPerThumb * pos);
                        if (imgFile == null || !imgFile.exists()) {
                            return null;
                        }
                        saveBitmapToFile(thumbBitmap, imgFile.getAbsolutePath());
                        thumbBitmap.recycle();
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showThumb(iv, imgFile, tag);
                            }
                        });
                    }
                    return null;
                }
            });
        } else {
            showThumb(iv, thumbFile, tag);
        }
    }

    @UiThread
    void genTrackThumb(@NonNull final ImageView iv, final int pos) {
        if (mContext == null) {
            return;
        }
        if (mTrackDirSuffix == null) {
            return;
        }
        iv.setImageResource(R.mipmap.ic_thumb_bg);
        final String tag = getTrackTag(mContext, mTrackDirSuffix, pos * mSecPerThumb);
        iv.setTag(tag);
        Bitmap bitmap = mThumbCache.getBitmap(tag);
        if (bitmap != null && !bitmap.isRecycled()) {
            iv.setImageBitmap(bitmap);
        } else {
            File file = getCacheTrackFile(mContext, mTrackDirSuffix, pos * mSecPerThumb);
            if (file != null) {
                showTrack(iv, file, tag);
            } else {
                mThumbExecutor.execute(new Callable() {
                    @Override
                    public Object call() {
                        if (mContext == null) {
                            return null;
                        }
                        if (mTrackDirSuffix == null) {
                            return null;
                        }
                        Bitmap thumbBitmap = mRetriever.getFrameAtTime((long) (mSecPerThumb * pos * 1000000));
                        if (thumbBitmap == null) {
                            return null;
                        }
                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(thumbBitmap, getTargetWidth(thumbBitmap, mPercent), getTargetHeight(thumbBitmap, mPercent), true);
                        thumbBitmap.recycle();
                        if (scaledBitmap == null) {
                            return null;
                        }
                        if (getCacheTrackFile(mContext, mTrackDirSuffix, mSecPerThumb * pos) == null) {
                            final File imgFile = makeTrackFile(mContext, mTrackDirSuffix, pos * mSecPerThumb);
                            if (imgFile == null || !imgFile.exists()) {
                                return null;
                            }
                            saveBitmapToFile(scaledBitmap, imgFile.getAbsolutePath());
                            scaledBitmap.recycle();
                            ((Activity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showTrack(iv, imgFile, tag);
                                }
                            });
                        }
                        return null;
                    }
                });
            }
        }
    }

    @UiThread
    void stop() {
        mThumbExecutor.stop();
        mThumbCache.clear();
        mRetriever.release();
        recycler();
        mContext = null;
    }

    private void showThumb(@NonNull ImageView thumbIv, @NonNull File file, @NonNull String tag) {
        ViewGroup.LayoutParams params = thumbIv.getLayoutParams();
        Bitmap bitmap = getThumbShowBitmap(file, params.width / 2, params.height / 2);
        if (bitmap != null && !bitmap.isRecycled()) {
            thumbIv.setImageBitmap(bitmap);
            thumbIv.setTag(tag);
            recycler();
            mThumbBitmap = bitmap;
        }
    }

    private void showTrack(@NonNull ImageView trackIv, @NonNull File file, @NonNull String key) {
        if (key.equals(trackIv.getTag())) {
            Bitmap bitmap = getTrackShowBitmap(file);
            if (bitmap != null && !bitmap.isRecycled()) {
                trackIv.setImageBitmap(bitmap);
                mThumbCache.cacheBitmap(key, bitmap);
            }
        }
    }

    private void recycler() {
        if (mThumbBitmap != null) {
            mThumbBitmap.recycle();
            mThumbBitmap = null;
        }
    }

}
