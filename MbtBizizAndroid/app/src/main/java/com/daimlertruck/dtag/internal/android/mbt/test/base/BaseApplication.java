package com.daimlertruck.dtag.internal.android.mbt.test.base;

import android.app.Application;
import android.content.Context;

import com.daimlertruck.dtag.internal.android.mbt.test.di.components.AppComponent;
import com.daimlertruck.dtag.internal.android.mbt.test.di.components.DaggerAppComponent;
import com.daimlertruck.dtag.internal.android.mbt.test.di.module.ContextModule;


public class BaseApplication extends Application {
    private AppComponent component;
    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder()
                .contextModule(new ContextModule(this))
                .build();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(this);
    }

    public AppComponent getComponent() {
        return component;
    }
}
