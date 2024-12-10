package com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinks;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.databinding.FragmentUsefulLinksPortalBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.birthday.BirthdayActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.main.portal.recycler.PortalSpacesItemDecoration;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinks.recycler.UsefulLinksPortalAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinkscategory.UsefulLinksCategoryFragment;

/**
 * x
 * A simple {@link Fragment} subclass.
 */
public class UsefulLinksPortalFragment extends Fragment {

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
                    ((com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinks.UsefulLinksActivity)getActivity()).addFragment(UsefulLinksCategoryFragment.newInstance(false));
                    break;
                case INTERNAL_ADS:
                    redirectToUrl("https://performancemanager5.successfactors.eu/acme?bplte_company=mercedesbe&fbacme_n=recruiting&recruiting%5fns=joblisting%20summary&_s.crb=9ZPnbTenOQEkBst64BF17GYPU8usDnQOfv2MFLy3e1U%3d");
                    break;
                case BIRTHDAYS:
                    BirthdayActivity.start(getContext());
                    break;
                case MBFIT:
                    redirectToPlayStore(WELLBEES_APP_PACKAGE_NAME);
                    break;
                case GURSEL:
                    redirectToGurselApp();
                    break;
                case ORCHESTRA:
                    redirectToCloudOffixApp();
                    break;
                case IMPROVEMENT_SYSTEM:
                    redirectToUrl("https://daimler-truck.ideas.cloud/app/");
                    break;
                case COMPANY_CAR:
                    redirectToUrl("https://vts.tr152.corpintra.net/account/LoginPortal?returnUrl=/");
                    break;
                case LIBRARY:
                    redirectToUrl("https://kitapdunyasi.tr152.corpintra.net/");
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
    private static final String WELLBEES_APP_PACKAGE_NAME  = "com.wellbees.android";
    private static final String CLOUD_OFFIX_PACKAGE_NAME  = "com.cloudoffix";
}
