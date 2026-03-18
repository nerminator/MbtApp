package com.daimlertruck.dtag.internal.android.mbt.test.di.module;

import com.daimlertruck.dtag.internal.android.mbt.test.ui.splash.SplashActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SplashActivityModule {

    @ContributesAndroidInjector
    abstract SplashActivity contributesSplashActivity();


}
