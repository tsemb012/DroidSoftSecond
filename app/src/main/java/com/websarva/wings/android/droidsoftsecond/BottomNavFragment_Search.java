package com.websarva.wings.android.droidsoftsecond;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
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
                (tab, position) -> tab.setText("OBJECT " + (position + 1))
        ).attach();//TabLayoutMediator を作成して TabLayout を ViewPager2 にリンクし、アタッチ。

        //-----Tabのレイアウト設定 *TabとViewPagerのリンク後に実施*
        tabLayout.getTabAt(0).setText("オススメ");
        tabLayout.getTabAt(1).setText("スケジュール");
        tabLayout.getTabAt(2).setText("マップ");

        //TODO BottomNavFragment_Search001 アニメーションを追加し、選択中のタブが中心に来るようにする。
    }

    @Override//フラグメント画面削除時
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }


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
            switch (position) {
                case 0:
                    Fragment fragment1 = new RecommendedGroupFragment();
                    Bundle args1 = new Bundle();
                    // Our object is just an integer :-P
                    args1.putInt(RecommendedGroupFragment.ARG_OBJECT, position + 1);
                    fragment1.setArguments(args1);
                    return fragment1;
                case 1:
                    Fragment fragment2 = new ScheduleGroupFragment();
                    Bundle args2= new Bundle();
                    // Our object is just an integer :-P
                    args2.putInt(ScheduleGroupFragment.ARG_OBJECT, position + 1);
                    fragment2.setArguments(args2);
                    return fragment2;
                default:
                    Fragment fragment3 = new MapGroupFragment();
                    Bundle args3 = new Bundle();
                    // Our object is just an integer :-P
                    args3.putInt(MapGroupFragment.ARG_OBJECT, position + 1);
                    fragment3.setArguments(args3);
                    return fragment3;
            }
        }

        @Override
        public int getItemCount() {
            return PAGE_COUNT;
        }
    }
}
