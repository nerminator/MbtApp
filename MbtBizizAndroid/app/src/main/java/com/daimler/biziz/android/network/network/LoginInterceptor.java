package com.daimler.biziz.android.network.network;

import android.os.ConditionVariable;
import android.text.TextUtils;

import com.daimler.biziz.android.manager.TokenManager;
import com.daimler.biziz.android.network.entity.base.BaseResponse;
import com.daimler.biziz.android.network.entity.login.LoginEntity;
import com.daimler.biziz.android.network.mockResponse.MOCK_Login;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


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
            newRequest = request.newBuilder().header("token",accessToken).header("lang",localeProperty).build();
        }
        Response response = chain.proceed(newRequest);
        if (isLoginRequest && response.isSuccessful()) {
            ResponseBody body = response.body();
            String bodyString = body.string();
            Type loginType = new TypeToken<BaseResponse<LoginEntity>>(){}.getType();
            BaseResponse<LoginEntity> loginModel = gson.fromJson(bodyString, loginType);
            if (loginModel != null && loginModel.getResponseData()!=null && !TextUtils.isEmpty(loginModel.getResponseData().getToken())) {
                tokenManager.setAccessToken(loginModel.getResponseData().getToken());
            }
            response =  response.newBuilder()
                    .body(ResponseBody.create(response.body().contentType(), bodyString)).build();

        }
        return response;
    }
}
