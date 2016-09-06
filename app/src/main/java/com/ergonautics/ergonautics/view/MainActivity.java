package com.ergonautics.ergonautics.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ergonautics.ergonautics.ErgonautAPI;
import com.ergonautics.ergonautics.R;
import com.ergonautics.ergonautics.app.ITaskListUpdateListener;
import com.ergonautics.ergonautics.app.MainPageAdapter;
import com.ergonautics.ergonautics.models.Task;
import com.ergonautics.ergonautics.storage.ErgonautContentProvider;
import com.ergonautics.ergonautics.storage.LocalStorage;

import java.util.ArrayList;

/**
 * App main page where user can view active tasks, current boards, notifications, etc.
 */
public class MainActivity extends AppCompatActivity implements ITaskListUpdateListener {
    private static final String TAG = "ERGONAUT-MAIN";

    //Fragment tags
    private static final String FRAGMENT_TAG_TASK_LIST = "tasks";
    private static final String FRAGMENT_TAG_TASK_CREATOR = "newtask";

    private static final int REQUEST_CODE_LOGIN = 1001;
    private static final int REQUEST_CODE_ADD_TASK = 1002;

    private TabOnlyViewPager mPager;
    private MainPageAdapter mPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check if user is logged in
        ErgonautAPI api = new ErgonautAPI(this);
        mPager = (TabOnlyViewPager) findViewById(R.id.pager_main);
        mPageAdapter = new MainPageAdapter(getSupportFragmentManager(), getFragmentsForPageAdapter());
        mPager.setAdapter(mPageAdapter);
        if(!api.isLoggedIn()){
            //If the user is not logged in, go to the login activity
            switchToLoginActivity();
        } else {
            switchToLandingPage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case REQUEST_CODE_LOGIN:
                if(resultCode == RESULT_OK){
                    //Login was successful
                    switchToLandingPage();
                } else {
                    //User hit back
                    finish();
                }
                break;
            case REQUEST_CODE_ADD_TASK:
                LocalStorage.getInstance(this).setTaskInProgress(null);
                switchToTaskList(null);
                break;
            default:
                Log.d(TAG, "onActivityResult: unknown activity result");
        }
    }

    private void switchToLandingPage(){
        //TODO: switch to different pages based on user preferences
        switchToBoardList();
    }

    //Show a task list for a specific board or (if forBoard is null) show all tasks in the db
    private void switchToTaskList(@Nullable String forBoard){
        String queryString;
        mPager.setCurrentItem(1);
    }

    private void switchToBoardList(){
        mPager.setCurrentItem(0);
    }

    private void switchToLoginActivity(){
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(loginIntent, REQUEST_CODE_LOGIN);
    }

    private ArrayList<Fragment> getFragmentsForPageAdapter(){
        ArrayList<Fragment> fragments = new ArrayList<>();
        //TODO: get all fragments that should be used for the main page
        BoardListFragment boardListFragment = BoardListFragment.newInstance();
        fragments.add(boardListFragment);
        String queryString = ErgonautContentProvider.TASKS_QUERY_URI.toString();
        TaskListFragment taskListFragment = TaskListFragment.newInstance(queryString);
        fragments.add(taskListFragment);
        return fragments;
    }

    @Override
    public void onAddTaskSelected() {
        Intent addTaskIntent = new Intent(MainActivity.this, AddTaskActivity.class);
        startActivityForResult(addTaskIntent, REQUEST_CODE_ADD_TASK);
    }

    @Override
    public void onTaskSubmitted(Task t) {}
}
