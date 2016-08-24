package com.ergonautics.ergonautics;

import android.support.test.runner.AndroidJUnit4;

import com.ergonautics.ergonautics.models.Board;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by patrickgrayson on 8/19/16.
 */
@RunWith(AndroidJUnit4.class)
public class APITest {
    private ErgonautAPI mApi;
    private String mTestUser;
    private String mTestPassword;

    @Before
    public void setup(){
        mApi = new ErgonautAPI(getInstrumentation().getTargetContext());
        mTestUser = getContext().getResources().getString(R.string.debug_username);
        mTestPassword = getContext().getResources().getString(R.string.debug_password);
    }

    @Test
    public void testLogin(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int result = mApi.login(mTestUser, mTestPassword);
                assertEquals(ErgonautAPI.SUCCESS, result);
            }
        }).start();
    }

    @Test
    public void testAddBoard(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int result = mApi.login(mTestUser, mTestPassword);
                assertEquals("Login Failed", ErgonautAPI.SUCCESS, result);
                Board b = new Board("Test Board");
                result = mApi.addBoard(b);
                assertEquals("Add Board Failed", ErgonautAPI.SUCCESS, result);
            }
        }).start();
    }

    @Test
    public void testGetBoard(){
        //TODO
    }

    @Test
    public void testGetAllBoards(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int result = mApi.login(mTestUser, mTestPassword);
                assertEquals("Login Failed", ErgonautAPI.SUCCESS, result);
                Board b = new Board("Test Board to Fetch");
                result = mApi.addBoard(b);
                assertEquals("Add Board Failed", ErgonautAPI.SUCCESS, result);
                ArrayList<Board> allBoards = mApi.getAllBoards();
                assertTrue(allBoards.size() > 0);
            }
        }).start();
    }

    @Test
    public void testAddTask(){
        //TODO
    }

    @Test
    public void testGetAllTasks(){
        //TODO
    }

}
