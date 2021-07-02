package com.websarva.wings.android.droidsoftsecond.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.websarva.wings.android.droidsoftsecond.R;
import com.websarva.wings.android.droidsoftsecond.databinding.A001ActivityMainBinding;
import com.websarva.wings.android.droidsoftsecond.viewmodel.MainViewModel;

public class a001_MainActivity extends AppCompatActivity {

    private MainViewModel mViewModel;
    private A001ActivityMainBinding mBinding;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //-----ViewModel
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
            //TODO コンストラクターからプロフィール情報を入れる。

        //-----DataBinding
        mBinding = DataBindingUtil.setContentView(this, R.layout.a001_activity_main);
        setContentView(mBinding.getRoot());

        //-----NavHost
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();

    }

    //-----NavigationUIにアップボタンを設定
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}



