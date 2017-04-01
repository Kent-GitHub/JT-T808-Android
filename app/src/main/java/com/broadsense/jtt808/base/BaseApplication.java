package com.broadsense.jtt808.base;

import android.app.Application;

/**
 * * Created by Kent_Lee on 2017/3/31.
 */

public class BaseApplication extends Application {
    public static BaseApplication mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}
