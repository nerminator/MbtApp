package com.daimler.biziz.android.base;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringDef;
import android.support.v4.content.LocalBroadcastManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Navigate {

    public static final String NAVIGATION_ACTION = "NAVIGATION.ACTION";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({action.RETURN_LOGIN, action.RETURN_DASHBOARD})
    public @interface action {
        String RETURN_LOGIN = "RETURN.LOGIN";
        String RETURN_DASHBOARD = "RETURN.DASHBOARD";
    }


    public static void to(Context context, @action String action) {
        Intent intent = new Intent(NAVIGATION_ACTION);
        intent.putExtra(NAVIGATION_ACTION, action);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

}
