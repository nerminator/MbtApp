package com.daimler.biziz.android.ui.settings;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.adapters.SettingsAdapter;
import com.daimler.biziz.android.base.BaseActivity;
import com.daimler.biziz.android.databinding.ActivitySettingsBinding;
import com.daimler.biziz.android.network.entity.settings.ISettingsChange;
import com.daimler.biziz.android.network.entity.settings.SettingObject;
import com.daimler.biziz.android.ui.toolbar.VMToolbar;

import java.util.ArrayList;

import retrofit2.Call;

public class SettingsActivity extends BaseActivity<ActivitySettingsBinding> {
    private ArrayList<SettingObject> list = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private SettingsAdapter settingsAdapter;
    private static final int REQUEST_APP_SETTINGS = 168;
    private VMSettings vmSettings;

    private AlertDialog alertDialog;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_settings;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
        vmSettings = ViewModelProviders.of(this).get(VMSettings.class);
        observe();
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
                showMessageDialog(null,s);
            }
        });

        vmSettings.logoutSucced.observe(this, aBoolean -> {
            if (aBoolean) {
                Intent intent = new Intent();
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
        binding.btnSignOutSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlertDialog();
            }
        });

        /*binding.relPermissionSettingsContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSettings();
            }
        });*/
    }

    public void getAlertDialog() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        alertDialog=builder.setTitle(R.string.TXT_SETTINGS_ALERT_DIALOG_TITLE)
                .setMessage(R.string.TXT_PROFILE_SETTINGS_QUIT_WARNING)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        vmSettings.logout();
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
        context.startActivity(starter);
    }

    /*public void getSettingsRequest() {
        IonRequest<BaseResponse<SettingRootObject>> request = new IonRequest<>(this);
        JsonObject jsonObject = new JsonObject();

        request.ion(requestMethods.POST, BaseUrl.getNotificationSettings, new NetworkInterface<BaseResponse<SettingRootObject>>() {
            @Override
            public void callBack(BaseResponse<SettingRootObject> result) {
                list.clear();
                list.addAll(result.getResponseData().getNotificationSettingList());
                settingsAdapter.notifyDataSetChanged();
            }

            @Override
            public void negativeCallback() {

            }

            @Override
            public void onError(Exception e) {

            }
        }, jsonObject, new TypeToken<BaseResponse<SettingRootObject>>() {
        });
    }*/

    /*public void changeNotificationSetting(final SettingObject settingObject) {
        IonRequest<BaseResponse> request = new IonRequest<>(this);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", settingObject.getType());
        if (settingObject.getValue() == 1)
            jsonObject.addProperty("value", 0);
        else
            jsonObject.addProperty("value", 1);

        request.ion(requestMethods.POST, BaseUrl.changeNotificationSetting, new NetworkInterface<BaseResponse>() {
            @Override
            public void callBack(BaseResponse result) {
                if (utils.getObjectIndexSetting(list, settingObject) != -1) {
                    if (settingObject.getValue() == 1)
                        list.get(utils.getObjectIndexSetting(list, settingObject)).setValue(0);
                    else
                        list.get(utils.getObjectIndexSetting(list, settingObject)).setValue(1);
                }
            }

            @Override
            public void negativeCallback() {

            }

            @Override
            public void onError(Exception e) {

            }
        }, jsonObject, new TypeToken<BaseResponse>() {
        });
    }*/
}
