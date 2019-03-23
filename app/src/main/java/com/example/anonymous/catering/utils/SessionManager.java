package com.example.anonymous.catering.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;

public class SessionManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context _context;

    public static final String IS_LOGGED_IN = "isLoggedIn";
    public static final String ID_DRIVER = "id_driver";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String NAMA = "nama";
    public static final String NO_HP = "no_hp";
    public static final String JK = "jk";
    public static final String ALAMAT = "alamat";

    public Context get_context(){
        return _context;
    }

    //constructor
    public SessionManager(Context context){
        this._context       = context;
        sharedPreferences   = PreferenceManager.getDefaultSharedPreferences(context);
        editor              = sharedPreferences.edit();
    }

    //session untuk member yang login (catering)
    public void createLoginSessionDriver(Integer id_driver, String username, String email,
                                         String password, String nama,
                                         String no_hp, String jk,
                                         String alamat){
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putInt(ID_DRIVER, id_driver);
        editor.putString(USERNAME, username);
        editor.putString(EMAIL, email);
        editor.putString(PASSWORD, password);
        editor.putString(NAMA, nama);
        editor.putString(NO_HP, no_hp);
        editor.putString(JK, jk);
        editor.putString(ALAMAT, alamat);
        editor.apply();
    }

    public HashMap<String, String> getDriverProfile(){
        HashMap<String,String> driver = new HashMap<>();
        driver.put(ID_DRIVER, String.valueOf(sharedPreferences.getInt(ID_DRIVER,0)));
        driver.put(USERNAME, sharedPreferences.getString(USERNAME,null));
        driver.put(EMAIL, sharedPreferences.getString(EMAIL,null));
        driver.put(PASSWORD, sharedPreferences.getString(PASSWORD,null));
        driver.put(NAMA, sharedPreferences.getString(NAMA,null));
        driver.put(NO_HP, sharedPreferences.getString(NO_HP,null));
        driver.put(JK, sharedPreferences.getString(JK,null));
        driver.put(ALAMAT, sharedPreferences.getString(ALAMAT,null));
        return driver;
    }

    public void logoutDriver(){
        editor.clear();
        editor.commit();
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

}
