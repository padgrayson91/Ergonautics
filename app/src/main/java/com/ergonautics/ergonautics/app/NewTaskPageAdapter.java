package com.ergonautics.ergonautics.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by patrickgrayson on 8/30/16.
 */
public class NewTaskPageAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mFragments;

    public NewTaskPageAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    public void setFragments(ArrayList<Fragment> update){
        mFragments = update;
    }

    public void addFragment(Fragment toAdd){
        mFragments.add(toAdd);
    }
}
