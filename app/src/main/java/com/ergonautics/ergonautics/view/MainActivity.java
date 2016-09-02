package com.ergonautics.ergonautics.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ergonautics.ergonautics.ErgonautAPI;
import com.ergonautics.ergonautics.R;
import com.ergonautics.ergonautics.app.ITaskListUpdateListener;
import com.ergonautics.ergonautics.models.Task;
import com.ergonautics.ergonautics.storage.ErgonautContentProvider;
import com.ergonautics.ergonautics.storage.UriHelper;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check if user is logged in
        ErgonautAPI api = new ErgonautAPI(this);
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
                switchToTaskList(null);
                break;
            default:
                Log.d(TAG, "onActivityResult: unknown activity result");
        }
    }

    private void switchToLandingPage(){
        //TODO: switch to different pages based on user preferences
        switchToTaskList(null);
    }

    //Show a task list for a specific board or (if forBoard is null) show all tasks in the db
    private void switchToTaskList(@Nullable String forBoard){
        String queryString = "";
        if(forBoard == null){
            queryString = ErgonautContentProvider.TASKS_QUERY_URI.toString();
        } else {
            queryString = UriHelper.getTaskForBoardQueryUri(forBoard).toString();
        }
        //First check if the task list was already on screen
        TaskListFragment tlf = (TaskListFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_TASK_LIST);
        if(tlf != null){
            Log.d(TAG, "switchToTaskList: Updating task list");
            tlf.updateTaskList();
        } else {
            Log.d(TAG, "switchToTaskList: Creating new task list fragment");
            TaskListFragment taskListFragment = TaskListFragment.getInstance(queryString);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.content, taskListFragment, FRAGMENT_TAG_TASK_LIST);
            ft.commit();
        }
    }

    private void switchToLoginActivity(){
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(loginIntent, REQUEST_CODE_LOGIN);
    }

    @Override
    public void onAddTaskSelected() {
        Intent addTaskIntent = new Intent(MainActivity.this, AddTaskActivity.class);
        startActivityForResult(addTaskIntent, REQUEST_CODE_ADD_TASK);
    }

    @Override
    public void onTaskSubmitted(Task t) {}
}
