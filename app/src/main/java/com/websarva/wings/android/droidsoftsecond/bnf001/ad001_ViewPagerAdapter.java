package com.websarva.wings.android.droidsoftsecond.bnf001;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import java.util.HashMap;
import java.util.Map;

//-----FragmentStateAdapterの派生クラス *同クラスでしか使用しないので、クラス内に含める*
public class ad001_ViewPagerAdapter extends FragmentStateAdapter {

    //ページ数
    private final int PAGE_COUNT = 3;
    Map<Integer, Fragment> map = new HashMap<>();

    public ad001_ViewPagerAdapter(Fragment fragment) {//フラグメン"BottomNavFragment_Search"を受け取る
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new pf001_RecommendPagerFragment();
                map.put(0, fragment);
                return fragment;
            case 1:
                fragment = new pf002_SchedulePagerFragment();
                map.put(1, fragment);
                return fragment;
            case 2:
                // Return a NEW fragment instance in createFragment(int)
                fragment = new pf003_MapPagerFragment();
                Bundle args = new Bundle();
                // Our object is just an integer -P;
                args.putInt(pf003_MapPagerFragment.ARG_OBJECT, position + 1);
                fragment.setArguments(args);
                map.put(2, fragment);
                return fragment;
        }
        fragment = new pf001_RecommendPagerFragment();
        map.put(0, fragment);
        return fragment;
        }



    @Override
    public int getItemCount() {
        return PAGE_COUNT;
    }
}
