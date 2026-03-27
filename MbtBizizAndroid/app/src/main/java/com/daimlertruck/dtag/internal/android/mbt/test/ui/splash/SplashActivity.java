package com.daimlertruck.dtag.internal.android.mbt.test.ui.splash;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.splashscreen.SplashScreen;
import androidx.lifecycle.ViewModelProviders;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.manager.SharedPreferenceManager;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.init.ButtonList;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.init.InitEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.dialogs.InitVersionDialog;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.login.LoginActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.utils.RootUtil;
import com.google.firebase.messaging.FirebaseMessaging;
import com.microsoft.identity.common.java.util.StringUtil;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity {

    @Inject
    SharedPreferenceManager sharedPreferenceManager;

    com.daimlertruck.dtag.internal.android.mbt.test.ui.splash.VMSplash vmSplash;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        ((BaseApplication) getApplication()).getComponent().inject(this);
        vmSplash = ViewModelProviders.of(this).get(com.daimlertruck.dtag.internal.android.mbt.test.ui.splash.VMSplash.class);
        binding.setLifecycleOwner(this);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        observe();

        if (RootUtil.isDeviceRooted()) {
            showMessageDialog("", getString(R.string.TXT_LOGIN_SPLASH_ROOTED_DEVICE), () -> {
                finish();
                System.exit(0);
            });
        } else {
            vmSplash.checkVersion(getVersionName());
        }
    }

    private void observe() {
        vmSplash.continueToApp.observe(this, aBoolean -> {
            continueToApp();
        });
        vmSplash.showDialog.observe(this, this::getVersionDialog);
    }

    private void continueToApp() {
        String newsId = "";
        if (getIntent().getExtras() != null) {
            newsId = getIntent().getExtras().getString("newsId", "");
        }
        if (StringUtil.isNullOrEmpty(newsId)) {
            LoginActivity.start(this);
        } else {
            LoginActivity.start(this, newsId);
        }
        finish();
    }

    private String getVersionName() {
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            String version = pInfo.versionName;
            // Strip flavor suffix (e.g. "-dev") so backend regex accepts it
            if (version != null && version.contains("-")) {
                version = version.substring(0, version.indexOf("-"));
            }
            return version != null ? version : "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void getVersionDialog(
            InitEntity initEntity
    ) {
        InitVersionDialog initVersionDialog = InitVersionDialog.newInstance(
                initEntity.getTitle(),
                initEntity.getMessage(),
                getDownloadAppText(initEntity.getButtonList()),
                getContinueToAppText(initEntity.getButtonList()),
                detectType(Objects.requireNonNull(initEntity.getButtonList())),
                getDownloadLink(initEntity.getButtonList())
        );
        initVersionDialog.setListener(new InitVersionDialog.VersionDialogListener() {
            @Override
            public void onDownloadApp(String url) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }

            @Override
            public void onContinueToApp() {
                continueToApp();
            }
        });
        initVersionDialog.show(getSupportFragmentManager(), "");
    }

    private String getDownloadLink(List<ButtonList> buttonList) {
        String downloadUrl = "";
        for (ButtonList button : buttonList) {
            if (Objects.equals(button.getType(), DOWNLOAD_APP_TYPE)) {
                downloadUrl = button.getUrl();
            }
        }
        return downloadUrl;
    }

    private String getContinueToAppText(List<ButtonList> buttonList) {
        return getButtonText(buttonList, CONTINUE_TO_APP_TYPE);
    }

    private String getDownloadAppText(List<ButtonList> buttonList) {
        return getButtonText(buttonList, DOWNLOAD_APP_TYPE);
    }

    private String getButtonText(List<ButtonList> buttonList, Long buttonType) {
        String buttonText = "";
        for (ButtonList button : buttonList) {
            if (Objects.equals(button.getType(), buttonType)) {
                buttonText = button.getText();
                break;
            }
        }
        return buttonText;
    }

    private InitUpdateDialogType detectType(List<ButtonList> buttonList) {
        InitUpdateDialogType dialogType = InitUpdateDialogType.HARD;
        for (ButtonList button : buttonList) {
            if (button.getType() != null && button.getType() == 1) {
                dialogType = InitUpdateDialogType.SOFT;
                break;
            }
        }
        return dialogType;
    }

    private static final Long DOWNLOAD_APP_TYPE = 2L;
    private static final Long CONTINUE_TO_APP_TYPE = 1L;
}
