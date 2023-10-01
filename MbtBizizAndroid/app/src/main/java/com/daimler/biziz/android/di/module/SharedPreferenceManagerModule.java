package com.daimler.biziz.android.di.module;

import android.content.Context;

import com.daimler.biziz.android.di.qualifiers.ApplicationContextQualifier;
import com.daimler.biziz.android.di.scopes.ApplicationScope;
import com.daimler.biziz.android.manager.SharedPreferenceManager;

import java.io.IOException;
import java.security.GeneralSecurityException;

import dagger.Module;
import dagger.Provides;


@ApplicationScope
@Module(includes = ContextModule.class)
public class SharedPreferenceManagerModule {

    @ApplicationScope
    @Provides
    public SharedPreferenceManager sharedPreferenceManager(@ApplicationContextQualifier Context context) throws GeneralSecurityException, IOException {
     return new SharedPreferenceManager(context);
    }
}
