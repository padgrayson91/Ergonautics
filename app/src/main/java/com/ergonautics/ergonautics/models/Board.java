package com.ergonautics.ergonautics.models;

import java.util.ArrayList;

/**
 * Created by patrickgrayson on 8/18/16.
 * Model class for boards, which contain a list of Tasks
 */
public class Board implements ModelConstants {
    private ArrayList<Task> mTasks;
    private String mDisplayName;
    private String mBoardId; //Used by remote API: may be null if user created this board while offline
    private int mLocalId; //Used by local db: all boards should have this value once stored

    public Board(String displayName){
        mDisplayName = displayName;
        mTasks = new ArrayList<>();
        mBoardId = REMOTE_ID_DEFAULT;
        mLocalId = LOCAL_ID_DEFAULT;
    }

    public String getDisplayName(){
        return mDisplayName;
    }

    public ArrayList<Task> getTasks(){
        return mTasks;
    }

    public String getBoardId(){
        return mBoardId;
    }

    public int getLocalId(){
        return mLocalId;
    }

    public void setBoardId(String id){
        mBoardId = id;
    }

    public void setLocalId(int id){
        mLocalId = id;
    }

    public void addTask(Task toAdd){
        mTasks.add(toAdd);
    }
}
