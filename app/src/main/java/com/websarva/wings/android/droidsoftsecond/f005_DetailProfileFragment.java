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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.websarva.wings.android.droidsoftsecond.databinding.F005FragmentProfileDetailBinding;
import com.websarva.wings.android.droidsoftsecond.model.Profile;


public class f005_DetailProfileFragment extends Fragment implements View.OnClickListener {

    private FirebaseFirestore mFireStore;
    private FirebaseStorage mFireStorage;
    private NavController navController;
    private F005FragmentProfileDetailBinding mBinding;
    private AppBarConfiguration appBarConfiguration;
    private Profile profile;
    private String UID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFireStore = FirebaseFirestore.getInstance();
        mFireStorage = FirebaseStorage.getInstance();
        UID = f005_DetailProfileFragmentArgs.fromBundle(getArguments()).getUid();

        //TODO　どちらもUIDを渡すが、片方はログインユーザーの、片方は他人のユーザーのを渡す。
        //TODO 受け取ったUIDが自分のIDと一致する場合は編集ボタンを表示。一致しない場合は非表示。
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = F005FragmentProfileDetailBinding.inflate(inflater,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CollapsingToolbarLayout layout = mBinding.collapsingToolbarLayoutProfile;
        Toolbar toolbar = mBinding.MaterialToolbarProfile;

        NavHostFragment navHostFragment = (NavHostFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();

        NavigationUI.setupWithNavController(layout, toolbar, navController, appBarConfiguration);

        mFireStore.collection("profiles").document(UID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                profile = snapshot.toObject(Profile.class);
                String backgroundPhotoPath = profile.getBackgroundPhotoPath();
                String profilePhotoPath = profile.getProfilePhotoPath();
                String userName = profile.getUserName();
                String gender = profile.getGender();
                String age = String.valueOf(profile.getAge());
                String residentialArea = profile.getResidentialArea();

                mBinding.profileNicknameProfile.setText(userName);
                mBinding.textView2ProfileGender.setText(gender);
                mBinding.textView2ProfileAge.setText(age);
                mBinding.textView2ProfileResidentialArea.setText(residentialArea);
                GlideApp.with(mBinding.profileImage)
                        .load(mFireStorage.getReferenceFromUrl(profilePhotoPath))
                        .into(mBinding.profileImage);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}