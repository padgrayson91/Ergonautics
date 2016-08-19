package com.ergonautics.ergonautics.models;

/**
 * Created by patrickgrayson on 8/18/16.
 * Model class for user tasks
 */
public class Task implements ModelConstants {
    private String mDisplayName;
    private String mTaskId; //Used for the remote API: May be null if user is offline
    private int mLocalId; //Used only in the local database: All Task objects should have this once saved

    public Task(String displayName){
        mDisplayName = displayName;
        mTaskId = REMOTE_ID_DEFAULT;
        mLocalId = LOCAL_ID_DEFAULT;
    }

    public String getDisplayName(){
        return mDisplayName;
    }

    public String getTaskId(){
        return mTaskId;
    }

    public int getLocalId(){
        return mLocalId;
    }

    public void setTaskId(String id){
        mTaskId = id;
    }

    public void setLocalId(int id){
        mLocalId = id;
    }


}
