package com.websarva.wings.android.droidsoftsecond;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.websarva.wings.android.droidsoftsecond.databinding.F003FragmentGroupDetailBinding;
import com.websarva.wings.android.droidsoftsecond.model.Group;

public class f003_DetailGroupFragment extends Fragment implements View.OnClickListener,ProfileAdapter.OnProfileSelectedListener {


    private String groupId;
    private DocumentReference mGroupRef;
    private FirebaseFirestore mFirestore;
    private NavController navController;
    private @NonNull F003FragmentGroupDetailBinding mBinding;
    private AppBarConfiguration appBarConfiguration;
    private Group group;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirestore = FirebaseFirestore.getInstance();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //-----ViewObjects for Navigation
        CollapsingToolbarLayout layout = mBinding.collapsingToolbarLayout;
        Toolbar toolbar = mBinding.MaterialToolbar;
        toolbar.setTitle("GroupName");


        //-----NavUI Objects
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();//NavHostFragmentから取り出したnavControllerでフラグメントの操作
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();//ConfigurationでNavigationUIの構成パターンを設定

        //-----Setup for NavigationUI
        NavigationUI.setupWithNavController(layout, toolbar, navController, appBarConfiguration);//パーツごとに実引数が異なっているので、公式サイトを要確認

        //-----getValue from bnf001
        groupId= f003_DetailGroupFragmentArgs.fromBundle(getArguments()).getGroupId();//Navigation受け取り側のマスターピース
        mGroupRef = mFirestore.collection("groups").document(groupId);
        mGroupRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //TODO　全体のレイアウト修正(画像の下にタイトル・アップボタンが隠れる不具合、フラグメント名が表示される不具合、全体のサイズ・配置・カラーリング)
                group = documentSnapshot.toObject(Group.class);
                GlideApp.with(mBinding.groupImage)
                        .load(FirebaseStorage.getInstance().getReference(group.getPhotoRefPath()))
                        .into(mBinding.groupImage);
                mBinding.groupName.setText(group.getGroupName());
                mBinding.textViewGroupIntro.setText(group.getGroupIntro());
                mBinding.textView2GroupType.setText(group.getGroupType());
                mBinding.textView2ActivityArea.setText(group.getActivityAreaPrefecture());//TODO 市区町村まで入れるよう修正する　
                mBinding.textView2FacilityEnvironment.setText(group.getFacilityEnvironment());
                mBinding.textView2LearningFrequency.setText(group.getLearningFrequency());
                //TODO 不具合修正　mBinding.textView2AgeRange.setText(String.format("%s~%s",group.getAgeRangeMin(),group.getAgeRangeMax()));
                //TODO 不具合修正　mBinding.textView2NumberPersons.setText(group.getNumberPersonMax());
                //TODO 後日Genderを追記する。
            }
        });//同スコープ外にPOJOGetterロジックを記述した場合、Nullが発生する。原因は不明だが、データ取得までのタイムラグが原因ではないかと推察する。
        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = F003FragmentGroupDetailBinding.inflate(inflater, container, false);


        return mBinding.getRoot();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onProfileSelected(DocumentSnapshot profile, View view) {
        String profileId = profile.getId();
        //TODO Navigationで各人のプロフィールに飛ばす。
    }
}