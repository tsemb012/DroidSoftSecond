package com.websarva.wings.android.droidsoftsecond;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.websarva.wings.android.droidsoftsecond.databinding.A001ActivityMainBinding;
import com.websarva.wings.android.droidsoftsecond.viewmodel.MainActivityViewModel;

import java.util.Arrays;
import java.util.List;

public class a001_MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private A001ActivityMainBinding mBinding;
    private FirebaseFirestore mFirestore;
    //private GroupAdapter mAdaptert;//TODO M001 リスト作成時にカスタムアダプタークラスを作成
    //private MainActivityViewModel mViewModel; //TODO M002 サインイン処理＆onStop時のためのViewModelを作成する
    private Query mQuery;//TODO　M003 理解度が低いので、学習しながらコードを記述していく
    private DialogFragment dialog;
    private MainActivityViewModel mViewModel;
    private FirebaseAuth mAuth;
    private List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

    AppBarConfiguration appBarConfiguration;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //-----レイアウト・ナビゲーション

        //-----DataBinding
        mBinding = DataBindingUtil.setContentView(this, R.layout.a001_activity_main);//OR ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        //-----Navigation for AppBar

        CollapsingToolbarLayout layout = mBinding.collapsingToolbarLayout;
        Toolbar toolbar = mBinding.toolbar;
        setSupportActionBar(toolbar);//アクションバーを使うときはこれがセット
        DrawerLayout drawer = mBinding.drawerLayout;
        NavigationView navView = mBinding.navView;//ドロワーの具体的な記述をインスタンス化
        BottomNavigationView bottomNav = mBinding.bottomNav;

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();//NavHostFragmentから取り出したnavControllerでフラグメントの操作
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).setDrawerLayout(drawer).build();//ConfigurationでNavigationUIの構成パターンを設定
        NavigationUI.setupWithNavController(layout, toolbar, navController, appBarConfiguration);//パーツごとに実引数が異なっているので、公式サイトを要確認
        NavigationUI.setupWithNavController(navView, navController);
        NavigationUI.setupWithNavController(bottomNav, navController);
        /*TODO M005 実験的にドロワーやビューワーなどを再現するが、フラグメントごとにツールバーを変更する場合は、それぞれのフラグメントで設定する必要がある。    */

        //-----Firebase

        //----ViewModel
        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        // Enable FireStore logging
        FirebaseFirestore.setLoggingEnabled(true); //SDK(AndroidStudio)がFireStoreにログインすることを可能にする。

        // Firestoreのインスタンス化
        mFirestore = FirebaseFirestore.getInstance();

        // Get ${LIMIT} restaurants//TODO M006　読解し、Groupに置き換える
        /*mQuery = mFirestore.collection("restaurants")
                .orderBy("avgRating", Query.Direction.DESCENDING)
                .limit(LIMIT);*/


        //-----RecyclerView //TODO M007　後日あらためてRecyclerViewのパーツを作成する
    }


    @Override
    protected void onStart() {
        super.onStart();


        //サインイン有無の分岐処理//サインインしていないのがTrueならば、StartSignIn
        //TODO M008 サンプルコードを使用して分岐処理をひとまず行うが、Navigationでの処理もできないか確認する。
        if (shouldStartSignIn()) {
            startSignIn();
            return;
        }
    }

    private boolean shouldStartSignIn() {
        return (!mViewModel.getIsSigningIn() && FirebaseAuth.getInstance().getCurrentUser() == null);
    }

    private void startSignIn() {
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {//出発点で設定したIDを確認する
            IdpResponse response = IdpResponse.fromResultIntent(data);//プロバイダーからのメッセージをここで取得
            mViewModel.setIsSigningIn(false);//SigningInにFalseを返すが、nullでは無いのでサインイン済確認には引っ掛からなくなる。

            if (resultCode != RESULT_OK) {
                if (response == null) {
                    finish();//プロバイダーからのレスポンスがなかった場合、Activityを終了させる
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
        AlertDialog dialog = new AlertDialog.Builder(this)
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
                        finish();
                    }
                }).create();

        dialog.show();
    }

    //-----NavigationUIにアップボタンを設定
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //-----onCreateOptionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }//アクションバーを使う際は必須


    //-----NavigationとMenuの紐付け設定
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sign_out) {
            AuthUI.getInstance().signOut(this);
            startSignIn();
        } else {
            NavController navController
                    = Navigation.findNavController(this, R.id.nav_host_fragment);
        }
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }


    @SuppressLint("NonConstantResourceId")
    public void btn_onclick_f002(View view) {
        switch (view.getId()) {
            case R.id.btn_to_groupDetailBar_group_type:
                dialog = new d001_GroupTypeDialog();//タイプ選択のダイアログインスタンス化
                dialog.show(getSupportFragmentManager(), "group_type");
                break;
            case R.id.btn_to_groupDetailBar_activity_area:
                dialog = new d002_ActivityAreaDialog();//オンラインおよび市町村選択ダイアログのインスタンス化
                dialog.show(getSupportFragmentManager(), "activity_area");
                break;
            case R.id.btn_to_groupDetailBar_facility_environment:
                dialog = new d003_FacilityEnvironmentDialog();//施設ダイアログのインスタンス化
                dialog.show(getSupportFragmentManager(), "facility_environment");
                break;
            case R.id.btn_to_groupDetailBar_learning_frequency:
                dialog = new d004_LeaningFrequencyDialog();//施設ダイアログのインスタンス化
                dialog.show(getSupportFragmentManager(), "learning_frequency");
                break;
            case R.id.btn_to_groupDetailBar_age_range:
                dialog = new d005_AgeRangeDialog();//施設ダイアログのインスタンス化
                dialog.show(getSupportFragmentManager(), "age_range");
                break;
            case R.id.btn_to_groupDetailBar_number_persons:
                dialog = new d006_NumberPersonsDialog();//施設ダイアログのインスタンス化
                dialog.show(getSupportFragmentManager(), "number_persons");
                break;
            case R.id.btn_to_groupDetailBar_gender_restriction:
                SwitchMaterial sm = findViewById(R.id.gender_restriction_switch);//施設ダイアログのインスタンス化
                sm.setOnCheckedChangeListener(
                        new CompoundButton.OnCheckedChangeListener() {
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                Toast.makeText(
                                        a001_MainActivity.this,
                                        isChecked ? "性別設定をOnにしました。" : "性別設定をOffにしました。",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        });
                break;
        }
    }
}
    //省略系のOnClickはやはりFragment上では起動しないので、Activity上に配置する。
    //理由は不明だけど、ダイアログフラグメントを匿名クラスにすることはできないんじゃないかな？
    //実際に見たら参考にしてみよう。実際に
