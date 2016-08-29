package com.ergonautics.ergonautics;

import android.content.ContentValues;
import android.support.test.runner.AndroidJUnit4;

import com.ergonautics.ergonautics.models.DBModelHelper;
import com.ergonautics.ergonautics.models.Task;
import com.ergonautics.ergonautics.storage.DBHelper;


import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.*;

/**
 * Created by patrickgrayson on 8/29/16.
 */
@RunWith(AndroidJUnit4.class)
public class DBModelHelperTest {

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
        Task t = new Task("Hello World");

        DBModelHelper.getTaskFromContentValues(cv, t);

        assertEquals("Some task", t.getDisplayName());
    }

    @Test
    public void testPropertiesFromTask(){

    }

    @Test
    public void testTaskFromCursor(){

    }
}
