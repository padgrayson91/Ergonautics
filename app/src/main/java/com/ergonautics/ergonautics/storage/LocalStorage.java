package com.ergonautics.ergonautics.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by patrickgrayson on 8/18/16.
 * Wrapper for Android SharedPreferences
 */
public class LocalStorage {
    private static final String TAG = "ERGONAUT-STORE";
    private static LocalStorage sInstance;
    private static SharedPreferences mPrefs;

    public static LocalStorage getInstance(Context c){
        if(sInstance == null){
            sInstance = new LocalStorage(c);
            Log.d(TAG, "getInstance: created new storage instance " + sInstance);
        }
        return sInstance;
    }

    private LocalStorage(Context c){
        mPrefs = c.getSharedPreferences(LocalStorage.class.getCanonicalName(), Context.MODE_PRIVATE);
    }

    //Keys for SharedPrefs
    private static final String PROPERTY_SESSION_TOKEN = "session_token";

    //Getters

    /**
     *
     * @return the current session token or the empty string if none is found
     */
    public String getSessionToken(){
        return mPrefs.getString(PROPERTY_SESSION_TOKEN, "");
    }

    //Setters

    /**
     * Update the session token
     * @param token the token returned by the API
     */
    public void setSessionToken(String token){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PROPERTY_SESSION_TOKEN, token);
        editor.commit();
    }
}
