package com.ergonautics.ergonautics.models;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by patrickgrayson on 8/18/16.
 * Model class for boards, which contain a list of Tasks
 */
public class Board extends RealmObject implements ModelConstants, Serializable {
    private RealmList<Task> tasks;
    private String displayName;
    private String boardId;
    private long createdAt;
    private long lastModified;

    public Board(String displayName){
        this.displayName = displayName;
        tasks = new RealmList<>();
        boardId = REMOTE_ID_DEFAULT;
        createdAt = System.currentTimeMillis();
        lastModified = System.currentTimeMillis();
    }

    public Board(){
        displayName = DISPLAY_NAME_DEFAULT;
        boardId = REMOTE_ID_DEFAULT;
        tasks = new RealmList<>();
        createdAt = System.currentTimeMillis();
        lastModified = System.currentTimeMillis();
    }

    public String getDisplayName(){
        return displayName;
    }

    public RealmList<Task> getTasks(){
        return tasks;
    }

    public String getBoardId(){
        return boardId;
    }

    public void setBoardId(String id){
        boardId = id;
    }

    public void setDisplayName(String name) { displayName = name; }

    public void addTask(Task toAdd){
        tasks.add(toAdd);
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public void setTasks(RealmList<Task> tasks) {
        this.tasks = tasks;
    }
}
