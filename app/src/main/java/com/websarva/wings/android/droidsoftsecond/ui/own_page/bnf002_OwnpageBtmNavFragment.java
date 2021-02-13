package com.websarva.wings.android.droidsoftsecond.ui.own_page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.websarva.wings.android.droidsoftsecond.R;
import com.websarva.wings.android.droidsoftsecond.databinding.Bnf002FragmentBtmNavOwnpageBinding;
import com.websarva.wings.android.droidsoftsecond.ui.adapter.GroupAdapter;

public class bnf002_OwnpageBtmNavFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener, GroupAdapter.OnGroupSelectedListener {

    private Bnf002FragmentBtmNavOwnpageBinding mBinding;
    private NavController navController;
    private NavHostFragment navHostFragment;
    private AppBarConfiguration appBarConfiguration;
    private FirebaseAuth mAuth;
    private String UID;
    private FirebaseFirestore mFirestore;
    private GroupAdapter mAdapter;
    private Query mQuery;
    private static final int LIMIT = 50;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //----EnableMenu on Fragment
        setHasOptionsMenu(true);

        //-----EntryPoint of FirebaseStore
        mFirestore = FirebaseFirestore.getInstance();

        //-----EntryPoint of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        //-----Query for Adapter
        mQuery = mFirestore.collection("groups")
                //.whereEqualTo() TODO Group内にProfilesSubcollectionを形成し、参照する。
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(LIMIT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = Bnf002FragmentBtmNavOwnpageBinding.inflate(inflater, container,false);

        //-----Adapter Objects
        mAdapter = new GroupAdapter(mQuery, this) {
            @Override
            protected void onDataChanged() {
                if (getItemCount() == 0) {
                    //mBinding.recyclerGroups.setVisibility(View.GONE);
                    //mBinding.viewEmpty.setVisibility(View.VISIBLE); TODO データ変化時のview対応を記述する。
                } else {
                    //mBinding.recyclerGroups.setVisibility(View.VISIBLE);
                    //mBinding.viewEmpty.setVisibility(View.GONE);
                }
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //Menu Generate for AppBar
        //mBinding.includeBnf002.toolbar.inflateMenu(R.menu.menu_main);//TODO UXを検討し、必要があればBNF002独自の仕様を検討する。
        /*mBinding.include.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.sign_out) {
                AuthUI.getInstance().signOut(getContext());
                startSignIn();
            } else {
                NavController navController
                        = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            }
            return NavigationUI.onNavDestinationSelected(item, navController)
                    || super.onOptionsItemSelected(item);
        });*/

        //-----ViewObjects for Navigation
        CollapsingToolbarLayout layout = mBinding.includeBnf002.collapsingToolbarLayout;
        MaterialToolbar toolbar = mBinding.includeBnf002.toolbar;
        DrawerLayout drawer = mBinding.drawerLayout;
        NavigationView navView = mBinding.navView;
        BottomNavigationView bottomNav = mBinding.bottomNav;

        //-----NavUI Objects
        navHostFragment = (NavHostFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).setOpenableLayout(drawer).build();

        //-----SetUp for NavigationUI
        NavigationUI.setupWithNavController(layout,toolbar,navController,appBarConfiguration);
        NavigationUI.setupWithNavController(navView,navController);
        NavigationUI.setupWithNavController(bottomNav, navController);

    }

    @Override
    public void onStart() {
        super.onStart();
        if(mAdapter != null){
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //TODO Filterを設定するので、本クラス独自のMenuを作成し、埋め込む。
        //inflater.inflate(R.menu.xxx, inflater);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //TODO Filterクリック時の処理を記述する。
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bnf001_SearchBtmNavFragment:
                NavHostFragment.findNavController(navHostFragment).navigate(R.id.bnf001_SearchBtmNavFragment);
                break;

            case R.id.bnf002_OwnpageBtmNavFragment:
                break;

                //TODO bnf003 bnf004の処理を追加。
        }
        return true;
    }

    @Override
    public void onGroupSelected(DocumentSnapshot group, View view) {
        String groupId = group.getId();
    //TODO Navigatio　を設定する。
        // NavDirections action = bnf001_SearchBtmNavFragmentDirections.actionBnf001SearchToF003DetailGroupFragment2(groupId);
        // Navigation.findNavController(view).navigate(action);

    }
}