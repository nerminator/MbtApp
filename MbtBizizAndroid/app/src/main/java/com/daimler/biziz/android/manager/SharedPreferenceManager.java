package com.daimler.biziz.android.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.daimler.biziz.android.ui.main.main.MainActivity;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;


public class SharedPreferenceManager {

    public static final String OLD_SHARED_PREF_FILE = "turkcell.saha";
    public static final String ENCRPTED_SHARED_PREF_FILE = "Wzoy7s.abc";

    public static final String KEY_REFRESH_TOKEN = "KEY_REFRESH_TOKEN";
    public static final String KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN";
    public static final String KEY_REMEMBER_USERNAME = "KEY_REMEMBER_USERNAME";
    public static final String KEY_REMEMBER_ME = "KEY_REMEMBER_ME";
    private final static String KEY_IS_READED_TERMS = "KEY_IS_READED_TERMS";
    public final static String KEY_IS_LOGIN = "KEY_IS_LOGIN";
    public final static String RECENT_SEARCH = "RECENT_SEARCH";
    private static final String KEY_IS_SHOWED_NOTIFICATION_POPUP = "KEY_IS_SHOWED_NOTIFICATION_POPUP" ;

    private SharedPreferences oldSharePref;
    private SharedPreferences encryptedSharePref;

    private Editor editor;
    private MasterKey masterKey;

    @SuppressLint("CommitPrefEdits")
    public SharedPreferenceManager(Context context) throws GeneralSecurityException, IOException {
        masterKey  = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        encryptedSharePref = EncryptedSharedPreferences.create(
                context.getApplicationContext(),
                ENCRPTED_SHARED_PREF_FILE,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );

        oldSharePref = context.getSharedPreferences(OLD_SHARED_PREF_FILE, 0);
        // Check if the new ESP has been initialised
        if (!oldSharePref.getAll().isEmpty()) {
            // Copy each item into the ESP
            copyTo(encryptedSharePref, oldSharePref);
        }


        editor = encryptedSharePref.edit();
    }

    private void copyTo(SharedPreferences dest, SharedPreferences source) {
        SharedPreferences.Editor destEd = dest.edit();
        SharedPreferences.Editor sourceEd = source.edit();
        destEd.clear();


        for(Map.Entry<String,?> entry : source.getAll().entrySet()){
            Object v = entry.getValue();
            String key = entry.getKey();

            if(v instanceof Boolean)
                destEd.putBoolean(key, ((Boolean)v).booleanValue());
            else if(v instanceof Float)
                destEd.putFloat(key, ((Float)v).floatValue());
            else if(v instanceof Integer)
                destEd.putInt(key, ((Integer)v).intValue());
            else if(v instanceof Long)
                destEd.putLong(key, ((Long)v).longValue());
            else if(v instanceof String)
                destEd.putString(key, ((String)v));
        }
        destEd.commit(); //save it.
        sourceEd.clear(); //clear the old one
    }

    public void setAccessToken(String accessToken) {
        try {
            editor.putString(KEY_ACCESS_TOKEN, accessToken);
            editor.commit();
        } catch (Exception ignored) {
        }
    }
    public void logout() {
        try {
            editor.putString(KEY_IS_LOGIN, "0");
            editor.putString(KEY_ACCESS_TOKEN, "");
            editor.apply();
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

        return encryptedSharePref.getString(KEY_REFRESH_TOKEN, "");

    }

    public String getAccesToken() {
        return encryptedSharePref.getString(KEY_ACCESS_TOKEN, "");

    }

    public String getRememberUserName(){
        return encryptedSharePref.getString(KEY_REMEMBER_USERNAME,"");
    }

    public void setRememberUserName(String userName){
        editor.putString(KEY_REMEMBER_USERNAME,userName);
        editor.commit();
    }


    public boolean getRememberMe(){
        return encryptedSharePref.getBoolean(KEY_REMEMBER_ME,false);
    }

    public void setRememberMe(boolean value){
        editor.putBoolean(KEY_REMEMBER_ME,value);
        editor.commit();
    }


    public Boolean isReadedTerms() {
        String isLogin = encryptedSharePref.getString(KEY_IS_READED_TERMS, "");
        return isLogin.equals("1") ? true : false;
    }

    public void setReadedTerms(String isReadedTerms) {
        editor.putString(this.KEY_IS_READED_TERMS, isReadedTerms);
        editor.apply();
    }

    public Boolean isShowedNotificationPopup() {
        String value = encryptedSharePref.getString(KEY_IS_SHOWED_NOTIFICATION_POPUP, "");
        return value.equals("1") ? true : false;
    }

    public void setShowedNotificationPopup(String value) {
        editor.putString(this.KEY_IS_SHOWED_NOTIFICATION_POPUP, value);
        editor.apply();
    }


    public Boolean isLogin() {
        String isLogin = encryptedSharePref.getString(KEY_IS_LOGIN, "");
        return isLogin.equals("1") ? true : false;
    }

    public void setisLogin(String isLogin) {
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
        recentSearch = encryptedSharePref.getString(RECENT_SEARCH, "");
        return recentSearch;
    }
}
