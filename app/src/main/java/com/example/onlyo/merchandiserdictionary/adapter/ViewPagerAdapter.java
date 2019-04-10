package com.example.onlyo.merchandiserdictionary.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.onlyo.merchandiserdictionary.fragment.FavoriteFagment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by onlyo on 4/7/2019.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public void removeFragment() {
        Log.e("aaa : ", String.valueOf(mFragmentList.size()));
        if(mFragmentList.size() > 4){
            for (int j = 4; j < mFragmentList.size(); j++){
                mFragmentList.remove(j);
                mFragmentTitleList.remove(j);
            }
        }
    }

    public void addFragmentforother(int i, Fragment fragment, String title) {
        Log.e("aaa : ",fragment.toString());
        mFragmentList.add(i,fragment);
        mFragmentTitleList.add(i,title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }


}
