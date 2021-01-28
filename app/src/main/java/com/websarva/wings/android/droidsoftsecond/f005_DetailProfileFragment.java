package com.websarva.wings.android.droidsoftsecond;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.websarva.wings.android.droidsoftsecond.databinding.F005FragmentProfileDetailBinding;
import com.websarva.wings.android.droidsoftsecond.model.Profile;
import com.websarva.wings.android.droidsoftsecond.viewmodel.MainActivityViewModel;


public class f005_DetailProfileFragment extends Fragment implements View.OnClickListener {

    private FirebaseFirestore mFireStore;
    private FirebaseStorage mFireStorage;
    private NavController navController;
    private F005FragmentProfileDetailBinding mBinding;
    private AppBarConfiguration appBarConfiguration;
    private Profile profile;
    private MainActivityViewModel model;
    private String pUID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFireStore = FirebaseFirestore.getInstance();
        mFireStorage = FirebaseStorage.getInstance();

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

        mBinding.btnProfileEdit.setOnClickListener(this);

        CollapsingToolbarLayout layout = mBinding.collapsingToolbarLayoutProfile;
        Toolbar toolbar = mBinding.MaterialToolbarProfile;

        NavHostFragment navHostFragment = (NavHostFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(layout, toolbar, navController, appBarConfiguration);
    }

    @Override
    public void onStart() {
        super.onStart();
        model = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        if(model.isOtherProfile() == true){//TODO f002Adapter側でTrueにする。
            mBinding.btnProfileEdit.setVisibility(View.GONE);
            pUID =getArguments().getString("pUID");
            Log.i("check2021/01/24",getArguments().getString("pUID"));
        }
        else{
            pUID = FirebaseAuth.getInstance().getUid();
        }

        //TODO UIDとProfileIDの整合性をとって判断する。
        //結局でもどっかからPathをもらわないといけないわけで、
        mFireStore.collection("profiles").document(pUID).get().addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {

                    profile = snapshot.toObject(Profile.class);
                    //String backgroundPhotoPath = profile.getBackgroundPhotoPath();
                    String profilePhotoPath = profile.getProfilePhotoPath();
                    String comment = profile.getComment();
                    String userName = profile.getUserName();
                    String gender = profile.getGender();
                    String age = String.valueOf(profile.getAge());
                    String residentialArea = profile.getResidentialArea();

                    mBinding.textViewProfileComment.setText(comment);
                    mBinding.profileNicknameProfile.setText(userName);
                    mBinding.textView2ProfileGender.setText(gender);
                    mBinding.textView2ProfileAge.setText(age);
                    mBinding.textView2ProfileResidentialArea.setText(residentialArea);
                    GlideApp.with(mBinding.profileImage)
                            .load(mFireStorage.getReference(profilePhotoPath))
                            .into(mBinding.profileImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("check2021/01/24","失敗");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        model.setOtherProfile(false);//画面破棄する際にFalseを投げる。
    }

    @Override
    public void onClick(View v) {
        NavDirections action = f005_DetailProfileFragmentDirections.actionF005DetailProfileFragmentToF006EditProfileFragment();
        Navigation.findNavController(v).navigate(action);
    }
}