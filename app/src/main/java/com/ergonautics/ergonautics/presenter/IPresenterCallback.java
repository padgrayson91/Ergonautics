package com.ergonautics.ergonautics.presenter;

/**
 * Created by patrickgrayson on 9/2/16.
 */
public interface IPresenterCallback {
    void notifyDataAdded(String id);
    void notifyDataUpdated();
    void notifyDataRemoved(Object data);
}
