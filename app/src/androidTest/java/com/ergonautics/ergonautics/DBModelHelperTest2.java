package com.ergonautics.ergonautics;

import android.content.ContentValues;
import android.database.MatrixCursor;
import android.support.test.runner.AndroidJUnit4;

import com.ergonautics.ergonautics.models.DBModelHelper;
import com.ergonautics.ergonautics.models.Task;
import com.ergonautics.ergonautics.storage.DBHelper;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

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
        //TODO
    }

    @Test
    public void testPropertiesForBoard(){
        //TODO
    }

    @Test
    public void testBoardFromCursor(){
        //TODO
    }
}
