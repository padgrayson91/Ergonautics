package com.ergonautics.ergonautics.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ergonautics.ergonautics.R;

/**
 * Created by patrickgrayson on 8/30/16.
 */
public class TaskNamerFragment extends Fragment {
    private EditText mTaskNameEdit;

    //TODO: this fragment will allow user to select from a list of all the task names used previously
    // Or create a new task name

    public static TaskNamerFragment getInstance(){
        return new TaskNamerFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_task_namer, container, false);
        mTaskNameEdit = (EditText) root.findViewById(R.id.edit_task_name);


        return root;
    }

    public String getName(){
        return mTaskNameEdit.getText().toString();
    }
}
