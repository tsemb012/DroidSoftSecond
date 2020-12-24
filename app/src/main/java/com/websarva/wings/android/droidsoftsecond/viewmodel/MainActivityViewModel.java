package com.websarva.wings.android.droidsoftsecond.viewmodel;

import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {//サインイン状態のゲッターとセッター

    private boolean mIsSigningIn;


    //サインイン状態の有無を返す。
    public boolean getIsSigningIn(){
        return mIsSigningIn;
    }

    //サインインされた段階で、mSigningInにTrueを渡す。//ViewModelが破棄されるまでTrue
    public void setIsSigningIn(boolean mIsSigningIn) {
        this.mIsSigningIn = mIsSigningIn;
    }
}


