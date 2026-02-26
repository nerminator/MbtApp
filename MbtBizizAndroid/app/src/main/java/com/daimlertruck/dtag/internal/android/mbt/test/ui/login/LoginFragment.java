package com.daimlertruck.dtag.internal.android.mbt.test.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseFragment;
import com.daimlertruck.dtag.internal.android.mbt.databinding.FragmentLoginBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.manager.MsalManager;
import com.daimlertruck.dtag.internal.android.mbt.test.manager.SharedPreferenceManager;
import com.daimlertruck.dtag.internal.android.mbt.test.manager.TokenManager;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.about.AppStartUpEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.AbstractApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.NetworkCallback;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.about.AboutActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.dialogs.BizizProgressDialog;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.main.main.MainActivity;
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
import com.microsoft.identity.common.java.util.StringUtil;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import com.daimlertruck.dtag.internal.android.mbt.BuildConfig;
import android.os.Handler;
/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment {
    //region Instance
    private VMLogin vmLogin;
    private BizizProgressDialog progressDialog;
    private FragmentLoginBinding binding;
    //endregion

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public LoginFragment() {
    }

    @Inject
    TokenManager tokenManager;

    MsalManager msalManager;
    @Inject
    SharedPreferenceManager sharedPreferenceManager;

    @Inject
    AbstractApiUtils apiUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        vmLogin = new ViewModelProvider(this).get(VMLogin.class);
        binding.setVm(vmLogin);

        msalManager = MsalManager.getSingleMsalManager();
        vmLogin.isLoading.postValue(true);

        // ⏳ Safety timeout: hide spinner after 10 seconds if MSAL doesn't respond
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (vmLogin.isLoading.getValue() != null && vmLogin.isLoading.getValue()) {
                vmLogin.isLoading.postValue(false); // hide spinner
                Log.d("MSAL", "Silent login timeout. Stopping spinner.");
                msalManager.signOut(new MsalManager.ISignoutCallback() {
                    @Override
                    public void onSuccess() {
                        vmLogin.isLoading.postValue(false); // hide spinner
                    }

                    @Override
                    public void onFailure(Exception e) {
                        vmLogin.isLoading.postValue(false); // hide spinner
                    }
                });
                //getInteractiveToken(); // fallback
            }
        }, 7000); // 7 seconds

        waitForActivityAndInitMsal();

        observe();
        callAppStartUp();

        setOnClicks();
        return binding.getRoot();
    }

    private void waitForActivityAndInitMsal() {
        Handler handler = new Handler(Looper.getMainLooper());
        int maxAttempts = 20;
        int delayMs = 100;

        handler.postDelayed(new Runnable() {
            int attempts = 0;

            @Override
            public void run() {
                if (getContext() != null && getActivity() != null) {
                    msalManager.init(getContext(), getActivity(), getAuthSilentCallback(), getAuthInteractiveCallback( getActivity(), getParentCallback() ), getLoadCallback());
                } else if (attempts < maxAttempts) {
                    attempts++;
                    handler.postDelayed(this, delayMs);
                } else {
                    Log.e("LoginFragment", "Failed to initialize MSAL: Context or Activity was null too long.");
                    vmLogin.isLoading.postValue(false); // fallback UI behavior
                }
            }
        }, delayMs);
    }

    private AuthenticationCallback getParentCallback() {
        return new AuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                Log.d("MSAL", "Fallback interactive login successful.");
                tokenManager.setAccessToken(authenticationResult.getAccessToken());
                vmLogin.isLoading.postValue(false);
                getNextScreen(); // ✅ Navigate after fallback login
            }

            @Override
            public void onError(MsalException exception) {
                Log.e("MSAL", "Fallback interactive login failed: " + exception.toString());
                vmLogin.isLoading.postValue(false);
            }

            @Override
            public void onCancel() {
                Log.d("MSAL", "Fallback interactive login canceled by user.");
                vmLogin.isLoading.postValue(false);
            }
        };
    }
    private SilentAuthenticationCallback getAuthSilentCallback() {
        return new SilentAuthenticationCallback() {

            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                Log.d("MSAL", "Successfully authenticated");
                vmLogin.isLoading.postValue(false);
                tokenManager.setAccessToken(authenticationResult.getAccessToken());
                getNextScreen();
            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                Log.d("MSAL", "Authentication failed: " + exception.toString());
                vmLogin.isLoading.postValue(false);
                if (exception instanceof MsalClientException) {
                    /* Exception inside MSAL, more info inside MsalError.java */
                    msalManager.getInteractiveToken(getActivity(), getAuthInteractiveCallback(getActivity(), getParentCallback()));

                } else if (exception instanceof MsalServiceException) {
                    /* Exception when communicating with the STS, likely config issue */
                    msalManager.getInteractiveToken(getActivity(), getAuthInteractiveCallback(getActivity(), getParentCallback()));

                } else if (exception instanceof MsalUiRequiredException) {
                    msalManager.getInteractiveToken(getActivity(), getAuthInteractiveCallback(getActivity(), getParentCallback()));
                }
            }
        };
    }

    private SilentAuthenticationCallback getLoadCallback() {
        return new SilentAuthenticationCallback() {

            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                vmLogin.isLoading.postValue(false);
            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to load account */
                vmLogin.isLoading.postValue(false);
            }
        };
    }

    private AuthenticationCallback getAuthInteractiveCallback(Activity activityContextRef, AuthenticationCallback parentCallback) {
        return new AuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                tokenManager.setAccessToken(authenticationResult.getAccessToken());
                getNextScreen();
            }
            @Override
            public void onError(MsalException exception) {
                Log.e("MSAL", "Interactive login failed: " + exception.toString());

                if (exception instanceof MsalClientException) {
                    String errorCode = exception.getErrorCode();
                    Log.w("MSAL", "MsalClientException errorCode: " + errorCode);

                    if ("current_account_mismatch".equals(errorCode)) {
                        Log.w("MSAL", "Detected account mismatch during interactive login. Attempting full reset.");

                        msalManager.signOut(new MsalManager.ISignoutCallback() {
                            @Override
                            public void onSuccess() {
                                msalManager.getInteractiveToken(activityContextRef, parentCallback);  // retry fresh
                            }

                            @Override
                            public void onFailure(Exception e) {
                                msalManager.getInteractiveToken(activityContextRef, parentCallback);  // still retry
                            }
                        });

                        return;
                    }
                }

                // fallback: just pass to UI
                parentCallback.onError(exception);
            }
            @Override
            public void onCancel() {
                vmLogin.isLoading.postValue(false);
            }
        };
    }

    public void callAppStartUp() {
        apiUtils.appStartUp(new NetworkCallback<BaseResponse<AppStartUpEntity>>() {
            @Override
            public void onSuccess(BaseResponse<AppStartUpEntity> response) {
                sharedPreferenceManager.putAboutText(response.getResponseData().getAboutText());
                sharedPreferenceManager.putAppDescriptionText(response.getResponseData().getAppDescription());
            }

            @Override
            public void onServiceFailure(int httpResponseCode, String message) {

            }

            @Override
            public void onNetworkFailure(Throwable message) {
                Log.e("LoginFragment", "Network error on appStartUp: " + message);
                vmLogin.isLoading.postValue(false);
            }
        });
    }

    private void setOnClicks() {
        binding.imageView.setOnClickListener(v -> AboutActivity.start(getContext()));
        binding.btnLogin.setOnClickListener(v -> {
            vmLogin.isLoading.postValue(true); // show spinner immediately
            msalManager.signOut(new MsalManager.ISignoutCallback() {
                @Override
                public void onSuccess() {
                    Log.d("MSAL", "Signed out successfully. Starting fresh login.");
                    msalManager.getInteractiveToken(getActivity(), getAuthInteractiveCallback(getActivity(), getParentCallback()));
                }

                @Override
                public void onFailure(Exception e) {
                    Log.w("MSAL", "Signout failed. Still trying interactive login.");
                    msalManager.getInteractiveToken(getActivity(), getAuthInteractiveCallback(getActivity(), getParentCallback()));
                }
            });
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseApplication) getActivity().getApplication()).getComponent().inject(this);
    }

    private void getNextScreen() {
        String newsId = "";
        if (getActivity().getIntent().getExtras() != null) {
            newsId = getActivity().getIntent().getExtras().getString("newsId", "");
        }
        if (StringUtil.isNullOrEmpty(newsId)) {
            MainActivity.start(getContext());
            getActivity().finish();
        } else {
            MainActivity.start(getContext(), newsId);
            getActivity().finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //loadAccount(false);
    }

    private void observe() {
        vmLogin.isLoading.observe(getViewLifecycleOwner(), (Boolean aBoolean) -> {
            if (aBoolean) {
                showProgressDialog();
            } else {
                dismissProgressDialog();
            }
        });

        vmLogin.getLoginPressed().observe(getViewLifecycleOwner(), (Boolean aBoolean) -> {
            msalManager.getInteractiveToken(getActivity(), getAuthInteractiveCallback(getActivity(), getParentCallback()));
        });
    }
}

