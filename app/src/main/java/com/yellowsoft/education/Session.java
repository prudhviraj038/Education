package com.yellowsoft.education;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.security.spec.ECPublicKeySpec;

/**
 * Created by Chinni on 14-03-2016.
 */
public class Session {
    public static final String SERVERURL = "http://www.clients.yellowsoft.in/education/api/";
    public static final String USERID = "education_id";
    public static final String NAME = "education_name";
    public static final String userdetails = "userdetails";

    public static void setUserid(Context context, String member_id, String name) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERID, member_id);
        editor.putString(NAME, name);
        editor.commit();
    }

    public static String getUserid(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(USERID, "-1");

    }

    public static void setUserdetails(Context context, String name) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(userdetails, name);
        editor.commit();
    }

    public static String getUserdetails(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(userdetails, "-1");

    }


}