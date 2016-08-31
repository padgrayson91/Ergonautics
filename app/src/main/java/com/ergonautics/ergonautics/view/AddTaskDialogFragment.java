package com.ergonautics.ergonautics.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ergonautics.ergonautics.R;
import com.ergonautics.ergonautics.app.ITaskListUpdateListener;
import com.ergonautics.ergonautics.app.NewTaskPageAdapter;
import com.ergonautics.ergonautics.models.Task;
import com.ergonautics.ergonautics.storage.LocalStorage;

import java.util.ArrayList;

/**
 * Created by patrickgrayson on 8/30/16.
 * Dialog for allowing users to create a new task
 */
public class AddTaskDialogFragment extends DialogFragment {
    private static final String TAG = "ERGONAUT-TASKDLG";

    private ViewPager mTaskCreationPager; //View pager containing fragments for each step in the task creation process
    private Button mPrevButton;
    private Button mNextButton;

    private NewTaskPageAdapter mNewTaskAdapter;
    private ITaskListUpdateListener mTaskSubmissionListener;
    private Task toConstruct;

    public static AddTaskDialogFragment newInstance() {
        AddTaskDialogFragment f = new AddTaskDialogFragment();
        //TODO: take in an optional ID of an existing task
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //TODO: check if we are updating from an existing task, and if so set all of our data from that
        try {
            mTaskSubmissionListener = (ITaskListUpdateListener) getActivity();
        } catch (ClassCastException ex){
            throw new ClassCastException("Activity containing AddTaskDialogFragment must implement ITaskListUpdateListener");
        }

        toConstruct = new Task(""); //Create an empty task to start
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_task, container, false);
        setStyle(STYLE_NORMAL, android.R.style.Theme);
        mTaskCreationPager = (ViewPager) root.findViewById(R.id.pager_task_creation);
        mPrevButton = (Button) root.findViewById(R.id.button_prev);
        mNextButton = (Button) root.findViewById(R.id.button_next);
        mNewTaskAdapter = new NewTaskPageAdapter(getChildFragmentManager());
        mTaskCreationPager.setAdapter(mNewTaskAdapter);
        mTaskCreationPager.addOnPageChangeListener(new OnCreationPageChangeListener());
        mPrevButton.setOnClickListener(mPagerControlClickListener);
        mNextButton.setOnClickListener(mPagerControlClickListener);
        loadTaskCreationSteps();
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalStorage storage = LocalStorage.getInstance(getContext());
        if(storage != null){
            storage.setTaskInProgress(toConstruct);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalStorage storage = LocalStorage.getInstance(getContext());
        if(storage != null){
            toConstruct = storage.getTaskInProgress();
        } else {
            Log.w(TAG, "onResume: Unable to retrieve task in progress, starting a new one");
            toConstruct = new Task("");
        }
    }

    private void loadTaskCreationSteps(){
        ArrayList<Fragment> fragments = new ArrayList<>();
        TaskNamerFragment tnf = TaskNamerFragment.getInstance();
        //TODO: other fragments representing different creation steps
        fragments.add(tnf);
        mNewTaskAdapter.setFragments(fragments);
        mNewTaskAdapter.notifyDataSetChanged();

    }

    private void updateTaskData(int fragmentPosition){
        //Update the Task object to include the data the user just entered

        //Get the fragment currently displayed and pull data based on what type it is
        Fragment current = mNewTaskAdapter.getItem(fragmentPosition);
        if(current instanceof TaskNamerFragment){
            toConstruct.setDisplayName(((TaskNamerFragment)current).getName());
        }
    }

    private void handleSubmit(){
        //Send the task back to the Activity so it can decide what to do with it
        mTaskSubmissionListener.onTaskSubmitted(toConstruct);
        LocalStorage storage = LocalStorage.getInstance(getContext());
        storage.setTaskInProgress(null);
        dismiss();
    }

    private void restoreData(int fragmentPosition){
        //TODO: pull relevant data from the Task object and send it to the fragment
    }

    private View.OnClickListener mPagerControlClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button_next:
                    int item = mTaskCreationPager.getCurrentItem();
                    updateTaskData(item);
                    if(item == mNewTaskAdapter.getCount() - 1){
                        handleSubmit();
                    } else {
                        mTaskCreationPager.setCurrentItem(item + 1);
                    }
                    break;
                case R.id.button_prev:
                    item = mTaskCreationPager.getCurrentItem();
                    mTaskCreationPager.setCurrentItem(item - 1);
                    break;
            }
        }
    };

    private class OnCreationPageChangeListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            if(position == 0){
                mPrevButton.setEnabled(false);
            } else {
                mPrevButton.setEnabled(true);
                if(position == mNewTaskAdapter.getCount() - 1){
                    mNextButton.setText(getResources().getString(R.string.text_button_submit));
                }
            }
            restoreData(position);
        }
    }
}
