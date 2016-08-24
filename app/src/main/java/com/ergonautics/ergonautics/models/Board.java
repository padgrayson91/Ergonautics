package com.ergonautics.ergonautics.models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by patrickgrayson on 8/18/16.
 * Model class for boards, which contain a list of Tasks
 */
public class Board extends RealmObject implements ModelConstants {
    private RealmList<Task> tasks;
    private String displayName;
    private String boardId; //Used by remote API: may be null if user created this board while offline

    public Board(String displayName){
        this.displayName = displayName;
        tasks = new RealmList<Task>();
        boardId = REMOTE_ID_DEFAULT;
    }

    public Board(){
        displayName = "";
        boardId = REMOTE_ID_DEFAULT;
        tasks = new RealmList<>();
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

    public void addTask(Task toAdd){
        tasks.add(toAdd);
    }
}
