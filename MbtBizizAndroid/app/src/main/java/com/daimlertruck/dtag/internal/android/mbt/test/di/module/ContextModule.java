package com.daimlertruck.dtag.internal.android.mbt.test.di.module;

import android.content.Context;

import com.daimlertruck.dtag.internal.android.mbt.test.di.qualifiers.ApplicationContextQualifier;
import com.daimlertruck.dtag.internal.android.mbt.test.di.scopes.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {

    private final Context context;

    public ContextModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    @ApplicationScope
    @ApplicationContextQualifier
    public Context context() {
        return context;
    }
}
