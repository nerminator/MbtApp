package com.daimlertruck.dtag.internal.android.mbt.test.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.daimlertruck.dtag.internal.android.mbt.test.BuildConfig;
import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.microsoft.identity.client.IPublicClientApplication;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.exception.MsalException;

import androidx.annotation.NonNull;


public class SharedPreferenceManager {

    public static final String BIZIZ_PREF = "MBT_PREF";
    public static final String KEY_REFRESH_TOKEN = "KEY_REFRESH_TOKEN";
    public static final String KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN";
    public static final String KEY_REMEMBER_USERNAME = "KEY_REMEMBER_USERNAME";
    public static final String KEY_REMEMBER_ME = "KEY_REMEMBER_ME";
    private final static String KEY_IS_READED_TERMS = "KEY_IS_READED_TERMS";
    public final static String KEY_IS_LOGIN = "KEY_IS_LOGIN";
    public final static String RECENT_SEARCH = "RECENT_SEARCH";
    public final static String ABOUT_TEXT = "ABOUT_TEXT";
    public final static String APP_DESCRIPTION_TEXT = "APP_DESCRIPTION_TEXT";
    private static final String KEY_IS_SHOWED_NOTIFICATION_POPUP = "KEY_IS_SHOWED_NOTIFICATION_POPUP" ;

    private SharedPreferences preferences;
    private Editor editor;

    private ISingleAccountPublicClientApplication mSingleAccountApp;

    @SuppressLint("CommitPrefEdits")
    public SharedPreferenceManager(Context context) {
        preferences = context.getSharedPreferences(BIZIZ_PREF, 0);
        editor = preferences.edit();
    }


    public void setAccessToken(String accessToken) {
        try {
            editor.putString(KEY_ACCESS_TOKEN, accessToken);
            editor.commit();
        } catch (Exception ignored) {
        }
    }

    public void fullLogout() {
        try {
            // Preserve the "read terms" flag
            String isReadedTerms = preferences.getString(KEY_IS_READED_TERMS, "");

            editor.clear();
            editor.commit();

            // Restore the terms acceptance flag
            editor.putString(KEY_IS_READED_TERMS, isReadedTerms);
            editor.commit();

            Log.i("SharedPreferenceManager", "All preferences cleared.");
        } catch (Exception e) {
            Log.e("SharedPreferenceManager", "Error clearing preferences: " + e.getMessage());
        }
    }
    public void logout() {
        try {
            editor.putString(KEY_IS_LOGIN, "0");
            editor.putString(KEY_ACCESS_TOKEN, "");
            editor.commit();
        } catch (Exception ignored) {
        }
    }

    public void setRefreshToken(String refreshToken) {
        try {
            editor.putString(KEY_REFRESH_TOKEN,refreshToken);
            editor.commit();
        } catch (Exception ignored) {
        }
    }


    public String getRefreshToken() {

        return preferences.getString(KEY_REFRESH_TOKEN, "");

    }

    public String getAccesToken() {
        return preferences.getString(KEY_ACCESS_TOKEN, "");

    }

    public String getRememberUserName(){
        return preferences.getString(KEY_REMEMBER_USERNAME,"");
    }

    public void setRememberUserName(String userName){
        editor.putString(KEY_REMEMBER_USERNAME,userName);
        editor.commit();
    }


    public boolean getRememberMe(){
        return preferences.getBoolean(KEY_REMEMBER_ME,false);
    }

    public void setRememberMe(boolean value){
        editor.putBoolean(KEY_REMEMBER_ME,value);
        editor.commit();
    }


    public Boolean isReadedTerms() {
        String isLogin = preferences.getString(KEY_IS_READED_TERMS, "");
        return isLogin.equals("1") ? true : false;
    }

    public void setReadedTerms(String isReadedTerms) {
        editor.putString(this.KEY_IS_READED_TERMS, isReadedTerms);
        editor.apply();
    }

    public Boolean isShowedNotificationPopup() {
        String value = preferences.getString(KEY_IS_SHOWED_NOTIFICATION_POPUP, "");
        return value.equals("1") ? true : false;
    }

    public void setShowedNotificationPopup(String value) {
        editor.putString(this.KEY_IS_SHOWED_NOTIFICATION_POPUP, value);
        editor.apply();
    }


    public Boolean isLogin() {
        String isLogin = preferences.getString(KEY_IS_LOGIN, "");
        return isLogin.equals("1") ? true : false;
    }

    public void setIsLogin(String isLogin) {
        editor.putString(this.KEY_IS_LOGIN, isLogin);
        editor.apply();
    }

    public void putRecentSearch(String key){
        String keyTemp;
        if (getRecentSearchs().equals("")){
            keyTemp=key;
        }
        else {
            keyTemp=getRecentSearchs()+"@"+key;
        }
        editor.putString(RECENT_SEARCH, keyTemp);
        editor.apply();
    }

    public String getRecentSearchs() {
        String recentSearch="";
        recentSearch = preferences.getString(RECENT_SEARCH, "");
        return recentSearch;
    }

    public void putAboutText(String description){
        editor.putString(ABOUT_TEXT, description);
        editor.apply();
    }

    public String getAboutText() {
        String desc="";
        desc = preferences.getString(ABOUT_TEXT, "");
        return desc;
    }

    public void putAppDescriptionText(String description){
        editor.putString(APP_DESCRIPTION_TEXT, description);
        editor.apply();
    }

    public String getAppDescriptionText() {
        String desc="";
        desc = preferences.getString(APP_DESCRIPTION_TEXT, "");
        return desc;
    }
}
