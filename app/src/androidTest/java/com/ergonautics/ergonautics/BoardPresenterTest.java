package com.ergonautics.ergonautics;

import android.content.ContentValues;
import android.support.test.runner.AndroidJUnit4;

import com.ergonautics.ergonautics.models.Board;
import com.ergonautics.ergonautics.models.DBModelHelper;
import com.ergonautics.ergonautics.models.Task;
import com.ergonautics.ergonautics.presenter.BoardPresenter;
import com.ergonautics.ergonautics.presenter.IPresenterCallback;
import com.ergonautics.ergonautics.storage.DBHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by patrickgrayson on 9/6/16.
 */
@RunWith(AndroidJUnit4.class)
public class BoardPresenterTest {
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
        BoardPresenter presenter = new BoardPresenter(getTargetContext(), null);
        assertTrue(presenter.getCount() >= mCreatedBoardIds.size());
    }

    @Test
    public void testGetData(){
        BoardPresenter presenter = new BoardPresenter(getTargetContext(), null);
        Board b = (Board) presenter.getData(0);
        assertTrue(mCreatedBoardIds.contains(b.getBoardId()));
    }

    @Test
    public void testDataAdded(){
        Board toAdd = new Board("Another Board");
        BoardPresenter presenter = new BoardPresenter(getTargetContext(), new IPresenterCallback() {
            @Override
            public void notifyDataAdded(String id) {
                mCreatedBoardIds.add(id);
            }

            @Override
            public void notifyDataUpdated() {}

            @Override
            public void notifyDataRemoved(Object data) {}
        });
        int previousSize = presenter.getCount();
        presenter.addData(toAdd);
        int newSize = presenter.getCount();
        assertEquals(previousSize + 1, newSize);
    }

    @Test
    public void testDataRemove(){
        BoardPresenter presenter = new BoardPresenter(getTargetContext(), null);
        Board toRemove = (Board) presenter.getData(0);
        int previousSize = presenter.getCount();
        presenter.removeData(toRemove);
        int newSize = presenter.getCount();
        assertEquals(previousSize - 1, newSize);
        mCreatedBoardIds.remove(toRemove.getBoardId());
    }

    @Test
    public void getTaskFromRetrievedBoard(){
        BoardPresenter presenter = new BoardPresenter(getTargetContext(), null);
        ArrayList<Board> boards = presenter.present();
        Task foundTask = null;
        for(Object o: boards){
            Board b = (Board) o;
            try {
                if(b.getTasks().get(0) != null) {
                    foundTask = b.getTasks().get(0);
                }
            } catch (IndexOutOfBoundsException ignored) {

            }
        }
        assertNotNull(foundTask);
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
