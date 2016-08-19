package com.ergonautics.ergonautics.storage;

import android.content.ContentValues;

import com.ergonautics.ergonautics.models.Board;
import com.ergonautics.ergonautics.models.Task;

/**
 * Created by patrickgrayson on 8/19/16.
 * class to help convert between model classes and local db entries
 */
public class DBModelHelper {
    public static ContentValues getContentValuesForTask(Task t){
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.TasksTable.COLUMN_DISPLAY_NAME, t.getDisplayName());
        cv.put(DBHelper.TasksTable.COLUMN_TASK_ID, t.getTaskId());
        return cv;
    }

    public static ContentValues getContentValuesForBoard(Board b){
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.BoardsTable.COLUMN_DISPLAY_NAME, b.getDisplayName());
        cv.put(DBHelper.BoardsTable.COLUMN_BOARD_ID, b.getBoardId());
        return cv;
    }

    public static ContentValues getContentValuesForBoardTaskRelations(long taskId, long boardId){
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.TaskBoardRelationTable.COLUMN_TASK_LOCAL_ID, taskId);
        cv.put(DBHelper.TaskBoardRelationTable.COLUMN_BOARD_LOCAL_ID, boardId);
        return cv;
    }
}
