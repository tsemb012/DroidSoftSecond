package com.websarva.wings.android.droidsoftsecond.bnf001;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.websarva.wings.android.droidsoftsecond.R;
import com.websarva.wings.android.droidsoftsecond.databinding.Bnf001FragmentBtmNavSearchBinding;
import com.websarva.wings.android.droidsoftsecond.viewmodel.MainActivityViewModel;

import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class bnf001_SearchBtmNavFragment extends Fragment {

    private static final int RC_SIGN_IN = 9001;
    private MainActivityViewModel mViewModel;
    private @NonNull Bnf001FragmentBtmNavSearchBinding mBinding;
    private ViewPager2 viewPager;
    private ad001_ViewPagerAdapter ad001ViewPagerAdapter;
    private NavController navController;
    private NavHostFragment navHostFragment;
    private AppBarConfiguration appBarConfiguration;
    private List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());
    private CollectionReference mProfilesRef;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //----EnableMenu フラグメント上でメニューを有効化
        setHasOptionsMenu(true);

        //----ViewModel
        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        //-----Enable FireStore logging　SDK(AndroidStudio)がFireStoreにログインすることを可能にする。
        FirebaseFirestore.setLoggingEnabled(true);

        //-----EntryPoint of FirebaseStore
        mFirestore = FirebaseFirestore.getInstance();
        mProfilesRef = mFirestore.collection("profiles");

        //-----EntryPoint of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = Bnf001FragmentBtmNavSearchBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //-----MenuGenerate for AppBar
        mBinding.include.toolbar.inflateMenu(R.menu.menu_main);
        mBinding.include.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.sign_out) {
                AuthUI.getInstance().signOut(getContext());
                startSignIn();
            } else {
                NavController navController
                        = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            }
            return NavigationUI.onNavDestinationSelected(item, navController)
                    || super.onOptionsItemSelected(item);
        });

        //-----ViewObjects for Navigation
        CollapsingToolbarLayout layout = mBinding.include.collapsingToolbarLayout;
        Toolbar toolbar = mBinding.include.toolbar;
        BottomNavigationView bottomNav = mBinding.bottomNav;


        //-----NavUI Objects
        navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();//NavHostFragmentから取り出したnavControllerでフラグメントの操作
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();

        //-----Setup for NavigationUI

        NavigationUI.setupWithNavController(layout, toolbar, navController, appBarConfiguration);//パーツごとに実引数が異なっているので、公式サイトを要確認

        NavigationUI.setupWithNavController(bottomNav, navController);

        //-----FragmentStateAdapter//-----ViewPager
        ad001ViewPagerAdapter = new ad001_ViewPagerAdapter(this);
        viewPager = mBinding.pager;
        viewPager.setAdapter(ad001ViewPagerAdapter);

        //-----TabLayout&ViewPager Linking
        TabLayout tabLayout = mBinding.tabLayout;
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText("OBJECT" + (position + 1))
        ).attach();//TabLayoutMediator を作成して TabLayout を ViewPager2 にリンクし、アタッチ。

        //-----Tabのレイアウト設定 *TabとViewPagerのリンク後に実施*
        tabLayout.getTabAt(0).setText("オススメ");
        tabLayout.getTabAt(1).setText("スケジュール");
        tabLayout.getTabAt(2).setText("マップ");
        //TODO BottomNavFragment_Search001 アニメーションを追加し、選択中のタブが中心に来るようにする。


        //mBinding.navView

        //-----NavHostFragmentを用いた画面遷移
        mBinding.floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                NavDirections action =
                        bnf001_SearchBtmNavFragmentDirections.actionBnf001SearchToF002AddGroupFragment2();
                //アクションにArgumentを引き渡す。
                //action.setNum((new Random()).nextInt());
                Navigation.findNavController(v).navigate(action);
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        //-----SignInBranchingProcessing
        if (shouldStartSignIn()) {
            startSignIn();
        }

        //-----Check if they have their profile document.
        mProfilesRef
                .document(mAuth.getCurrentUser().getUid())//当該ユーザーのプロフィールデータがあるか確認
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        if(!snapshot.exists()){
                            NavDirections action = bnf001_SearchBtmNavFragmentDirections.actionBnf001SearchToF004AddProfileFragment();
                            navHostFragment.getNavController().navigate(R.id.action_bnf001_Search_to_f004_AddProfileFragment);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //TODO 通信失敗時の処理を記載。→　別のアプリを参考にする。OR　通信環境がよくなってからやり直させる。
                    }
                });

        return;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sign_out) {
            AuthUI.getInstance().signOut(getContext());
            startSignIn();
        } else {
            NavController navController
                    = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        }
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    private boolean shouldStartSignIn() {
        return (!mViewModel.getIsSigningIn() && FirebaseAuth.getInstance().getCurrentUser() == null);
    }

    public void startSignIn() {
        // Sign in with FirebaseUI
        Intent intent = AuthUI
                .getInstance()
                .createSignInIntentBuilder()
                /*.setAvailableProviders(Collections.singletonList(
                        new AuthUI.IdpConfig.EmailBuilder().build()))*/
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .setTheme(R.style.AppTheme_LogIn)
                .setLogo(R.drawable.ic_baseline_school_24)
                .build();
        // Create and launch sign-in intent
        startActivityForResult(intent, RC_SIGN_IN);
        mViewModel.setIsSigningIn(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {//出発点で設定したIDを確認する
            IdpResponse response = IdpResponse.fromResultIntent(data);//プロバイダーからのメッセージをここで取得
            mViewModel.setIsSigningIn(false);//SigningInにFalseを返すが、nullでは無いのでサインイン済確認には引っ掛からなくなる。

            if (resultCode != RESULT_OK) {
                if (response == null) {
                    getActivity().finish();//プロバイダーからのレスポンスがなかった場合、Activityを終了させる
                } else if (response.getError() != null
                        && response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSignInErrorDialog(R.string.meessage_no_network);
                } else {
                    showSignInErrorDialog(R.string.message_unknown);
                }
            }
        }
    }

    //サインのエラーを表示するダイアログ
    private void showSignInErrorDialog(@StringRes int message) {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.title_sign_in_error)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.option_retry, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startSignIn();
                    }
                })
                .setNegativeButton(R.string.option_exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().finish();
                    }
                }).create();

        dialog.show();
    }

    private void startWriteProfile() {
    }
}

    /*
    @Override//フラグメント画面削除時
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }*/




