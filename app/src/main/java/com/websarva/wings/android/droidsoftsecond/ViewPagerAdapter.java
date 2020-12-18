package com.websarva.wings.android.droidsoftsecond;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

//-----FragmentStateAdapterの派生クラス *同クラスでしか使用しないので、クラス内に含める*
public class ViewPagerAdapter extends FragmentStateAdapter {

    //ページ数
    private final int PAGE_COUNT = 3;

    public ViewPagerAdapter(Fragment fragment) {//フラグメン"BottomNavFragment_Search"を受け取る
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return a NEW fragment instance in createFragment(int)
        Fragment fragment = new DemoObjectFragment();
        Bundle args = new Bundle();
        // Our object is just an integer :-P;
        args.putInt(DemoObjectFragment.ARG_OBJECT, position + 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return PAGE_COUNT;
    }
}
