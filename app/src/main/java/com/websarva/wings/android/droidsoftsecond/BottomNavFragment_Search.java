package com.websarva.wings.android.droidsoftsecond;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.websarva.wings.android.droidsoftsecond.databinding.FragmentBottomNavBinding;


public class BottomNavFragment_Search extends Fragment {
    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    private FragmentBottomNavBinding mBinding;
    ViewPagerAdapter viewPagerAdapter;
    ViewPager2 viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mBinding = FragmentBottomNavBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //-----FragmentStateAdapterのインスタンス化　&　ViewPagerとのリンク
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager = mBinding.pager;
        viewPager.setAdapter(viewPagerAdapter);
        TabLayout tabLayout = mBinding.tabLayout;

        //-----TabLayoutとViewPagerのリンク
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText("OBJECT" + (position + 1))
        ).attach();//TabLayoutMediator を作成して TabLayout を ViewPager2 にリンクし、アタッチ。

        //-----Tabのレイアウト設定 *TabとViewPagerのリンク後に実施*
        tabLayout.getTabAt(0).setText("オススメ");
        tabLayout.getTabAt(1).setText("スケジュール");
        tabLayout.getTabAt(2).setText("マップ");

        //TODO BottomNavFragment_Search001 アニメーションを追加し、選択中のタブが中心に来るようにする。
    }
/*
    @Override//フラグメント画面削除時
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }*/
}


// Instances of this class are fragments representing a single
    // object in our collection.


