package com.ergonautics.ergonautics.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ergonautics.ergonautics.R;
import com.ergonautics.ergonautics.models.ModelConstants;
import com.ergonautics.ergonautics.models.Task;

/**
 * Created by patrickgrayson on 8/24/16.
 */
public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final String TAG = "ERGONAUT-VIEW";
    private TextView mDisplayNameText;
    private Button mActionButton1;
    private Button mActionButton2;
    private TextView mElapsedTimeText;
    private Context mContext;
    private Task mTask;
    private ITaskViewHolderClickCallback mCallback;
    public TaskViewHolder(View itemView, Context c, ITaskViewHolderClickCallback callback) {
        super(itemView);
        mContext = c;
        mCallback = callback;
        mDisplayNameText = (TextView) itemView.findViewById(R.id.text_display_name);
        mElapsedTimeText = (TextView) itemView.findViewById(R.id.text_elapsed_time);
        mActionButton1 = (Button) itemView.findViewById(R.id.button_task_action_1);
        mActionButton2 = (Button) itemView.findViewById(R.id.button_task_action_2);
        mActionButton1.setOnClickListener(this);
        mActionButton2.setOnClickListener(this);
    }

    public void setViews(Task t){
        mTask = t;
        mDisplayNameText.setText(t.getDisplayName());
        switch (t.getStatus()){
            case ModelConstants.STATUS_UNSTARTED:
                mElapsedTimeText.setVisibility(View.GONE);
                mActionButton2.setVisibility(View.GONE);
                mActionButton1.setVisibility(View.VISIBLE);
                mActionButton1.setText(mContext.getResources().getString(R.string.text_button_start));
                break;
            case ModelConstants.STATUS_IN_PROGRESS:
                mActionButton2.setVisibility(View.VISIBLE);
                mActionButton1.setVisibility(View.VISIBLE);
                mElapsedTimeText.setVisibility(View.GONE);
                mActionButton2.setText(mContext.getResources().getString(R.string.text_button_finish));
                mActionButton1.setText(mContext.getResources().getString(R.string.text_button_pause));
                break;
            case ModelConstants.STATUS_PAUSED:
                mActionButton2.setVisibility(View.VISIBLE);
                mActionButton1.setVisibility(View.VISIBLE);
                mElapsedTimeText.setVisibility(View.GONE);
                mActionButton2.setText(mContext.getResources().getString(R.string.text_button_finish));
                mActionButton1.setText(mContext.getResources().getString(R.string.text_button_resume));
                break;
            case ModelConstants.STATUS_COMPLETED:
                mActionButton1.setVisibility(View.GONE);
                mActionButton2.setVisibility(View.GONE);
                mElapsedTimeText.setVisibility(View.VISIBLE);
                //TODO: more formal way of getting the time
                long hours = t.getTimeElapsed()/(1000L * 60L * 60L);
                long remaining = t.getTimeElapsed() - (hours * 1000L * 60L * 60L);
                long minutes = remaining/(1000L * 60L);
                remaining = remaining - (minutes * 1000L * 60L);
                long seconds = remaining/1000L;
                mElapsedTimeText.setText(String.format("%d hr %d min %d sec", hours, minutes, seconds));
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (mTask.getStatus()){
            case ModelConstants.STATUS_UNSTARTED:
                if(view.getId() == R.id.button_task_action_1) {
                    mCallback.onClickStart(getAdapterPosition());
                } else {
                    Log.w(TAG, "onClick: unknown view action");
                }
                break;
            case ModelConstants.STATUS_IN_PROGRESS:
                if(view.getId() == R.id.button_task_action_1) {
                    mCallback.onClickPause(getAdapterPosition());
                } else {
                    mCallback.onClickFinish(getAdapterPosition());
                }
                break;
            case ModelConstants.STATUS_PAUSED:
                if(view.getId() == R.id.button_task_action_1) {
                    mCallback.onClickResume(getAdapterPosition());
                } else {
                    mCallback.onClickFinish(getAdapterPosition());
                }
                break;
        }
    }

    public interface ITaskViewHolderClickCallback {
        void onClickStart(int position);
        void onClickPause(int position);
        void onClickResume(int position);
        void onClickFinish(int position);
    }
}
