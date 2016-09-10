package com.ergonautics.ergonautics.view;

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
import com.ergonautics.ergonautics.app.SimpleSwipeListener;
import com.ergonautics.ergonautics.app.TaskRecyclerAdapter;
import com.ergonautics.ergonautics.presenter.IPresenterCallback;
import com.ergonautics.ergonautics.presenter.TaskPresenter;

/**
 * Created by patrickgrayson on 8/24/16.
 */
public class TaskListFragment extends Fragment implements IPresenterCallback{
    private static final String TAG = "ERGONAUT-TASKS";
    private static final String ARGS_KEY_QUERY = "query";
    private RecyclerView mTasksRecycler;
    private TaskRecyclerAdapter mAdapter;
    private String mQuery;
    private static TaskPresenter mPresenter;

    public static TaskListFragment newInstance(String query){
        TaskListFragment fragment = new TaskListFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_KEY_QUERY, query);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Creating View for TaskList");
        View root = inflater.inflate(R.layout.fragment_task_list, container, false);
        mTasksRecycler = (RecyclerView) root.findViewById(R.id.recycler_tasks);

        //Get the query String
        Bundle args = getArguments();
        if (args != null) {
            mQuery = args.getString(ARGS_KEY_QUERY);
            //get a new presenter instance if we need one
            if(mPresenter == null){
                mPresenter = new TaskPresenter(mQuery, getContext(), this);
                if(getActivity() instanceof IPresenterCallback) {
                    mPresenter.addCallback((IPresenterCallback) getActivity());
                }
            }
            //Assign the presenter to the adapter
            mAdapter = new TaskRecyclerAdapter(mQuery, getContext(), mPresenter);
        } else {
            throw new IllegalStateException("Cannot initialize TaskListFragment with no arguments! Did you forget to use newInstance?");
        }
        mTasksRecycler.setAdapter(mAdapter);
        mTasksRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        SimpleSwipeListener listener = new SimpleSwipeListener(mAdapter.getPresenter());
        mTasksRecycler.addOnItemTouchListener(new SwipeableRecyclerViewTouchListener(mTasksRecycler, listener));
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.setQuery(mQuery);
        mAdapter.notifyDataSetChanged();
    }

    public void newQuery(String query){
        mQuery = query;
        mAdapter.setQuery(query);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyDataAdded(String id) {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyDataUpdated() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyDataRemoved(Object data) {
        //TODO: keep a local copy of the task so it can be re-inserted if the user hits undo
        mAdapter.notifyDataSetChanged();
    }
}
