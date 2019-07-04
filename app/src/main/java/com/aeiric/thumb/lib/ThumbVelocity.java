package com.aeiric.thumb.lib;


class ThumbVelocity {

    static final float S_MAX_VELOCITY = 10.0f;

    private long mLastTime;

    private float mVelocity;


    float getVelocity() {
        return mVelocity;
    }

    void start(int dx) {
        long current = System.currentTimeMillis();
        if (mLastTime == 0) {
            mVelocity = 0;
        } else {
            mVelocity = dx / (float) (current - mLastTime);
        }
        mLastTime = current;
    }

    void stop() {
        mVelocity = 0;
        mLastTime = 0;
    }

}
