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
    private static final String[] PAGE_TITLES = {"Tasks"};

    //Constants for identifying page indices
    public static final int PAGE_TASKS = 0;

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

    @Override
    public CharSequence getPageTitle(int position) {
        return PAGE_TITLES[position];
    }
}
