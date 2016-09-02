package com.ergonautics.ergonautics.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ergonautics.ergonautics.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskTimeEstimateFragment extends Fragment {
    //TODO: switch from ListView to RadioGroup
    private static final String TAG = "ERGONAUT-VIEW";

    private RadioGroup mTimeOptionsRadioGroup;
    private long [] mTimeOptions;
    private String [] mTimesDisplayed;


    public TaskTimeEstimateFragment() {
        // Required empty public constructor
    }

    public static TaskTimeEstimateFragment newInstance(){
        return new TaskTimeEstimateFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_task_time_estimate, container, false);
        mTimeOptionsRadioGroup = (RadioGroup) root.findViewById(R.id.radio_group_time_options);
        String [] timesMap = getResources().getStringArray(R.array.array_time_suggestions);
        mTimeOptions = new long[timesMap.length];
        mTimesDisplayed = new String[timesMap.length];
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for(int i = 0; i < timesMap.length; i++){
            String timeItem = timesMap[i];
            //For each string in the resource, separate out the human readable time and the long it maps to
            String [] tokens = timeItem.split("\\|");
            RadioButton radioButtonView = new RadioButton(getContext());
            radioButtonView.setText(tokens[0]);
            mTimeOptionsRadioGroup.addView(radioButtonView, params);
            mTimeOptions[i] = Long.valueOf(tokens[1]);
        }
        return root;
    }

    public long getSelection(){
        try {
            return mTimeOptions[mTimeOptionsRadioGroup.indexOfChild(mTimeOptionsRadioGroup.findViewById(mTimeOptionsRadioGroup.getCheckedRadioButtonId()))];
        } catch (IndexOutOfBoundsException ex){
            return 1000L * 60L * 30L;
        }
    }

}
