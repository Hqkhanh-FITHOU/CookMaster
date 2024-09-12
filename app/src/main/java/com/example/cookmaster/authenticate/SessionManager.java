package com.example.cookmaster.authenticate;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    public static String TAG = SessionManager.class.getName();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    private final String SESSION_NAME = "cookmaster";
    private final String KEY_LOGIN = "isLogin";

    private final String EMAIL = "email";
    private final String PASSWORD = "password";

    public SessionManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SESSION_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveLoginInfo(String email, String password){
        editor.putString(EMAIL, email);
        editor.putString(PASSWORD, password);
        editor.commit();
    }

    public String getEmail(){
        return sharedPreferences.getString(EMAIL, null);
    }

    public String getPassword(){
        return sharedPreferences.getString(PASSWORD, null);
    }

    public void setLoginState(boolean isLogin){
        editor.putBoolean(KEY_LOGIN, isLogin);
        editor.commit();
    }

    public boolean isLogin(){
        return sharedPreferences.getBoolean(KEY_LOGIN, false);
    }

}
