package com.example.advisoryservice.ui.genderquestions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.advisoryservice.data.model.Questions;
import com.example.advisoryservice.data.model.instructionDetail.InstructionDetail;
import com.example.advisoryservice.data.rest.SkinExpertRepository;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Body;

public class GenderQuestionViewModel extends ViewModel {

    private SkinExpertRepository skinExpertRepository;
    private CompositeDisposable disposable;

    private final MutableLiveData<Questions> questionsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> questionsLoadError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();

    private final MutableLiveData<InstructionDetail> instructionDetailLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> instructionLoadError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> instructionLoading = new MutableLiveData<>();

    public GenderQuestionViewModel() {
        skinExpertRepository = SkinExpertRepository.getInstance();
        disposable = new CompositeDisposable();
        fetchQuestions();
       // fetchInstructionDetail();
    }

    LiveData<Questions> getAllQuestions() {
        return questionsLiveData;
    }

    LiveData<Boolean> getError() {
        return questionsLoadError;
    }

    LiveData<Boolean> getLoading() {
        return loading;
    }

    private void fetchQuestions() {

        loading.setValue(true);
        disposable.add(skinExpertRepository.getQuestions().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Questions>() {
                    @Override
                    public void onSuccess(Questions value) {
                        loading.setValue(false);
                        questionsLiveData.setValue(value);
                        questionsLoadError.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        loading.setValue(false);
                        questionsLoadError.setValue(true);
                    }
                })
        );
    }

    LiveData<InstructionDetail> getInstructionDetail() {
        return instructionDetailLiveData;
    }

    LiveData<Boolean> getInstructionError() {
        return instructionLoadError;
    }

    LiveData<Boolean> getInstructionLoading() {
        return instructionLoading;
    }


     void fetchInstructionDetail(@Body HashMap<String, Object> body) {

        instructionLoading.setValue(true);
        disposable.add(skinExpertRepository.getInstructionDetail(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<InstructionDetail>() {
                    @Override
                    public void onSuccess(InstructionDetail value) {
                        instructionLoading.setValue(false);
                        instructionDetailLiveData.setValue(value);
                        instructionLoadError.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        instructionLoading.setValue(false);
                        instructionLoadError.setValue(true);
                    }
                })
        );
    }
}

