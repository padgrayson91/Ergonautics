package com.ergonautics.ergonautics.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ergonautics.ergonautics.R;
import com.ergonautics.ergonautics.app.IBoardListUpdateListener;
import com.ergonautics.ergonautics.models.Board;
import com.ergonautics.ergonautics.presenter.BoardPresenter;

public class BoardAddActivity extends AppCompatActivity implements IBoardListUpdateListener {
    private static BoardPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_board);
        if(mPresenter == null){
            mPresenter = new BoardPresenter(BoardAddActivity.this, null);
        }
    }

    @Override
    public void onBoardSubmitted(Board b) {
        mPresenter.addData(b);
        setResult(RESULT_OK);
        finish();
    }
}
