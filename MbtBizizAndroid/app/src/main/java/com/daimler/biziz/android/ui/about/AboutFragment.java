package com.daimler.biziz.android.ui.about;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.base.BaseFragment;
import com.daimler.biziz.android.databinding.FragmentAboutBinding;
import com.daimler.biziz.android.ui.toolbar.VMToolbar;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends BaseFragment implements View.OnClickListener {

    private FragmentAboutBinding binding;

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
        InitView();
        return binding.getRoot();
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
    }

    @Override
    public void onClick(View v) {
        AboutDetailFragment aboutDetailFragment = new AboutDetailFragment();
        Bundle bunle = new Bundle();
        switch (v.getId()) {
            //Uygulama Açıklaması
            case R.id.relAppDescriptionAbout:
                if (Locale.getDefault().getLanguage().equals("tr")) {
                    bunle.putString("fileName", "appdescriptionTR");
                } else {
                    bunle.putString("fileName", "appdescriptionEN");
                }
                bunle.putString("titleString", getResources().getString(R.string.TXT_ABOUT_MAIN_APP_DESCRIPTION));
                break;
            //Kullanım Koşulları
            case R.id.relTermsOfUseAbout:
                if (Locale.getDefault().getLanguage().equals("tr")) {
                    bunle.putString("fileName", "termsofuseaboutTR");
                } else {
                    bunle.putString("fileName", "termsofuseaboutEN");
                }
                //bunle.putString("bodyString", getResources().getString(R.string.TXT_ABOUT_TERMS_OF_USE_BODY));
                bunle.putString("titleString", getResources().getString(R.string.TXT_ABOUT_MAIN_TERMS_OF_USE));
                break;
            //Veri koruması
            case R.id.relDataProtectionAbout:
                if (Locale.getDefault().getLanguage().equals("tr")) {
                    bunle.putString("fileName", "dataprotectionTR");
                } else {
                    bunle.putString("fileName", "dataprotectionEN");
                }
                //bunle.putString("bodyString", getResources().getString(R.string.TXT_ABOUT_DATA_PROTECTION_BODY));
                bunle.putString("titleString", getResources().getString(R.string.TXT_ABOUT_MAIN_DATA_PROTECTION));
                break;
            //Ücretsiz veya Açık Kaynaklı Yazılum
            case R.id.relOpenSourceAbout:
                if (Locale.getDefault().getLanguage().equals("tr")) {
                    bunle.putString("fileName", "fossTR");
                } else {
                    bunle.putString("fileName", "fossEN");
                }
                bunle.putString("titleString", getResources().getString(R.string.TXT_ABOUT_MAIN_FOSS));

                break;
            //Üçüncü taraf içeriği
            case R.id.relThirdPartContentAbout:
                if (Locale.getDefault().getLanguage().equals("tr")) {
                    bunle.putString("fileName", "thirdpartyTR");
                } else {
                    bunle.putString("fileName", "thirdpartyEN");
                }
                bunle.putString("titleString", getResources().getString(R.string.TXT_ABOUT_MAIN_3RD_PARTY_CONTENT));

                break;
            //Yasal Uyarılar
            case R.id.relLegalNoticesAbout:
                if (Locale.getDefault().getLanguage().equals("tr")) {
                    bunle.putString("fileName", "legalTR");
                } else {
                    bunle.putString("fileName", "legalEN");
                }
                bunle.putString("titleString", getResources().getString(R.string.TXT_ABOUT_MAIN_LEGAL_NOTICES));

                break;
            //Uygulama Desteği
            case R.id.relAppSupportAbout:
                if (Locale.getDefault().getLanguage().equals("tr")) {
                    bunle.putString("fileName", "appsupportTR");
                } else {
                    bunle.putString("fileName", "appsupportEN");
                }
                bunle.putString("titleString", getResources().getString(R.string.TXT_ABOUT_MAIN_APP_SUPPORT));
                break;
        }
        aboutDetailFragment.setArguments(bunle);
        ((AboutActivity) getActivity()).loadFragment(R.id.container,aboutDetailFragment,true);

    }
}
