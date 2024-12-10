package com.daimlertruck.dtag.internal.android.mbt.test.network.network;

import android.text.TextUtils;

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

    public LoginInterceptor(TokenManager tokenManager, Gson gson) {
        this.tokenManager = tokenManager;
        this.gson = gson;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest = request;
        boolean isLoginRequest = false;

        String encodedPath = request.url().encodedPath();

        String localeProperty = "en";
        if (Locale.getDefault().getLanguage().equals("tr")) {
            localeProperty = "tr";
        }

        if ("/bizizBackend/public/index.php/api/v1/login".contains(encodedPath)) { // Burasi access tokenin donecegi yer demek oluyor
            isLoginRequest = true;
        }
        String accessToken = tokenManager.getAccesToken();
        if (!TextUtils.isEmpty(accessToken) && !isLoginRequest) {
            newRequest = request.newBuilder()
                    .header("Authorization", "Bearer " + accessToken)
                    .header("lang",localeProperty).build();
        }
        Response response = chain.proceed(newRequest);
        return response;
    }
}
