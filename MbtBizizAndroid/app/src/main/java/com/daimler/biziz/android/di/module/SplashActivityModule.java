package com.daimler.biziz.android.di.module;

import com.daimler.biziz.android.ui.splash.SplashActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SplashActivityModule {

    @ContributesAndroidInjector
    abstract SplashActivity contributesSplashActivity();


}
