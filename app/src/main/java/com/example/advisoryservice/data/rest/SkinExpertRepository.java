package com.example.advisoryservice.data.rest;

import com.example.advisoryservice.data.model.CompleteResultDetailsResponde;
import com.example.advisoryservice.data.model.CreateCustomerResponse;
import com.example.advisoryservice.data.model.CustomerDetailsResponse;
import com.example.advisoryservice.data.model.IntroDetail;
import com.example.advisoryservice.data.model.ProductQuestion;
import com.example.advisoryservice.data.model.Questions;
import com.example.advisoryservice.data.model.Response;
import com.example.advisoryservice.data.model.Response1;
import com.example.advisoryservice.data.model.StoreLoginResponse;
import com.example.advisoryservice.data.model.feedback.FeedbackDetails;
import com.example.advisoryservice.data.model.instructionDetail.InstructionDetail;
import com.example.advisoryservice.data.model.instructionDetail.IntroDetailPage;
import com.example.advisoryservice.data.model.testresult.ResultDetailResponse;
import com.example.advisoryservice.login.data.model.LoginResponse;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.RequestBody;
import retrofit2.http.Body;

public class SkinExpertRepository {
    private APIInterface apiInterface;
    private APIInterface apiInterfaceCustomer, apiInterfaceSkinAnalysis, loginApiInterface;
    private static SkinExpertRepository skinExpertRepository;

    public static SkinExpertRepository getInstance() {
        if (skinExpertRepository == null) {
            skinExpertRepository = new SkinExpertRepository();
        }
        return skinExpertRepository;
    }

    public SkinExpertRepository() {
        apiInterface = RetrofitService.provideRetrofitService();
        apiInterfaceCustomer = RetrofitService.provideCustomerRetrofitService();
        apiInterfaceSkinAnalysis = RetrofitService.provideSkinAnalysisRetrofitService();
        loginApiInterface = RetrofitService.provideLoginRetrofitService();
    }

    public Single<IntroDetail> getIntoDetail() {
        return apiInterface.getIntroDetail();
    }

    public Single<IntroDetailPage> getIntoDetailPage() {
        return apiInterface.getIntroDetailPage();
    }

    public Single<Questions> getQuestions() {
        return apiInterface.getAllQuestions();
    }

    public Single<ProductQuestion> getProductQuestions(RequestBody data) {
        return apiInterface.getProductQuestion(data);
    }

    public Single<ResultDetailResponse> getResultDetail(RequestBody data) {
        return apiInterface.getResultDetail(data);
    }

    public Single<FeedbackDetails> getFeedbackDetails() {
        return apiInterface.getFeedbackDetails();
    }

    public Observable<Response1> postFeedback(RequestBody data) {
        return apiInterface.postFeedback(data);
    }

    public Single<StoreLoginResponse> getStoreLoginResponse() {
        return apiInterfaceCustomer.getStoreLoginResponse();
    }

    public Single<List<CustomerDetailsResponse>> getCustomerDetails(String token, String mobile) {
        return apiInterfaceCustomer.getCustomerDetails(token, mobile);
    }


    public Single<CompleteResultDetailsResponde> getCustomerDetailsResponse(@Body HashMap<String, Object> body) {
        return apiInterface.getcustomerDetailsResponse(body);
    }



    public Single<CreateCustomerResponse> insertCustomerDetails(HashMap<String, Object> body) {
        return apiInterfaceCustomer.insertCustomerDetails(body);
    }

    public Single<String> updateCustomerDetails(HashMap<String, String> body) {
        return apiInterfaceCustomer.updateCustomerDetails(body);
    }

    public Single<LoginResponse> getLoginResponse(String userId, String pwd) {
        return loginApiInterface.getLoginResponse(userId, pwd);
    }

    public Single<InstructionDetail> getInstructionDetail(@Body HashMap<String, Object> body) {
        return apiInterface.getInstructionDetail(body);
    }

    public Single<Response> getVersion(String locationCode, String version) {
        return apiInterface.getVersion(locationCode, version);
    }
}

