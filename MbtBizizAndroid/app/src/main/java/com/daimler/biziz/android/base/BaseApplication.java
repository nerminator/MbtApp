package com.daimler.biziz.android.base;

import android.app.Application;
import android.content.Context;

import com.daimler.biziz.android.di.components.AppComponent;
import com.daimler.biziz.android.di.components.DaggerAppComponent;
import com.daimler.biziz.android.di.module.ContextModule;


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
