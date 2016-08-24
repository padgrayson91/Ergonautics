package com.ergonautics.ergonautics.models;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by patrickgrayson on 8/18/16.
 * Model class for user tasks
 */
public class Task extends RealmObject implements ModelConstants, Serializable {
    private String displayName;
    private String taskId; //Used for the remote API: May be null if user is offline

    public Task(){
        displayName = DISPLAY_NAME_DEFAULT;
        taskId = REMOTE_ID_DEFAULT;
    }

    public Task(String name){
        displayName = name;
        taskId = REMOTE_ID_DEFAULT;
    }

    public String getDisplayName(){
        return displayName;
    }

    public String getTaskId(){
        return taskId;
    }

    public void setDisplayName(String name) { displayName = name; }

    public void setTaskId(String id){
        taskId = id;
    }



}
