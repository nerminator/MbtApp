package com.daimlertruck.dtag.internal.android.mbt.test.ui.main.portal;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.databinding.FragmentPortalBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.manager.SharedPreferenceManager;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.MenuIncrementBody;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.APIService;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.AbstractApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.ApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.about.AboutActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.dialogs.ConfirmationDialog;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.dialogs.bottomSheetDialog.SupportCustomBottomSheetDialog;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.foodMenu.FoodMenuActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.htmlview.HtmlActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.main.portal.recycler.PortalAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.main.portal.recycler.PortalSpacesItemDecoration;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.news.NewsActivity;

import com.daimlertruck.dtag.internal.android.mbt.test.ui.news.NewsType;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.residential.ResidentialActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinks.UsefulLinksActivity;

import java.util.Locale;

import javax.inject.Inject;
import android.util.Log;
/**
 * x
 * A simple {@link Fragment} subclass.
 */
public class PortalFragment extends Fragment {

    private static final int REQUEST_APP_SETTINGS = 168;

    @Inject
    SharedPreferenceManager sharedPreferenceManager;

    private com.daimlertruck.dtag.internal.android.mbt.test.ui.main.portal.VMPortal vmPortal;
    private FragmentPortalBinding binding;
    @Inject
    AbstractApiUtils abstractApiUtils;

    public static PortalFragment newInstance() {

        Bundle args = new Bundle();

        PortalFragment fragment = new PortalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public PortalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_portal, container, false);
        ((BaseApplication) getActivity().getApplication()).getComponent().injectPortalFragment(this);

        vmPortal = ViewModelProviders.of(getActivity()).get(com.daimlertruck.dtag.internal.android.mbt.test.ui.main.portal.VMPortal.class);
        binding.setLifecycleOwner(this);
        initView();
        vmPortal.getFcmTokenAndTryToSendDeviceInfo();
        vmPortal.getUserConfig();
        return binding.getRoot();
    }

    private void initView() {
        initRecyclerView();
        decideShowNotificationPopUp();
        setClickListener();
    }

    private void initRecyclerView() {
        binding.recyclerViewPortal.setLayoutManager(new GridLayoutManager(getContext(), 3) {
            @Override
            public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
                lp.height = (getHeight() - (getResources().getDimensionPixelSize(R.dimen.grid_layout_manager_spacing) * 2)) / 3;
                return true;
            }
        });

        PortalAdapter portalAdapter = new PortalAdapter(vmPortal.getPortalList(), itemCallback -> {
            switch (itemCallback) {
                case ABOUT_US:
                    showAboutUsHTML();
                    abstractApiUtils.menuIncrement( new MenuIncrementBody("AboutUs"));
                    break;
                case NEWS:
                    NewsActivity.start(getContext(), NewsType.ANNOUNCEMENT);
                    break;
                case SHUTTLE:
                    redirectToGurselApp();
                    abstractApiUtils.menuIncrement( new MenuIncrementBody("Transportation"));
                    break;
                case LOCATION:
                    ResidentialActivity.start(getContext());
                    break;
                case MEAL_MENU:
                    FoodMenuActivity.start(getContext());
                    break;
                case DISCOUNTS:
                    NewsActivity.start(getContext(), NewsType.DISCOUNTS);
                    break;
                case USEFUL_LINKS:
                    UsefulLinksActivity.start(getContext(), false);
                    break;
                case EVENTS:
                    NewsActivity.start(getContext(), NewsType.EVENTS);
                    break;
                case EMERGENCY_NUMBERS:
                    UsefulLinksActivity.start(getContext(), true);
                    break;
            }
        });
        binding.recyclerViewPortal.setAdapter(portalAdapter);
        binding.recyclerViewPortal.addItemDecoration(new PortalSpacesItemDecoration(3, getResources().getDimensionPixelSize(R.dimen.grid_layout_manager_spacing), false));
    }

    private void redirectToGurselApp() {
        if (isGurselAppInstalled()) {
            Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage(GURSEL_APP_PACKAGE_NAME);
            if (launchIntent != null) {
                startActivity(launchIntent);
            }
        } else {
            redirectToPlayStore(GURSEL_APP_PACKAGE_NAME);
        }
    }

    private boolean isGurselAppInstalled() {
        PackageManager pm = getActivity().getPackageManager();
        try {
            pm.getPackageInfo(GURSEL_APP_PACKAGE_NAME, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void redirectToPlayStore(String packageName) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
            // Removing the package specification ensures it works with other app stores too
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
            // Fallback: open the app's page in a web browser
            //Log.d("myerror", e.toString());
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
            startActivity(webIntent);
        }
    }

    private void showAboutUsHTML() {
        String url = "https://bizizapp.com/bizizPanel/public/storage/aboutus" + Locale.getDefault().getLanguage().toUpperCase(Locale.ROOT) + ".html";
        HtmlActivity.start(getContext(), url, true);
    }

    private void decideShowNotificationPopUp() {
        if (!sharedPreferenceManager.isShowedNotificationPopup()) {
            showNotificationDialog();
        }
    }


    public void showNotificationDialog() {
        ConfirmationDialog confirmationDialog = ConfirmationDialog.newInstance(getString(R.string.TXT_COMMON_WARNING), getString(R.string.TXT_NOTIFICATION_ALERT_MESSAGE), getString(R.string.TXT_NOTIFICATION_ALERT_MESSAGE_CHANGE_SETTINGS), getString(R.string.TXT_NOTIFICATION_ALERT_MESSAGE_KEEP_SETTINGS));
        confirmationDialog.setListener(isConfirmed -> {
            if (isConfirmed) {
                goToSettings();
            }
            sharedPreferenceManager.setShowedNotificationPopup("1");
        });
        confirmationDialog.show(getChildFragmentManager(), "");
    }

    private void goToSettings() {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getActivity().getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(myAppSettings, REQUEST_APP_SETTINGS);
    }

    //region Bottom Sheet


    private void setClickListener() {
        View.OnClickListener onClickListener = v -> {
            if (v.getId() == R.id.imgCallCenter) {
                SupportCustomBottomSheetDialog.createCustomDialog(PortalFragment.this).show();
            } else if (v.getId() == R.id.imgInfo) {
                AboutActivity.start(getContext());
            }
        };
        binding.imgCallCenter.setOnClickListener(onClickListener);
        binding.imgInfo.setOnClickListener(onClickListener);
    }
    //endregion

    private static final String GURSEL_APP_PACKAGE_NAME = "app.milenyum.gursel.gursel_yolcu";
}
