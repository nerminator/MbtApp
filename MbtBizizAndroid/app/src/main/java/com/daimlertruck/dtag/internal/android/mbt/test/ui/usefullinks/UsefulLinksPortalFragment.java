package com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinks;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.databinding.FragmentUsefulLinksPortalBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.MenuIncrementBody;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.AbstractApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.birthday.BirthdayActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.main.portal.recycler.PortalSpacesItemDecoration;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.socialmedia.SocialMediaActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinks.recycler.UsefulLinksPortalAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinkscategory.UsefulLinksCategoryFragment;

import javax.inject.Inject;

/**
 * x
 * A simple {@link Fragment} subclass.
 */
public class UsefulLinksPortalFragment extends Fragment {

    @Inject
    AbstractApiUtils abstractApiUtils;

    private com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinks.VMUsefulLinksPortal vmUsefulLinksPortal;
    private FragmentUsefulLinksPortalBinding binding;

    public static UsefulLinksPortalFragment newInstance() {
        return new UsefulLinksPortalFragment();
    }

    public UsefulLinksPortalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_useful_links_portal, container, false);
        ((BaseApplication) getActivity().getApplication()).getComponent().injectUsefulLinksPortalFragment(this);

        vmUsefulLinksPortal = ViewModelProviders.of(getActivity()).get(com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinks.VMUsefulLinksPortal.class);
        binding.setLifecycleOwner(this);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        binding.recyclerViewUsefulLinksPortal.setLayoutManager(new GridLayoutManager(getContext(), 3) {
            @Override
            public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
                lp.height = (getHeight() - (getResources().getDimensionPixelSize(R.dimen.grid_layout_manager_spacing)*2)) / 3;
                return true;
            }
        });

        UsefulLinksPortalAdapter portalAdapter = new UsefulLinksPortalAdapter(vmUsefulLinksPortal.getPortalList(), itemCallback -> {
            switch (itemCallback){
                case SOCIAL_CLUBS:
                    // Update the toolbar title
                    UsefulLinksActivity activity = (UsefulLinksActivity) getActivity();
                    if (activity != null) {
                        activity.updateToolbarTitle(getString(R.string.TXT_LINKS_TITLE1)); // Call a method in Activity
                    }

                    ((com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinks.UsefulLinksActivity)getActivity()).addFragment(UsefulLinksCategoryFragment.newInstance(false));

                    break;
                case INTERNAL_ADS:
                    redirectToUrl("https://performancemanager5.successfactors.eu/sf/careers/jobsearch?bplte_company=mercedesbe&_s.crb=McXCkAgE%252fh%252bQRsZU9VjG2iZj%252f4QF%252b%252bcAtBlOkPdHY4U%253d");
                    abstractApiUtils.menuIncrement( new MenuIncrementBody("InternalAds"));
                    break;
                case BIRTHDAYS:
                    BirthdayActivity.start(getContext());
                    break;
                case SAP_CONCUR:
                    redirectToPlayStore(SAP_CONCUR_APP_PACKAGE_NAME);
                    abstractApiUtils.menuIncrement( new MenuIncrementBody("SapConcur"));
                    break;
                case MEALBOX:
                    redirectToUrl("https://mercedes-benz.rezervem.com.tr/");
                    abstractApiUtils.menuIncrement( new MenuIncrementBody("YemegimEvimde"));
                    break;
                case ORCHESTRA:
                    redirectToCloudOffixApp();
                    abstractApiUtils.menuIncrement( new MenuIncrementBody("CloudOffix"));
                    break;
                case IMPROVEMENT_SYSTEM:
                    redirectToUrl("https://daimlertruck.crowdworx.com/ideas");
                    abstractApiUtils.menuIncrement( new MenuIncrementBody("Ideas"));
                    break;
                case ORACLE_HCM:
                    redirectToUrl("https://login-exdu-saasfaprod1.fa.ocs.oraclecloud.com");
                    abstractApiUtils.menuIncrement( new MenuIncrementBody("OracleHcm"));
                    break;
                case SOCIAL_MEDIA:
                    SocialMediaActivity.start(this.getContext());
                    //redirectToUrl("https://kitapdunyasi.tr152.corpintra.net/");
                    break;
            }
        });
        binding.recyclerViewUsefulLinksPortal.setAdapter(portalAdapter);
        binding.recyclerViewUsefulLinksPortal.addItemDecoration(new PortalSpacesItemDecoration(3, getResources().getDimensionPixelSize(R.dimen.grid_layout_manager_spacing), false));
    }

    private void redirectToUrl(String url) {
        if (getContext()!= null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            getContext().startActivity(intent);
        }
    }

    //region Gursel App
    private void redirectToGurselApp() {
        if (isGurselAppInstalled()){
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
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
        }
    }
    //endregion

    //region cloudoffix
    private void redirectToCloudOffixApp() {
        if (isCloudOffixInstalled()){
            Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage(CLOUD_OFFIX_PACKAGE_NAME);
            if (launchIntent != null) {
                startActivity(launchIntent);
            }
        } else {
            redirectToPlayStore(CLOUD_OFFIX_PACKAGE_NAME);
        }
    }
    private boolean isCloudOffixInstalled() {
        PackageManager pm = getActivity().getPackageManager();
        try {
            pm.getPackageInfo(CLOUD_OFFIX_PACKAGE_NAME, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    //endregion

    private static final String GURSEL_APP_PACKAGE_NAME  = "app.milenyum.gursel.gursel_yolcu";
    //private static final String WELLBEES_APP_PACKAGE_NAME  = "com.wellbees.android";
    private static final String SAP_CONCUR_APP_PACKAGE_NAME  = "com.concur.breeze";
    private static final String CLOUD_OFFIX_PACKAGE_NAME  = "com.cloudoffix";
}
