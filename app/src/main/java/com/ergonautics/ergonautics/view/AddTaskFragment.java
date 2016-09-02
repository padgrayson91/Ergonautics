package com.ergonautics.ergonautics.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
public class AddTaskFragment extends Fragment {
    private static final String TAG = "ERGONAUT-TASKDLG";

    private ViewPager mTaskCreationPager; //View pager containing fragments for each step in the task creation process
    private Button mPrevButton;
    private Button mNextButton;
    private TextView mHeaderText;

    private NewTaskPageAdapter mNewTaskAdapter;
    private ITaskListUpdateListener mTaskSubmissionListener;
    private Task toConstruct;

    public static AddTaskFragment newInstance() {
        AddTaskFragment f = new AddTaskFragment();
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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_task, container, false);
        mTaskCreationPager = (ViewPager) root.findViewById(R.id.pager_task_creation);
        mPrevButton = (Button) root.findViewById(R.id.button_prev);
        mNextButton = (Button) root.findViewById(R.id.button_next);
        mNewTaskAdapter = new NewTaskPageAdapter(getChildFragmentManager());
        mTaskCreationPager.setAdapter(mNewTaskAdapter);
        mTaskCreationPager.addOnPageChangeListener(new OnCreationPageChangeListener());
        mPrevButton.setOnClickListener(mPagerControlClickListener);
        mNextButton.setOnClickListener(mPagerControlClickListener);
        mHeaderText = (TextView) root.findViewById(R.id.text_header);
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
        if(storage != null && storage.getTaskInProgress() != null){
            toConstruct = storage.getTaskInProgress();
        } else {
            Log.w(TAG, "onResume: Unable to retrieve task in progress, starting a new one");
            toConstruct = new Task("");
        }
        updateHeaderText(0);
        restoreData(0);
    }

    private void loadTaskCreationSteps(){
        ArrayList<Fragment> fragments = new ArrayList<>();
        TaskNamerFragment tnf = TaskNamerFragment.getInstance();
        TaskTimeEstimateFragment ttef = TaskTimeEstimateFragment.newInstance();
        //TODO: other fragments representing different creation steps
        fragments.add(tnf);
        fragments.add(ttef);
        mNewTaskAdapter.setFragments(fragments);
        mNewTaskAdapter.notifyDataSetChanged();

    }

    private void updateTaskData(int fragmentPosition){
        //Update the Task object to include the data the user just entered

        //Get the fragment currently displayed and pull data based on what type it is
        Fragment current = mNewTaskAdapter.getItem(fragmentPosition);
        if(current instanceof TaskNamerFragment){
            toConstruct.setDisplayName(((TaskNamerFragment)current).getName());
        } else if (current instanceof TaskTimeEstimateFragment){
            toConstruct.setTimeEstimate(((TaskTimeEstimateFragment)current).getSelection());
        }
    }

    private void handleSubmit(){
        //Let the activity know we submitted a task
        Log.d(TAG, "handleSubmit: Submitting task " + toConstruct.getDisplayName());
        mTaskSubmissionListener.onTaskSubmitted(toConstruct);
        LocalStorage storage = LocalStorage.getInstance(getContext());
        storage.setTaskInProgress(null);
    }

    private void restoreData(int fragmentPosition){
        //TODO: pull relevant data from the Task object and send it to the fragments
    }

    private void updateHeaderText(int fragmentPosition){
        Fragment f = mNewTaskAdapter.getItem(fragmentPosition);
        if(f instanceof TaskNamerFragment) {
            mHeaderText.setText(getResources().getString(R.string.text_header_name_task));
        } else if(f instanceof TaskTimeEstimateFragment) {
            mHeaderText.setText(getResources().getString(R.string.text_header_time_estimate_task));
        }
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
                mNextButton.setText(getResources().getString(R.string.text_button_next));
            } else {
                mPrevButton.setEnabled(true);
                if(position == mNewTaskAdapter.getCount() - 1){
                    mNextButton.setText(getResources().getString(R.string.text_button_submit));
                } else {
                    mNextButton.setText(getResources().getString(R.string.text_button_next));
                }
            }
            updateHeaderText(position);
            restoreData(position);
        }
    }
}
