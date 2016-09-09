package com.ergonautics.ergonautics.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ergonautics.ergonautics.R;
import com.ergonautics.ergonautics.app.ICreationFragmentCallback;

/**
 * Created by patrickgrayson on 8/30/16.
 */
public class TaskNamerFragment extends Fragment {
    private static final String TAG = "ERGONAUT-TASK";
    private EditText mTaskNameEdit;
    private ICreationFragmentCallback mCallback;

    //TODO: this fragment will allow user to select from a list of all the task names used previously
    // Or create a new task name


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (ICreationFragmentCallback) getParentFragment();
        } catch (ClassCastException ex) {
            Log.w(TAG, "onAttach: fragment created with no callback");
        }
    }

    public static TaskNamerFragment getInstance(){
        return new TaskNamerFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_task_namer, container, false);
        mTaskNameEdit = (EditText) root.findViewById(R.id.edit_task_name);
        mTaskNameEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER  && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    if(mCallback != null){
                        Log.d(TAG, "onEditorAction: Reporting name to callback");
                        mCallback.onDataSubmitted();
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        });


        return root;
    }

    public String getName(){
        return mTaskNameEdit.getText().toString();
    }
}
