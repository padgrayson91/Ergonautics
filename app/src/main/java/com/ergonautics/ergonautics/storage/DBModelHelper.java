package com.ergonautics.ergonautics.storage;

import android.content.ContentValues;
import android.database.Cursor;

import com.ergonautics.ergonautics.models.Board;
import com.ergonautics.ergonautics.models.Task;

import io.realm.RealmList;

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
        cv.put(DBHelper.BoardsTable.COLUMN_TASKS, Serializer.serialize(b.getTasks()));
        return cv;
    }

    //NOTE: This should always match the order of DBHelper.TasksTable.COLUMNS!
    public static Object[] getTaskProperties(Task t){
        Object [] props = new Object[DBHelper.TasksTable.COLUMNS.length];
        props[0] = t.getTaskId();
        props[1] = t.getDisplayName();
        //TODO: further fields be accessed via reflection to avoid the need to update this method
        return props;
    }

    //NOTE: This should always match the order of DBHelper.BoardsTable.COLUMNS!
    public static Object[] getBoardProperties(Board b) {
        Object [] props = new Object[DBHelper.BoardsTable.COLUMNS.length];
        props[0] = b.getBoardId();
        props[1] = b.getDisplayName();
        props[2] = Serializer.serialize(b.getTasks());
        //TODO: further fields can be accessed via reflection to avoid the need to update this method
        return props;
    }

    //NOTE: This should always match the order of DBHelper.TasksTable.COLUMNS!
    public static Task getTaskFromContentValues(ContentValues cv, Task toUpdate){
        toUpdate.setTaskId(cv.getAsString(DBHelper.TasksTable.COLUMN_TASK_ID));
        toUpdate.setDisplayName(cv.getAsString(DBHelper.TasksTable.COLUMN_DISPLAY_NAME));
        //TODO: the fields can be accessed via reflection to avoid the need to update this method
        return toUpdate;
    }

    public static Board getBoardFromContentValues(ContentValues cv, Board toUpdate){
        toUpdate.setBoardId(cv.getAsString(DBHelper.BoardsTable.COLUMN_BOARD_ID));
        toUpdate.setDisplayName(cv.getAsString(DBHelper.BoardsTable.COLUMN_DISPLAY_NAME));
        try {
            RealmList<Task> tasks = (RealmList<Task>) Serializer.deserialize(cv.getAsByteArray(DBHelper.BoardsTable.COLUMN_TASKS));
            for (Task t : tasks) {
                toUpdate.addTask(t);
            }
        } catch (NullPointerException ignored){}

        return toUpdate;
    }

    //methods to convert DB entries to model objects

    /**
     * Creates a task object from the current row of the cursor.  This will not move the cursor
     * @param c the cursor, which should have been pulled from the "tasks" table
     * @return a Task object with appropriate values pulled from the cursor
     */
    public static Task getTaskFromCursor(Cursor c){
        String displayName = c.getString(c.getColumnIndex(DBHelper.TasksTable.COLUMN_DISPLAY_NAME));
        Task t = new Task(displayName);
        String taskId = c.getString(c.getColumnIndex(DBHelper.TasksTable.COLUMN_TASK_ID));
        t.setTaskId(taskId);
        return t;
    }

    /**
     * Creates a board object from the current row of the cursor. This will not move the cursor
     * @param c the cursor, which should have been pulled from the "boards" table
     * @return a Board object with appropriate values pulled from the cursor, but with an empty task list
     */
    public static Board getBoardFromCursor(Cursor c){
        String displayName = c.getString(c.getColumnIndex(DBHelper.BoardsTable.COLUMN_DISPLAY_NAME));
        Board b = new Board(displayName);
        String boardId = c.getString(c.getColumnIndex(DBHelper.BoardsTable.COLUMN_BOARD_ID));
        b.setBoardId(boardId);
        RealmList<Task> tasks = (RealmList<Task>) Serializer.deserialize(c.getBlob(c.getColumnIndex(DBHelper.BoardsTable.COLUMN_TASKS)));
        for(Task t: tasks){
            b.addTask(t);
        }

        return b;
    }
}
