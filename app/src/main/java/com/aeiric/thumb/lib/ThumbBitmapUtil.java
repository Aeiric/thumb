package com.aeiric.thumb.lib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * @author xujian
 * @desc ThumbBitmapUtil
 * @from v1.0.0
 */
@SuppressWarnings("JavaDoc")
class ThumbBitmapUtil {

    private static final int S_TRACK_THUMB_SHOW_LENGTH = 125;

    static void saveBitmapToFile(Bitmap bitmap, String filePath) {
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从path中获取图片信息,在通过BitmapFactory.decodeFile(String path)方法将突破转成Bitmap时，
     * 遇到大一些的图片，我们经常会遇到OOM(Out Of Memory)的问题，该方法核心是使用BitmapFactory避免OOM
     *
     * @param path   文件路径
     * @param width  目标图片的宽度
     * @param height 目标图片的高度
     * @return
     */
    @Nullable
    static Bitmap decodeBitmap(String path, int width, int height) {
        FileInputStream fis = null;
        BitmapFactory.Options op = new BitmapFactory.Options();
        try {
            fis = new FileInputStream(path);
            // inJustDecodeBounds如果设置为true,仅仅返回图片实际的宽和高,宽和高是赋值给opts.outWidth,opts.outHeight;
            op.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fis.getFD(), null, op);
            //获取尺寸信息

            //获取比例大小
            int wRatio = (int) Math.ceil(op.outWidth / width);
            int hRatio = (int) Math.ceil(op.outHeight / height);
            //如果超出指定大小，则缩小相应的比例
            if (wRatio > 1 && hRatio > 1) {
                if (wRatio > hRatio) {
                    op.inSampleSize = wRatio;
                } else {
                    op.inSampleSize = hRatio;
                }
            }
            op.inJustDecodeBounds = false;
            return BitmapFactory.decodeFileDescriptor(fis.getFD(), null, op);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

//    /**
//     * 从path中获取图片信息,在通过BitmapFactory.decodeFile(String path)方法将突破转成Bitmap时，
//     * 遇到大一些的图片，我们经常会遇到OOM(Out Of Memory)的问题，该方法核心是使用BitmapFactory避免OOM
//     *
//     * @param path   文件路径
//     * @param width  目标图片的宽度
//     * @param height 目标图片的高度
//     * @return
//     */
//    @NonNull
//    static Bitmap decodeBitmap(Bitmap bitmap, int width, int height) {
//        BitmapFactory.Options op = new BitmapFactory.Options();
//        // inJustDecodeBounds如果设置为true,仅仅返回图片实际的宽和高,宽和高是赋值给opts.outWidth,opts.outHeight;
//        op.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(path, op); //获取尺寸信息
//        //获取比例大小
//        int wRatio = (int) Math.ceil(op.outWidth / width);
//        int hRatio = (int) Math.ceil(op.outHeight / height);
//        //如果超出指定大小，则缩小相应的比例
//        if (wRatio > 1 && hRatio > 1) {
//            if (wRatio > hRatio) {
//                op.inSampleSize = wRatio;
//            } else {
//                op.inSampleSize = hRatio;
//            }
//        }
//        op.inJustDecodeBounds = false;
//        return BitmapFactory.decodeFile(path, op);
//    }

    @Nullable
    static Bitmap getThumbShowBitmap(@NonNull File file, int thumbShowWidth, int thumbShowHeight) {
        return getBitmapFromFile(file, thumbShowWidth, thumbShowHeight);
    }

    @Nullable
    static Bitmap getTrackShowBitmap(@NonNull File file) {
        return getBitmapFromFile(file, S_TRACK_THUMB_SHOW_LENGTH, S_TRACK_THUMB_SHOW_LENGTH);
    }

    @Nullable
    private static Bitmap getBitmapFromFile(@NonNull File file, int showWidth, int showHeight) {
        return decodeBitmap(file.getAbsolutePath(), showWidth, showHeight);
    }

}
