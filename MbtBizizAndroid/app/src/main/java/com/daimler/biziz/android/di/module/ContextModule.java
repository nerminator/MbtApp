package com.daimler.biziz.android.di.module;

import android.content.Context;

import com.daimler.biziz.android.di.qualifiers.ApplicationContextQualifier;
import com.daimler.biziz.android.di.scopes.ApplicationScope;

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
