package com.daimlertruck.dtag.internal.android.mbt.test.network.network;


import com.daimlertruck.dtag.internal.android.mbt.test.manager.TokenManager;

public abstract class AbstractApiUtils implements ApiUtilsInterface {

    protected APIService APIService;
    protected TokenManager tokenManager;


    public AbstractApiUtils(APIService APIService, TokenManager tokenManager) {
        this.APIService = APIService;
        this.tokenManager = tokenManager;
    }
}
