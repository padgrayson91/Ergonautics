package com.ergonautics.ergonautics.models;

import android.util.Log;

import org.json.JSONException;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by patrickgrayson on 8/18/16.
 * Model class for user tasks
 */
public class Task extends RealmObject implements ModelConstants, Serializable {
    private static final String TAG = "ERGONAUT-TASK";
    //NOTE: when adding a property, you must also add a column to DBHelper.TasksTable
    // and a key to the JsonModelHelper
    private String displayName;
    private String taskId; //Used for the remote API: May be null if user is offline
    private long createdAt;
    private long startedAt;
    private long completedAt;
    private long scheduledFor;
    private long timeEstimate; //Estimated amount of time in ms that this task will take
    private long timeElapsed;
    private long resumedAt;
    private int value;
    private int status;

    public Task(){
        displayName = DISPLAY_NAME_DEFAULT;
        taskId = REMOTE_ID_DEFAULT;
        createdAt = System.currentTimeMillis();
        startedAt = STARTED_AT_DEFAULT;
        completedAt = COMPLETED_AT_DEFAULT;
        scheduledFor = SCHEDULED_FOR_DEFAULT;
        value = VALUE_DEFAULT;
        timeEstimate = TIME_ESTIMATE_DEFAULT;
        status = STATUS_DEFUALT;
        timeElapsed = 0;
        Log.d(TAG, "Task created at " + createdAt);
    }

    public Task(String name){
        displayName = name;
        taskId = REMOTE_ID_DEFAULT;
        createdAt = System.currentTimeMillis();
        startedAt = STARTED_AT_DEFAULT;
        completedAt = COMPLETED_AT_DEFAULT;
        scheduledFor = SCHEDULED_FOR_DEFAULT;
        value = VALUE_DEFAULT;
        timeEstimate = TIME_ESTIMATE_DEFAULT;
        status = STATUS_DEFUALT;
        timeElapsed = 0;
        Log.d(TAG, "Task " + name + " created at " + createdAt);
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


    public long getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(long completedAt) {
        this.completedAt = completedAt;
    }

    public long getCreatedAt() {
        Log.d(TAG, "getCreatedAt: " + displayName + " created at " + createdAt);
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        Log.d(TAG, "setCreatedAt: timestamp for " + displayName + " updated at " + createdAt);
        this.createdAt = createdAt;
    }

    public long getScheduledFor() {
        return scheduledFor;
    }

    public void setScheduledFor(long scheduledFor) {
        this.scheduledFor = scheduledFor;
    }

    public long getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(long startedAt) {
        this.startedAt = startedAt;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public long getTimeEstimate() {
        return timeEstimate;
    }

    public void setTimeEstimate(long timeEstimate) {
        this.timeEstimate = timeEstimate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        try {
            return JsonModelHelper.getTaskAsJson(this);
        } catch (JSONException e) {
            return "Invalid JSON conversion";
        }
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(long timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public long getResumedAt() {
        return resumedAt;
    }

    public void setResumedAt(long resumedAt) {
        this.resumedAt = resumedAt;
    }
}
