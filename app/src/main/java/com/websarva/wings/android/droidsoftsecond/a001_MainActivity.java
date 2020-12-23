package com.websarva.wings.android.droidsoftsecond;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.websarva.wings.android.droidsoftsecond.databinding.A001ActivityMainBinding;

public class a001_MainActivity extends AppCompatActivity {

    private A001ActivityMainBinding mBinding;
    private FirebaseFirestore mFirestore;
    //private GroupAdapter mAdaptert;//TODO M001 リスト作成時にカスタムアダプタークラスを作成
    //private MainActivityViewModel mViewModel; //TODO M002 サインイン処理＆onStop時のためのViewModelを作成する
    private Query mQuery;//TODO　M003 理解度が低いので、学習しながらコードを記述していく
    private DialogFragment dialog;

    AppBarConfiguration appBarConfiguration;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
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