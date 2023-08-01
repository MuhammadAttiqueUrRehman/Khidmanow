package com.aic.khidmanow;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import layout.postcouponlist;
import layout.postvoucherlist;

public class postTabPagerAdapter  extends FragmentStatePagerAdapter {

    int mNumTabs;

    public postTabPagerAdapter(FragmentManager fm, int NumTabs){
        super(fm);
        mNumTabs = NumTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch(i) {
            case 0:
                postcouponlist tab1 = new postcouponlist();
                return tab1;
            case 1:
                postvoucherlist tab2 = new postvoucherlist();
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
