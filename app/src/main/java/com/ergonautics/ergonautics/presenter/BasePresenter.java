package com.ergonautics.ergonautics.presenter;

import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by patrickgrayson on 9/2/16.
 */
public abstract class BasePresenter {
    private IPresenterCallback mCallback;

    public abstract ArrayList<Object> present();
    public abstract Object getData(int position);
    public abstract void onDataRemoved(Object... data);
    public abstract void onDataAdded(Object... data);
    public abstract void newQuery(String query);
    public abstract void refresh();
    public abstract int getCount();

    public void setCallback(@Nullable IPresenterCallback callback){
        mCallback = callback;
    }

    @Nullable
    public IPresenterCallback getCallback(){
        return mCallback;
    }
}
