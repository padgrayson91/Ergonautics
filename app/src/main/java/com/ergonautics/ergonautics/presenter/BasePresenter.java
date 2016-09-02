package com.ergonautics.ergonautics.presenter;

import android.database.Cursor;

/**
 * Created by patrickgrayson on 9/2/16.
 */
public abstract class BasePresenter {
    public abstract Cursor present();
    public abstract void onDataRemoved(Object... data);
    public abstract void onDataAdded(Object... data);
    public abstract void newQuery(String query);
}
