package com.ergonautics.ergonautics.app;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ergonautics.ergonautics.R;
import com.ergonautics.ergonautics.models.Task;
import com.ergonautics.ergonautics.models.DBModelHelper;
import com.ergonautics.ergonautics.view.TaskViewHolder;

/**
 * Created by patrickgrayson on 8/24/16.
 */
public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskViewHolder> {
    private Cursor mCursor;

    public TaskRecyclerAdapter(Cursor c){
        mCursor = c;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View taskView = inflater.inflate(R.layout.recycler_item_task, parent, false);
        TaskViewHolder viewHolder = new TaskViewHolder(taskView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        Task t = DBModelHelper.getTaskFromCursor(mCursor);
        holder.setViews(t);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
}
