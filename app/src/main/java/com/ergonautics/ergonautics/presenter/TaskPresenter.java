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

import java.util.ArrayList;

/**
 * Created by patrickgrayson on 9/2/16.
 * Presenter used to expose Task objects to view components
 */
@SuppressWarnings("ConstantConditions")
public class TaskPresenter extends BasePresenter<Task> implements SwipeableRecyclerViewTouchListener.SwipeListener {
    private static final String TAG = "ERGONAUT-PRESENT";

    private Cursor mCursor;
    private Context mContext;
    private String mQuery;

    private ArrayList<Task> mTasks;

    /**
     * Constructor which uses a default query to return the full list of tasks
     * @param c Context where this presenter is being used
     * @param callback Callback to receive information about data that is changed
     */
    public TaskPresenter(Context c, IPresenterCallback callback){
        super.setCallback(callback);
        mQuery = ErgonautContentProvider.TASKS_QUERY_URI.toString();
        mContext = c;
        newQuery(mQuery);
    }

    /**
     * Constructor which takes a query to return specific tasks
     * @param query the desired query.  Can be null if no tasks are desired (e.g. when only adding tasks)
     * @param c Context where this presenter is being used
     * @param callback Callback to receive information about data that is changed
     */
    public TaskPresenter(@Nullable String query, @NonNull Context c, @Nullable IPresenterCallback callback){
        super.setCallback(callback);
        mQuery = query;
        mContext = c;
        if(mQuery != null) {
            newQuery(mQuery);
        }
    }

    @Override
    public int getCount() {
        if(mTasks == null){
            return 0;
        } else {
            return mTasks.size();
        }
    }

    @Override
    @Nullable
    public ArrayList<Task> present() {
        getArrayListFromCursor();
        return mTasks;
    }

    @Override
    public Task getData(int position) {
        return mTasks.get(position);
    }

    @Override
    public void removeData(Task... data) {
        try {
            Task removed = data[0];
            Uri deleteUri = Uri.withAppendedPath(ErgonautContentProvider.TASKS_QUERY_URI, removed.getTaskId());
            mContext.getContentResolver().delete(deleteUri, null, null);
            Log.d(TAG, "removeData: Removed task with id: " + removed.getTaskId());
            try {
                super.getCallback().notifyDataRemoved(removed);
            } catch (NullPointerException ignored){
                Log.w(TAG, "removeData: presenter didn't have a callback");
            }
            refresh();
        } catch (IndexOutOfBoundsException ex){
            Log.w(TAG, "removeData: no data provided");
        }
    }

    @Override
    public void refresh() {
        if(mQuery != null) {
            newQuery(mQuery);
        }
    }

    @Override
    public void addData(Object... data) {
        try {
            Task added = (Task) data[0];
            String boardId = (String) data[1];
            Uri addUri = UriHelper.getTaskInsertUri(boardId);
            Uri result = mContext.getContentResolver().insert(addUri, DBModelHelper.getContentValuesForTask(added));
            String taskId = result.getLastPathSegment();
            Log.d(TAG, "addData: Added task with id: " + added.getTaskId());
            try {
                super.getCallback().notifyDataAdded(taskId);
            } catch (NullPointerException ignored){
                Log.w(TAG, "addData: presenter didn't have a callback");
            }
            refresh();
        } catch (ClassCastException ex) {
            throw new ClassCastException("Adding task requires a Task object and a String! Got: " + data.getClass());
        } catch (IndexOutOfBoundsException ex){
            Log.w(TAG, "addData: insufficient data provided, did you forget a board id?");
        }
    }

    @Override
    public void updateData(Task... data) {
        //TODO
    }

    @Override
    public void newQuery(String query) {
        Log.d(TAG, "newQuery: Getting data from contentresolver");
        mQuery = query;
        mCursor = mContext.getContentResolver().query(Uri.parse(query), null, null, null, null);
        getArrayListFromCursor();
        try {
            super.getCallback().notifyDataUpdated();
        } catch (NullPointerException ignored){}
    }

    //TODO: the listener should really be a separate class or implemented in the TaskRecyclerAdapter

    //////////////////////////////////////////////////////////////////////////////////////////////
    //                      Listener Methods                                                    //
    //////////////////////////////////////////////////////////////////////////////////////////////

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
            removeData(data);
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
            removeData(data);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //                      Listener Methods                                                    //
    //////////////////////////////////////////////////////////////////////////////////////////////

    public void startTask(int position){
        Task t = mTasks.get(position);
        t.setStartedAt(System.currentTimeMillis());
        t.setResumedAt(System.currentTimeMillis());
        t.setStatus(ModelConstants.STATUS_IN_PROGRESS);
        mContext.getContentResolver().update(Uri.withAppendedPath(ErgonautContentProvider.TASKS_QUERY_URI, t.getTaskId()), DBModelHelper.getContentValuesForTask(t), null, null);
        refresh();
        try {
            super.getCallback().notifyDataUpdated();
        } catch (NullPointerException ignored){}
    }

    public void pauseTask(int position){
        Task t = mTasks.get(position);
        long timeSinceLastResume = System.currentTimeMillis() - t.getResumedAt();
        t.setTimeElapsed(t.getTimeElapsed() + timeSinceLastResume);
        t.setStatus(ModelConstants.STATUS_PAUSED);
        mContext.getContentResolver().update(Uri.withAppendedPath(ErgonautContentProvider.TASKS_QUERY_URI, t.getTaskId()), DBModelHelper.getContentValuesForTask(t), null, null);
        refresh();
        try {
            super.getCallback().notifyDataUpdated();
        } catch (NullPointerException ignored){}
    }

    public void resumeTask(int position){
        Task t = mTasks.get(position);
        t.setResumedAt(System.currentTimeMillis());
        t.setStatus(ModelConstants.STATUS_IN_PROGRESS);
        mContext.getContentResolver().update(Uri.withAppendedPath(ErgonautContentProvider.TASKS_QUERY_URI, t.getTaskId()), DBModelHelper.getContentValuesForTask(t), null, null);
        refresh();
        try {
            super.getCallback().notifyDataUpdated();
        } catch (NullPointerException ignored){}
    }

    public void finishTask(int position){
        Task t = mTasks.get(position);
        long timeSinceLastResume = System.currentTimeMillis() - t.getResumedAt();
        t.setTimeElapsed(t.getTimeElapsed() + timeSinceLastResume);
        t.setStatus(ModelConstants.STATUS_COMPLETED);
        mContext.getContentResolver().update(Uri.withAppendedPath(ErgonautContentProvider.TASKS_QUERY_URI, t.getTaskId()), DBModelHelper.getContentValuesForTask(t), null, null);
        refresh();
        try {
            super.getCallback().notifyDataUpdated();
        } catch (NullPointerException ignored){}
    }

    private void getArrayListFromCursor(){
        mTasks = new ArrayList<>();
        if(mCursor == null){
            return;
        } else {
            mCursor.moveToFirst();
            while(!mCursor.isAfterLast()){
                mTasks.add(DBModelHelper.getTaskFromCursor(mCursor));
                mCursor.moveToNext();
            }
        }
    }
}
