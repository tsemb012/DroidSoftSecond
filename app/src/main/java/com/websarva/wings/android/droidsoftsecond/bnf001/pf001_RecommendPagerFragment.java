package com.websarva.wings.android.droidsoftsecond.bnf001;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.websarva.wings.android.droidsoftsecond.databinding.Pf001FragmentPagerRecommendBinding;

public class pf001_RecommendPagerFragment extends Fragment {
    public static final String ARG_OBJECT = "object";
    private Pf001FragmentPagerRecommendBinding mBinding;

    public static pf001_RecommendPagerFragment newInstance() {
        return new pf001_RecommendPagerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mBinding = Pf001FragmentPagerRecommendBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

/*     @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
         ((TextView) view.findViewById(android.R.id.text1))
        .setText(Integer.toString(args.getInt(ARG_OBJECT)));
    }*/

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = new ViewModelProvider(this).get(pvm001_RecommendPagerViewModel.class);
        // TODO: Use the ViewModel
    }
}