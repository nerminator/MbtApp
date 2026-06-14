package com.daimlertruck.dtag.internal.android.mbt.test.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;


public class SharedPreferenceManager {

    public static final String BIZIZ_PREF = "MBT_PREF";
    private static final String SECURE_BIZIZ_PREF = "MBT_SECURE_PREF";
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

    private final SharedPreferences preferences;
    private final Editor editor;
    private SharedPreferences securePreferences;
    private Editor secureEditor;
    private String inMemoryAccessToken = "";
    private String inMemoryRefreshToken = "";

    @SuppressLint("CommitPrefEdits")
    public SharedPreferenceManager(Context context) {
        preferences = context.getSharedPreferences(BIZIZ_PREF, 0);
        editor = preferences.edit();
        initializeSecurePreferences(context);
        migrateLegacySensitiveValues();
    }

    private void initializeSecurePreferences(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            securePreferences = EncryptedSharedPreferences.create(
                    context,
                    SECURE_BIZIZ_PREF,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            secureEditor = securePreferences.edit();
        } catch (Exception exception) {
            Log.e("SharedPreferenceManager", "Encrypted token storage unavailable. Falling back to in-memory token storage.", exception);
            securePreferences = null;
            secureEditor = null;
        }
    }

    private void migrateLegacySensitiveValues() {
        String legacyAccessToken = preferences.getString(KEY_ACCESS_TOKEN, "");
        String legacyRefreshToken = preferences.getString(KEY_REFRESH_TOKEN, "");

        if (!legacyAccessToken.isEmpty() && getAccesToken().isEmpty()) {
            saveSecureString(KEY_ACCESS_TOKEN, legacyAccessToken);
        }

        if (!legacyRefreshToken.isEmpty() && getRefreshToken().isEmpty()) {
            saveSecureString(KEY_REFRESH_TOKEN, legacyRefreshToken);
        }

        if (!legacyAccessToken.isEmpty() || !legacyRefreshToken.isEmpty()) {
            editor.remove(KEY_ACCESS_TOKEN);
            editor.remove(KEY_REFRESH_TOKEN);
            editor.commit();
        }
    }

    private void saveSecureString(String key, String value) {
        String sanitizedValue = value == null ? "" : value;
        if (KEY_ACCESS_TOKEN.equals(key)) {
            inMemoryAccessToken = sanitizedValue;
        } else if (KEY_REFRESH_TOKEN.equals(key)) {
            inMemoryRefreshToken = sanitizedValue;
        }

        if (secureEditor != null) {
            secureEditor.putString(key, sanitizedValue);
            secureEditor.commit();
        }
    }

    private String getSecureString(String key) {
        if (securePreferences != null) {
            return securePreferences.getString(key, "");
        }

        if (KEY_ACCESS_TOKEN.equals(key)) {
            return inMemoryAccessToken;
        }

        if (KEY_REFRESH_TOKEN.equals(key)) {
            return inMemoryRefreshToken;
        }

        return "";
    }

    private void clearSecureTokens() {
        inMemoryAccessToken = "";
        inMemoryRefreshToken = "";

        if (secureEditor != null) {
            secureEditor.remove(KEY_ACCESS_TOKEN);
            secureEditor.remove(KEY_REFRESH_TOKEN);
            secureEditor.commit();
        }
    }

    public void setAccessToken(String accessToken) {
        try {
            saveSecureString(KEY_ACCESS_TOKEN, accessToken);
            editor.remove(KEY_ACCESS_TOKEN);
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
            clearSecureTokens();

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
            editor.commit();
            clearSecureTokens();
        } catch (Exception ignored) {
        }
    }

    public void setRefreshToken(String refreshToken) {
        try {
            saveSecureString(KEY_REFRESH_TOKEN, refreshToken);
            editor.remove(KEY_REFRESH_TOKEN);
            editor.commit();
        } catch (Exception ignored) {
        }
    }


    public String getRefreshToken() {

        return getSecureString(KEY_REFRESH_TOKEN);

    }

    public String getAccesToken() {
        return getSecureString(KEY_ACCESS_TOKEN);

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
