package com.ergonautics.ergonautics.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ergonautics.ergonautics.models.Board;
import com.ergonautics.ergonautics.models.Task;

import java.util.HashMap;

/**
 * Created by patrickgrayson on 8/18/16.
 * Wrapper for Android SharedPreferences
 */
public class LocalStorage {
    private static final String TAG = "ERGONAUT-STORE";
    private static LocalStorage sInstance;
    private static SharedPreferences mPrefs;
    private static HashMap<String, Object> mSessionStorage; //Kept in memory as long as user leaves the app open

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
        mSessionStorage = new HashMap<>();
    }

    //Keys for SharedPrefs and SessionStorage
    private static final String PROPERTY_SESSION_TOKEN = "session_token";
    private static final String PROPERTY_TASK_IN_PROGRESS = "task_in_progress";
    private static final String PROPERTY_SELECTED_BOARD = "selected_board";

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
     * @return a Task object that was most recently being constructed or null
     */
    public Task getTaskInProgress() {
        if(mSessionStorage.containsKey(PROPERTY_TASK_IN_PROGRESS)) {
            return (Task) mSessionStorage.get(PROPERTY_TASK_IN_PROGRESS);
        } else {
            return null;
        }
    }

    public Board getSelectedBoard() {
        if(mSessionStorage.containsKey(PROPERTY_SELECTED_BOARD)) {
            return (Board) mSessionStorage.get(PROPERTY_SELECTED_BOARD);
        } else {
            return null;
        }
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
        mSessionStorage.put(PROPERTY_TASK_IN_PROGRESS, inProgress);
    }

    /**
     * Store which board the user is currently viewing
     * @param selected the board the user wants to view
     */
    public void setSelectedBoard(Board selected){
        mSessionStorage.put(PROPERTY_SELECTED_BOARD, selected);
    }
}
