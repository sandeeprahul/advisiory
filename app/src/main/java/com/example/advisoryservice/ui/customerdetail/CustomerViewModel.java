package com.example.advisoryservice.ui.customerdetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.advisoryservice.data.model.CompleteResultDetailsResponde;
import com.example.advisoryservice.data.model.CreateCustomerResponse;
import com.example.advisoryservice.data.model.CustomerDetailsResponse;
import com.example.advisoryservice.data.model.StoreLoginResponse;
import com.example.advisoryservice.data.rest.SkinExpertRepository;
import com.example.advisoryservice.util.Log;

import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class CustomerViewModel extends ViewModel {

    public static final String TAG = "CustomerViewModel";

    private SkinExpertRepository skinExpertRepository;
    private CompositeDisposable disposable;

    private final MutableLiveData<StoreLoginResponse> storeLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<CustomerDetailsResponse>> customerDetailLiveData = new MutableLiveData<>();
    private final MutableLiveData<CompleteResultDetailsResponde> completeResultDetailLiveData = new MutableLiveData<>();
    private final MutableLiveData<CreateCustomerResponse> customerInsertResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> updateCustomerLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public CustomerViewModel() {
        skinExpertRepository = SkinExpertRepository.getInstance();
        disposable = new CompositeDisposable();
        fetchStoreLogin();
    }

    LiveData<StoreLoginResponse> getStoreLogin() {
        return storeLiveData;
    }

    LiveData<List<CustomerDetailsResponse>> getCustomerDetail() {
        return customerDetailLiveData;
    }

    LiveData<CompleteResultDetailsResponde> getCustomerCompleteDetail() {
        return completeResultDetailLiveData;
    }

    LiveData<CreateCustomerResponse> getCustomerInsertResponse() {
        return customerInsertResponseLiveData;
    }

    LiveData<String> getUpdateCustomerResponse() {
        return updateCustomerLiveData;
    }

    LiveData<Boolean> getError() {
        return loadError;
    }

    LiveData<Boolean> getLoading() {
        return loading;
    }

    private void fetchStoreLogin() {

        loading.setValue(true);

        disposable.add(skinExpertRepository.getStoreLoginResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<StoreLoginResponse>() {
                    @Override
                    public void onSuccess(StoreLoginResponse value) {

                        loadError.setValue(false);
                        loading.setValue(false);
                        storeLiveData.setValue(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadError.setValue(true);
                        loading.setValue(false);
                    }
                })
        );
    }


    void fetchCustomerDetails(String token, String mobileNo) {

        Log.d(TAG, "Fetching Customer Detail " + mobileNo);
        loading.setValue(true);

        disposable.add(skinExpertRepository.getCustomerDetails(token, mobileNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<CustomerDetailsResponse>>() {
                    @Override
                    public void onSuccess(List<CustomerDetailsResponse> value) {
                        customerDetailLiveData.setValue(value);
                        loading.setValue(false);
                        loadError.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadError.setValue(true);
                        loading.setValue(false);
                    }
                })
        );
    }



    public void completeResultDetail(HashMap<String, Object> mobileNo) {


        Log.d(TAG, "Fetching Customer Detail " + mobileNo);
        //  loading.setValue(true);

        disposable.add(skinExpertRepository.getCustomerDetailsResponse(mobileNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<CompleteResultDetailsResponde>() {
                    @Override
                    public void onSuccess(CompleteResultDetailsResponde value) {
                        completeResultDetailLiveData.setValue(value);
                        // loading.setValue(false);
                        // loadError.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadError.setValue(true);
                        loading.setValue(false);
                    }
                })
        );
    }


    void insertCustomerDetails(HashMap<String, Object> body) {

        loading.setValue(true);

        disposable.add(skinExpertRepository.insertCustomerDetails(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<CreateCustomerResponse>() {
                    @Override
                    public void onSuccess(CreateCustomerResponse value) {
                        customerInsertResponseLiveData.setValue(value);
                        loading.setValue(false);
                        loadError.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadError.setValue(true);
                        loading.setValue(false);
                    }
                })
        );

    }

    void updateCustomerDetails(HashMap<String, String> body) {

        loading.setValue(true);

        disposable.add(skinExpertRepository.updateCustomerDetails(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<String>() {
                    @Override
                    public void onSuccess(String value) {
                        updateCustomerLiveData.setValue(value);
                        loading.setValue(false);
                        loadError.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadError.setValue(true);
                        loading.setValue(false);
                    }
                })
        );

    }
}

