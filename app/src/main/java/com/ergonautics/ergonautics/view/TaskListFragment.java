package com.ergonautics.ergonautics.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ergonautics.ergonautics.R;
import com.ergonautics.ergonautics.app.ITaskListUpdateListener;
import com.ergonautics.ergonautics.app.TaskRecyclerAdapter;

/**
 * Created by patrickgrayson on 8/24/16.
 */
public class TaskListFragment extends Fragment{
    private static final String TAG = "ERGONAUT-TASKS";
    private static final String ARGS_KEY_QUERY = "query";
    private RecyclerView mTasksRecycler;
    private FloatingActionButton mAddTaskButton;
    private ITaskListUpdateListener mTaskAddSelectedListener;
    private TaskRecyclerAdapter mAdapter;

    public static TaskListFragment getInstance(String query){
        TaskListFragment fragment = new TaskListFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_KEY_QUERY, query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mTaskAddSelectedListener = (ITaskListUpdateListener) getActivity();
        } catch (ClassCastException ex) {
            throw  new ClassCastException("Activity must implement ITaskAddSelectedListener in order to use TaskListFragment");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Creating View for TaskList");
        View root = inflater.inflate(R.layout.fragment_task_list, container, false);
        mTasksRecycler = (RecyclerView) root.findViewById(R.id.recycler_tasks);
        mAddTaskButton = (FloatingActionButton) root.findViewById(R.id.button_add_task);
        mAddTaskButton.setOnClickListener(mAddTaskButtonListener);

        //Get the query String
        Bundle args = getArguments();
        if (args != null) {
            String query = args.getString(ARGS_KEY_QUERY);
            mAdapter = new TaskRecyclerAdapter(query, getContext());
        } else {
            throw new IllegalStateException("Cannot initialize TaskListFragment with no arguments! Did you forget to use getInstance?");
        }
        mTasksRecycler.setAdapter(mAdapter);
        mTasksRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mTasksRecycler.addOnItemTouchListener(new SwipeableRecyclerViewTouchListener(mTasksRecycler, mAdapter.getPresenter()));
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    private View.OnClickListener mAddTaskButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mTaskAddSelectedListener.onAddTaskSelected();
        }
    };
}
