package com.aeiric.thumb.lib;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.aeiric.thumb.R;

import java.io.File;

import static com.aeiric.thumb.lib.ThumbConstant.PREFIX_THUMB;
import static com.aeiric.thumb.lib.ThumbFileUtil.getCacheThumbFile;
import static com.aeiric.thumb.lib.ThumbSpUtil.putThumbSuffix;
import static com.aeiric.thumb.lib.ThumbSpUtil.putTrackSuffix;
import static com.aeiric.thumb.lib.ThumbVideoUtil.getPercent;


/**
 * @author xujian
 * @desc ThumbFragment
 * @from v1.0.0
 */
@SuppressWarnings({"FieldCanBeLocal", "ConstantConditions", "unused"})
public class ThumbFragment extends Fragment {
    private static final String TAG = "ThumbFragment";
    public static final String EXTRA_PATH = "EXTRA_PATH";
    private static final float S_VIEW_HEIGHT_PERCENT = 0.66944444f;
    private static final int S_SMALL_THUMB_SHOW_LENGTH = 250;
    @Nullable
    private ImageView mThumbView;
    @Nullable
    private RecyclerView mRecycler;
    @Nullable
    private ImageView mSmallThumb;
    @Nullable
    private FrameLayout mFrame;
    @NonNull
    private MediaMetadataRetriever mRetriever = new MediaMetadataRetriever();
    @Nullable
    private AsyncTask<Integer, Integer, String> mThumbLoadTask;
    @Nullable
    private ThumbGenerator mThumbGenerator;
    @Nullable
    private ThumbAdapter mThumbAdapter;
    @Nullable
    private String mPath;
    @Nullable
    private String mThumbDirSuffix;
    @Nullable
    private String mTrackDirSuffix;
    @Nullable
    private Bitmap mThumbBitmap;
    @Nullable
    private Bitmap mSmallThumbBitmap;

    private boolean mSystemError;
    private int mCurrentRecyclerX;
    private int mCurrentItemPos = 1;
    private long mVideoDuration;
    private int mThumbWidth;
    private int mThumbHeight;
    private int mSecPerThumb;
    private float mPercent;


    public static ThumbFragment create(String videoPath) {
        ThumbFragment fragment = new ThumbFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_PATH, videoPath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intVideoPath(getArguments());
        initRetriever();
        initDir();
        initPercent();
        initVideoDuration();
        initSecPerThumb();
        initGenerator();

    }

    private void intVideoPath(@Nullable Bundle bundle) {
        if (bundle != null) {
            String path = bundle.getString(EXTRA_PATH);
            if (path != null) {
                mPath = path;
            }
        }
    }

    private void initRetriever() {
        mRetriever = new MediaMetadataRetriever();
        if (!TextUtils.isEmpty(mPath)) {
            try {
                mRetriever.setDataSource(mPath);
            } catch (Exception e) {
                setSystemError();
                ThumbSpUtil.putError(getActivity(), mPath);
            }
        } else {
            setSystemError();
        }
    }

    private void initDir() {
        initThumbDirSuffix();
        initTrackDirSuffix();
        createCacheDir();
    }

    private void initThumbDirSuffix() {
        if (getContext() != null && mPath != null) {
            mThumbDirSuffix = ThumbDirUtil.createThumbDirSuffix(getContext(), mPath);
            if (mThumbDirSuffix != null) {
                putThumbSuffix(getContext(), mPath, mThumbDirSuffix);
            }
        }
    }

    private void initTrackDirSuffix() {
        if (getContext() != null && mPath != null) {
            mTrackDirSuffix = ThumbDirUtil.createTrackDirSuffix(getContext(), mPath);
            if (mTrackDirSuffix != null) {
                putTrackSuffix(getContext(), mPath, mTrackDirSuffix);
            }
        }
    }

    private void createCacheDir() {
        if (getContext() != null && mThumbDirSuffix != null && mTrackDirSuffix != null) {
            ThumbFileUtil.makeRootDir(getContext());
            ThumbFileUtil.makeThumbDir(getContext(), mThumbDirSuffix);
            ThumbFileUtil.makeTrackDir(getContext(), mTrackDirSuffix);
        }
    }

    private void initSecPerThumb() {
        mSecPerThumb = ThumbCalculate.getThumbPerSec(mVideoDuration);
    }

    private void initPercent() {
        mPercent = getPercent(mRetriever);
    }

    private void initGenerator() {
        mThumbGenerator = new ThumbGeneratorBuilder(mRetriever)
                .context(getContext())
                .thumbDirSuffix(mThumbDirSuffix)
                .trackDirSuffix(mTrackDirSuffix)
                .secPerThumb(mSecPerThumb)
                .percent(mPercent)
                .build();

    }

