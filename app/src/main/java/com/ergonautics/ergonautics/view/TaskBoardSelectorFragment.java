package com.ergonautics.ergonautics.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ergonautics.ergonautics.R;
import com.ergonautics.ergonautics.app.BoardRecyclerAdapter;
import com.ergonautics.ergonautics.app.ICreationFragmentCallback;
import com.ergonautics.ergonautics.models.Board;

/**
 * Created by patrickgrayson on 9/9/16.
 */
public class TaskBoardSelectorFragment extends Fragment implements BoardRecyclerAdapter.BoardSelectedListener{
    private static final String TAG = "ERGONAUT-TASK";
    private ICreationFragmentCallback mCallback;
    private RecyclerView mRecycler;
    private BoardRecyclerAdapter mAdapter;
    private Board mSelectedBoard;

    public TaskBoardSelectorFragment(){}


    public static TaskBoardSelectorFragment newInstance(){
        return new TaskBoardSelectorFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (ICreationFragmentCallback) getParentFragment();
        } catch (ClassCastException ex) {
            Log.w(TAG, "onAttach: fragment created with no callback");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_task_board_selector, container, false);
        mRecycler = (RecyclerView) root.findViewById(R.id.recycler_board);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new BoardRecyclerAdapter(getContext(), R.layout.recycler_item_board);
        mAdapter.addBoardSelectedListener(this);
        mRecycler.setAdapter(mAdapter);
        return root;
    }

    public Board getSelection(){
        return mSelectedBoard;
    }

    @Override
    public void onBoardSelected(Board b) {
        mSelectedBoard = b;
        mCallback.onDataSubmitted();
    }
}
