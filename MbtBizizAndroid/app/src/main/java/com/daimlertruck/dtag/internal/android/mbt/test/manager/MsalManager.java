package com.daimlertruck.dtag.internal.android.mbt.test.manager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.daimlertruck.dtag.internal.android.mbt.test.BuildConfig;
import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.microsoft.identity.client.AcquireTokenParameters;
import com.microsoft.identity.client.AcquireTokenSilentParameters;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.IPublicClientApplication;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.SilentAuthenticationCallback;
import com.microsoft.identity.client.exception.MsalClientException;
import com.microsoft.identity.client.exception.MsalException;
import com.microsoft.identity.client.exception.MsalServiceException;
import com.microsoft.identity.client.exception.MsalUiRequiredException;
import com.daimlertruck.dtag.internal.android.mbt.test.manager.SharedPreferenceManager;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class MsalManager {

    public SharedPreferenceManager sharedPreferenceManager;

    private static MsalManager singleMsalManager = null;
    private ISingleAccountPublicClientApplication mSingleAccountApp;
    private IAccount mAccount;

    public interface ISignoutCallback {
        void onSuccess();
        void onFailure(Exception e);
    }


    private MsalManager() {
    }
    public static MsalManager getSingleMsalManager() {
        if (singleMsalManager == null) {
            singleMsalManager = new MsalManager();
        }
        return singleMsalManager;
    }

    public void init(Context context, Activity activity, SilentAuthenticationCallback silentCallback,  AuthenticationCallback interactiveCallback, SilentAuthenticationCallback loadCallback){

        this.sharedPreferenceManager = new SharedPreferenceManager(context);

        int configFileResourceId = R.raw.auth_config_single_account;
        if (BuildConfig.DEBUG) {
            configFileResourceId = R.raw.auth_config_single_account_debug;
        }

        PublicClientApplication.createSingleAccountPublicClientApplication(context,
                configFileResourceId,
                new IPublicClientApplication.ISingleAccountApplicationCreatedListener() {
                    @Override
                    public void onCreated(ISingleAccountPublicClientApplication application) {
                        mSingleAccountApp = application;
                        if (sharedPreferenceManager.isLogin())
                            loadAccount(true, activity, silentCallback, interactiveCallback, loadCallback);
                        else
                            loadCallback.onError(null);
                    }

                    @Override
                    public void onError(MsalException exception) {
                        Log.d("MSAL", exception.getMessage());
                        loadCallback.onError(exception);
                    }
                });
    }

    private void loadAccount(boolean withLogin, Activity activity, SilentAuthenticationCallback parentSilentCallback,
                             AuthenticationCallback parentInteractiveCallback, SilentAuthenticationCallback loadCallback) {
        if (mSingleAccountApp == null) {
            Log.e("MSAL", "loadAccount: mSingleAccountApp is null");
            loadCallback.onError(null);
            return;
        }

        mSingleAccountApp.getCurrentAccountAsync(new ISingleAccountPublicClientApplication.CurrentAccountCallback() {
            @Override
            public void onAccountLoaded(@Nullable IAccount activeAccount) {
                Log.d("MSAL", "Account loaded: " + (activeAccount != null ? activeAccount.getUsername() : "null"));

                if (activeAccount != null) {
                    mAccount = activeAccount;

                    if (withLogin) {
                        loginSilent(activity, parentSilentCallback, parentInteractiveCallback);
                    } else {
                        loadCallback.onSuccess(null);
                    }

                } else {
                    Log.w("MSAL", "Account is null — possible corrupted state or first login.");
                    if (withLogin) {
                        Log.i("MSAL", "Attempting interactive login after clearing cache.");
                        signOut(new ISignoutCallback() {
                            @Override
                            public void onSuccess() {
                                getInteractiveToken(activity, parentInteractiveCallback);
                            }

                            @Override
                            public void onFailure(Exception e) {
                                getInteractiveToken(activity, parentInteractiveCallback);
                            }
                        });
                    } else {
                        loadCallback.onError(null); // fallback silent fail
                    }
                }
            }

            @Override
            public void onAccountChanged(@Nullable IAccount priorAccount, @Nullable IAccount currentAccount) {
                Log.i("MSAL", "Account changed. Prior: "
                        + (priorAccount != null ? priorAccount.getUsername() : "null")
                        + ", Current: " + (currentAccount != null ? currentAccount.getUsername() : "null"));
                mAccount = currentAccount;
            }

            @Override
            public void onError(@NonNull MsalException exception) {
                Log.e("MSAL", "getCurrentAccountAsync failed: " + exception.getMessage());
                // Attempt sign out on load failure
                signOut(new ISignoutCallback() {
                    @Override
                    public void onSuccess() {
                        getInteractiveToken(activity, parentInteractiveCallback);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        getInteractiveToken(activity, parentInteractiveCallback);
                    }
                });
            }
        });
    }
    public void getInteractiveToken(Activity activity, AuthenticationCallback parentCallback) {
        if (mSingleAccountApp == null) {
            Log.e("MSAL", "getInteractiveToken() called but mSingleAccountApp is null!");
            return;
        }

        AcquireTokenParameters tokenParameters = new AcquireTokenParameters.Builder()
                .startAuthorizationFromActivity(activity)
                .withScopes(getScopes())
                .withCallback(getAuthInteractiveCallback(parentCallback))
                .build();
        mSingleAccountApp.acquireToken(tokenParameters);
    }

    private void loginSilent(Activity activity, SilentAuthenticationCallback parentSilentCallback, AuthenticationCallback parentInteractiveCallback) {
        if (mSingleAccountApp == null) {
            Log.e("MSAL", "Silent login failed: mSingleAccountApp is null");
            parentSilentCallback.onError(new MsalClientException("null_app", "SingleAccountApp not initialized"));
            return;
        }

        mSingleAccountApp.getCurrentAccountAsync(new ISingleAccountPublicClientApplication.CurrentAccountCallback() {
            @Override
            public void onAccountLoaded(@Nullable IAccount activeAccount) {
                if (activeAccount != null) {
                    mAccount = activeAccount;
                    Log.d("MSAL", "Loaded account for silent login: " + activeAccount.getUsername());

                    AcquireTokenSilentParameters parameters = new AcquireTokenSilentParameters.Builder()
                            .withScopes(getScopes())
                            .forAccount(activeAccount)
                            .fromAuthority(activeAccount.getAuthority())
                            .withCallback(getAuthSilentCallback(activity, parentSilentCallback, parentInteractiveCallback))
                            .build();

                    mSingleAccountApp.acquireTokenSilentAsync(parameters);
                } else {
                    Log.w("MSAL", "Silent login failed: No active account.");
                    sharedPreferenceManager.fullLogout();
                    parentSilentCallback.onError(new MsalUiRequiredException("no_account", "No cached account available"));
                }
            }

            @Override
            public void onAccountChanged(@Nullable IAccount priorAccount, @Nullable IAccount currentAccount) {
                Log.d("MSAL", "Account changed. Current is now: " + (currentAccount != null ? currentAccount.getUsername() : "null"));
                mAccount = currentAccount;
            }

            @Override
            public void onError(@NonNull MsalException exception) {
                Log.e("MSAL", "getCurrentAccountAsync failed: " + exception.getMessage());
                sharedPreferenceManager.fullLogout();
                parentSilentCallback.onError(exception);
            }
        });
    }
    private SilentAuthenticationCallback getAuthSilentCallback(Activity activity, SilentAuthenticationCallback parentSilentCallback, AuthenticationCallback parentInterCallback) {
        return new SilentAuthenticationCallback() {

            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                Log.d("MSAL", "Successfully authenticated");
                parentSilentCallback.onSuccess(authenticationResult);
            }

            @Override
            public void onError(MsalException exception) {
                Log.e("MSAL", "Silent authentication failed: " + exception.toString());

                if (exception instanceof MsalClientException) {
                    String errorCode = exception.getErrorCode();
                    Log.w("MSAL", "MsalClientException errorCode: " + errorCode);

                    if ("current_account_mismatch".equals(errorCode)) {
                        Log.w("MSAL", "Detected account mismatch. Signing out to recover.");
                        signOut(new ISignoutCallback() {
                            @Override
                            public void onSuccess() {
                                getInteractiveToken(activity, parentInterCallback);
                            }

                            @Override
                            public void onFailure(Exception e) {
                                getInteractiveToken(activity, parentInterCallback);
                            }
                        });
                        return;
                    }
                }

                // fallback
                sharedPreferenceManager.logout();
                parentSilentCallback.onError(exception);
            }
        };
    }

    private List<String> getScopes() {
        return Collections.singletonList("api://48252d22-0987-4d84-b1d9-00468ec9d424/Read");
    }

    private AuthenticationCallback getAuthInteractiveCallback( AuthenticationCallback parentCallback) {
        return new AuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                Log.d("MSAL", "Interactive login success for: " + authenticationResult.getAccount().getUsername());
                mAccount = authenticationResult.getAccount();
                sharedPreferenceManager.setIsLogin("1");
                parentCallback.onSuccess(authenticationResult);
            }

            @Override
            public void onError(MsalException exception) {
                Log.d("MSAL", "Authentication failed: " + exception.toString());
                parentCallback.onError(exception);
                if (exception instanceof MsalClientException) {
                    /* Exception inside MSAL, more info inside MsalError.java */
                } else if (exception instanceof MsalServiceException) {
                    /* Exception when communicating with the STS, likely config issue */
                }
            }

            @Override
            public void onCancel() {
                Log.d("MSAL", "User cancelled login.");
            }
        };
    }
    public void resetIfCorruptedState() {
        if (sharedPreferenceManager.isLogin() && mAccount == null) {
            Log.w("MSAL", "Corrupted login state detected. Resetting...");
            signOut(new ISignoutCallback() {
                @Override
                public void onSuccess() {
                    sharedPreferenceManager.fullLogout();
                }

                @Override
                public void onFailure(Exception e) {
                    sharedPreferenceManager.fullLogout(); // still do it
                }
            });
        }
    }
    public void signOut(@NonNull final ISignoutCallback callback) {
        if (mSingleAccountApp == null) {
            Log.w("MsalManager", "signOut() called but mSingleAccountApp is null");
            callback.onFailure(new IllegalStateException("MSAL not initialized"));
            return;
        }

        mSingleAccountApp.signOut(new ISingleAccountPublicClientApplication.SignOutCallback() {
            @Override
            public void onSignOut() {
                Log.d("MSAL", "Sign-out successful.");
                mAccount = null;
                sharedPreferenceManager.fullLogout();
                callback.onSuccess();
            }

            @Override
            public void onError(@NonNull MsalException exception) {
                Log.e("MSAL", "Sign-out failed: " + exception.getMessage());
                mAccount = null;
                sharedPreferenceManager.fullLogout();
                callback.onFailure(exception);
            }
        });
    }

    public String acquireTokenBlocking() {
        final Object lock = new Object();
        final String[] tokenHolder = new String[1];

        if (mSingleAccountApp == null) {
            Log.e("MSAL", "acquireTokenBlocking: mSingleAccountApp is null.");
            return null;
        }

        mSingleAccountApp.getCurrentAccountAsync(new ISingleAccountPublicClientApplication.CurrentAccountCallback() {
            @Override
            public void onAccountLoaded(@Nullable IAccount activeAccount) {
                if (activeAccount == null) {
                    synchronized (lock) {
                        tokenHolder[0] = null;
                        lock.notify();
                    }
                    return;
                }

                AcquireTokenSilentParameters silentParams = new AcquireTokenSilentParameters.Builder()
                        .forAccount(activeAccount)
                        .fromAuthority(activeAccount.getAuthority())
                        .withScopes(getScopes())
                        .withCallback(new SilentAuthenticationCallback() {
                            @Override
                            public void onSuccess(IAuthenticationResult authenticationResult) {
                                Log.d("MSAL", "Blocking token refresh succeeded.");
                                synchronized (lock) {
                                    tokenHolder[0] = authenticationResult.getAccessToken();
                                    lock.notify();
                                }
                            }

                            @Override
                            public void onError(MsalException exception) {
                                Log.e("MSAL", "Blocking token refresh failed: " + exception.toString());
                                synchronized (lock) {
                                    tokenHolder[0] = null;
                                    lock.notify();
                                }
                            }
                        })
                        .build();

                mSingleAccountApp.acquireTokenSilentAsync(silentParams);
            }

            @Override
            public void onAccountChanged(@Nullable IAccount priorAccount, @Nullable IAccount currentAccount) {}

            @Override
            public void onError(@NonNull MsalException exception) {
                synchronized (lock) {
                    tokenHolder[0] = null;
                    lock.notify();
                }
            }
        });

        try {
            synchronized (lock) {
                lock.wait(5000); // Wait max 5s
            }
        } catch (InterruptedException ignored) {}

        return tokenHolder[0];
    }
}
