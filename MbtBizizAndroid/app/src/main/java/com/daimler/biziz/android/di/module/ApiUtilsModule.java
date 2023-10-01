package com.daimler.biziz.android.di.module;

import com.daimler.biziz.android.di.scopes.ApplicationScope;
import com.daimler.biziz.android.manager.TokenManager;
import com.daimler.biziz.android.network.network.AbstractApiUtils;
import com.daimler.biziz.android.network.network.ApiUtils;
import com.daimler.biziz.android.network.network.APIService;
import com.daimler.biziz.android.network.network.MockApiUtils;

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
