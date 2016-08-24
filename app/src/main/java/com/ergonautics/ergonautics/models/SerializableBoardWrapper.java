package com.ergonautics.ergonautics.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by patrickgrayson on 8/24/16.
 */
public class SerializableBoardWrapper extends SerializableWrapper implements Serializable {
    private String displayName;
    private String boardId;
    private ArrayList<Task> tasks;

    public static SerializableBoardWrapper fromBoard(Board b){
        SerializableBoardWrapper wrapper = new SerializableBoardWrapper();
        wrapper.displayName = b.getDisplayName();
        wrapper.boardId = b.getBoardId();
        wrapper.tasks = new ArrayList<>();
        for(Task t: b.getTasks()){
            wrapper.tasks.add(t);
        }
        return wrapper;
    }

    public Object unwrap(){
        Board toUnwrap = new Board(displayName);
        toUnwrap.setBoardId(boardId);
        for(Task t: tasks){
            toUnwrap.addTask(t);
        }
        return toUnwrap;
    }

    private SerializableBoardWrapper(){}
}
