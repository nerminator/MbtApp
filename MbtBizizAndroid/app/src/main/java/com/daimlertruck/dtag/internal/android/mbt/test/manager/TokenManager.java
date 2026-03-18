package com.daimlertruck.dtag.internal.android.mbt.test.manager;



public class TokenManager {
    private SharedPreferenceManager sharedPreferenceManager;


    public TokenManager(SharedPreferenceManager sharedPreferenceManager) {
        this.sharedPreferenceManager = sharedPreferenceManager;
    }

    public void setAccessToken(String accessToken) {
        sharedPreferenceManager.setAccessToken(accessToken);
    }

    public void setRefreshToken(String refreshToken) {
        sharedPreferenceManager.setRefreshToken(refreshToken);
    }

    public String getRefreshToken() {
        return sharedPreferenceManager.getRefreshToken();
    }

    public String getAccesToken() {
        return sharedPreferenceManager.getAccesToken();
    }


}
