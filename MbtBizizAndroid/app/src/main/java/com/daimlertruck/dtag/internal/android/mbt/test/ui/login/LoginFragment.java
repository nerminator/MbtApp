package com.daimlertruck.dtag.internal.android.mbt.test.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseFragment;
import com.daimlertruck.dtag.internal.android.mbt.test.databinding.FragmentLoginBinding;
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
import com.daimlertruck.dtag.internal.android.mbt.test.BuildConfig;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment {
    //region Instance
    private VMLogin vmLogin;
    private BizizProgressDialog progressDialog;
    private FragmentLoginBinding binding;
    private ISingleAccountPublicClientApplication mSingleAccountApp;
    private IAccount mAccount;
    //endregion

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public LoginFragment() {
    }

    @Inject
    TokenManager tokenManager;

    @Inject
    SharedPreferenceManager sharedPreferenceManager;

    @Inject
    AbstractApiUtils apiUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        vmLogin = ViewModelProviders.of(this).get(VMLogin.class);
        binding.setVm(vmLogin);

        int configFileResourceId = R.raw.auth_config_single_account;
        if (BuildConfig.DEBUG) {
            configFileResourceId = R.raw.auth_config_single_account_debug;
        }

        PublicClientApplication.createSingleAccountPublicClientApplication(getContext(),
                configFileResourceId,
                new IPublicClientApplication.ISingleAccountApplicationCreatedListener() {
                    @Override
                    public void onCreated(ISingleAccountPublicClientApplication application) {
                        mSingleAccountApp = application;
                        loadAccount(true);
                    }

                    @Override
                    public void onError(MsalException exception) {
                        Log.d("MSAL", exception.getMessage());
                    }
                });

        observe();
        callAppStartUp();

        setOnClicks();
        return binding.getRoot();
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

            }
        });
    }

    private void setOnClicks() {
        binding.imageView.setOnClickListener(v -> AboutActivity.start(getContext()));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseApplication) getActivity().getApplication()).getComponent().inject(this);
    }

    private void getInteractiveToken() {
        if (mSingleAccountApp == null) {
            return;
        }

        AcquireTokenParameters tokenParameters = new AcquireTokenParameters.Builder()
                .startAuthorizationFromActivity(getActivity())
                .withScopes(getScopes())
                .withCallback(getAuthInteractiveCallback())
                .build();
        mSingleAccountApp.acquireToken(tokenParameters);
    }

    private void loginSilent() {
        if (sharedPreferenceManager.isLogin()) {
            AcquireTokenSilentParameters parameters = new AcquireTokenSilentParameters.Builder()
                    .withScopes(getScopes())
                    .fromAuthority(mAccount.getAuthority())
                    .forAccount(mAccount)
                    .withCallback(getAuthSilentCallback())
                    .build();
            vmLogin.isLoading.postValue(true);
            try {
                mSingleAccountApp.acquireTokenSilentAsync(parameters);
            } catch (Exception e) {
                vmLogin.isLoading.postValue(false);
                getInteractiveToken();
            }
        } else {
            mSingleAccountApp.signOut(new ISingleAccountPublicClientApplication.SignOutCallback() {
                @Override
                public void onSignOut() {
                    mAccount = null;
                }

                @Override
                public void onError(@NonNull MsalException exception) {
                }
            });
        }
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

                if (exception instanceof MsalClientException) {
                    /* Exception inside MSAL, more info inside MsalError.java */
                } else if (exception instanceof MsalServiceException) {
                    /* Exception when communicating with the STS, likely config issue */
                } else if (exception instanceof MsalUiRequiredException) {
                    vmLogin.isLoading.postValue(false);
                    getInteractiveToken();
                }
            }
        };
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
        loadAccount(false);
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
            getInteractiveToken();
        });
    }

    private List<String> getScopes() {
        return Collections.singletonList("api://48252d22-0987-4d84-b1d9-00468ec9d424/Read");
    }

    private AuthenticationCallback getAuthInteractiveCallback() {
        return new AuthenticationCallback() {

            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                Log.d("MSAL", "Successfully authenticated");

                mAccount = authenticationResult.getAccount();
                tokenManager.setAccessToken(authenticationResult.getAccessToken());
                sharedPreferenceManager.setIsLogin("1");
                getNextScreen();
            }

            @Override
            public void onError(MsalException exception) {
                Log.d("MSAL", "Authentication failed: " + exception.toString());

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

    private void loadAccount(boolean withLogin) {
        if (mSingleAccountApp == null) {
            return;
        }

        mSingleAccountApp.getCurrentAccountAsync(new ISingleAccountPublicClientApplication.CurrentAccountCallback() {
            @Override
            public void onAccountLoaded(@Nullable IAccount activeAccount) {
                // You can use the account data to update your UI or your app database.
                mAccount = activeAccount;
                if (withLogin && activeAccount != null) {
                    loginSilent();
                }
            }

            @Override
            public void onAccountChanged(@Nullable IAccount priorAccount, @Nullable IAccount currentAccount) {
                if (currentAccount == null) {
                    // Perform a cleanup task as the signed-in account changed.
                }
            }

            @Override
            public void onError(@NonNull MsalException exception) {
            }
        });
    }
}

