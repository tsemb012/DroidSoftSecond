package com.websarva.wings.android.droidsoftsecond.bnf001;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.websarva.wings.android.droidsoftsecond.GroupAdapter;
import com.websarva.wings.android.droidsoftsecond.databinding.Pf001FragmentPagerRecommendBinding;

public class pf001_RecommendPagerFragment extends Fragment implements GroupAdapter.OnGroupSelectedListener{

    private Pf001FragmentPagerRecommendBinding mBinding;
    private FirebaseFirestore mFirestore;
    private GroupAdapter mAdapter;
    private Query mQuery;
    private static final int LIMIT = 50;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirestore = FirebaseFirestore.getInstance();
        mQuery = mFirestore.collection("groups")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(LIMIT);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = Pf001FragmentPagerRecommendBinding.inflate(inflater, container, false);
        mAdapter = new GroupAdapter(mQuery, this) {

            @Override
            protected void onDataChanged() {//TODO PF001　データチェンジの中身を記載
                /*if (getItemCount() == 0) {
                    mBinding.recyclerGroups.setVisibility(View.GONE);
                    mBinding.viewEmpty.setVisibility(View.VISIBLE);
                } else {
                    mBinding.recyclerGroups.setVisibility(View.VISIBLE);
                    mBinding.viewEmpty.setVisibility(View.GONE);
                }*/
            }

            @Override
            protected void onError(FirebaseFirestoreException error) {
                super.onError(error);
            }
        };
        mBinding.recyclerGroups.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerGroups.setAdapter(mAdapter);

        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        //FireStoreAdapterのセット
        if(mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    @Override
    public void onGroupSelected(DocumentSnapshot group) {
    //TODO PF001　それぞれのカードタップ時の処理を記入
    }
}