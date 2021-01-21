package com.websarva.wings.android.droidsoftsecond;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.websarva.wings.android.droidsoftsecond.databinding.A001ActivityMainBinding;
import com.websarva.wings.android.droidsoftsecond.model.Profile;

public class a001_MainActivity extends AppCompatActivity {

    private A001ActivityMainBinding mBinding;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private String uid;
    private CollectionReference mProfilesRef;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //-----DataBinding
        mBinding = DataBindingUtil.setContentView(this, R.layout.a001_activity_main);
        setContentView(mBinding.getRoot());

        //-----EntryPoint of FirebaseStore
        mFirestore = FirebaseFirestore.getInstance();
        mProfilesRef = mFirestore.collection("profiles");


        //-----EntryPoint of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        //-----NavHost
        DrawerLayout drawer = mBinding.drawerLayout;
        NavigationView navView = mBinding.navView;


        /*navView.getHeaderView(0)
                .findViewById(R.id.nav_header)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NavDirections action = bnf001_SearchBtmNavFragmentDirections.actionBnf001SearchToF005DetailProfileFragment(uid);
                        Navigation.findNavController(v).navigate(action);
                    }
                });*/

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(navView, navController);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).setDrawerLayout(drawer).build();
        // ConfigurationでNavigationUIの構成パターンを設定

        //-----NavHeaderの生成
        ImageView headerProfile = (ImageView) mBinding.navView.getHeaderView(0).findViewById(R.id.nav_header_profilePhoto);
        //ImageView headerBackground = (ImageView)mBinding.navView.getHeaderView(0).findViewById(R.id.nav_header_background);
        TextView headerUserName = (TextView) mBinding.navView.getHeaderView(0).findViewById(R.id.nav_header_userName);
        mProfilesRef.document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {

                profile = snapshot.toObject(Profile.class);
                String profilePhotoPath = profile.getProfilePhotoPath();
                /*String  backgroundPhotoRef = profile.getBackgroundPhotoRef();*/
                String username = profile.getUserName();
                GlideApp.with(headerProfile)
                        .load(FirebaseStorage.getInstance().getReference(profilePhotoPath))
                        .into(headerProfile);
              /*  GlideApp.with(headerProfile)
                        .load(FirebaseStorage.getInstance().getReference(backgroundPhotoRef))//TODO BackgroundPhotoRefの名称をBackGroundPhotoPathに変更する。
                        .into(headerProfile);*/
                headerUserName.setText(username);

                Log.i("check2021/01/20", snapshot.get("profilePhotoPath").toString());
            }
        });



    }

    //-----NavigationUIにアップボタンを設定
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}



