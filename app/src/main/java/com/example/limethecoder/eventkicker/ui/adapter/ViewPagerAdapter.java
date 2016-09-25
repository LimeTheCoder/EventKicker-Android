package com.example.limethecoder.eventkicker.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.limethecoder.eventkicker.ui.activity.RecycleViewFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    CharSequence titles[];
    int NumbOfTabs;

    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabs) {
        super(fm);

        this.titles = mTitles;
        this.NumbOfTabs = mNumbOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        return RecycleViewFragment.newInstance(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
