package com.praveenbhati.agrovision.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Custom Preference Manager
 *
 * @author by Bhati on 11/8/2015.
 */
public class PrefManager {
    SharedPreferences preferences;
    Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "Agrovision";

    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";

    public PrefManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }


    public void setLogin(){
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.commit();
    }


    public boolean getLogin(){
        return  preferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setUserID(String userId) {
        editor.putString(KEY_USER_ID, userId);
        editor.commit();
    }

    public String getUserID() {
        return preferences.getString(KEY_USER_ID, null);
    }

    public void setUserName(String userName) {
        editor.putString(KEY_USER_NAME, userName);
        editor.commit();
    }

    public String getUserName() {
        return preferences.getString(KEY_USER_NAME, null);
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }
}
