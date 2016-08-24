package com.ergonautics.ergonautics;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.test.runner.AndroidJUnit4;

import com.ergonautics.ergonautics.models.Board;
import com.ergonautics.ergonautics.models.Task;
import com.ergonautics.ergonautics.storage.DBHelper;
import com.ergonautics.ergonautics.storage.DBModelHelper;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by patrickgrayson on 8/19/16.
 */
@RunWith(AndroidJUnit4.class)
public class DBTest {
    private DBHelper db;

    @Test
    public void testAddBoard(){
        db = new DBHelper(getTargetContext());
        Board toAdd = new Board("Example Board");
        ContentValues boardContent = DBModelHelper.getContentValuesForBoard(toAdd);
        String id = db.createBoard(boardContent);
        assertNotNull(id);
    }

    @Test
    public void testAddTask(){
        db = new DBHelper(getTargetContext());
        Board toAdd = new Board("Example Board with Task");
        ContentValues boardContent = DBModelHelper.getContentValuesForBoard(toAdd);
        String boardId = db.createBoard(boardContent);
        assertNotNull(boardId);
        Task taskToAdd = new Task("Example Task");
        ContentValues taskContent = DBModelHelper.getContentValuesForTask(taskToAdd);
        String id = db.createTask(taskContent, boardId);
        assertNotNull(id);
    }

    @Test
    public void testGetAllBoards(){
        db = new DBHelper(getTargetContext());
        Board toAdd = new Board("Example Board for fetching");
        ContentValues boardContent = DBModelHelper.getContentValuesForBoard(toAdd);
        String id = db.createBoard(boardContent);
        assertNotNull(id);
        Cursor c = db.getAllBoards();
        assertTrue(c.getCount() > 0);
    }

    @Test
    public void testGetBoard(){
        //TODO
    }

    @Test
    public void testGetTask(){
        //TODO
    }

    @Test
    public void testGetAllTasks(){
        db = new DBHelper(getTargetContext());
        Board toAdd = new Board("Example Board with Task");
        ContentValues boardContent = DBModelHelper.getContentValuesForBoard(toAdd);
        String boardId = db.createBoard(boardContent);
        assertNotNull(boardId);
        Task taskToAdd = new Task("Example Task");
        ContentValues taskContent = DBModelHelper.getContentValuesForTask(taskToAdd);
        String id = db.createTask(taskContent, boardId);
        assertNotNull(id);
        Cursor c = db.getAllTasks();
        assertTrue(c.getCount() > 0);
    }

    @Test
    public void testGetTasksForBoard(){
        //TODO
    }


}
