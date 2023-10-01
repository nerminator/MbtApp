package com.daimler.biziz.android.base;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.ui.dialogs.BizizProgressDialog;
import com.daimler.biziz.android.ui.dialogs.MessageDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {


    private BizizProgressDialog progressDialog;

    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_base, container, false);
    }

    public void loadFragment(int contanerId, Fragment fragment, boolean addToBackStack) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (addToBackStack) {
            ft.addToBackStack("");
        }
        ft.replace(contanerId, fragment);
        ft.commit();
    }

    //region Progress

    public void showProgressDialog() {
        progressDialog = BizizProgressDialog.getDialog();
        progressDialog.show(getContext());
    }

    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void  showMessageDialog(String title,String content){
        if (TextUtils.isEmpty(title)){
            title = getString(R.string.TXT_COMMON_WARNING);
        }
        MessageDialog messageDialog = MessageDialog.newInstance(
                title,
                content
        );
        messageDialog.show(getChildFragmentManager(),"");
    }

    //endregion
}
