package com.ergonautics.ergonautics;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;

import com.ergonautics.ergonautics.models.Board;
import com.ergonautics.ergonautics.models.Task;
import com.ergonautics.ergonautics.models.DBModelHelper;
import com.ergonautics.ergonautics.storage.ErgonautContentProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.InstrumentationRegistry.getContext;
import static org.junit.Assert.*;

/**
 * Created by patrickgrayson on 8/19/16.
 */
@RunWith(AndroidJUnit4.class)
public class ErgonautContentProviderTest {
    private ContentResolver mResolver;
    private ArrayList<String> mCreatedTasks;
    private ArrayList<String> mCreatedBoards;

    @Before
    public void setUp(){
        mResolver = getContext().getContentResolver();
        //Lists to keep track of all of the ids of elements created during the test so they can be deleted
        mCreatedTasks = new ArrayList<>();
        mCreatedBoards = new ArrayList<>();
    }

    @Test
    public void testTaskInsert(){
        Board b = new Board("Example Board with task");
        ContentValues bVals = DBModelHelper.getContentValuesForBoard(b);
        Uri uri = mResolver.insert(ErgonautContentProvider.BOARDS_INSERT_URI, bVals);
        //Ensure the last segment of the path (i.e. the board id) is longer than 4 digits
        String boardId = uri.getLastPathSegment();
        assertNotNull(boardId);
        assertTrue(boardId.length() > 4);
        mCreatedBoards.add(boardId);
        Task t = new Task("Example Task");
        ContentValues tVals = DBModelHelper.getContentValuesForTask(t);
        String uriString = ErgonautContentProvider.TASKS_INSERT_URI.toString();
        uriString = uriString.replace("*", boardId);
        Uri taskUri = mResolver.insert(Uri.parse(uriString), tVals);
        assertNotNull(taskUri);
        //Ensure the task id is at least 4 digits
        String taskId = taskUri.getLastPathSegment();
        assertTrue(taskId.length() > 4);
        mCreatedTasks.add(taskId);
    }

    @Test
    public void testBoardInsert(){
        Board b = new Board("Example Board");
        ContentValues bVals = DBModelHelper.getContentValuesForBoard(b);
        Uri uri = mResolver.insert(ErgonautContentProvider.BOARDS_INSERT_URI, bVals);
        assertNotNull(uri);
        //Ensure the last segment of the path (i.e. the board id) is longer than 4 digits
        String boardId = uri.getLastPathSegment();
        assertTrue(boardId.length() > 4);
        mCreatedBoards.add(boardId);

    }

    @Test
    public void testAllBoardQuery(){
        Board b = new Board("Example Board to query");
        ContentValues bVals = DBModelHelper.getContentValuesForBoard(b);
        Uri uri = mResolver.insert(ErgonautContentProvider.BOARDS_INSERT_URI, bVals);
        assertNotNull(uri);
        //Ensure the last segment of the path (i.e. the board id) is longer than 4 digits
        String boardId = uri.getLastPathSegment();
        assertTrue(boardId.length() > 4);
        mCreatedBoards.add(boardId);
        Cursor cursor = mResolver.query(ErgonautContentProvider.BOARDS_QUERY_URI, null, null, null, null);
        assertTrue(cursor.getCount() > 0);
    }

    @Test
    public void testAllTaskQuery(){
        Board b = new Board("Example Board with task");
        ContentValues bVals = DBModelHelper.getContentValuesForBoard(b);
        Uri uri = mResolver.insert(ErgonautContentProvider.BOARDS_INSERT_URI, bVals);
        //Ensure the last segment of the path (i.e. the board id) is longer than 4 digits
        String boardId = uri.getLastPathSegment();
        assertNotNull(boardId);
        assertTrue(boardId.length() > 4);
        Task t = new Task("Example Task");
        ContentValues tVals = DBModelHelper.getContentValuesForTask(t);
        String uriString = ErgonautContentProvider.TASKS_INSERT_URI.toString();
        uriString = uriString.replace("*", boardId);
        Uri taskUri = mResolver.insert(Uri.parse(uriString), tVals);
        assertNotNull(taskUri);
        //Ensure the task id is at least 4 digits
        String taskId = taskUri.getLastPathSegment();
        assertTrue(taskId.length() > 4);
        mCreatedTasks.add(taskId);

        Cursor cursor = mResolver.query(ErgonautContentProvider.TASKS_QUERY_URI, null, null, null, null);
        assertTrue(cursor.getCount() > 0);
    }

    @Test
    public void testSpecificBoardQuery(){
        Board b = new Board("Example Board to Query by Id");
        ContentValues bVals = DBModelHelper.getContentValuesForBoard(b);
        Uri uri = mResolver.insert(ErgonautContentProvider.BOARDS_INSERT_URI, bVals);
        assertNotNull(uri);
        //Ensure the last segment of the path (i.e. the board id) is longer than 4 digits
        String boardId = uri.getLastPathSegment();
        assertTrue(boardId.length() > 4);
        mCreatedBoards.add(boardId);
        Cursor cursor = mResolver.query(Uri.withAppendedPath(ErgonautContentProvider.BOARDS_QUERY_URI, boardId), null, null, null, null);
        cursor.moveToFirst();
        assertEquals("Multiple boards retured from ID query!", cursor.getCount(), 1);
        Board result = DBModelHelper.getBoardFromCursor(cursor);
        assertEquals(b.getDisplayName(), result.getDisplayName());
    }

    @Test
    public void testSpecificTaskQuery(){
        Board b = new Board("Example Board with task to query");
        ContentValues bVals = DBModelHelper.getContentValuesForBoard(b);
        Uri uri = mResolver.insert(ErgonautContentProvider.BOARDS_INSERT_URI, bVals);
        //Ensure the last segment of the path (i.e. the board id) is longer than 4 digits
        String boardId = uri.getLastPathSegment();
        assertNotNull(boardId);
        assertTrue(boardId.length() > 4);
        mCreatedBoards.add(boardId);
        Task t = new Task("Example Task to Query by ID");
        ContentValues tVals = DBModelHelper.getContentValuesForTask(t);
        String uriString = ErgonautContentProvider.TASKS_INSERT_URI.toString();
        uriString = uriString.replace("*", boardId);
        Uri taskUri = mResolver.insert(Uri.parse(uriString), tVals);
        assertNotNull(taskUri);
        //Ensure the task id is at least 4 digits
        String taskId = taskUri.getLastPathSegment();
        assertTrue(taskId.length() > 4);
        mCreatedTasks.add(taskId);

        Cursor cursor = mResolver.query(Uri.withAppendedPath(ErgonautContentProvider.TASKS_QUERY_URI, taskId), null, null, null, null);
        cursor.moveToFirst();
        assertEquals("Multiple tasks retured from ID query!", cursor.getCount(), 1);
        Task result = DBModelHelper.getTaskFromCursor(cursor);
        assertEquals(t.getDisplayName(), result.getDisplayName());

    }

    @Test
    public void testTasksForBoardQuery(){
        //TODO
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
        for(String s: mCreatedTasks){
            mResolver.delete(Uri.withAppendedPath(ErgonautContentProvider.TASKS_QUERY_URI, s), null, null);
        }
        for(String s: mCreatedBoards){
            mResolver.delete(Uri.withAppendedPath(ErgonautContentProvider.BOARDS_QUERY_URI, s), null, null);
        }

        mResolver = null;
    }
}
