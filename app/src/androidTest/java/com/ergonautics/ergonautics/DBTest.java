package com.ergonautics.ergonautics;

import android.content.ContentValues;
import android.support.test.runner.AndroidJUnit4;

import com.ergonautics.ergonautics.models.Board;
import com.ergonautics.ergonautics.models.Task;
import com.ergonautics.ergonautics.storage.DBHelper;
import com.ergonautics.ergonautics.storage.DBModelHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.*;

/**
 * Created by patrickgrayson on 8/19/16.
 */
@RunWith(AndroidJUnit4.class)
public class DBTest {
    private DBHelper db;

    @Before
    public void setup(){
        db = new DBHelper(getTargetContext(), null);
    }

    @After
    public void tearDown(){
        db.close();
    }

    @Test
    public void testAddBoard(){
        Board toAdd = new Board("Example Board");
        ContentValues boardContent = DBModelHelper.getContentValuesForBoard(toAdd);
        long id = db.addBoard(boardContent);
        assertTrue(id > 0);
    }

    @Test
    public void testAddTask(){
        Task taskToAdd = new Task("Example Task");
        ContentValues taskContent = DBModelHelper.getContentValuesForTask(taskToAdd);
        long id = db.addTask(taskContent);
        assertTrue(id > 0);
    }

    @Test
    public void testAddTaskBoardRelation(){
        Board toAdd = new Board("Example Board with Task");
        ContentValues boardContent = DBModelHelper.getContentValuesForBoard(toAdd);
        long boardId = db.addBoard(boardContent);
        Task taskToAdd = new Task("Example Task in Board");
        ContentValues taskContent = DBModelHelper.getContentValuesForTask(taskToAdd);
        long taskId = db.addTask(taskContent);
        ContentValues taskBoardRelationship = DBModelHelper.getContentValuesForBoardTaskRelations(taskId, boardId);
        long relationshipId = db.addBoardTaskRelationship(taskBoardRelationship);
        assertTrue(relationshipId > 0);
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
        //TODO
    }

    @Test
    public void testGetTasksForBoard(){
        //TODO
    }


}
