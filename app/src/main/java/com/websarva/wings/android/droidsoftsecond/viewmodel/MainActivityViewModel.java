package com.websarva.wings.android.droidsoftsecond.viewmodel;

import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {//サインイン状態のゲッターとセッター


    private boolean mIsSigningIn;
    private int minAge;
    private int maxAge;
    private int minNumberPerson;
    private int maxNumberPerson;

    //サインイン状態の有無を返す。
    public boolean getIsSigningIn() {
        return mIsSigningIn;
    }
    public int getMinAge() {
        return minAge;
    }
    public int getMaxAge() {
        return maxAge;
    }
    public int getMinNumberPerson() {
        return minNumberPerson;
    }
    public int getMaxNumberPerson() { return maxNumberPerson; }

    //サインインされた段階で、mSigningInにTrueを渡す。//ViewModelが破棄されるまでTrue
    public void setIsSigningIn(boolean mIsSigningIn) {
        this.mIsSigningIn = mIsSigningIn;
    }
    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }
    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }
    public void setMinNumberPerson(int minNumberPerson) { this.minNumberPerson = minNumberPerson; }
    public void setMaxNumberPerson(int maxNumberPerson) { this.maxNumberPerson = maxNumberPerson; }
}



/* 実際のLiveDataの使い方がよくわからない。→　情報を読み解くには、Kotlinを覚え、実際のコードを解読する必要がある。
    private MutableLiveData<Integer> minAge;
    public MutableLiveData<Integer> getMinAge(){
        if(minAge == null){
            minAge = new MutableLiveData<>();
        }
        return minAge;
    }
*/




