package com.ergonautics.ergonautics.presenter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.ergonautics.ergonautics.models.Board;
import com.ergonautics.ergonautics.models.DBModelHelper;
import com.ergonautics.ergonautics.models.Task;
import com.ergonautics.ergonautics.storage.ErgonautContentProvider;
import com.ergonautics.ergonautics.storage.UriHelper;

import java.util.ArrayList;

import io.realm.RealmList;

/**
 * Created by patrickgrayson on 9/6/16.
 */
public class BoardPresenter extends BasePresenter<Board>{
    private static final String TAG = "ERGONAUT-PRESENT";

    private Cursor mCursor;
    private String mQuery;
    private ArrayList<Board> mBoards;
    private Context mContext;

    public BoardPresenter(Context c, IPresenterCallback callback){
        super.addCallback(callback);
        mContext = c;
        mQuery = ErgonautContentProvider.BOARDS_QUERY_URI.toString();
        newQuery(mQuery);
    }

    @Override
    public ArrayList<Board> present() {
        getArrayListFromCursor();
        return mBoards;
    }

    @Override
    public Board getData(int position) {
        if(mBoards == null){
            return null;
        } else {
            return mBoards.get(position);
        }
    }

    @Override
    public void removeData(Board... data) {
        try {
            Board toRemove =  data[0];
            String id = toRemove.getBoardId();
            mContext.getContentResolver().delete(Uri.withAppendedPath(ErgonautContentProvider.BOARDS_QUERY_URI, id), null, null);
            try {
                super.getCallback().notifyDataRemoved(toRemove);
            } catch (NullPointerException ignored){
                Log.w(TAG, "removeData: presenter didn't have a callback");
            }
            refresh();
        } catch (IndexOutOfBoundsException ex){
            throw new IllegalArgumentException("method removeData requires at least one object of type Board to remove");
        }

    }

    @Override
    public void removeData(int position) {
        Board toRemove = mBoards.get(position);
        String id = toRemove.getBoardId();
        mContext.getContentResolver().delete(Uri.withAppendedPath(ErgonautContentProvider.BOARDS_QUERY_URI, id), null, null);
        try {
            super.getCallback().notifyDataRemoved(toRemove);
        } catch (NullPointerException ignored){
            Log.w(TAG, "removeData: presenter didn't have a callback");
        }
        refresh();
    }

    @Override
    public void addData(Object... data) {
        try {
            Board toAdd = (Board) data[0];
            Uri insertionUri = mContext.getContentResolver().insert(ErgonautContentProvider.BOARDS_INSERT_URI, DBModelHelper.getContentValuesForBoard(toAdd));
            String id = insertionUri.getLastPathSegment();
            try {
                super.getCallback().notifyDataAdded(id);
            } catch (NullPointerException ignored){
                Log.w(TAG, "removeData: presenter didn't have a callback");
            }
            refresh();
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException("Expected argument of type Board but got " + data[0].getClass());
        } catch (IndexOutOfBoundsException ex){
            throw new IllegalArgumentException("method addData requires at least one object of type Board to add");
        }
    }

    @Override
    public void updateData(Board... data) {
        //TODO: use contentresolver to update board and notify callback
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

    @Override
    public void refresh() {
        if(mQuery != null) {
            newQuery(mQuery);
        }
    }

    @Override
    public int getCount() {
        if(mBoards == null){
            return 0;
        } else {
            return mBoards.size();
        }
    }

    private void getArrayListFromCursor(){
        mBoards = new ArrayList<>();
        if(mCursor == null){
            return;
        } else {
            mCursor.moveToFirst();
            while(!mCursor.isAfterLast()){
                Board toAdd = DBModelHelper.getBoardFromCursor(mCursor);
                String id = toAdd.getBoardId();
                Cursor tasksCursor = mContext.getContentResolver().query(UriHelper.getTaskForBoardQueryUri(id), null, null, null, null);
                tasksCursor.moveToFirst();
                RealmList<Task> boardTasks = new RealmList<>();
                while(!tasksCursor.isAfterLast()){
                    boardTasks.add(DBModelHelper.getTaskFromCursor(tasksCursor));
                    tasksCursor.moveToNext();
                }
                tasksCursor.close();
                toAdd.setTasks(boardTasks);
                Log.d(TAG, "getArrayListFromCursor: Added " + boardTasks.size() + " tasks to board " + toAdd.getDisplayName());
                mBoards.add(toAdd);
                mCursor.moveToNext();
            }
        }
    }
}
