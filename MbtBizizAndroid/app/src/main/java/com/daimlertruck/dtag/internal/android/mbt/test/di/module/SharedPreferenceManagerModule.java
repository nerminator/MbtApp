package com.daimlertruck.dtag.internal.android.mbt.test.di.module;

import android.content.Context;

import com.daimlertruck.dtag.internal.android.mbt.test.di.qualifiers.ApplicationContextQualifier;
import com.daimlertruck.dtag.internal.android.mbt.test.di.scopes.ApplicationScope;
import com.daimlertruck.dtag.internal.android.mbt.test.manager.SharedPreferenceManager;

import dagger.Module;
import dagger.Provides;


@ApplicationScope
@Module(includes = ContextModule.class)
public class SharedPreferenceManagerModule {

    @ApplicationScope
    @Provides
    public SharedPreferenceManager sharedPreferenceManager(@ApplicationContextQualifier Context context){
     return new SharedPreferenceManager(context);
    }
}
