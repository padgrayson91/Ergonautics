package com.ergonautics.ergonautics.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ergonautics.ergonautics.models.JsonModelHelper;
import com.ergonautics.ergonautics.models.Task;

import org.json.JSONException;

/**
 * Created by patrickgrayson on 8/18/16.
 * Wrapper for Android SharedPreferences
 */
public class LocalStorage {
    private static final String TAG = "ERGONAUT-STORE";
    private static LocalStorage sInstance;
    private static SharedPreferences mPrefs;

    public static LocalStorage getInstance(Context c){
        if(c == null){
            return null;
        }
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
    private static final String PROPERTY_TASK_IN_PROGRESS = "task_in_progress";

    //Getters

    /**
     *
     * @return the current session token or the empty string if none is found
     */
    public String getSessionToken(){
        return mPrefs.getString(PROPERTY_SESSION_TOKEN, "");
    }

    /**
     *
     * @return a Task object that was most recently being constructed
     */
    public Task getTaskInProgress() {
        Task inProgress = new Task("");
        String taskJSON = mPrefs.getString(PROPERTY_TASK_IN_PROGRESS, null);
        if(taskJSON != null){
            try {
                inProgress = JsonModelHelper.getTaskFromJson(taskJSON);
            } catch (JSONException e) {
                Log.e(TAG, "getTaskInProgress: Malformed JSON in Local Storage! " + taskJSON);
            }
        }
        return inProgress;
    }

    //Setters

    /**
     * Update the session token
     * @param token the token returned by the API
     */
    public void setSessionToken(String token){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PROPERTY_SESSION_TOKEN, token);
        editor.apply();
    }

    /**
     *
     * @param inProgress the task to store
     */
    public void setTaskInProgress(Task inProgress){
        SharedPreferences.Editor editor = mPrefs.edit();
        if(inProgress == null){
            editor.putString(PROPERTY_TASK_IN_PROGRESS, null);
        } else {
            try {
                String taskJSON = JsonModelHelper.getTaskAsJson(inProgress);
                editor.putString(PROPERTY_TASK_IN_PROGRESS, taskJSON);
            } catch (JSONException e) {
                Log.e(TAG, "setTaskInProgress: Unable to get JSON from Task!");
                editor.putString(PROPERTY_TASK_IN_PROGRESS, null);
            }
        }
        editor.commit();
    }
}
