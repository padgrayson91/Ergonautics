package com.ergonautics.ergonautics.presenter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ergonautics.ergonautics.models.DBModelHelper;
import com.ergonautics.ergonautics.models.ModelConstants;
import com.ergonautics.ergonautics.models.Task;
import com.ergonautics.ergonautics.storage.ErgonautContentProvider;
import com.ergonautics.ergonautics.storage.UriHelper;
import com.ergonautics.ergonautics.view.SwipeableRecyclerViewTouchListener;

/**
 * Created by patrickgrayson on 9/2/16.
 */
public class TaskPresenter extends BasePresenter implements SwipeableRecyclerViewTouchListener.SwipeListener {
    private static final String TAG = "ERGONAUT-PRESENT";

    private Cursor mCursor;
    private Context mContext;
    private String mQuery;

    public TaskPresenter(Context c, IPresenterCallback callback){
        mQuery = ErgonautContentProvider.TASKS_QUERY_URI.toString();
        mContext = c;
    }

    public TaskPresenter(@Nullable String query, @NonNull Context c, @Nullable IPresenterCallback callback){
        super.setCallback(callback);
        mQuery = query;
        mContext = c;
    }

    @Override
    @Nullable
    //TODO: this is being called too many times, need to trace that
    public Cursor present() {
        if(mQuery != null) {
            mCursor = mContext.getContentResolver().query(Uri.parse(mQuery), null, null, null, null);
            Log.d(TAG, String.format("present: Presenting %d tasks", mCursor.getCount()));
        }
        return mCursor;
    }

    @Override
    public Object getData(int position) {
        if(mQuery != null) {
            mCursor = mContext.getContentResolver().query(Uri.parse(mQuery), null, null, null, null);
            mCursor.moveToPosition(position);
            Task data = DBModelHelper.getTaskFromCursor(mCursor);
            return data;
        }
        return null;
    }

    @Override
    public void onDataRemoved(Object... data) {
        try {
            Task removed = (Task) data[0];
            Uri deleteUri = Uri.withAppendedPath(ErgonautContentProvider.TASKS_QUERY_URI, removed.getTaskId());
            mContext.getContentResolver().delete(deleteUri, null, null);
            Log.d(TAG, "onDataRemoved: Removed task with id: " + removed.getTaskId());
            try {
                super.getCallback().notifyDataRemoved(removed);
            } catch (NullPointerException ignored){
                Log.w(TAG, "onDataRemoved: presenter didn't have a callback");
            }
        } catch (ClassCastException ex) {
            throw new ClassCastException("TaskPresenter must only be used with Task objects! Got: " + data.getClass());
        } catch (IndexOutOfBoundsException ex){
            Log.w(TAG, "onDataRemoved: no data provided");
        }
    }

    @Override
    public void onDataAdded(Object... data) {
        try {
            Task added = (Task) data[0];
            String boardId = (String) data[1];
            Uri addUri = UriHelper.getTaskInsertUri(boardId);
            Uri result = mContext.getContentResolver().insert(addUri, DBModelHelper.getContentValuesForTask(added));
            String taskId = result.getLastPathSegment();
            Log.d(TAG, "onDataAdded: Added task with id: " + added.getTaskId());
            try {
                super.getCallback().notifyDataAdded(taskId);
            } catch (NullPointerException ignored){
                Log.w(TAG, "onDataAdded: presenter didn't have a callback");
            }
        } catch (ClassCastException ex) {
            throw new ClassCastException("Adding task requires a Task object and a String! Got: " + data.getClass());
        } catch (IndexOutOfBoundsException ex){
            Log.w(TAG, "onDataAdded: insufficient data provided, did you forget a board id?");
        }
    }

    @Override
    public void newQuery(String query) {
        mQuery = query;
        mCursor = mContext.getContentResolver().query(Uri.parse(query), null, null, null, null);
        try {
            super.getCallback().notifyDataUpdated();
        } catch (NullPointerException ignored){}
    }

    @Override
    public boolean canSwipeLeft(int position) {
        return true;
    }

    @Override
    public boolean canSwipeRight(int position) {
        return true;
    }

