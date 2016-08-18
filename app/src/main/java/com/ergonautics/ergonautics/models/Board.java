package com.ergonautics.ergonautics.models;

import java.util.ArrayList;

/**
 * Created by patrickgrayson on 8/18/16.
 * Model class for boards, which contain a list of Tasks
 */
public class Board {
    private ArrayList<String> mTaskIds;
    private String mDisplayName;
    private String mBoardId;

    public Board(String displayName){
        mDisplayName = displayName;
        mTaskIds = new ArrayList<>();
    }

    public String getDisplayName(){
        return mDisplayName;
    }

    public ArrayList<String> getTaskIds(){
        return mTaskIds;
    }

    public String getBoardId(){
        return mBoardId;
    }

    public void setBoardId(String id){
        mBoardId = id;
    }

    public void addTask(String idToAdd){
        mTaskIds.add(idToAdd);
    }
}
