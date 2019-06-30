package com.aeiric.thumb.lib;

import android.os.Process;

import java.util.concurrent.ThreadFactory;


/**
 * @author xujian
 * @desc NoMainThreadException
 * @from v1.0.0
 */
class ThumbThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);
        return thread;
    }
}
