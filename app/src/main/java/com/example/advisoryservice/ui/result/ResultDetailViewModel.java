package com.example.advisoryservice.ui.result;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.advisoryservice.data.model.testresult.ResultDetailResponse;
import com.example.advisoryservice.data.rest.SkinExpertRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ResultDetailViewModel  extends ViewModel {

    private SkinExpertRepository skinExpertRepository;
    private CompositeDisposable compositeDisposable;

    private final MutableLiveData<ResultDetailResponse> resultLiveData = new MediatorLiveData<>();
    private final MutableLiveData<Boolean> loadError = new MediatorLiveData<>();
    private final MutableLiveData<Boolean> loading = new MediatorLiveData<>();

    public ResultDetailViewModel(Application application, String data) {
        skinExpertRepository = SkinExpertRepository.getInstance();
        compositeDisposable = new CompositeDisposable();
        fetchResultDetails(data);
    }

    public LiveData<ResultDetailResponse> getResultDetail(){
        return resultLiveData;
    }

    public LiveData<Boolean> getError(){
        return loadError;
    }

    public LiveData<Boolean> getLoading(){
        return loading;
    }

    private void fetchResultDetails(String data){

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), data);

        loading.setValue(true);
        compositeDisposable.add(skinExpertRepository.getResultDetail(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ResultDetailResponse>() {
                    @Override
                    public void onSuccess(ResultDetailResponse value) {

                        loading.setValue(false);
                        loadError.setValue(false);
                        resultLiveData.setValue(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadError.setValue(true);
                        loading.setValue(false);
                    }
                })
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
            compositeDisposable = null;
        }
    }
}
