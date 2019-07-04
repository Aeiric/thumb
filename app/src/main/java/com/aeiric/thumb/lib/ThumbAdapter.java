package com.aeiric.thumb.lib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.aeiric.thumb.R;

import java.util.ArrayList;
import java.util.List;


/**
 * @author xujian
 * @desc ThumbAdapter
 * @from v1.0.0
 */
public class ThumbAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_EMPTY = -2;
    private static final int TYPE_THUMB_NARROW = -1;
    private static final int TYPE_THUMB_NORMAL = 0;

    @NonNull
    private List<String> mList;
    @Nullable
    private ThumbGenerator mGenerator;
    @Nullable
    private Context mContext;
    private int mRecyclerHeight;
    private boolean mIsNeedCut;
    private float mPercent;

    private ThumbAdapter(Builder builder) {
        mList = builder.list;
        mContext = builder.context;
        mGenerator = builder.generator;
        mRecyclerHeight = builder.recyclerHeight;
        mIsNeedCut = builder.isNeedCut;
        mPercent = builder.percent;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_EMPTY) {
            return new EmptyHolder(LayoutInflater.from(mContext).inflate(R.layout.list_layout_empty, null));
        } else {
            return new ThumbHolder(LayoutInflater.from(mContext).inflate(R.layout.list_layout_thumb, null));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder hd, int position) {
        if (hd instanceof ThumbHolder) {
            ThumbHolder holder = (ThumbHolder) hd;
            if (mGenerator == null) {
                return;
            }
            mGenerator.genTrackThumb(holder.mImageView, position - 1);
            if (getItemViewType(position) == TYPE_THUMB_NARROW) {
                int width = (int) (mPercent * mRecyclerHeight);
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.mImageView.getLayoutParams();
                params.width = width;
                params.height = mRecyclerHeight;
                holder.mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                holder.mImageView.setLayoutParams(params);
            }
        } else {
            EmptyHolder holder = (EmptyHolder) hd;
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.mEmpty.getLayoutParams();
            params.width = ThumbDensityUtil.getDevicesWidthPixels(holder.mEmpty.getContext()) / 2;
            params.height = mRecyclerHeight;
            holder.mEmpty.setLayoutParams(params);
        }

    }

    void pause() {
        if (mGenerator != null) {
            mGenerator.pause();
        }
    }

    void resume() {
        if (mGenerator != null) {
            mGenerator.resume();
        }
    }

    boolean checkNoBind(int pos) {
        if (mGenerator == null) {
            return false;
        }
        if (pos <= 0 || pos >= mList.size() - 1) {
            return false;
        }
        return !mGenerator.checkExecuted(pos - 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == mList.size() - 1) {
            return TYPE_EMPTY;
        } else if (position == mList.size() - 2) {
            return mIsNeedCut ? TYPE_THUMB_NARROW : TYPE_THUMB_NORMAL;
        } else {
            return TYPE_THUMB_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public static class ThumbHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;

        ThumbHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv_thumb);
        }
    }

    public static class EmptyHolder extends RecyclerView.ViewHolder {
        ImageView mEmpty;

        EmptyHolder(View itemView) {
            super(itemView);
            mEmpty = itemView.findViewById(R.id.iv_empty);
        }
    }


    public static final class Builder {
        @NonNull
        private List<String> list;
        @Nullable
        private Context context;
        @Nullable
        private ThumbGenerator generator;
        private int recyclerHeight;
        private boolean isNeedCut;
        private float percent;

        Builder() {
            this.list = new ArrayList<>();
        }

        Builder list(long duration) {
            buildList(duration);
            return this;
        }

        Builder context(@Nullable Context context) {
            this.context = context;
            return this;
        }

        Builder generator(@Nullable ThumbGenerator generator) {
            this.generator = generator;
            return this;
        }

        Builder recyclerHeight(int recyclerHeight) {
            this.recyclerHeight = recyclerHeight;
            return this;
        }

        Builder isNeedCut(long duration) {
            this.isNeedCut = ThumbCalculate.isNeedCut(duration);
            return this;
        }

        public Builder percent(long duration) {
            this.percent = ThumbCalculate.getPercentWidth(duration);
            return this;
        }

        private void buildList(long duration) {
            int count = ThumbCalculate.getThumbCount(duration);
            for (int i = 0; i < count + 2; i++) {
                list.add("");
            }
        }

        ThumbAdapter build() {
            return new ThumbAdapter(this);
        }
    }

}
