package com.example.advisoryservice.data.rest;

import com.example.advisoryservice.data.model.CompleteResultDetailsResponde;
import com.example.advisoryservice.data.model.CreateCustomerResponse;
import com.example.advisoryservice.data.model.CustomerDetailsResponse;
import com.example.advisoryservice.data.model.CustomerInsertResponse;
import com.example.advisoryservice.data.model.CustomerPostRequest;
import com.example.advisoryservice.data.model.IntroDetail;
import com.example.advisoryservice.data.model.ProductQuestion;
import com.example.advisoryservice.data.model.Questions;
import com.example.advisoryservice.data.model.Response;
import com.example.advisoryservice.data.model.Response1;
import com.example.advisoryservice.data.model.ServicePostRequest;
import com.example.advisoryservice.data.model.StoreLoginResponse;
import com.example.advisoryservice.data.model.analyzeImage.AnalyzeImage;
import com.example.advisoryservice.data.model.feedback.FeedbackDetails;
import com.example.advisoryservice.data.model.instructionDetail.InstructionDetail;
import com.example.advisoryservice.data.model.instructionDetail.IntroDetailPage;
import com.example.advisoryservice.data.model.revieve.RevieveResponse;
import com.example.advisoryservice.data.model.revieveDetail.RevieveDetail;
import com.example.advisoryservice.data.model.testresult.ResultDetailResponse;
import com.example.advisoryservice.login.data.model.LoginResponse;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("loginapi")
    Single<LoginResponse> getLoginResponse(@Query("userid") String userId, @Query("pwd") String password);

    @GET("intro/detail")
    Single<IntroDetail> getIntroDetail();

    @GET("introduction/detail/")
    Single<IntroDetailPage> getIntroDetailPage();

    @GET("Account/Authenticate?userName=hsrlayout1@dd.com&password=Food@123&serviceType=2")
    Single<StoreLoginResponse> getStoreLoginResponse();

    @GET("Customer/SearchCustomer")
    Single<List<CustomerDetailsResponse>> getCustomerDetails(@Query(value = "accountId", encoded = true) String token, @Query("mobile") String mobile);



    @Headers("Content-Type: application/json")
    @POST("Customer/UpdateCustomer")
    Single<String> updateCustomerDetails(@Body HashMap<String, String> body);

    @POST("details/insert")
    Observable<CustomerInsertResponse> insertCustomerDetails(@Body CustomerPostRequest customerPostRequest);

    @Headers("Content-Type: application/json")
    @POST("questions/allquestions/")
    Call<Questions> insertServiceDetails(@Body ServicePostRequest servicePostRequest);

    @GET("questions/Allquestions")
    Single<Questions> getAllQuestions();

    /*@POST("questions/validate/")
    Call<ResponseBody> validateQuestionAnswers(@Body RequestBody data);*/
    //Observable<Response> validateQuestionAnswers(@Body RequestBody data);
    @POST("questions/validation")
    Call<ResponseBody> validateQuestionAnswers(@Body RequestBody data);

    @POST("questions/save")
    Observable<Response> postQuestionAnswers(@Body RequestBody data);

    @POST("regime/detail/")
    Single<ProductQuestion> getProductQuestion(@Body RequestBody data);

    @POST("regime/insert")
    Observable<Response> postRegime(@Body RequestBody data);

    @POST("result/detail_new/")
    Single<ResultDetailResponse> getResultDetail(@Body RequestBody data);

    @GET("feedback/details_new")
    Single<FeedbackDetails> getFeedbackDetails();

    @POST("feedback/insert_new")
    Observable<Response1> postFeedback(@Body RequestBody data);

    @POST("revieve/insert")
    Observable<RevieveResponse> revieveInsert(@Body RequestBody data);

    @POST("result/detail_new/")
    Observable<RevieveDetail> revieveDetails(@Body RequestBody data);

    @POST("result/detail_new/")
    Call<ResultDetailResponse> revieveDetailsTest(@Body RequestBody data);

    @Multipart
    @POST("analyzeImage/")
    Observable<ResponseBody> postImage(@Part MultipartBody.Part image,
                                       @Part("skintone") RequestBody skintone,
                                       @Part("gender") RequestBody gender,
                                       @Part("partner_id") RequestBody partner_id);

    @Headers({"Accept: application/json"})
    @POST("analyzeImage/")
    @Multipart
    Call<AnalyzeImage> postImage1(@Part MultipartBody.Part image,
                                  @Part("skintone") RequestBody skintone,
                                  @Part("gender") RequestBody gender,
                                  @Part("partner_id") RequestBody partner_id);

    @Headers("Content-Type:application/json")
    @POST("revieve/PostImage")
    Call<ResponseBody> revieveImage(@Body RequestBody data);

    @Headers("Content-Type:application/json")
    @POST("revieve/PostImage")
    Call<RevieveResponse> revieveImage1(@Body String data);

    @POST("Customer/CreateCustomer")
    Single<CreateCustomerResponse> insertCustomerDetails(@Body HashMap<String, Object> body);


    @Headers("Content-Type: application/json")
    @POST("instruction/detail")
    Single<InstructionDetail> getInstructionDetail(@Body HashMap<String, Object> body);



    @GET("intro/check_version?")
    Single<Response> getVersion(@Query( "loctionCode") String loctionCode, @Query( "version") String version);

    @Headers("Content-Type:application/json")
    @GET("intro/check_version?")
    Call<ResponseBody> getVersionTest(@Query( "loctionCode") String loctionCode, @Query( "version") String version);

/*    @GET("instruction/detail")
    Call<ResponseBody> getInstructionDetailApi(@Query( "advisoryService") String advisoryService);*/


    @POST("completresult/detail/")
    Single<CompleteResultDetailsResponde> getcustomerDetailsResponse(@Body HashMap<String, Object> body);
}
