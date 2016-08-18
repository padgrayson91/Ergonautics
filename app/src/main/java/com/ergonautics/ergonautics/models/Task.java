package com.ergonautics.ergonautics.models;

/**
 * Created by patrickgrayson on 8/18/16.
 * Model class for user tasks
 */
public class Task {
    private String mDisplayName;
    private String mTaskId;

    public Task(String displayName){
        mDisplayName = displayName;
    }

    public String getDisplayName(){
        return mDisplayName;
    }

    public String getTaskId(){
        return mTaskId;
    }

    public void setTaskId(String id){
        mTaskId = id;
    }


}
