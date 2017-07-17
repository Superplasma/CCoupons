package com.example.administrator.ccoupons.Tools;

import android.content.SharedPreferences;

/**
 * Created by CZJ on 2017/7/16.
 */

public class LoginInformationManager {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public LoginInformationManager(SharedPreferences p) {
        preferences = p;
        editor = preferences.edit();
    }

    public boolean getAutoLogin() {
        return preferences.getBoolean("auto_login", false);
    }

    public String getPhoneNumber() {
        return preferences.getString("phone_number", "");
    }

    public String getPassword() {
        return preferences.getString("password", "");
    }

    public LoginInformationManager setAutoLogin(boolean b) {
        editor.putBoolean("auto_login", b).commit();
        return this;
    }

    public LoginInformationManager setPhoneNumber(String str) {
        editor.putString("phone_number", str).commit();
        return this;
    }

    public LoginInformationManager setPassword(String str) {
        editor.putString("password", str).commit();
        return this;
    }

    public LoginInformationManager clear() {
        editor.clear().commit();
        return this;
    }

    public LoginInformationManager removePhoneNumber(){
        editor.remove("phone_number").commit();
        return this;
    }

    public LoginInformationManager removePassword(){
        editor.remove("password").commit();
        return this;
    }
}
