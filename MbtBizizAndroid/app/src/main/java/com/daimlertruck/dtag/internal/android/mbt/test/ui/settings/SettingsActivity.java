package com.daimlertruck.dtag.internal.android.mbt.test.ui.settings;

import static java.security.AccessController.getContext;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.adapters.SettingsAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.databinding.ActivitySettingsBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.manager.SharedPreferenceManager;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.settings.ISettingsChange;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.settings.SettingObject;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.toolbar.VMToolbar;

import java.util.ArrayList;

import javax.inject.Inject;

public class SettingsActivity extends BaseActivity<ActivitySettingsBinding> {
    private ArrayList<SettingObject> list = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private SettingsAdapter settingsAdapter;
    private static final int REQUEST_APP_SETTINGS = 168;
    private com.daimlertruck.dtag.internal.android.mbt.test.ui.settings.VMSettings vmSettings;
    private AlertDialog alertDialog;

    @Inject
    SharedPreferenceManager sharedPreferenceManager;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_settings;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
        vmSettings = ViewModelProviders.of(this).get(com.daimlertruck.dtag.internal.android.mbt.test.ui.settings.VMSettings.class);
    }

    private void observe() {
        vmSettings.getSettingRootObject().observe(this, settingRootObject -> {
            list.clear();
            if (settingRootObject != null && settingRootObject.getNotificationSettingList() != null) {
                list.addAll(settingRootObject.getNotificationSettingList());
                settingsAdapter.notifyDataSetChanged();
            }
        });

        vmSettings.getSettingObject().observe(this, settingObject -> {
            list.get(getObjectIndexSetting(list, settingObject)).setValue(settingObject.getValue());
        });

        vmSettings.isLoading.observe(this, (Boolean aBoolean) -> {
            if (aBoolean) {
                showProgressDialog();
            } else {
                dismissProgressDialog();
            }
        });

        vmSettings.showErrorMessage.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                showMessageDialog(null, s);
            }
        });

        vmSettings.logoutSucced.observe(this, aBoolean -> {
            if (aBoolean) {
                sharedPreferenceManager.logout();
                Intent intent = new Intent();
                intent.setPackage("com.daimlertruck.dtag.internal.android.mbt.test");
                intent.setAction("com.daimler.biziz.intent.LOG_OUT");
                sendBroadcast(intent);
            }
        });
        goToSettings();
    }

    private void InitView() {
        binding.recyclerSettings.setNestedScrollingEnabled(false);
        setToolbar();
        setRecyclerView();
        setListener();
    }

    private void setToolbar() {
        VMToolbar vmToolbar = VMToolbar.createVMToolbarFor(this, getString(R.string.TXT_PROFILE_SETTINGS_TITLE), null);
        binding.toolbar.setVm(vmToolbar);
    }

    private void setListener() {
        binding.btnSignOutSetting.setOnClickListener(v -> getAlertDialog());
    }

    public void getAlertDialog() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        alertDialog = builder.setTitle("R.string.TXT_SETTINGS_ALERT_DIALOG_TITLE")
                .setMessage(R.string.TXT_PROFILE_SETTINGS_QUIT_WARNING)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                })
                .show();
    }

    private void goToSettings() {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        myAppSettings.setPackage("com.daimlertruck.dtag.internal.android.mbt.test");
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(myAppSettings, REQUEST_APP_SETTINGS);
    }

    private void setRecyclerView() {

        binding.recyclerSettings.setHasFixedSize(true);
        binding.recyclerSettings.setNestedScrollingEnabled(false);
        binding.nestedViewSettings.setNestedScrollingEnabled(false);
        settingsAdapter = new SettingsAdapter(list, this, getISettingChange());
        linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerSettings.setLayoutManager(linearLayoutManager);
        binding.recyclerSettings.setAdapter(settingsAdapter);
    }

    private ISettingsChange getISettingChange() {
        return settingObject -> {
            vmSettings.changeSettings(settingObject);
        };
    }

    public static int getObjectIndexSetting(ArrayList<SettingObject> list, SettingObject settingObject) {
        for (SettingObject item : list) {
            if (item.getType().equals(settingObject.getType())) {
                return list.indexOf(item);
            }
        }
        return -1;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, SettingsActivity.class);
        starter.setPackage("com.daimlertruck.dtag.internal.android.mbt.test");
        context.startActivity(starter);
    }
}
