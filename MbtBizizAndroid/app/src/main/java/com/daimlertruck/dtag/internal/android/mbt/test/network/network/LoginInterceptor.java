package com.daimlertruck.dtag.internal.android.mbt.test.network.network;

import android.text.TextUtils;
import android.util.Log;

import com.daimlertruck.dtag.internal.android.mbt.test.manager.MsalManager;
import com.daimlertruck.dtag.internal.android.mbt.test.manager.TokenManager;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LoginInterceptor implements Interceptor {
    private final TokenManager tokenManager;
    private final Gson gson;
    private final MsalManager msalManager;

    public LoginInterceptor(TokenManager tokenManager, Gson gson, MsalManager msalManager) {
        this.tokenManager = tokenManager;
        this.gson = gson;
        this.msalManager = msalManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request requestToProceed = originalRequest;

        boolean isLoginRequest = originalRequest.url().encodedPath().contains("/login");
        boolean isCrashLogRequest = originalRequest.url().encodedPath().contains("/sendCrashLog");

        // Determine language
        String lang = Locale.getDefault().getLanguage().equals("tr") ? "tr" : "en";

        String accessToken = tokenManager.getAccesToken();

        // Attempt token refresh if token is missing (skip for login and crash-log requests)
        if (!isLoginRequest && !isCrashLogRequest && TextUtils.isEmpty(accessToken)) {
            Log.d("LoginInterceptor", "Access token empty. Attempting silent refresh.");
            accessToken = msalManager.acquireTokenBlocking(); // Blocking call

            if (!TextUtils.isEmpty(accessToken)) {
                tokenManager.setAccessToken(accessToken); // Save new token
            }
        }

        // Add token to request if present
        if (!TextUtils.isEmpty(accessToken)) {
            requestToProceed = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + accessToken)
                    .header("lang", lang)
                    .build();
        }

        Response response = chain.proceed(requestToProceed);

        // Retry once if unauthorized (skip for crash-log requests)
        if (response.code() == 401 && !isLoginRequest && !isCrashLogRequest) {
            Log.w("LoginInterceptor", "Received 401. Retrying with fresh token.");
            response.close();

            accessToken = msalManager.acquireTokenBlocking();
            if (!TextUtils.isEmpty(accessToken)) {
                tokenManager.setAccessToken(accessToken);

                Request retryRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer " + accessToken)
                        .header("lang", lang)
                        .build();

                return chain.proceed(retryRequest);
            }
        }

        return response;
    }
}