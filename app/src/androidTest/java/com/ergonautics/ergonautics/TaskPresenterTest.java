package com.ergonautics.ergonautics;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.test.runner.AndroidJUnit4;

import com.ergonautics.ergonautics.models.Board;
import com.ergonautics.ergonautics.models.DBModelHelper;
import com.ergonautics.ergonautics.models.Task;
import com.ergonautics.ergonautics.presenter.IPresenterCallback;
import com.ergonautics.ergonautics.presenter.TaskPresenter;
import com.ergonautics.ergonautics.storage.DBHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by patrickgrayson on 9/3/16.
 */
@RunWith(AndroidJUnit4.class)
public class TaskPresenterTest {
    private ArrayList<String> mCreatedTaskIds;
    private ArrayList<String> mCreatedBoardIds;

    @Before
    public void setup(){
        mCreatedTaskIds = new ArrayList<>();
        mCreatedBoardIds = new ArrayList<>();
        //Add a task and board to the db
        DBHelper db = new DBHelper(getTargetContext());
        Board toAdd = new Board("Example Board with Task");
        ContentValues boardContent = DBModelHelper.getContentValuesForBoard(toAdd);
        String boardId = db.createBoard(boardContent);
        mCreatedBoardIds.add(boardId);
        Task taskToAdd = new Task("Example Task");
        ContentValues taskContent = DBModelHelper.getContentValuesForTask(taskToAdd);
        String id = db.createTask(taskContent, boardId);
        mCreatedTaskIds.add(id);
    }

    @Test
    public void testPresent(){
        TaskPresenter presenter = new TaskPresenter(getTargetContext(), null);
        Cursor c = presenter.present();
        assertTrue(c.getCount() >= mCreatedTaskIds.size());
    }

    @Test
    public void testGetData(){
        TaskPresenter presenter = new TaskPresenter(getTargetContext(), null);
        Task t = (Task) presenter.getData(0);
        assertTrue(mCreatedTaskIds.contains(t.getTaskId()));
    }

    @Test
    public void testDataAdded(){
        Task toAdd = new Task("Another task");
        String boardId = mCreatedBoardIds.get(0);
        TaskPresenter presenter = new TaskPresenter(getTargetContext(), new IPresenterCallback() {
            @Override
            public void notifyDataAdded(String id) {
                mCreatedTaskIds.add(id);
            }

            @Override
            public void notifyDataUpdated() {}

            @Override
            public void notifyDataRemoved(Object data) {}
        });
        int previousSize = presenter.present().getCount();
        presenter.onDataAdded(toAdd, boardId);
        int newSize = presenter.present().getCount();
        assertEquals(previousSize + 1, newSize);
    }

    @Test
    public void testDataRemove(){
        TaskPresenter presenter = new TaskPresenter(getTargetContext(), null);
        Task toRemove = (Task) presenter.getData(0);
        int previousSize = presenter.present().getCount();
        presenter.onDataRemoved(toRemove);
        int newSize = presenter.present().getCount();
        assertEquals(previousSize - 1, newSize);
        mCreatedTaskIds.remove(toRemove.getTaskId());
    }

    @After
    public void tearDown(){
        DBHelper db = new DBHelper(getTargetContext());
        for(String taskId: mCreatedTaskIds){
            db.deleteTaskById(taskId);
        }
        for(String boardId: mCreatedBoardIds){
            db.deleteBoardById(boardId);
        }


    }


}
