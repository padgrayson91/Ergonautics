package com.ergonautics.ergonautics.app;

/**
 * Created by patrickgrayson on 9/9/16.
 * Used to communicate actions within a fragment used for creating/editing
 * tasks, boards, etc. (e.g. one in a viewpager)
 * with the parent fragment or activity containing that viewpager
 */
public interface ICreationFragmentCallback {
    void onDataSubmitted();
}
