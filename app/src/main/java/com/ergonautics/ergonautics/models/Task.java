package com.ergonautics.ergonautics.models;

import org.json.JSONException;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by patrickgrayson on 8/18/16.
 * Model class for user tasks
 */
public class Task extends RealmObject implements ModelConstants, Serializable {
    //NOTE: when adding a property, you must also add a column to DBHelper.TasksTable
    private String displayName;
    private String taskId; //Used for the remote API: May be null if user is offline
    private long createdAt;
    private long startedAt;
    private long completedAt;
    private long scheduledFor;
    private long timeEstimate; //Estimated amount of time in ms that this task will take
    private int value;
    private int status;

    public Task(){
        displayName = DISPLAY_NAME_DEFAULT;
        taskId = REMOTE_ID_DEFAULT;
        createdAt = new Date().getTime();
        startedAt = STARTED_AT_DEFAULT;
        completedAt = COMPLETED_AT_DEFAULT;
        scheduledFor = SCHEDULED_FOR_DEFAULT;
        value = VALUE_DEFAULT;
        timeEstimate = TIME_ESTIMATE_DEFAULT;
        status = STATUS_DEFUALT;
    }

    public Task(String name){
        displayName = name;
        taskId = REMOTE_ID_DEFAULT;
        createdAt = new Date().getTime();
        startedAt = STARTED_AT_DEFAULT;
        completedAt = COMPLETED_AT_DEFAULT;
        scheduledFor = SCHEDULED_FOR_DEFAULT;
        value = VALUE_DEFAULT;
        timeEstimate = TIME_ESTIMATE_DEFAULT;
        status = STATUS_DEFUALT;
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
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
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
}
