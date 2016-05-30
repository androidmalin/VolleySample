package com.malin.volley.application;

import android.app.Application;

import com.orhanobut.logger.Logger;

/**
 * Created by malin on 16-5-30.
 */
public class VolleyApplication extends Application {
    private static final String TAG = VolleyApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init(TAG);
    }
}