    @Override
    public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
        if(mQuery == null){
            return;
        }
        mCursor = mContext.getContentResolver().query(Uri.parse(mQuery), null, null, null, null);
        for(int position : reverseSortedPositions){
            mCursor.moveToPosition(position);
            Task data = DBModelHelper.getTaskFromCursor(mCursor);
            onDataRemoved(data);
        }
    }

    @Override
    public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
        if(mQuery == null){
            return;
        }
        mCursor = mContext.getContentResolver().query(Uri.parse(mQuery), null, null, null, null);
        for(int position : reverseSortedPositions){
            mCursor.moveToPosition(position);
            Task data = DBModelHelper.getTaskFromCursor(mCursor);
            onDataRemoved(data);
        }
    }

    public void startTask(int position){
        if(mQuery == null){
            return;
        }
        mCursor = mContext.getContentResolver().query(Uri.parse(mQuery), null, null, null, null);
        mCursor.moveToPosition(position);
        Task t = DBModelHelper.getTaskFromCursor(mCursor);
        t.setStartedAt(System.currentTimeMillis());
        t.setResumedAt(System.currentTimeMillis());
        t.setStatus(ModelConstants.STATUS_IN_PROGRESS);
        mContext.getContentResolver().update(Uri.withAppendedPath(ErgonautContentProvider.TASKS_QUERY_URI, t.getTaskId()), DBModelHelper.getContentValuesForTask(t), null, null);
        mCursor = mContext.getContentResolver().query(Uri.parse(mQuery), null, null, null, null);
        try {
            super.getCallback().notifyDataUpdated();
        } catch (NullPointerException ignored){}
    }

    public void pauseTask(int position){
        if(mQuery == null){
            return;
        }
        mCursor = mContext.getContentResolver().query(Uri.parse(mQuery), null, null, null, null);
        mCursor.moveToPosition(position);
        Task t = DBModelHelper.getTaskFromCursor(mCursor);
        long timeSinceLastResume = System.currentTimeMillis() - t.getResumedAt();
        t.setTimeElapsed(t.getTimeElapsed() + timeSinceLastResume);
        t.setStatus(ModelConstants.STATUS_PAUSED);
        mContext.getContentResolver().update(Uri.withAppendedPath(ErgonautContentProvider.TASKS_QUERY_URI, t.getTaskId()), DBModelHelper.getContentValuesForTask(t), null, null);
        mCursor = mContext.getContentResolver().query(Uri.parse(mQuery), null, null, null, null);
        try {
            super.getCallback().notifyDataUpdated();
        } catch (NullPointerException ignored){}
    }

    public void resumeTask(int position){
        if(mQuery == null){
            return;
        }
        mCursor = mContext.getContentResolver().query(Uri.parse(mQuery), null, null, null, null);
        mCursor.moveToPosition(position);
        Task t = DBModelHelper.getTaskFromCursor(mCursor);
        t.setResumedAt(System.currentTimeMillis());
        t.setStatus(ModelConstants.STATUS_IN_PROGRESS);
        mContext.getContentResolver().update(Uri.withAppendedPath(ErgonautContentProvider.TASKS_QUERY_URI, t.getTaskId()), DBModelHelper.getContentValuesForTask(t), null, null);
        mCursor = mContext.getContentResolver().query(Uri.parse(mQuery), null, null, null, null);
        try {
            super.getCallback().notifyDataUpdated();
        } catch (NullPointerException ignored){}
    }

    public void finishTask(int position){
        if(mQuery == null){
            return;
        }
        mCursor = mContext.getContentResolver().query(Uri.parse(mQuery), null, null, null, null);
        mCursor.moveToPosition(position);
        Task t = DBModelHelper.getTaskFromCursor(mCursor);
        long timeSinceLastResume = System.currentTimeMillis() - t.getResumedAt();
        t.setTimeElapsed(t.getTimeElapsed() + timeSinceLastResume);
        t.setStatus(ModelConstants.STATUS_COMPLETED);
        mContext.getContentResolver().update(Uri.withAppendedPath(ErgonautContentProvider.TASKS_QUERY_URI, t.getTaskId()), DBModelHelper.getContentValuesForTask(t), null, null);
        mCursor = mContext.getContentResolver().query(Uri.parse(mQuery), null, null, null, null);
        try {
            super.getCallback().notifyDataUpdated();
        } catch (NullPointerException ignored){}
    }
}
