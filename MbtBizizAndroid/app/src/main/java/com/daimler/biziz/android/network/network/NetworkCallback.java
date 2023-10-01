package com.daimler.biziz.android.network.network;

/**
 * Created by Enes on 02.03.2017.
 */

public abstract class NetworkCallback<R> {
    public abstract void onSuccess(R response);

    public abstract void onServiceFailure(int httpResponseCode, String message);

    public abstract void onNetworkFailure(Throwable message);

    public void onFinish(){

    }
}
