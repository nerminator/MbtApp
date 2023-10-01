package com.daimler.biziz.android.ui.login;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.base.BaseActivity;
import com.daimler.biziz.android.base.BaseFragment;
import com.daimler.biziz.android.databinding.FragmentLoginBinding;
import com.daimler.biziz.android.ui.about.AboutActivity;
import com.daimler.biziz.android.ui.dialogs.BizizProgressDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment {
    //region Instance
    private VMLogin vmLogin;
    private BizizProgressDialog progressDialog;
    private FragmentLoginBinding binding;
    //endregion

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        vmLogin = ViewModelProviders.of(this).get(VMLogin.class);
        binding.setVm(vmLogin);
        binding.setLifecycleOwner(this);
        binding.imageView.setOnClickListener(this::openInfo);
        observe();
        edtTxtDoneBtn();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void openInfo(View view) {
        AboutActivity.start(getContext());
    }

    private void observe() {
        vmLogin.isLoading.observe(this, (Boolean aBoolean) -> {
            if (aBoolean) {
                showProgressDialog();
            } else {
                dismissProgressDialog();
            }
        });

        vmLogin.phone.observe(this, s -> {
            boolean isEnabled = s.length() > 12 && !s.contains("X");
            vmLogin.isButtonEnabled.postValue(isEnabled);
            // TODO: 5.05.2018  binding.edtPhone.getRawText() en bastaki 5 rakamini vermiyor.Simdilik elle ekledim(Tr de 5 ile baslamayan yok sanirim)
            vmLogin.phoneValue.postValue("5"+binding.edtPhone.getRawText());
        });

        vmLogin.showErrorMessage.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                showMessageDialog(null,s);
            }
        });

        vmLogin.loginSuccessed.observe(this, aBoolean -> ((BaseActivity) getActivity()).loadFragment(R.id.container, SmsFragment.newInstance(vmLogin.phoneValue.getValue()), true));
    }

    private void edtTxtDoneBtn(){
        binding.edtPhone.setImeOptions(EditorInfo.IME_ACTION_DONE);
        binding.edtPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId== EditorInfo.IME_ACTION_NEXT)) {
                    binding.btnLogin.performClick();
                }
                return false;
            }
        });
    }
}
