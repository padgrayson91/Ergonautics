package com.ergonautics.ergonautics.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.ergonautics.ergonautics.ErgonautAPI;
import com.ergonautics.ergonautics.R;
import com.ergonautics.ergonautics.models.Board;
import com.ergonautics.ergonautics.models.DBModelHelper;
import com.ergonautics.ergonautics.models.Task;
import com.ergonautics.ergonautics.storage.DBHelper;
import com.ergonautics.ergonautics.storage.ErgonautContentProvider;
import com.ergonautics.ergonautics.storage.UriHelper;
import com.ergonautics.ergonautics.view.AddTaskDialogFragment;
import com.ergonautics.ergonautics.view.TaskListFragment;

/**
 * App main page where user can view active tasks, current boards, notifications, etc.
 */
public class MainActivity extends AppCompatActivity implements ITaskListUpdateListener {
    private static final String TAG = "ERGONAUT-MAIN";

    //Fragment tags
    private static final String FRAGMENT_TAG_TASK_LIST = "tasks";
    private static final String FRAGMENT_TAG_TASK_CREATOR = "newtask";

    private static final int REQUEST_CODE_LOGIN = 1001;

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
            default:
                Log.d(TAG, "onActivityResult: unknown activity result");
        }
    }

    private void switchToLandingPage(){
        TaskListFragment taskListFragment = TaskListFragment.getInstance(ErgonautContentProvider.TASKS_QUERY_URI.toString());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content, taskListFragment, FRAGMENT_TAG_TASK_CREATOR);
        ft.commit();
    }

    private void switchToLoginActivity(){
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(loginIntent, REQUEST_CODE_LOGIN);
    }

    @Override
    public void onAddTaskSelected() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_TASK_CREATOR);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = AddTaskDialogFragment.newInstance();
        newFragment.show(ft, FRAGMENT_TAG_TASK_CREATOR);
    }

    @Override
    public void onTaskSubmitted(Task t) {
        //TODO: should have a current board active when creating tasks, explicit DB Access here is a No No in general
        Cursor allBoards = new DBHelper(MainActivity.this).getAllBoards();
        allBoards.moveToFirst();
        Board temp = DBModelHelper.getBoardFromCursor(allBoards);
        allBoards.close();

        Uri insertUri = UriHelper.getTaskInsertUri(temp.getBoardId());
        getContentResolver().insert(insertUri, DBModelHelper.getContentValuesForTask(t));
        Toast.makeText(MainActivity.this, "New Task Added!", Toast.LENGTH_LONG).show();
        try {
            TaskListFragment tlf = (TaskListFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_TASK_LIST);
            tlf.updateTaskList();
        } catch (NullPointerException ex) {
            Log.e(TAG, "onTaskSubmitted: Task list was no longer in fragment manager");
        }

    }
}
