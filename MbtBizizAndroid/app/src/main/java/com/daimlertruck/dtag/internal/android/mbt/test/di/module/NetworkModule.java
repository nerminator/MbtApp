package com.daimlertruck.dtag.internal.android.mbt.test.di.module;

import com.daimlertruck.dtag.internal.android.mbt.test.di.scopes.ApplicationScope;
import com.daimlertruck.dtag.internal.android.mbt.test.manager.TokenManager;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.APIService;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.LoginInterceptor;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.NetworkConstants;
import com.google.gson.Gson;


import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by atakankersit on 14.12.2017.
 */


@ApplicationScope
@Module(includes = {TokenManagerModule.class,ContextModule.class})
public class NetworkModule {



    @ApplicationScope
    @Provides
    public APIService provideAuthorizedApiService(OkHttpClient okHttpClient, Gson gson ) {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(NetworkConstants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson));
        return retrofitBuilder.build().create(APIService.class);
    }


    @ApplicationScope
    @Provides
    public OkHttpClient provideAuthorizedOkHttpClient( OkHttpClient.Builder builder) {
        return  builder.build();
    }




    @ApplicationScope
    @Provides
    public OkHttpClient.Builder provideAuthorizedOkHttpBuilder(HttpLoggingInterceptor loggingInterceptor, LoginInterceptor loginIntercepter, CookieJar cookieJar) {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        //okHttpBuilder.cookieJar(cookieJar);

        //CertificatePinner certificatePinner=new CertificatePinner.Builder().add("https://bizizapp.com","sha256/246e2b2dd06a925151256901aa9a47a689e74020").build();

        okHttpBuilder.cookieJar(cookieJar)
                //.addInterceptor(loggingInterceptor)
                //Authorized oldugu icin loginIntercepter ekleniyor.
                //.certificatePinner(certificatePinner)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(loginIntercepter)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS);
        return okHttpBuilder;

    }



    //region Intercepters

    @ApplicationScope
    @Provides
    public HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return loggingInterceptor;
    }



    @ApplicationScope
    @Provides
    public LoginInterceptor getLoginIntercepter(Gson gson, TokenManager tokenManager) {
        return new LoginInterceptor(tokenManager, gson);
    }
    //endregion



    @ApplicationScope
    @Provides
    public Gson provideGson(){
        return  new Gson();
    }

    @ApplicationScope
    @Provides
    public CookieJar provideCookieJar() {
        return new CookieJar() {
            public static final String TAG = "Cookie Jar";
            public List<Cookie> cookies = Collections.EMPTY_LIST;

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                for (Cookie cookie : cookies) {
                    //Log.i(TAG, "saveFromResponse: ahan da cookie " + cookie.toString());
                }
                this.cookies = cookies;
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                //Log.d(TAG, "loadForRequest() called with: url = [" + url + "]");
                return cookies;
            }
        };
    }

    @ApplicationScope
    @Provides
    public NetworkConstants provideNetworkConstants() {
        return new NetworkConstants();
    }

}
