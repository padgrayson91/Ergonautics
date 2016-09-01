package com.ergonautics.ergonautics;

import android.content.ContentValues;
import android.database.MatrixCursor;
import android.support.test.runner.AndroidJUnit4;

import com.ergonautics.ergonautics.models.Board;
import com.ergonautics.ergonautics.models.DBModelHelper;
import com.ergonautics.ergonautics.models.Task;
import com.ergonautics.ergonautics.storage.DBHelper;
import com.ergonautics.ergonautics.storage.Serializer;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import io.realm.RealmList;

import static junit.framework.Assert.assertEquals;

/**
 * Created by patrickgrayson on 8/29/16.
 */
@RunWith(AndroidJUnit4.class)
public class DBModelHelperTest2 {

    @Test
    public void testTaskFromContentVals(){
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.TasksTable.COLUMN_TASK_ID, "12345");
        cv.put(DBHelper.TasksTable.COLUMN_DISPLAY_NAME, "Some task");
        cv.put(DBHelper.TasksTable.COLUMN_COMPLETED_AT, -1L);
        cv.put(DBHelper.TasksTable.COLUMN_CREATED_AT, -1L);
        cv.put(DBHelper.TasksTable.COLUMN_SCHEDULED_FOR, -1L);
        cv.put(DBHelper.TasksTable.COLUMN_STARTED_AT, -1L);
        cv.put(DBHelper.TasksTable.COLUMN_TIME_ESTIMATE, 1000L * 60L *60L);
        cv.put(DBHelper.TasksTable.COLUMN_VALUE, 100);
        cv.put(DBHelper.TasksTable.COLUMN_STATUS, 1);
        Task t = new Task("Hello World");

        DBModelHelper.getTaskFromContentValues(cv, t);

        assertEquals("Some task", t.getDisplayName());
        assertEquals(1, t.getStatus());
    }

    @Test
    public void testPropertiesForTask(){
        Task t = new Task("Some task");
        t.setTaskId("23e3fd827");
        t.setCompletedAt(-1);
        t.setCreatedAt(12345567);
        t.setScheduledFor(-1);
        t.setStartedAt(-1);
        t.setTimeEstimate(123456);
        t.setValue(0);
        t.setStatus(2);
        Object [] props = DBModelHelper.getTaskProperties(t);
        ArrayList<String> columnsAsArrayList = new ArrayList<>(DBHelper.TasksTable.COLUMNS.length);
        for(String s: DBHelper.TasksTable.COLUMNS){
            columnsAsArrayList.add(s);
        }
        assertEquals("23e3fd827", props[columnsAsArrayList.indexOf(DBHelper.TasksTable.COLUMN_TASK_ID)]);
        assertEquals(2, props[columnsAsArrayList.indexOf(DBHelper.TasksTable.COLUMN_STATUS)]);

    }

    @Test
    public void testTaskFromCursor(){
        Task t = new Task("Some task");
        t.setTaskId("23e3fd827");
        t.setCompletedAt(-1);
        t.setCreatedAt(12345567);
        t.setScheduledFor(-1);
        t.setStartedAt(-1);
        t.setTimeEstimate(123456);
        t.setValue(0);
        t.setStatus(1);
        Object [] props = DBModelHelper.getTaskProperties(t);
        MatrixCursor c = new MatrixCursor(DBHelper.TasksTable.COLUMNS);
        c.addRow(props);
        c.moveToFirst();
        Task result = DBModelHelper.getTaskFromCursor(c);
        assertEquals(123456, result.getTimeEstimate());
        assertEquals(1, result.getStatus());

    }

    @Test
    public void testBoardFromContentVals(){
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.BoardsTable.COLUMN_BOARD_ID, "12345");
        cv.put(DBHelper.BoardsTable.COLUMN_DISPLAY_NAME, "Some Board");
        cv.put(DBHelper.BoardsTable.COLUMN_CREATED_AT, 123973791L);
        cv.put(DBHelper.BoardsTable.COLUMN_LAST_MODIFIED, -1L);
        cv.put(DBHelper.BoardsTable.COLUMN_TASKS, Serializer.serialize(new RealmList<Task>()));
        Board b = new Board("");

        DBModelHelper.getBoardFromContentValues(cv, b);

        assertEquals("12345", b.getBoardId());
        assertEquals(123973791, b.getCreatedAt());
    }

    @Test
    public void testPropertiesForBoard(){
        Board b = new Board("Some Board");
        b.setBoardId("12343a1224");
        b.setCreatedAt(1249719247L);
        b.setLastModified(124581972L);
        b.setTasks(new RealmList<Task>());
        Object[] props = DBModelHelper.getBoardProperties(b);
        ArrayList<String> columnsAsArrayList = new ArrayList<>(DBHelper.BoardsTable.COLUMNS.length);
        for(String s: DBHelper.BoardsTable.COLUMNS){
            columnsAsArrayList.add(s);
        }
        assertEquals("12343a1224", props[columnsAsArrayList.indexOf(DBHelper.BoardsTable.COLUMN_BOARD_ID)]);
        assertEquals(124581972L, props[columnsAsArrayList.indexOf(DBHelper.BoardsTable.COLUMN_LAST_MODIFIED)]);
    }

    @Test
    public void testBoardFromCursor(){
        Board b = new Board("Some Board");
        b.setBoardId("12343a1224");
        b.setCreatedAt(1249719247L);
        b.setLastModified(124581972L);
        b.setTasks(new RealmList<Task>());
        Object[] props = DBModelHelper.getBoardProperties(b);
        ArrayList<String> columnsAsArrayList = new ArrayList<>(DBHelper.BoardsTable.COLUMNS.length);
        for(String s: DBHelper.BoardsTable.COLUMNS){
            columnsAsArrayList.add(s);
        }
        MatrixCursor c = new MatrixCursor(DBHelper.BoardsTable.COLUMNS);
        c.addRow(props);
        c.moveToFirst();
        Board result = DBModelHelper.getBoardFromCursor(c);
        assertEquals(1249719247L, result.getCreatedAt());
    }
}
