package com.ergonautics.ergonautics;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.test.runner.AndroidJUnit4;

import com.ergonautics.ergonautics.models.Board;
import com.ergonautics.ergonautics.models.Task;
import com.ergonautics.ergonautics.storage.DBHelper;
import com.ergonautics.ergonautics.storage.DBModelHelper;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by patrickgrayson on 8/19/16.
 * Tests for the DBHelper class
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
        db = new DBHelper(getTargetContext());
        Board toAdd = new Board("Example Board to Get");
        ContentValues boardContent = DBModelHelper.getContentValuesForBoard(toAdd);
        String id = db.createBoard(boardContent);
        assertNotNull(id);

        Cursor getResult = db.getBoardById(id);
        getResult.moveToFirst();
        assertEquals("Multiple boards returned from ID query!", getResult.getCount(), 1);
        Board fetchedBoard = DBModelHelper.getBoardFromCursor(getResult);
        assertEquals(toAdd.getDisplayName(), fetchedBoard.getDisplayName());
    }

    @Test
    public void testGetTask(){
        db = new DBHelper(getTargetContext());
        Board toAdd = new Board("Example Board with Task to fetch");
        ContentValues boardContent = DBModelHelper.getContentValuesForBoard(toAdd);
        String boardId = db.createBoard(boardContent);
        assertNotNull(boardId);
        Task taskToAdd = new Task("Example Task to fetch");
        ContentValues taskContent = DBModelHelper.getContentValuesForTask(taskToAdd);
        String id = db.createTask(taskContent, boardId);
        assertNotNull(id);

        Cursor getResult = db.getTaskById(id);
        getResult.moveToFirst();
        assertEquals("Multiple tasks returned from ID query!", getResult.getCount(), 1);
        Task fetchedTask = DBModelHelper.getTaskFromCursor(getResult);
        assertEquals(taskToAdd.getDisplayName(), fetchedTask.getDisplayName());
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
        db = new DBHelper(getTargetContext());
        Board toAdd = new Board("Example Board with Task 2");
        ContentValues boardContent = DBModelHelper.getContentValuesForBoard(toAdd);
        String boardId = db.createBoard(boardContent);
        assertNotNull(boardId);
        Task taskToAdd = new Task("Example Task");
        ContentValues taskContent = DBModelHelper.getContentValuesForTask(taskToAdd);
        String id = db.createTask(taskContent, boardId);
        assertNotNull(id);

        Cursor result = db.getTasksForBoard(boardId);
        assertTrue(result.getCount() > 0);
    }

    @Test
    public void testUpdateBoard(){
        db = new DBHelper(getTargetContext());
        Board toAdd = new Board("Example Board to Update");
        ContentValues boardContent = DBModelHelper.getContentValuesForBoard(toAdd);
        String id = db.createBoard(boardContent);
        assertNotNull(id);

        Cursor getResult = db.getBoardById(id);
        getResult.moveToFirst();
        Board fetchedBoard = DBModelHelper.getBoardFromCursor(getResult);
        assertEquals(toAdd.getDisplayName(), fetchedBoard.getDisplayName());

        fetchedBoard.setDisplayName("Example Board Updated");
        db.updateBoard(DBModelHelper.getContentValuesForBoard(fetchedBoard));

        getResult = db.getBoardById(id);
        getResult.moveToFirst();
        Board updatedBoard = DBModelHelper.getBoardFromCursor(getResult);
        assertEquals("Example Board Updated", updatedBoard.getDisplayName());
    }

    @Test
    public void testUpdateTask(){
        db = new DBHelper(getTargetContext());
        Board toAdd = new Board("Example Board with Task to fetch");
        ContentValues boardContent = DBModelHelper.getContentValuesForBoard(toAdd);
        String boardId = db.createBoard(boardContent);
        assertNotNull(boardId);
        Task taskToAdd = new Task("Example Task to Update");
        ContentValues taskContent = DBModelHelper.getContentValuesForTask(taskToAdd);
        String id = db.createTask(taskContent, boardId);
        assertNotNull(id);

        Cursor getResult = db.getTaskById(id);
        getResult.moveToFirst();
        Task fetchedTask = DBModelHelper.getTaskFromCursor(getResult);
        assertEquals(taskToAdd.getDisplayName(), fetchedTask.getDisplayName());

        fetchedTask.setDisplayName("Example Task Updated");
        db.updateTask(DBModelHelper.getContentValuesForTask(fetchedTask));

        getResult = db.getTaskById(id);
        getResult.moveToFirst();
        Task updatedTask = DBModelHelper.getTaskFromCursor(getResult);
        assertEquals("Example Task Updated", updatedTask.getDisplayName());
    }

    @Test
    public void testDeleteTask(){
        //TODO
    }

    @Test
    public void testDeleteBoard(){
        //TODO
    }

    @After
    public void tearDown(){
        db = new DBHelper(getTargetContext());
        //Note: when running on a device or emulator, this will fully clear the db
        //So probably to be avoided if you are also doing manual testing on the device
        db.clearDb();
    }


}
