package com.ergonautics.ergonautics.view;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ergonautics.ergonautics.R;
import com.ergonautics.ergonautics.app.TaskRecyclerAdapter;

/**
 * Created by patrickgrayson on 8/24/16.
 */
public class TaskListFragment extends Fragment {
    private static final String TAG = "ERGONAUT-TASKS";
    private static final String ARGS_KEY_QUERY = "query";
    private RecyclerView mTasksRecycler;

    public static TaskListFragment getInstance(String query){
        TaskListFragment fragment = new TaskListFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_KEY_QUERY, query);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_task_list, container, false);
        mTasksRecycler = (RecyclerView) root.findViewById(R.id.recycler_tasks);
        mTasksRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        //Perform our db query to get a cursor
        Bundle args = getArguments();
        if(args != null){
            Uri queryUri = Uri.parse(args.getString(ARGS_KEY_QUERY));
            Cursor result = getContext().getContentResolver().query(queryUri, null, null, null, null);
            TaskRecyclerAdapter adapter = new TaskRecyclerAdapter(result);
            mTasksRecycler.setAdapter(adapter);
        } else {
            throw new IllegalStateException("Cannot initialize TaskListFragment with no arguments! Did you forget to use getInstance?");
        }
        return root;
    }


}
