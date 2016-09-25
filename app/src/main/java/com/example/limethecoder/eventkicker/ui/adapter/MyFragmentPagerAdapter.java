package com.example.limethecoder.eventkicker.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.limethecoder.eventkicker.ui.activity.SimpleFragment;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    static final int PAGE_COUNT = 3;

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return SimpleFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    public CharSequence getPageTitle(int position) {
        return "Title " + position;
    }
}
