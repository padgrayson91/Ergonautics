package com.ergonautics.ergonautics.app;

import android.support.v7.widget.RecyclerView;

import com.ergonautics.ergonautics.presenter.BasePresenter;
import com.ergonautics.ergonautics.view.SwipeableRecyclerViewTouchListener;

/**
 * Created by patrickgrayson on 9/9/16.
 */
public class SimpleSwipeListener implements SwipeableRecyclerViewTouchListener.SwipeListener {
    private BasePresenter mPresenter;

    public SimpleSwipeListener(BasePresenter presenter){
        mPresenter = presenter;
    }

    @Override
    public boolean canSwipeLeft(int position) {
        return true;
    }

    @Override
    public boolean canSwipeRight(int position) {
        return true;
    }

    @Override
    public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
        for(int i: reverseSortedPositions){
            mPresenter.removeData(i);
        }
    }

    @Override
    public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
        for(int i: reverseSortedPositions){
            mPresenter.removeData(i);
        }
    }
}
