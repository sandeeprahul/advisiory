package com.example.advisoryservice.ui.result;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.advisoryservice.data.model.feedback.FeedbackDetails;
import com.example.advisoryservice.data.rest.SkinExpertRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class FeedbackViewModel extends ViewModel {

    private SkinExpertRepository skinExpertRepository;
    private CompositeDisposable compositeDisposable;

    private final MutableLiveData<FeedbackDetails> feedbackLiveData = new MediatorLiveData<>();
    //private final MutableLiveData<ResponseBody> responseLiveData = new MediatorLiveData<>();
    private final MutableLiveData<Boolean> loadError = new MediatorLiveData<>();
    private final MutableLiveData<Boolean> loading = new MediatorLiveData<>();

    public FeedbackViewModel() {
        skinExpertRepository = SkinExpertRepository.getInstance();
        compositeDisposable = new CompositeDisposable();

        fetchFeedback();
    }

    LiveData<FeedbackDetails> getFeedbackDetail() {
        return feedbackLiveData;
    }

    /*LiveData<ResponseBody> getFeedbackResponse() {
        return responseLiveData;
    }*/

    LiveData<Boolean> getError() {
        return loadError;
    }

    LiveData<Boolean> getLoading() {
        return loading;
    }

    private void fetchFeedback() {

        loading.setValue(true);
        compositeDisposable.add(skinExpertRepository.getFeedbackDetails()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<FeedbackDetails>() {
                    @Override
                    public void onSuccess(FeedbackDetails value) {

                        loading.setValue(false);
                        loadError.setValue(false);
                        feedbackLiveData.setValue(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadError.setValue(true);
                        loading.setValue(false);
                    }
                })
        );
    }

    void postFeedbackDetails(String data) {

        /*Log.d("TAG", "PostFeedback " + data);
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json"), data);

        skinExpertRepository.postFeedback(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody value) {
                        Log.d("FeedbackViewMode", "Response value " + value.toString());
                        responseLiveData.setValue(value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("FeedbackViewMode", "Exception " + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d("TAG", "ONCOMPLETE <<::>> ");
                    }
                });*/
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

