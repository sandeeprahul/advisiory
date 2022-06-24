package com.example.advisoryservice.util;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.advisoryservice.ui.productquestions.ProductQuestionViewModel;
import com.example.advisoryservice.ui.result.ResultDetailViewModel;

public class SkinViewModelFactory implements ViewModelProvider.Factory {

    private Application mApplication;
    private String mParam;

    public SkinViewModelFactory(Application application, String param) {
        mApplication = application;
        mParam = param;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProductQuestionViewModel.class)) {
            return (T) new ProductQuestionViewModel(mApplication, mParam);
        } else if (modelClass.isAssignableFrom(ResultDetailViewModel.class)) {
            return (T) new ResultDetailViewModel(mApplication, mParam);
        } else
            return null;
    }
}

