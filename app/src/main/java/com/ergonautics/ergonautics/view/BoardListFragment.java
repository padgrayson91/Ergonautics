package com.ergonautics.ergonautics.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ergonautics.ergonautics.R;
import com.ergonautics.ergonautics.app.BoardRecyclerAdapter;

/**
 * Fragment for displaying the list of the user's boards
 * This is not currently in use, but this may serve in place of the main navigation drawer on
 * Tablets
 */
public class BoardListFragment extends Fragment {
    private static final String TAG = "ERGONAUT-BOARDLIST";

    //TODO: need a listener/method for when the task list is updated so we can refresh task counts for each board

    private RecyclerView mRecycler;
    private BoardRecyclerAdapter mAdapter;


    public BoardListFragment() {
        // Required empty public constructor
    }

    public static BoardListFragment newInstance(){
        return new BoardListFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_board_list, container, false);
        mRecycler = (RecyclerView) root.findViewById(R.id.recycler_board);
        mAdapter = new BoardRecyclerAdapter(getContext(), R.layout.recycler_item_board);
        try {
            mAdapter.addBoardSelectedListener((BoardRecyclerAdapter.BoardSelectedListener) getActivity());
        } catch (ClassCastException ex){
            Log.w(TAG, "onCreateView: No listener attached to board list!");
        }
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        return root;
    }

}
