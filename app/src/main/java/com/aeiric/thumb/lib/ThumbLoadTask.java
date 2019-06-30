package com.aeiric.thumb.lib;


import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;
import java.lang.ref.WeakReference;

import static com.aeiric.thumb.lib.ThumbBitmapUtil.saveBitmapToFile;
import static com.aeiric.thumb.lib.ThumbFileUtil.makeThumbFile;


/**
 * @author xujian
 * @desc ThumbLoadTask
 * @from v1.0.0
 */
public class ThumbLoadTask extends AsyncTask<Integer, Integer, String> {

    @Nullable
    private OnThumbLoadedListener mListener;

    @Nullable
    private ThumbLoadingDialog mLoadingDialog;

    @Nullable
    private MediaMetadataRetriever mMMR;

    @Nullable
    private final WeakReference<Context> mWeakContext;

    @Nullable
    private String mThumbDirSuffix;

    @Nullable
    private String mPath;

    private int mCurrentItemPos;

    private int mSecPerThumb;

    ThumbLoadTask(@NonNull Context context) {
        this.mWeakContext = new WeakReference<>(context);
    }

    ThumbLoadTask setOnThumbLoadFinishListener(OnThumbLoadedListener listener) {
        this.mListener = listener;
        return this;
    }

    ThumbLoadTask setMMR(@Nullable MediaMetadataRetriever mMMR) {
        this.mMMR = mMMR;
        return this;
    }

    ThumbLoadTask setPath(@Nullable String path) {
        this.mPath = path;
        return this;
    }

    ThumbLoadTask setThumbDirSuffix(@Nullable String thumbDirSuffix) {
        this.mThumbDirSuffix = thumbDirSuffix;
        return this;
    }

    ThumbLoadTask setCurrentItemPos(int mCurrentItemPos) {
        this.mCurrentItemPos = mCurrentItemPos;
        return this;
    }

    ThumbLoadTask setPerS(int secPerThumb) {
        this.mSecPerThumb = secPerThumb;
        return this;
    }

    @Override
    protected void onPreExecute() {
        if (mWeakContext == null || mWeakContext.get() == null) {
            return;
        }
        if (mLoadingDialog == null) {
            mLoadingDialog = new ThumbLoadingDialog(mWeakContext.get());
        }
        mLoadingDialog.show();

    }

    @Override
    protected String doInBackground(Integer... integers) {
        if (mWeakContext == null || mWeakContext.get() == null) {
            return null;
        }
        if (mThumbDirSuffix == null) {
            return null;
        }
        if (mMMR == null) {
            return null;
        }
        if (TextUtils.isEmpty(mPath)) {
            return null;
        }
        Bitmap thumbBitmap = mMMR.getFrameAtTime((long) ((mCurrentItemPos - 1) * mSecPerThumb * 1000000));
        if (thumbBitmap == null) {
            return null;
        }
        File imgFile = makeThumbFile(mWeakContext.get(), mThumbDirSuffix, mSecPerThumb * mCurrentItemPos);
        if (imgFile == null) {
            return null;
        }
        saveBitmapToFile(thumbBitmap, imgFile.getAbsolutePath());
        thumbBitmap.recycle();
        if (!imgFile.exists()) {
            return null;
        }
        return imgFile.getAbsolutePath();
    }

    @Override
    protected void onPostExecute(String s) {
        if (mWeakContext == null || mWeakContext.get() == null) {
            return;
        }
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        if (s != null) {
            if (mListener != null) {
                mListener.onThumbLoaded(s);
            }
        } else {
            Toast.makeText(mWeakContext.get(), "获取图片失败，请重新获取~", Toast.LENGTH_LONG).show();
        }
    }

}
