package com.ergonautics.ergonautics.view;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ergonautics.ergonautics.R;
import com.ergonautics.ergonautics.app.ITaskListUpdateListener;
import com.ergonautics.ergonautics.models.Board;
import com.ergonautics.ergonautics.models.DBModelHelper;
import com.ergonautics.ergonautics.models.Task;
import com.ergonautics.ergonautics.presenter.TaskPresenter;
import com.ergonautics.ergonautics.storage.DBHelper;

public class AddTaskActivity extends AppCompatActivity implements ITaskListUpdateListener {
    private static final String TAG = "ERGONAUT-ADDTASK";

    private static TaskPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        if(mPresenter == null){
            mPresenter = new TaskPresenter(null, this, null);
        }
    }

    @Override
    public void onAddTaskSelected() {}

    @Override
    public void onTaskSubmitted(Task t) {
        //TODO: should have a current board active when creating tasks, explicit DB Access here is a No No in general
        Cursor allBoards = new DBHelper(AddTaskActivity.this).getAllBoards();
        allBoards.moveToFirst();
        Board temp = DBModelHelper.getBoardFromCursor(allBoards);
        allBoards.close();
        mPresenter.addData(t, temp.getBoardId());
        Log.d(TAG, "onTaskSubmitted: Added task : " + t.getDisplayName());
        setResult(RESULT_OK);
        finish();
    }
}
