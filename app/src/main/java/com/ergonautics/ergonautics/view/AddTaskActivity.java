package com.ergonautics.ergonautics.view;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ergonautics.ergonautics.R;
import com.ergonautics.ergonautics.app.ITaskListUpdateListener;
import com.ergonautics.ergonautics.models.Board;
import com.ergonautics.ergonautics.models.DBModelHelper;
import com.ergonautics.ergonautics.models.Task;
import com.ergonautics.ergonautics.presenter.TaskPresenter;
import com.ergonautics.ergonautics.storage.DBHelper;
import com.ergonautics.ergonautics.storage.LocalStorage;

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
    public void onTaskSubmitted(Task t) {
        Board current = LocalStorage.getInstance(this).getSelectedBoard();
        if(current == null) {
            //TODO: explicit DB Access here is a No No in general, should force board selection
            Cursor allBoards = new DBHelper(AddTaskActivity.this).getAllBoards();
            allBoards.moveToFirst();
            current = DBModelHelper.getBoardFromCursor(allBoards);
            allBoards.close();
        }
        mPresenter.addData(t, current.getBoardId());
        setResult(RESULT_OK);
        finish();
    }
}
