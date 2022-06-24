package com.example.advisoryservice.ui.productquestions;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.advisoryservice.data.model.ProductQuestion;
import com.example.advisoryservice.data.rest.SkinExpertRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ProductQuestionViewModel extends ViewModel {

    private SkinExpertRepository skinExpertRepository;
    private CompositeDisposable disposable;

    private final MutableLiveData<ProductQuestion> questionLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> repoLoadError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public ProductQuestionViewModel(Application application, String data) {
        skinExpertRepository = SkinExpertRepository.getInstance();
        disposable = new CompositeDisposable();
        fetchProductQuestion(data);
    }

    LiveData<ProductQuestion> getProductQuestion() {
        return questionLiveData;
    }

    LiveData<Boolean> getError() {
        return repoLoadError;
    }

    LiveData<Boolean> getLoading() {
        return loading;
    }

    private void fetchProductQuestion(String data) {

        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json"), data);

        loading.setValue(true);
        disposable.add(skinExpertRepository.getProductQuestions(requestBody).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ProductQuestion>() {
                    @Override
                    public void onSuccess(ProductQuestion value) {
                        repoLoadError.setValue(false);
                        questionLiveData.setValue(value);
                        loading.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        repoLoadError.setValue(true);
                        loading.setValue(false);
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null) {
            disposable.clear();
            disposable = null;
        }
    }

}

