package com.example.advisoryservice.ui.login;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.advisoryservice.data.model.IntroDetail;
import com.example.advisoryservice.data.model.Response;
import com.example.advisoryservice.data.model.instructionDetail.IntroDetailPage;
import com.example.advisoryservice.data.rest.SkinExpertRepository;
import com.example.advisoryservice.login.data.model.LoginResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {

    private SkinExpertRepository skinExpertRepository;
    private CompositeDisposable disposable;

    private final MutableLiveData<IntroDetail> introLiveData = new MutableLiveData<>();
    private final MutableLiveData<IntroDetailPage> introPageLiveData = new MutableLiveData<>();
    private final MutableLiveData<LoginResponse> loginLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();

    private final MutableLiveData<Boolean> repoLoadError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> introloading = new MutableLiveData<>();
    private final MutableLiveData<Response> versionLiveData = new MutableLiveData<>();
    /*public LoginViewModel(String username, String pwd) {
        skinExpertRepository = SkinExpertRepository.getInstance();
        disposable = new CompositeDisposable();
        //login(username, pwd);
    }*/

    public LoginViewModel() {
        skinExpertRepository = SkinExpertRepository.getInstance();
        disposable = new CompositeDisposable();
    }

    LiveData<IntroDetail> getIntroDetails() {
        return introLiveData;
    }

    LiveData<LoginResponse> getLoginResponse() {
        return loginLiveData;
    }


    LiveData<Response> getLatestVersion() {
        return versionLiveData;
    }
    LiveData<Boolean> getError() {
        return loadError;
    }

    LiveData<Boolean> getLoading() {
        return loading;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        loading.setValue(true);
        disposable.add(skinExpertRepository.getLoginResponse(username, password).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<LoginResponse>() {
                    @Override
                    public void onSuccess(LoginResponse value) {
                        loading.setValue(false);
                        loginLiveData.setValue(value);
                        loadError.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        loading.setValue(false);
                        loadError.setValue(true);
                    }
                })
        );
    }

    public void fetchIntro() {

        introloading.setValue(true);
        disposable.add(skinExpertRepository.getIntoDetail().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<IntroDetail>() {
                    @Override
                    public void onSuccess(IntroDetail value) {
                        repoLoadError.setValue(false);
                        introLiveData.setValue(value);
                        introloading.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        repoLoadError.setValue(true);
                        introloading.setValue(false);
                    }
                }));
    }


    public void fetchIntroPage() {

        introloading.setValue(true);
        disposable.add(skinExpertRepository.getIntoDetailPage().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<IntroDetailPage>() {
                    @Override
                    public void onSuccess(IntroDetailPage value) {
                        repoLoadError.setValue(false);
                        introPageLiveData.setValue(value);
                        introloading.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        repoLoadError.setValue(true);
                        introloading.setValue(false);
                    }
                }));
    }

    /*public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }
*/
    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }
    public void LatestVersion(String location, String version){
        //Log.d(TAG, "Fetching Customer Detail " + mobileNo);
        loading.setValue(true);

        disposable.add(skinExpertRepository.getVersion(location,version)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response>() {
                    @Override
                    public void onSuccess(Response value) {
                        versionLiveData.setValue(value);
                        loading.setValue(false);
                        loadError.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadError.setValue(true);
                        loading.setValue(false);
                    }
                }));

      /*  disposable.add(skinExpertRepository.getCustomerDetails(location, version)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Response>>() {
                    @Override
                    public void onSuccess(Response value) {
                        versionLiveData.setValue(value);
                        loading.setValue(false);
                        loadError.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadError.setValue(true);
                        loading.setValue(false);
                    }
                })
        );*/

        // return skinExpertRepository.getVersion(location,version);

    }





    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
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
