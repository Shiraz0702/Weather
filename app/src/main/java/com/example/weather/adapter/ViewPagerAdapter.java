package com.example.weather.adapter;


import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragment = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragment.get(position);
    }

    @Override
    public int getCount() {
        return mFragment.size();
    }

    @NonNull
    public void addFrag(Fragment fragment) {
        mFragment.add(fragment);
        this.notifyDataSetChanged();
    }

    public void delete(int position) {
        mFragment.remove(position);
        this.notifyDataSetChanged();
    }

    public void refresh(Fragment fragment, int position) {
        mFragment.remove(position);
        mFragment.add(position, fragment);
        this.notifyDataSetChanged();
    }
}
