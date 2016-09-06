package com.ergonautics.ergonautics.presenter;

import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by patrickgrayson on 9/2/16.
 */
public abstract class BasePresenter<T> {
    private IPresenterCallback mCallback;

    public abstract ArrayList<T> present();
    public abstract T getData(int position);
    public abstract void removeData(T... data);
    public abstract void addData(Object... data);
    public abstract void updateData(T... data);
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
