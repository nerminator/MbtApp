package com.daimlertruck.dtag.internal.android.mbt.test.ui.about;


import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimlertruck.dtag.internal.android.mbt.BuildConfig;
import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseFragment;
import com.daimlertruck.dtag.internal.android.mbt.databinding.FragmentAboutBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.manager.SharedPreferenceManager;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.about.appfeedback.AppFeedbackFragment;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.toolbar.VMToolbar;

import java.util.Locale;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends BaseFragment implements View.OnClickListener {

    private FragmentAboutBinding binding;

    @Inject
    SharedPreferenceManager sharedPreferenceManager;

    public AboutFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about, container, false);
        VMToolbar vmToolbar = VMToolbar.createVMToolbarFor(this, getString(R.string.TXT_ABOUT_NAV), null);
        binding.toolbar.setVm(vmToolbar);
        binding.toolbar.imgvInfoToolbar.setVisibility(View.GONE);
        binding.tvMainDescAbout.setText(sharedPreferenceManager.getAboutText());
        InitView();
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseApplication) getActivity().getApplication()).getComponent().inject(this);
    }

    private void InitView() {
        setListener();
    }

    public static AboutFragment newInstance() {
        Bundle args = new Bundle();
        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void setListener() {
        binding.relThirdPartContentAbout.setOnClickListener(this);
        binding.relOpenSourceAbout.setOnClickListener(this);
        binding.relDataProtectionAbout.setOnClickListener(this);
        binding.relTermsOfUseAbout.setOnClickListener(this);
        binding.relAppDescriptionAbout.setOnClickListener(this);
        binding.relAppSupportAbout.setOnClickListener(this);
        binding.relLegalNoticesAbout.setOnClickListener(this);
        binding.frameLayoutAppFeedback.setOnClickListener(v -> openFeedbackFragment());
        setVersionText();
    }

    private void setVersionText() {
        String versionText = getResources().getString(R.string.TXT_VERSION_INFO) +
                BuildConfig.VERSION_NAME+"."+BuildConfig.VERSION_CODE;
        binding.txtVersionInfo.setText(versionText);
    }

    private void openFeedbackFragment() {
        AppFeedbackFragment appFeedbackFragment = AppFeedbackFragment.newInstance();
        ((AboutActivity) getActivity()).loadFragment(R.id.container, appFeedbackFragment, true);
    }

    @Override
    public void onClick(View v) {
        AboutDetailFragment aboutDetailFragment = new AboutDetailFragment();
        Bundle bunle = new Bundle();

        if (v.getId() == R.id.relAppDescriptionAbout) {
            //Uygulama Açıklaması
            if (Locale.getDefault().getLanguage().equals("tr")) {
                bunle.putString("fileName", "appdescriptionTR");
            } else {
                bunle.putString("fileName", "appdescriptionEN");
            }
            bunle.putString("titleString", getResources().getString(R.string.TXT_ABOUT_MAIN_APP_DESCRIPTION));
        } else if (v.getId() == R.id.relTermsOfUseAbout) {
            //Kullanım Koşulları
            if (Locale.getDefault().getLanguage().equals("tr")) {
                bunle.putString("fileName", "termsofuseaboutTR");
            } else {
                bunle.putString("fileName", "termsofuseaboutEN");
            }
            //bunle.putString("bodyString", getResources().getString(R.string.TXT_ABOUT_TERMS_OF_USE_BODY));
            bunle.putString("titleString", getResources().getString(R.string.TXT_ABOUT_MAIN_TERMS_OF_USE));
        } else if (v.getId() == R.id.relDataProtectionAbout) {
            //Veri koruması
            if (Locale.getDefault().getLanguage().equals("tr")) {
                bunle.putString("fileName", "dataprotectionTR");
            } else {
                bunle.putString("fileName", "dataprotectionEN");
            }
            //bunle.putString("bodyString", getResources().getString(R.string.TXT_ABOUT_DATA_PROTECTION_BODY));
            bunle.putString("titleString", getResources().getString(R.string.TXT_ABOUT_MAIN_DATA_PROTECTION));
        } else if (v.getId() == R.id.relOpenSourceAbout) {
            //Ücretsiz veya Açık Kaynaklı Yazılum
            if (Locale.getDefault().getLanguage().equals("tr")) {
                bunle.putString("fileName", "fossTR");
            } else {
                bunle.putString("fileName", "fossEN");
            }
            bunle.putString("titleString", getResources().getString(R.string.TXT_ABOUT_MAIN_FOSS));
        } else if (v.getId() == R.id.relThirdPartContentAbout) {
            //Üçüncü taraf içeriği
            if (Locale.getDefault().getLanguage().equals("tr")) {
                bunle.putString("fileName", "thirdpartyTR");
            } else {
                bunle.putString("fileName", "thirdpartyEN");
            }
            bunle.putString("titleString", getResources().getString(R.string.TXT_ABOUT_MAIN_3RD_PARTY_CONTENT));

        } else if (v.getId() == R.id.relLegalNoticesAbout) {
            //Yasal Uyarılar
            if (Locale.getDefault().getLanguage().equals("tr")) {
                bunle.putString("fileName", "legalTR");
            } else {
                bunle.putString("fileName", "legalEN");
            }
            bunle.putString("titleString", getResources().getString(R.string.TXT_ABOUT_MAIN_LEGAL_NOTICES));
        } else if (v.getId() == R.id.relAppSupportAbout) {
            //Uygulama Desteği
            if (Locale.getDefault().getLanguage().equals("tr")) {
                bunle.putString("fileName", "appsupportTR");
            } else {
                bunle.putString("fileName", "appsupportEN");
            }
            bunle.putString("titleString", getResources().getString(R.string.TXT_ABOUT_MAIN_APP_SUPPORT));
        }

        aboutDetailFragment.setArguments(bunle);
        ((AboutActivity) getActivity()).loadFragment(R.id.container, aboutDetailFragment, true);

        // throw new RuntimeException("This is a crash");
    }
}
