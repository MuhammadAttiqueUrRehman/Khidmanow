package com.aic.khidmanow;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import layout.Tab1;
import layout.Tab2;


public class tabPagerAdapter extends FragmentStatePagerAdapter {
    int mNumTabs;

    public tabPagerAdapter(FragmentManager fm, int NumTabs) {
        super(fm);
        mNumTabs = NumTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                Tab1 tab1 = new Tab1();
                return tab1;
            case 1:
                Tab2 tab2 = new Tab2();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumTabs;
    }

}
