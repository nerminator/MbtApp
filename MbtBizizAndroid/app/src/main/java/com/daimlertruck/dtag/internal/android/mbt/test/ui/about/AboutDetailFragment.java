package com.daimlertruck.dtag.internal.android.mbt.test.ui.about;


import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseFragment;
import com.daimlertruck.dtag.internal.android.mbt.test.databinding.FragmentAboutDetailBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.manager.SharedPreferenceManager;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.toolbar.VMToolbar;

import java.util.Locale;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutDetailFragment extends BaseFragment {

    private FragmentAboutDetailBinding binding;
    private String fileName;
    private String titleString;

    public AboutDetailFragment() {
        // Required empty public constructor
    }

    @Inject
    SharedPreferenceManager sharedPreferenceManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about_detail, container, false);
        InitView();
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseApplication) getActivity().getApplication()).getComponent().inject(this);
    }

    private void InitView() {
        getBundle();
        setToolbar();
    }

    private void setToolbar() {
        VMToolbar vmToolbar = VMToolbar.createVMToolbarFor(this, titleString, null);
        binding.toolbarAboutDetail.setVm(vmToolbar);
    }

    @Override
    public void onResume() {
        super.onResume();
        setWebView();
    }

    private void setWebView() {
        if (isAppDescription()) {
            binding.webViewAbout.setVisibility(View.GONE);
            binding.textViewAbout.setVisibility(View.VISIBLE);
            binding.textViewAbout.setText(sharedPreferenceManager.getAppDescriptionText());
        } else {
            binding.webViewAbout.setVisibility(View.VISIBLE);
            binding.textViewAbout.setVisibility(View.GONE);
            binding.webViewAbout.loadUrl("file:///android_asset/" + fileName + ".html");
        }
        binding.webViewAbout.getSettings();
        binding.webViewAbout.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));


        binding.webViewAbout.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (!url.contains("http") && !url.startsWith("mailto")) {
                    openDetailPage(url);
                } else if (url.startsWith("mailto")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.setPackage("com.daimlertruck.dtag.internal.android.mbt.test");
                    view.getContext().startActivity(intent);
                    // Return true means, leave the current web view and handle the url itself
                    return true;
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.setPackage("com.daimlertruck.dtag.internal.android.mbt.test");
                    startActivity(intent);
                }
                return super.shouldOverrideUrlLoading(view, request);
            }


        });
    }

    private void getBundle() {
        Bundle bundle = getArguments();
        fileName = bundle.getString("fileName", "");
        titleString = bundle.getString("titleString", "");
    }

    private Boolean isAppDescription() {
        return fileName.equals("appdescriptionTR") || fileName.equals("appdescriptionEN");
    }

    private void openDetailPage(String url) {
        AboutDetailFragment aboutDetailFragment = new AboutDetailFragment();
        Bundle bunle = new Bundle();
        switch (url) {
            //Uygulama Açıklaması
            case "event:APP_DESCRIPTION_TR":
                if (Locale.getDefault().getLanguage().equals("tr")) {
                    bunle.putString("fileName", "appdescriptionTR");
                } else {
                    bunle.putString("fileName", "appdescriptionEN");
                }
                bunle.putString("titleString", getResources().getString(R.string.TXT_ABOUT_MAIN_APP_DESCRIPTION));
                break;
            //Uygulama Açıklaması
            case "event:APP_DESCRIPTION_EN":
                if (Locale.getDefault().getLanguage().equals("tr")) {
                    bunle.putString("fileName", "appdescriptionTR");
                } else {
                    bunle.putString("fileName", "appdescriptionEN");
                }
                bunle.putString("titleString", getResources().getString(R.string.TXT_ABOUT_MAIN_APP_DESCRIPTION));
                break;
            //Uygulama Açıklaması
            case "event:APP_DESCRIPTION":
                if (Locale.getDefault().getLanguage().equals("tr")) {
                    bunle.putString("fileName", "appdescriptionTR");
                } else {
                    bunle.putString("fileName", "appdescriptionEN");
                }
                bunle.putString("titleString", getResources().getString(R.string.TXT_ABOUT_MAIN_APP_DESCRIPTION));
                break;
            //Kullanım Koşulları
            case "":
                if (Locale.getDefault().getLanguage().equals("tr")) {
                    bunle.putString("fileName", "termsofuseaboutTR");
                } else {
                    bunle.putString("fileName", "termsofuseaboutEN");
                }
                //bunle.putString("bodyString", getResources().getString(R.string.TXT_ABOUT_TERMS_OF_USE_BODY));
                bunle.putString("titleString", getResources().getString(R.string.TXT_ABOUT_MAIN_TERMS_OF_USE));
                break;
            //Veri koruması
           /* case "":
                if (Locale.getDefault().getLanguage().equals("tr")) {
                    bunle.putString("fileName", "dataprotectionTR");
                } else {
                    bunle.putString("fileName", "dataprotectionEN");
                }
                //bunle.putString("bodyString", getResources().getString(R.string.TXT_ABOUT_DATA_PROTECTION_BODY));
                bunle.putString("titleString", getResources().getString(R.string.TXT_ABOUT_MAIN_DATA_PROTECTION));
                break;*/
            //Ücretsiz veya Açık Kaynaklı Yazılum
            case "event:FOSS":
                if (Locale.getDefault().getLanguage().equals("tr")) {
                    bunle.putString("fileName", "fossTR");
                } else {
                    bunle.putString("fileName", "fossEN");
                }
                bunle.putString("titleString", getResources().getString(R.string.TXT_ABOUT_MAIN_FOSS));
                break;
            //Üçüncü taraf içeriği
         /*   case "":
                if (Locale.getDefault().getLanguage().equals("tr")) {
                    bunle.putString("fileName", "fossTR");
                } else {
                    bunle.putString("fileName", "fossEN");
                }
                bunle.putString("titleString", getResources().getString(R.string.TXT_ABOUT_MAIN_3RD_PARTY_CONTENT));

                break;*/
            //Yasal Uyarılar
            case "event:THIRDPARTY":
                if (Locale.getDefault().getLanguage().equals("tr")) {
                    bunle.putString("fileName", "thirdpartyTR");
                } else {
                    bunle.putString("fileName", "thirdpartyEN");
                }
                bunle.putString("titleString", getResources().getString(R.string.TXT_ABOUT_MAIN_LEGAL_NOTICES));
                break;
            //Uygulama Desteği
            case "event:APPSUPPORT_TR":
                if (Locale.getDefault().getLanguage().equals("tr")) {
                    bunle.putString("fileName", "appsupportTR");
                } else {
                    bunle.putString("fileName", "appsupportEN");
                }
                bunle.putString("titleString", getResources().getString(R.string.TXT_ABOUT_MAIN_APP_SUPPORT));
                break;

            //Uygulama Desteği
            case "event:APPSUPPORT_EN":
                if (Locale.getDefault().getLanguage().equals("tr")) {
                    bunle.putString("fileName", "appsupportTR");
                } else {
                    bunle.putString("fileName", "appsupportEN");
                }
                bunle.putString("titleString", getResources().getString(R.string.TXT_ABOUT_MAIN_APP_SUPPORT));
                break;

            case "event:APPSUPPORT":
                if (Locale.getDefault().getLanguage().equals("tr")) {
                    bunle.putString("fileName", "appsupportTR");
                } else {
                    bunle.putString("fileName", "appsupportEN");
                }
                bunle.putString("titleString", getResources().getString(R.string.TXT_ABOUT_MAIN_APP_SUPPORT));
                break;
            default:
                break;
        }
        aboutDetailFragment.setArguments(bunle);
        ((com.daimlertruck.dtag.internal.android.mbt.test.ui.about.AboutActivity) getActivity()).loadFragment(R.id.container, aboutDetailFragment, true);

    }


}
