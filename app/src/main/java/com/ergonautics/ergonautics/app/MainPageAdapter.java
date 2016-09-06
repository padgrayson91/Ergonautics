package com.ergonautics.ergonautics.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by patrickgrayson on 9/6/16.
 */
public class MainPageAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mFragments;

    public MainPageAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    public void updateFragment(int index, Fragment f){
        mFragments.set(index, f);
    }
}