    private void initVideoDuration() {
        mVideoDuration = ThumbVideoUtil.getVideoDuration(mRetriever);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thumb, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRecycler = view.findViewById(R.id.recycler);
        mFrame = view.findViewById(R.id.fv_thumb);
        mThumbView = view.findViewById(R.id.thumb);
        mSmallThumb = view.findViewById(R.id.small_cover);
        mRecycler.setLayoutManager(new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mSystemError) {
            showSysError();
            return;
        }
        setThumbView();
    }

    public void showSysError() {
        new AlertDialog.Builder(getContext())
                .setTitle("该视频暂不支持视频截取封面")
                .setMessage("可从右下角'相册选择'选择封面")
                .setCancelable(false)
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    //设置封面图宽高
    private void setThumbView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mFrame.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    sizeThumb();
                    thumbUI();
                    mFrame.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        } else {
            mFrame.post(new Runnable() {
                @Override
                public void run() {
                    ThumbFragment.this.sizeThumb();
                    ThumbFragment.this.thumbUI();
                }
            });
        }
    }

    private void sizeThumb() {
        if (mRetriever == null) {
            return;
        }
        int video_width = ThumbVideoUtil.getVideoWidth(mRetriever);
        int video_height = ThumbVideoUtil.getVideoHeight(mRetriever);
        if (video_width == 0 || video_height == 0) {
            setSystemError();
            ThumbSpUtil.putError(getActivity(), mPath);
            showSysError();
            return;
        }
        int screen_width = ThumbDensityUtil.getDevicesWidthPixels(getContext());
        int layout_height = mFrame.getHeight();
        if (layout_height == 0) {
            layout_height = (int) (ThumbDensityUtil.getDevicesHeightPixels(getContext()) * S_VIEW_HEIGHT_PERCENT);
        }
        int view_width;
        int view_height;
        //视频高大于屏幕布局高，视频宽大于屏幕布局宽
        //1、宽高比大于布局宽高比，宽为屏幕宽，高按比例
        if (video_width / video_height > screen_width / layout_height) {
            view_width = screen_width;
            view_height = video_height * screen_width / video_width;
        }
        //2、宽高比小于布局宽高比，高为屏幕高，宽按比例
        else {
            view_height = layout_height;
            view_width = video_width * layout_height / video_height;
        }
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mThumbView.getLayoutParams();
        params.width = view_width;
        params.height = view_height;
        mThumbView.setLayoutParams(params);
        mThumbWidth = video_width;
        mThumbHeight = view_height;
    }

    private void thumbUI() {
        if (getContext() == null) {
            return;
        }
        if (mPath == null) {
            return;
        }
        mThumbAdapter = new ThumbAdapter.Builder()
                .recyclerHeight(thumbItemHeight())
                .generator(mThumbGenerator)
                .isNeedCut(mVideoDuration)
                .percent(mVideoDuration)
                .list(mVideoDuration)
                .context(getContext())
                .build();
        mRecycler.setAdapter(mThumbAdapter);
        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        if (getContext() == null) {
                            break;
                        }
                        if (ThumbSpUtil.getError(getContext(), mPath)) {
                            break;
                        }
                        getBigThumb();
                        ThumbSpUtil.putSeekX(getContext(), mPath, mCurrentRecyclerX);
                        break;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                updateRecyclerX(dx);
                updatePos();
                getSmallThumb();
                getQuickBigThumb();
            }
        });
        mRecycler.post(new Runnable() {
            @Override
            public void run() {
                if (ThumbSpUtil.getSeekX(ThumbFragment.this.getContext(), mPath) != 0) {
                    mRecycler.scrollBy(ThumbSpUtil.getSeekX(ThumbFragment.this.getContext(), mPath), 0);
                } else {
                    getBigThumb();
                }
            }
        });
    }


    private void getSmallThumb() {
        if (getContext() == null) {
            return;
        }
        if (TextUtils.isEmpty(mPath)) {
            return;
        }
        if (mTrackDirSuffix == null) {
            return;
        }
        File file = ThumbFileUtil.getCacheTrackFile(getContext(), mTrackDirSuffix, (mCurrentItemPos - 1) * mSecPerThumb);
        if (file != null) {
            String key = file.getAbsolutePath() + (mCurrentItemPos - 1);
            if (isSameThumb(mSmallThumb, key)) {
                return;
            }
            Bitmap bitmap = ThumbBitmapUtil.decodeBitmap(file.getAbsolutePath(), S_SMALL_THUMB_SHOW_LENGTH, S_SMALL_THUMB_SHOW_LENGTH);
            if (bitmap != null) {
                mSmallThumb.setImageBitmap(bitmap);
                mSmallThumb.setTag(key);
                smallThumbBitmapRecycler();
                mSmallThumbBitmap = bitmap;
            }
        }
    }

    private void smallThumbBitmapRecycler() {
        if (mSmallThumbBitmap != null && !mSmallThumbBitmap.isRecycled()) {
            mSmallThumbBitmap.recycle();
        }
    }

    private void getQuickBigThumb() {
        if (getContext() == null) {
            return;
        }
        if (TextUtils.isEmpty(mPath)) {
            return;
        }
        if (mTrackDirSuffix == null) {
            return;
        }

        File bigFile = getCacheThumbFile(getContext(), mThumbDirSuffix, (mCurrentItemPos - 1) * mSecPerThumb);
        if (bigFile == null) {
            File smallFile = ThumbFileUtil.getCacheTrackFile(getContext(), mTrackDirSuffix, (mCurrentItemPos - 1) * mSecPerThumb);
            if (smallFile != null) {
                String key = "QUICK_BIG" + smallFile.getAbsolutePath() + (mCurrentItemPos - 1);
                if (isSameThumb(mThumbView, key)) {
                    return;
                }
                Bitmap bitmap = ThumbBitmapUtil.decodeBitmap(smallFile.getAbsolutePath(), mThumbWidth / 2, mThumbHeight / 2);
                if (bitmap != null) {
                    mThumbView.setImageBitmap(bitmap);
                    mThumbView.setTag(key);
                    thumbBitmapRecycler();
                    mThumbBitmap = bitmap;
                }

            }
        } else {
            String key = PREFIX_THUMB + bigFile.getAbsolutePath() + (mCurrentItemPos - 1);
            if (isSameThumb(mThumbView, key)) {
                return;
            }
            Bitmap bitmap = ThumbBitmapUtil.decodeBitmap(bigFile.getAbsolutePath(), mThumbWidth / 2, mThumbHeight / 2);
            if (bitmap != null) {
                mThumbView.setImageBitmap(bitmap);
                mThumbView.setTag(key);
                thumbBitmapRecycler();
                mThumbBitmap = bitmap;
            }
        }
    }

    private void thumbBitmapRecycler() {
        if (mThumbBitmap != null) {
            mThumbBitmap.recycle();
            mThumbBitmap = null;
        }
    }

    private void getBigThumb() {
        if (mThumbGenerator != null) {
            mThumbGenerator.genThumb(mThumbView, mCurrentItemPos - 1);
        }
    }

    private boolean isSameThumb(@NonNull ImageView iv, @NonNull String key) {
        if (iv.getTag() != null) {
            String tag = (String) iv.getTag();
            return key.equals(tag);
        }
        return false;
    }

    private void updateRecyclerX(int dx) {
        mCurrentRecyclerX += dx;
    }

    private void updatePos() {
        if (mCurrentRecyclerX < 0) {
            return;
        }
        if (thumbItemWidth() == 0) {
            mCurrentItemPos = 1;
        } else {
            mCurrentItemPos = (mCurrentRecyclerX / thumbItemWidth() <= 0 ? 1 : mCurrentRecyclerX / thumbItemWidth() + 1);
        }
    }

    public void createThumbFile(@NonNull OnThumbLoadedListener listener) {
        if (mThumbGenerator == null) {
            return;
        }
        if (mThumbDirSuffix == null) {
            return;
        }
        String selectedPath = null;
        File file = getCacheThumbFile(getContext(), mThumbDirSuffix, (mCurrentItemPos - 1) * mSecPerThumb);
        if (file != null) {
            selectedPath = file.getAbsolutePath();
        }
        if (null == selectedPath) {
            if (getContext() != null) {
                mThumbLoadTask = new ThumbLoadTask(getContext())
                        .setThumbDirSuffix(mThumbDirSuffix)
                        .setMMR(mRetriever)
                        .setCurrentItemPos(mCurrentItemPos)
                        .setPerS(mSecPerThumb)
                        .setPath(mPath)
                        .setMMR(mRetriever)
                        .setOnThumbLoadFinishListener(listener)
                        .execute();
            }
        } else {
            listener.onThumbLoaded(selectedPath);
        }
    }

    private int thumbItemWidth() {
        return mRecycler.getHeight();
    }

    private int thumbItemHeight() {
        return thumbItemWidth();
    }

    private void setSystemError() {
        this.mSystemError = true;
    }

    public boolean isSysError() {
        return mSystemError;
    }

    private void generatorDestroy() {
        mThumbGenerator.stop();
    }

    private void thumbLoadTaskCancel() {
        if (mThumbLoadTask != null && mThumbLoadTask.getStatus() != AsyncTask.Status.FINISHED) {
            mThumbLoadTask.cancel(true);
        }
    }

    private void bitmapRecycler() {
        thumbBitmapRecycler();
        smallThumbBitmapRecycler();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        generatorDestroy();
        thumbLoadTaskCancel();
        bitmapRecycler();
    }

}
