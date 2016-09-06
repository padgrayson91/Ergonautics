package com.ergonautics.ergonautics.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ergonautics.ergonautics.R;
import com.ergonautics.ergonautics.app.BoardRecyclerAdapter;

/**
 * Fragment for displaying the list of the user's boards
 */
public class BoardListFragment extends Fragment {

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
        mAdapter = new BoardRecyclerAdapter(getContext());
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        return root;
    }

}
