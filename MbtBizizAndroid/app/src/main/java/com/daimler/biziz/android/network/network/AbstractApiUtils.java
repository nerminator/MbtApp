package com.daimler.biziz.android.network.network;


import com.daimler.biziz.android.manager.TokenManager;

public abstract class AbstractApiUtils implements ApiUtilsInterface {

    protected APIService APIService;
    protected TokenManager tokenManager;


    public AbstractApiUtils(APIService APIService, TokenManager tokenManager) {
        this.APIService = APIService;
        this.tokenManager = tokenManager;
    }
}
