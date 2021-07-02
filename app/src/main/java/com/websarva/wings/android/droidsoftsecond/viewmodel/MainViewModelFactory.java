package com.websarva.wings.android.droidsoftsecond.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.websarva.wings.android.droidsoftsecond.repository.MainRepository;

public class MainViewModelFactory implements ViewModelProvider.Factory {
    private MainRepository mainRepository;
    public MainViewModelFactory(MainRepository repository){
        mainRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(mainRepository);
    }
}
