package com.websarva.wings.android.droidsoftsecond;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.websarva.wings.android.droidsoftsecond.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private FirebaseFirestore mFirestore;
    //private GroupAdapter mAdaptert;//TODO M001 リスト作成時にカスタムアダプタークラスを作成
    //private MainActivityViewModel mViewModel; //TODO M002 サインイン処理＆onStop時のためのViewModelを作成する
    private Query mQuery;//TODO　M003 理解度が低いので、学習しながらコードを記述していく

    AppBarConfiguration appBarConfiguration;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //-----DataBinding
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);//OR ActivityMainBinding.inflate(getLayoutInflater());
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
}