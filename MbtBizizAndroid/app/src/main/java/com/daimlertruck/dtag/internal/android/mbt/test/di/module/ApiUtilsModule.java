package com.daimlertruck.dtag.internal.android.mbt.test.di.module;

import com.daimlertruck.dtag.internal.android.mbt.test.di.scopes.ApplicationScope;
import com.daimlertruck.dtag.internal.android.mbt.test.manager.TokenManager;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.AbstractApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.ApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.APIService;

import dagger.Module;
import dagger.Provides;

/**
 * Created by atakankersit on 14.12.2017.
 */

@ApplicationScope
@Module(includes = {TokenManagerModule.class, NetworkModule.class})
public class ApiUtilsModule {

    @ApplicationScope
    @Provides
    public AbstractApiUtils apiUtils(APIService APIService, TokenManager tokenManager) {
         //return new MockApiUtils(APIService, tokenManager);
         return new ApiUtils(APIService, tokenManager);
    }
}
