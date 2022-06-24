package com.example.advisoryservice.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.advisoryservice.data.model.IntroDetail;
import com.example.advisoryservice.data.model.instructionDetail.IntroDetailPage;
import com.example.advisoryservice.data.rest.SkinExpertRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends ViewModel {

    private SkinExpertRepository skinExpertRepository;
    private CompositeDisposable disposable;

    private final MutableLiveData<IntroDetail> introLiveData = new MutableLiveData<>();

    private final MutableLiveData<IntroDetailPage> introPageLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> repoLoadError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public MainViewModel(){
        skinExpertRepository = SkinExpertRepository.getInstance();
        disposable = new CompositeDisposable();
        //fetchIntro();
        fetchIntroPage();
    }

    LiveData<IntroDetail> getIntroDetails() {
        return introLiveData;
    }

    public LiveData<IntroDetailPage> getIntroDetailsPage() {
        return introPageLiveData;
    }
    public LiveData<Boolean> getError() {
        return repoLoadError;
    }
    public LiveData<Boolean> getLoading() {
        return loading;
    }

    private void fetchIntro() {



        loading.setValue(true);
        disposable.add(skinExpertRepository.getIntoDetail().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<IntroDetail>() {
                    @Override
                    public void onSuccess(IntroDetail value) {
                        repoLoadError.setValue(false);
                        introLiveData.setValue(value);
                        loading.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        repoLoadError.setValue(true);
                        loading.setValue(false);
                    }
                }));
    }


    public void fetchIntroPage() {

        loading.setValue(true);
        disposable.add(skinExpertRepository.getIntoDetailPage().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<IntroDetailPage>() {
                    @Override
                    public void onSuccess(IntroDetailPage value) {
                        repoLoadError.setValue(false);
                        introPageLiveData.setValue(value);
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

