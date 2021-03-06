package com.ergonautics.ergonautics.presenter;

import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by patrickgrayson on 9/2/16.
 */
public abstract class BasePresenter<T> {
    private ArrayList<IPresenterCallback> mCallbacks;

    public abstract ArrayList<T> present();
    public abstract T getData(int position);
    public abstract void removeData(T... data);
    public abstract void removeData(int position);
    public abstract void addData(Object... data);
    public abstract void updateData(T... data);
    public abstract void newQuery(String query);
    public abstract void refresh();
    public abstract int getCount();

    public void addCallback(@Nullable IPresenterCallback callback){
        if(mCallbacks == null){
            mCallbacks = new ArrayList<>();
        }
        mCallbacks.add(callback);
    }

    @Nullable
    public ArrayList<IPresenterCallback> getCallbacks(){
        return mCallbacks;
    }
}
