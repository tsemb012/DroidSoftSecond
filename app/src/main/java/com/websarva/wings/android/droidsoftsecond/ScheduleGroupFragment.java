package com.websarva.wings.android.droidsoftsecond;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class ScheduleGroupFragment extends Fragment {
    public static final String ARG_OBJECT = "object";
    private ScheduleGroupViewModel mViewModel;

    public static ScheduleGroupFragment newInstance() {
        return new ScheduleGroupFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.schedule_group_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        // ((TextView) view.findViewById(android.R.id.text1))
        //.setText(Integer.toString(args.getInt(ARG_OBJECT)));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ScheduleGroupViewModel.class);
        // TODO: Use the ViewModel
    }

}


