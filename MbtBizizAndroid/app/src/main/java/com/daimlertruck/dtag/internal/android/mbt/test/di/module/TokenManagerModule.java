package com.daimlertruck.dtag.internal.android.mbt.test.di.module;

import com.daimlertruck.dtag.internal.android.mbt.test.di.scopes.ApplicationScope;
import com.daimlertruck.dtag.internal.android.mbt.test.manager.SharedPreferenceManager;
import com.daimlertruck.dtag.internal.android.mbt.test.manager.TokenManager;

import dagger.Module;
import dagger.Provides;


@ApplicationScope
@Module(includes = SharedPreferenceManagerModule.class)
public class TokenManagerModule {

    @ApplicationScope
    @Provides
    public TokenManager tokenManager(SharedPreferenceManager sharedPreferenceManager) {
        return new TokenManager(sharedPreferenceManager);
    }
}
