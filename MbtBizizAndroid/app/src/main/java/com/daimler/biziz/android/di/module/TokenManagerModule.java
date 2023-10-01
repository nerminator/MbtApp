package com.daimler.biziz.android.di.module;

import com.daimler.biziz.android.di.scopes.ApplicationScope;
import com.daimler.biziz.android.manager.SharedPreferenceManager;
import com.daimler.biziz.android.manager.TokenManager;

import dagger.Module;
import dagger.Provides;


@ApplicationScope
@Module(includes = SharedPreferenceManagerModule.class)
public class TokenManagerModule {

    @ApplicationScope
    @Provides
    public TokenManager tokenManager(SharedPreferenceManager sharedPreferenceManager){
     return new TokenManager(sharedPreferenceManager);
    }
}
