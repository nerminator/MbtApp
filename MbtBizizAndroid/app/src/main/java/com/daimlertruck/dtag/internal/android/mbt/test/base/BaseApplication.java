package com.daimlertruck.dtag.internal.android.mbt.test.base;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.daimlertruck.dtag.internal.android.mbt.test.di.components.AppComponent;
import com.daimlertruck.dtag.internal.android.mbt.test.di.components.DaggerAppComponent;
import com.daimlertruck.dtag.internal.android.mbt.test.di.module.ContextModule;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.feedback.SubmitFeedbackBody;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.NetworkCallback;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.CrashLogBody;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.AbstractApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.NetworkCallback;
import com.daimlertruck.dtag.internal.android.mbt.test.BuildConfig;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.inject.Inject;


public class BaseApplication extends Application {
    private AppComponent component;
    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerAppComponent.builder()
                .contextModule(new ContextModule(this))
                .build();

        component.inject(this);  // This performs the injection in BaseApplication

        // Setup global crash handler
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            sendCrashLogToServer(throwable);

            // Optional: add delay to ensure log is sent before the app closes
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // Ignore
            }

            // Kill the app
            System.exit(1);
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(this);
    }

    public AppComponent getComponent() {
        return component;
    }

    @Inject
    AbstractApiUtils abstractApiUtils;

    private void sendCrashLogToServer(Throwable throwable) {
        new Thread(() -> {
            try {
                String stackTrace = Log.getStackTraceString(throwable);

                CrashLogBody body = new CrashLogBody();
                body.setPlatform("android");
                body.setMessage(throwable.getMessage());
                body.setStackTrace(stackTrace);
                body.setDeviceModel(Build.MODEL);
                body.setOsVersion(Build.VERSION.RELEASE);

                body.setAppVersion(BuildConfig.VERSION_NAME + "." + BuildConfig.VERSION_CODE);

                abstractApiUtils.sendCrashLog(body, new NetworkCallback<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        Log.d("CrashLogger", "Crash log sent successfully");
                    }

                    @Override
                    public void onServiceFailure(int httpResponseCode, String message) {
                        Log.w("CrashLogger", "Service failure: " + httpResponseCode + " - " + message);
                    }

                    @Override
                    public void onNetworkFailure(Throwable message) {
                        Log.e("CrashLogger", "Network failure", message);
                    }
                });

            } catch (Exception e) {
                Log.e("CrashLogger", "Error in crash handler", e);
            }
        }).start();
    }
}
