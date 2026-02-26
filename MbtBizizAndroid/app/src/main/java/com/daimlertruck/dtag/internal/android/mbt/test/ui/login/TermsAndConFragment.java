package com.daimlertruck.dtag.internal.android.mbt.test.ui.login;


import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.databinding.FragmentTermsandconBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.manager.SharedPreferenceManager;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.about.AboutDetailFragment;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.dialogs.MessageDialog;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class TermsAndConFragment extends Fragment {

    @Inject
    SharedPreferenceManager sharedPreferenceManager;

    private FragmentTermsandconBinding binding;
    private boolean haveSeenEndOfTerms = false;

    public static TermsAndConFragment newInstance() {
        TermsAndConFragment fragment = new TermsAndConFragment();
        return fragment;
    }

    public TermsAndConFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_termsandcon, container, false);
        ((BaseApplication) getActivity().getApplication()).getComponent().inject(this);
        InitView();
        return binding.getRoot();
    }

    private void InitView() {
        setWebView();
        setListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (haveSeenEndOfTerms) {
            binding.btnAgreeTerms.setEnabled(haveSeenEndOfTerms);
            binding.btnRejectTerms.setEnabled(haveSeenEndOfTerms);
        }
    }

    private void setWebView() {
        String str = "";

        InputStream is = null;
        String fileName;
        if (Locale.getDefault().getLanguage().equals("tr")) {
            fileName = "termsofuseTR.html";
        } else {
            fileName = "termsofuseEN.html";
        }
        try {
            is = getContext().getAssets().open(fileName);
            int size = is.available();

            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            str = new String(buffer);
        } catch (IOException e) {
            closeStreams(is);
            e.printStackTrace();
        }

        setTextViewHTML(binding.tvDescTerms, str);
        binding.tvDescTerms.setLinkTextColor(ContextCompat.getColor(getContext(), (R.color.gradientWhite)));


    }

    private void setListener() {

        binding.btnAgreeTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferenceManager.setReadedTerms("1");
                ((LoginActivity) getActivity()).loadFragment(R.id.container, LoginFragment.newInstance(), true);
            }
        });

        binding.btnRejectTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageDialog messageDialog = MessageDialog.newInstance(
                        getString(R.string.TXT_LOGIN_TERMS_WELCOME_REJECT_ALERT_TITLE),
                        getString(R.string.TXT_LOGIN_TERMS_WELCOME_REJECT_ALERT_TEXT)
                );
                messageDialog.show(getChildFragmentManager(), "");

                /*new MaterialDialog.Builder(getContext())
                        .title(getString(R.string.TXT_LOGIN_TERMS_WELCOME_REJECT_ALERT_TITLE))
                        .titleColorRes(Color.WHITE)
                        .content(getString(R.string.TXT_LOGIN_TERMS_WELCOME_REJECT_ALERT_TEXT))
                        .contentColor(ContextCompat.getColor(getContext(), android.R.color.white))
                        .backgroundColor(ContextCompat.getColor(getContext(), R.color.appBackground))
                        .positiveText(android.R.string.ok)
                        .positiveColor(ContextCompat.getColor(getContext(), R.color.buttonBackgroundColor))
                        .cancelable(true)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();*/
            }
        });

        binding.scrollViewTerms.setScrollViewListener((scrollView, x, y, oldx, oldy) -> {
            View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
            int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

            // if diff is zero, then the bottom has been reached
            if (diff == 0) {
                // do stuff
                binding.btnAgreeTerms.setEnabled(true);
                binding.btnRejectTerms.setEnabled(true);
                haveSeenEndOfTerms = true;
            }
        });


    }

    protected void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span) {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                // Do something with span.getURL() to handle the link click...
                //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(span.getURL()));
                //startActivity(browserIntent);
                openDetailPage(span.getURL());
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
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
            case "event:DATA_PROTECTION":
                if (Locale.getDefault().getLanguage().equals("tr")) {
                    bunle.putString("fileName", "dataprotectionTR");
                } else {
                    bunle.putString("fileName", "dataprotectionEN");
                }
                //bunle.putString("bodyString", getResources().getString(R.string.TXT_ABOUT_DATA_PROTECTION_BODY));
                bunle.putString("titleString", getResources().getString(R.string.TXT_ABOUT_MAIN_DATA_PROTECTION));
                break;
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
            case "event:APPSUPPORT":
                if (Locale.getDefault().getLanguage().equals("tr")) {
                    bunle.putString("fileName", "appsupportTR");
                } else {
                    bunle.putString("fileName", "appsupportEN");
                }
                bunle.putString("titleString", getResources().getString(R.string.TXT_ABOUT_MAIN_APP_SUPPORT));
                break;
        }
        aboutDetailFragment.setArguments(bunle);
        ((LoginActivity) getActivity()).loadFragment(R.id.container, aboutDetailFragment, true);
    }

    protected void setTextViewHTML(TextView text, String html) {
        CharSequence sequence = Html.fromHtml(html);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for (URLSpan span : urls) {
            makeLinkClickable(strBuilder, span);
        }
        text.setText(strBuilder);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }


    public static void closeStreams(Closeable c) {
        if (c == null) return;
        try {
            c.close();
        } catch (IOException e) {
            //log the exception
        }
    }


}
