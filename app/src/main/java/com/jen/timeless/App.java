package com.jen.timeless;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;

/**
 * Created by Administrator on 2016/3/16.
 */
public class App extends Application {
    public static App app;
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Bmob.initialize(this, "7428acdbcbbf507bf463331590b849d8");
    }

    public static Context getApplication() {
        return app;
    }
}
