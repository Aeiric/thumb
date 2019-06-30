/*
 * Copyright (c) 2015-2019 BiliBili Inc.
 */

package com.aeiric.thumb.lib;

import android.os.Process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author xujian
 * @desc ThumbExecutor
 * @from v1.0.0
 */
class ThumbExecutor {
    private static final int POOL_THREAD_CORE_NUMBER = Runtime.getRuntime().availableProcessors() * 10;
    private static final int POOL_THREAD_MAX_NUMBER = 2 * POOL_THREAD_CORE_NUMBER;
    private static final int QUEUE_CAPACITY = 10;
    private BlockingQueue<Runnable> mQueue;
    private ExecutorService mWorkExecutor;
    private Map<String, Boolean> mTaskStatusMap;
    private List<Future> mTaskList;

    ThumbExecutor() {
        mQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        mTaskList = new ArrayList<>();
        mTaskStatusMap = new HashMap<>();
        mWorkExecutor = new ThreadPoolExecutor(POOL_THREAD_CORE_NUMBER, POOL_THREAD_MAX_NUMBER, 1, TimeUnit.SECONDS, mQueue, new BackgroundThreadFactory());
    }

    @SuppressWarnings("unchecked")
    void addCallable(Callable callable, String key) {
        if (mTaskStatusMap.get(key) == null) {
            Future future = mWorkExecutor.submit(callable);
            mTaskList.add(future);
            mTaskStatusMap.put(key, true);
        }
    }

    void stop() {
        mQueue.clear();
        for (Future task : mTaskList) {
            if (!task.isDone()) {
                task.cancel(true);
            }
        }
        mTaskList.clear();
        mTaskStatusMap.clear();
        mWorkExecutor.shutdownNow();
    }

    private static class BackgroundThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);
            return thread;
        }
    }
}
