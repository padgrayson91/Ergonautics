package com.ergonautics.ergonautics.presenter;

import android.database.Cursor;
import android.support.annotation.Nullable;

/**
 * Created by patrickgrayson on 9/2/16.
 */
public abstract class BasePresenter {
    private IPresenterCallback mCallback;

    public abstract Cursor present();
    public abstract Object getData(int position);
    public abstract void onDataRemoved(Object... data);
    public abstract void onDataAdded(Object... data);
    public abstract void newQuery(String query);

    public void setCallback(@Nullable IPresenterCallback callback){
        mCallback = callback;
    }

    @Nullable
    public IPresenterCallback getCallback(){
        return mCallback;
    }
}
