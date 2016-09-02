package com.ergonautics.ergonautics.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ergonautics.ergonautics.R;
import com.ergonautics.ergonautics.models.Task;
import com.ergonautics.ergonautics.presenter.IPresenterCallback;
import com.ergonautics.ergonautics.presenter.TaskPresenter;
import com.ergonautics.ergonautics.view.TaskViewHolder;

/**
 * Created by patrickgrayson on 8/24/16.
 */
public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskViewHolder> implements IPresenterCallback {
    private static final String TAG = "ERGONAUT-ADAPT";
    private static TaskPresenter mPresenter;
    private String mQuery;

    public TaskRecyclerAdapter(String query, Context c){
        if(mPresenter == null) {
            mPresenter = new TaskPresenter(query, c, this);
        }
        mQuery = query;
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
        Task t = (Task) mPresenter.getData(position);
        holder.setViews(t);
    }

    @Override
    public int getItemCount() {
        return mPresenter.present().getCount();
    }

    public void setQuery(String query) {
        mQuery = query;
        mPresenter.newQuery(query);
        notifyDataSetChanged();
    }

    @Override
    public void onDataChanged() {
        Log.d(TAG, "onDataChanged: Data changed in adapter, notifying...");
        notifyDataSetChanged();
    }

    public TaskPresenter getPresenter(){
        return mPresenter;
    }
}
