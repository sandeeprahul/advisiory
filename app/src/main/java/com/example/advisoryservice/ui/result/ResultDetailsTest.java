package com.example.advisoryservice.ui.result;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.ArrayMap;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.advisoryservice.R;
import com.example.advisoryservice.base.BaseActivity;
import com.example.advisoryservice.data.model.Response1;
import com.example.advisoryservice.data.model.feedback.Answer;
import com.example.advisoryservice.data.model.questions.SubQuestion;
import com.example.advisoryservice.data.model.questions.SubQuestionOption;
import com.example.advisoryservice.data.model.revieveDetail.ApiResponse;
import com.example.advisoryservice.data.model.revieveDetail.MappingResponse;
import com.example.advisoryservice.data.model.revieveDetail.ResultResponse;
import com.example.advisoryservice.data.model.revieveDetail.RevieveDetail;
import com.example.advisoryservice.data.model.testresult.Category;
import com.example.advisoryservice.data.model.testresult.Filter;
import com.example.advisoryservice.data.model.testresult.RegimeHeader;
import com.example.advisoryservice.data.model.testresult.RegimeSchedule;
import com.example.advisoryservice.data.model.testresult.RestProduct;
import com.example.advisoryservice.data.model.testresult.SubCategory;
import com.example.advisoryservice.data.model.testresult.Summary;
import com.example.advisoryservice.data.model.testresult.TopProduct;
import com.example.advisoryservice.data.rest.APIInterface;
import com.example.advisoryservice.data.rest.RetrofitService;
import com.example.advisoryservice.ui.customerdetail.CustomerInsertDetailsActivity;
import com.example.advisoryservice.util.Constants;
import com.example.advisoryservice.util.LinePagerIndicatorDecoration;
import com.example.advisoryservice.util.Log;
import com.example.advisoryservice.util.SkinViewModelFactory;
import com.example.advisoryservice.util.WrappingViewPager;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.HttpException;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class ResultDetailsTest extends BaseActivity implements ExpandableCategorySection.ClickListener, FeedbackAdapter.ClickListener,LatestFeedbackTestAdapter.ClickListener, View.OnClickListener {

   // public static final String TAG = ResultDetailActivity.class.getSimpleName();

    TabLayout tabLayout;
    WrappingViewPager viewPager;
    ImageView ivImage, ivGenericSkinTips;
    TextView tvSkinType, tvErrorMessage, tvFeedbackQuestion, tvGenericSkinCare, tvAboutMySkin, tvApiResponse, tvMappingResponse, tvResultResponse;
    LinearLayout llRegimeHeader, llSpinnerLayout, llSkinRegimeLayout, llAboutMySkinLayout, llApiResponse;
    ProgressBar progressBar;
    RecyclerView rvRegimeSchedule, rvCategory, rvFeedback, rvApiResponse, rvMappingResponse, rvResultResponse;
    AppCompatSpinner spinnerRecommendedProducts;
    Button btnRetakeTest;
    ResultDetailViewModel resultDetailViewModel;
    FeedbackViewModel feedbackViewModel;
    ResultViewPagerAdapter adapter;
    RegimeScheduleAdapter regimeScheduleAdapter;
    FeedbackAdapter feedbackAdapter;
    ArrayAdapter<String> filterSpinnerAdapter;
    private SectionedRecyclerViewAdapter sectionedAdapter;
    ExpandableCategorySection.ClickListener clickListener;
    ApiResponseAdapter revieveDetailAdapterApiResponse;
    MappingResponseAdapter revieveDetailAdapterMappingResponse;
    ResultResponseAdapter revieveDetailAdapterResultResponse;
    //RevieveDetailAdapter revieveDetailAdapterApiResponse, revieveDetailAdapterMappingResponse, revieveDetailAdapterResultResponse;

    List<RegimeSchedule> regimeScheduleList = new ArrayList<>();
    List<Summary> summaryList = new ArrayList<>();
    List<Category> categoryList = new ArrayList<>();
    List<String> recommendedProductsList = new ArrayList<>();
    ArrayMap<String, String> categoryMap = new ArrayMap<>();
    ArrayMap<String, String> categoryIDMap = new ArrayMap<>();
    List<ApiResponse> apiResponseList = new ArrayList<>();
    List<MappingResponse> mappingResponseList = new ArrayList<>();
    List<ResultResponse> resultResponseList = new ArrayList<>();
    String selectedFeedbackID = "";

    String data, feedbackData = "";
    String custTransId = "", genericImageUrl = "", chartData,advisoryServiceNumber;
    boolean isClicked = false, isAboutSkinClicked = true, isApiResponseClicked = true, isMappingResponseClicked = true, isResultResponseClicked = true;
    boolean isProductsAvailable = false;
    boolean isSelfieTaken = false;
    int tabPosition;
    RevieveDetail revieveDetail;
    Gson gson;
    int questionCnt = 1;
    List<SubQuestion> subQuestions;
    RadioGroup radioGroup;
    public static Dialog dialog;

    LinkedHashMap<String, String> optionMap = new LinkedHashMap<>();
    HashMap<String, SubQuestionOption> subQuestionOptionHashMap = new HashMap<>();
    TextView tvQuestion;
    SubQuestionOption optionss;
    Button btnNext, btnBack;
    boolean isChecked = false;
    String gender = "", locationCode = "",mobileNumberPref;
    String successData ="";
    SharedPreferences sharedPreferences;
    public  int errorCode;
    String errorBody;
    EditText userFeedback,excellentUserFeedback,userFeedbackReycler;
    LinearLayout parentLl,excellentFeedbackLl;
    String customerInputFeedback ="";
    SubQuestionOption subQuestionOption;
    ArrayList<HashMap<String, String>> prodArrayList = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> prodHashMap = new HashMap<String, String>();
    String prodtest;
    HashMap<String, String> test;
    boolean radioCheckValue;
    String radioCheckValidation;
    HashMap<String,ArrayList<String>> radioButtonCheck;
    ArrayList<String> rbTestArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(getSharedPreferences().getBoolean(Constants.TABLET, false) ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_result_detail);

        sharedPreferences = getSharedPreferences();

        locationCode = sharedPreferences.getString(Constants.LOCATION_CODE, "");
        mobileNumberPref = getSharedPreferences().getString(Constants.MOBILE_NUM_PREF, "");

        init();

        clickListener = this;
        sectionedAdapter = new SectionedRecyclerViewAdapter();

        observeResultDetail();
        //observeFeedbackDetails();
    }

    private void observeResultDetail() {

        summaryList.clear();
        regimeScheduleList.clear();
        recommendedProductsList.clear();
        categoryMap.clear();
        categoryIDMap.clear();

        resultDetailViewModel.getResultDetail().observe(this, resultDetailResponse -> {

            Log.d(TAG, "ResultDetail API Response <<::>> " + new Gson().toJson(resultDetailResponse));

            if (resultDetailResponse.getMessage().equalsIgnoreCase("success")) {

                Glide.with(this).load(resultDetailResponse.getImageURL())
                        .placeholder(R.drawable.progress_animation).into(ivImage);

                //SpannableString spannableString = new SpannableString("Your Skin is " + resultDetailResponse.getSkinType());
                SpannableString spannableString = new SpannableString("Your have " + resultDetailResponse.getSkinType() + " Skin");
                spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)),
                        10, spannableString.length() - 5,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvSkinType.setText(spannableString);

                addTabLayout(resultDetailResponse.getSummary());
                addRegimeHeader(resultDetailResponse.getRegimeHeader());

                regimeScheduleList.addAll(resultDetailResponse.getRegimeSchedule());
                regimeScheduleAdapter.notifyDataSetChanged();

                llSpinnerLayout.setVisibility(resultDetailResponse.getFilter().size() == 0 ? View.GONE : View.VISIBLE);
                if (resultDetailResponse.getFilter().size() > 0) {
                    for (Filter filter : resultDetailResponse.getFilter()) {
                        recommendedProductsList.add(filter.getCategoryName());
                        categoryMap.put(filter.getCategoryName(), filter.getCategoryId().isEmpty() ? "all" : filter.getCategoryId());
                    }
                    spinnerRecommendedProducts.setAdapter(filterSpinnerAdapter);
                }

                categoryList.addAll(resultDetailResponse.getCategory());

                for (Category category : resultDetailResponse.getCategory()) {
                    sectionedAdapter.addSection(new ExpandableCategorySection(this, category.getCategoryName(),
                            category.getSubCategory(), this, ""));
                    categoryIDMap.put(category.getCategoryName(), category.getCategoryId());
                }

                if (resultDetailResponse.getGenericSkin().size() > 0)
                    genericImageUrl = resultDetailResponse.getGenericSkin().get(0).getGenericSkinTipsUrl();

                rvCategory.setAdapter(sectionedAdapter);
                sectionedAdapter.notifyDataSetChanged();
            }


        });

        resultDetailViewModel.getError().observe(this, isError -> {
            if (isError != null) if (isError) {
                Log.w(TAG, "An Error Occurred While Loading Data <<::>>");
                tvErrorMessage.setVisibility(View.VISIBLE);
                tvErrorMessage.setText(getResources().getString(R.string.error));
            } else {
                Log.w(TAG, "No Errors");
                tvErrorMessage.setVisibility(View.GONE);
                tvErrorMessage.setText(null);
            }
        });

        resultDetailViewModel.getLoading().observe(this, isLoading -> {
            Log.d(TAG, "Progress Bar loading <<::>> " + isLoading);
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if (isLoading) {
                    tvErrorMessage.setVisibility(View.GONE);
                }
            }
        });
    }

    private void observeFeedbackDetails() {
        feedbackViewModel.getFeedbackDetail().observe(this, feedbackDetails -> {
            if (feedbackDetails.getMessage().equalsIgnoreCase("success"))
                tvFeedbackQuestion.setText(feedbackDetails.getData().get(0).getQuestion());
        });

        feedbackViewModel.getError().observe(this, isError -> {
            if (isError != null) if (isError) {
                Log.w(TAG, "An Error Occurred While Loading Data Feedback<<::>>");
            } else {
                Log.w(TAG, "No Errors");
                tvErrorMessage.setVisibility(View.GONE);
                tvErrorMessage.setText(null);
            }
        });

        feedbackViewModel.getLoading().observe(this, isLoading -> {

            if (isLoading != null) {
                //progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if (isLoading) {
                    tvErrorMessage.setVisibility(View.GONE);
                }
            }
        });
    }

    private void addTabLayout(List<Summary> summaryList) {
        for (Summary summary : summaryList) {
            tabLayout.addTab(tabLayout.newTab().setText(summary.getTitle()));
        }
       /* adapter = new ResultViewPagerAdapter(this,
                getSupportFragmentManager(), tabLayout.getTabCount(), summaryList,custTransId,isSelfieTaken,tabPosition);*/

        adapter = new ResultViewPagerAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount(), summaryList, custTransId, isSelfieTaken, tabPosition, revieveDetail);
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void addRegimeHeader(List<RegimeHeader> regimeHeaderList) {

        llRegimeHeader.removeAllViews();
        for (RegimeHeader regimeHeader : regimeHeaderList) {

            String value = regimeHeader.getRegimeImageUrl() + "-" + regimeHeader.getRegimeName();
            TextView tvHeader = new TextView(this);
            Spanned message;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                message = Html.fromHtml(value, Html.FROM_HTML_MODE_COMPACT);
            } else {
                message = Html.fromHtml(value);
            }
            tvHeader.setText(message);
            tvHeader.setTextColor(Color.parseColor("#595959"));
            if (isTablet()) {
                tvHeader.setTextSize(17);
                tvHeader.setPadding(2, 5, 2, 5);
            } else {
                tvHeader.setPadding(1, 5, 1, 5);
                tvHeader.setTextSize(14);
            }

            llRegimeHeader.addView(tvHeader);
        }
    }

    private void getReviveDetails() {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("custTransId", custTransId);

            String data = jsonObject.toString();
            Log.d(TAG, "Revieve Detail request data " + data);

            RequestBody requestBody =
                    RequestBody.create(MediaType.parse("application/json"), data);

            APIInterface apiInterface = RetrofitService.getRetrofit().create(APIInterface.class);

            Log.d(TAG, "Calling API revieve detail api");

            apiInterface.revieveDetails(requestBody).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<RevieveDetail>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(RevieveDetail value) {
                            try {
                                Log.e(TAG, "RevieveDetail Response Message <<::>> " + value.getMessage());

                                String gson = new Gson().toJson(value);
                                JSONObject gsonJson = new JSONObject(gson);
                                Log.d(TAG, "RevieveDetail Response data <<::>> " + gsonJson);

                                Toast.makeText(ResultDetailsTest.this, value.getMessage(), Toast.LENGTH_SHORT).show();

                                if (!value.getSkinScoreDescription().isEmpty()) {
                                    String skinScoreDescription = value.getSkinScoreDescription();
                                    String[] splitData = skinScoreDescription.split("is");
                                    String[] val = splitData[1].trim().split("\\s+");
                                    if (val.length > 2) {


                                        ApiResponse apiResponse = new ApiResponse();
                                        apiResponse.setResultDescription(splitData[0] + " is : " + val[0] + " " + val[1]);
                                        apiResponse.setValue(val[2]);
                                        apiResponse.setResultColor("#36B8E3");

                                        apiResponseList.add(apiResponse);
                                    } else {
                                        Log.w(TAG, "Value 1 <<::>> " + val[0]);
                                        Log.w(TAG, "Value 2 <<::>> " + val[1]);


                                        ApiResponse apiResponse = new ApiResponse();
                                        apiResponse.setResultDescription(splitData[0] + " is : " + val[0]);
                                        apiResponse.setValue(val[1]);
                                        apiResponse.setResultColor("#36B8E3");

                                        apiResponseList.add(apiResponse);
                                    }


                                }

                                apiResponseList.addAll(value.getApiResponse());
                                revieveDetailAdapterApiResponse = new ApiResponseAdapter(apiResponseList);
                                rvApiResponse.setAdapter(revieveDetailAdapterApiResponse);
                                revieveDetailAdapterApiResponse.notifyDataSetChanged();

                                /*mappingResponseList.addAll(value.getMappingResponse());
                                revieveDetailAdapterMappingResponse = new MappingResponseAdapter(mappingResponseList);
                                rvMappingResponse.setAdapter(revieveDetailAdapterMappingResponse);
                                revieveDetailAdapterMappingResponse.notifyDataSetChanged();

                                resultResponseList.addAll(value.getResultResponse());
                                revieveDetailAdapterResultResponse = new ResultResponseAdapter(resultResponseList);
                                rvResultResponse.setAdapter(revieveDetailAdapterResultResponse);
                                revieveDetailAdapterResultResponse.notifyDataSetChanged();*/

                            } catch (Exception ex) {
                                ex.printStackTrace();
                                Log.e(TAG, "Revieve detail onNext Exception " + ex);
                                Toast.makeText(ResultDetailsTest.this, ex.toString(), Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            HttpException error = (HttpException) e;
                            String errorBody = Objects.requireNonNull(error.response().errorBody()).toString();
                            Log.d(TAG, "Exception e <<::>> " + e);
                            Log.d(TAG, "errorBody <<::>> " + errorBody);
                            Toast.makeText(ResultDetailsTest.this, errorBody, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Reveieve Details Exception <<::>> " + e);
        }
    }

    private void init() {

        setActionBarTitle("SKIN ANALYSIS RESULT");

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        ivImage = findViewById(R.id.ivImage);
        ivGenericSkinTips = findViewById(R.id.ivGenericSkinTips);
        tvGenericSkinCare = findViewById(R.id.tvGenericSkinCare);
        tvSkinType = findViewById(R.id.tvSkinType);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        progressBar = findViewById(R.id.progressBar);
        llRegimeHeader = findViewById(R.id.llRegimeHeader);
        llSpinnerLayout = findViewById(R.id.llSpinnerLayout);
        llSkinRegimeLayout = findViewById(R.id.llSkinRegimeLayout);
        rvRegimeSchedule = findViewById(R.id.rvRegimeSchedule);
        rvCategory = findViewById(R.id.rvCategory);
        rvFeedback = findViewById(R.id.rvFeedback);
        rvApiResponse = findViewById(R.id.rvApiResponse);
        rvMappingResponse = findViewById(R.id.rvMappingResponse);
        rvResultResponse = findViewById(R.id.rvResultResponse);
        spinnerRecommendedProducts = findViewById(R.id.spinnerRecommendedProducts);
        tvFeedbackQuestion = findViewById(R.id.tvFeedbackQuestion);
        tvAboutMySkin = findViewById(R.id.tvAboutMySkin);
        llAboutMySkinLayout = findViewById(R.id.llAboutMySkinLayout);
        llApiResponse = findViewById(R.id.llApiResponse);
        tvApiResponse = findViewById(R.id.tvApiResponse);
        tvMappingResponse = findViewById(R.id.tvMappingResponse);
        tvResultResponse = findViewById(R.id.tvResultResponse);
        btnRetakeTest = findViewById(R.id.btnRetakeTest);


        //APIResponse
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvApiResponse.setLayoutManager(manager);
        rvApiResponse.setNestedScrollingEnabled(false);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvApiResponse);
        rvApiResponse.addItemDecoration(new LinePagerIndicatorDecoration());
        //revieveDetailAdapterApiResponse = new RevieveDetailAdapter(apiResponseList, mappingResponseList, resultResponseList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvRegimeSchedule.setLayoutManager(linearLayoutManager);
        rvRegimeSchedule.setNestedScrollingEnabled(false);
        regimeScheduleAdapter = new RegimeScheduleAdapter(this, regimeScheduleList);
        rvRegimeSchedule.setAdapter(regimeScheduleAdapter);

        rvCategory.setLayoutManager(new LinearLayoutManager(this));
        rvCategory.setNestedScrollingEnabled(false);
        rvCategory.setAdapter(sectionedAdapter);

        filterSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, recommendedProductsList);
        filterSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRecommendedProducts.setAdapter(filterSpinnerAdapter);
        gson = new Gson();
        Bundle bundle = getIntent().getExtras();
        Intent intent = getIntent();
        if (bundle != null) {
            if (intent.hasExtra(Constants.CUSTOMER_TRANS_ID)) {
                custTransId = bundle.getString(Constants.CUSTOMER_TRANS_ID);
                advisoryServiceNumber = bundle.getString(Constants.ADVISORY_SERVICE_NUMBER);
              //  chartData = bundle.getString(Constants.CHART_DATA);
              //  revieveDetail = gson.fromJson(chartData, RevieveDetail.class);
                Log.w(TAG, "Customer Transaction Id " + custTransId);
            }
            if (intent.hasExtra(Constants.TAKE_SELFIE)) {
                isSelfieTaken = bundle.getBoolean(Constants.TAKE_SELFIE);
                Log.d(TAG, "TAKE SELFIE <<::>> " + bundle.getBoolean(Constants.TAKE_SELFIE));
               // revieveDetail = gson.fromJson(chartData, RevieveDetail.class);
                llApiResponse.setVisibility(bundle.getBoolean(Constants.TAKE_SELFIE) ? View.VISIBLE : View.GONE);
            }
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("custTransId", custTransId);
            jsonObject.put("phoneNo", mobileNumberPref);
            jsonObject.put("advisoryService",advisoryServiceNumber );
        } catch (Exception e) {
            Log.e(TAG, "Exception <<::>> " + e);
        }
        data = jsonObject.toString();
        Log.e(TAG, "JSON " + data);

        SkinViewModelFactory factory = new SkinViewModelFactory(this.getApplication(), data);
        resultDetailViewModel = ViewModelProviders.of(this, factory).get(ResultDetailViewModel.class);

        feedbackViewModel = ViewModelProviders.of(this).get(FeedbackViewModel.class);

        feedbackAdapter = new FeedbackAdapter(feedbackViewModel, this, this, this);
        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false);
        gridLayoutManager.setReverseLayout(true);
        rvFeedback.setLayoutManager(gridLayoutManager);
        rvFeedback.setNestedScrollingEnabled(false);
        rvFeedback.setAdapter(feedbackAdapter);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tvGenericSkinCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "IS Clicked<<::>> " + isClicked);
                if (!isClicked) {
                    isClicked = true;
                    expand(ivGenericSkinTips);
                    if (!genericImageUrl.isEmpty())
                        Glide.with(ResultDetailsTest.this).load(genericImageUrl)
                                .placeholder(R.drawable.progress_animation).into(ivGenericSkinTips);
                } else {
                    isClicked = false;
                    collapse(ivGenericSkinTips);
                }
            }
        });

        tvAboutMySkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isAboutSkinClicked) {
                    isAboutSkinClicked = false;
                    collapse(llAboutMySkinLayout);
                } else {
                    isAboutSkinClicked = true;
                    expand(llAboutMySkinLayout);
                }
            }
        });

        tvApiResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isApiResponseClicked) {
                    isApiResponseClicked = false;
                    collapse(rvApiResponse);
                } else {
                    isApiResponseClicked = true;
                    expand(rvApiResponse);
                }
            }
        });

        tvMappingResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMappingResponseClicked) {
                    isMappingResponseClicked = false;
                    collapse(rvMappingResponse);
                } else {
                    isMappingResponseClicked = true;
                    expand(rvMappingResponse);
                }
            }
        });

        tvResultResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isResultResponseClicked) {
                    isResultResponseClicked = false;
                    collapse(rvResultResponse);
                } else {
                    isResultResponseClicked = true;
                    expand(rvResultResponse);
                }
            }
        });

        btnRetakeTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!getSharedPreferences().getString(Constants.FILE_URL, "").isEmpty())
                    deleteImage(getSharedPreferences().getString(Constants.FILE_URL, ""), Constants.FILE_URL);
                if (!getSharedPreferences().getString(Constants.THUMB_IMAGE_FILE_URL, "").isEmpty())
                    deleteImage(getSharedPreferences().getString(Constants.THUMB_IMAGE_FILE_URL, ""), Constants.THUMB_IMAGE_FILE_URL);

                Intent intent = new Intent(ResultDetailsTest.this, CustomerInsertDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                tabPosition = tab.getPosition();
                viewPager.setCurrentItem(tab.getPosition());
                llSkinRegimeLayout.setVisibility(tab.getPosition() == 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        spinnerRecommendedProducts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sectionedAdapter.removeAllSections();

                String categoryId = categoryMap.get(spinnerRecommendedProducts.getSelectedItem().toString());
                Log.d(TAG, "Category ID <<::>> " + categoryId);

                for (Category category : categoryList) {

                    if (categoryId.equalsIgnoreCase("all")) {
                        sectionedAdapter.addSection(new ExpandableCategorySection(ResultDetailsTest.this,
                                category.getCategoryName(),
                                category.getSubCategory(), clickListener, categoryId.equalsIgnoreCase("all") ? "" : categoryId));
                    } else {

                        List<SubCategory> subCategoryList = new ArrayList<>();

                        for (SubCategory subcategory : category.getSubCategory()) {

                            isProductsAvailable = false;
                            Log.d(TAG, "Subcategory Name <<::>> " + subcategory.getSubCategoryName());

                            if (subcategory.getTopProducts().size() != 0) {
                                for (TopProduct topProduct : subcategory.getTopProducts()) {
                                    if (topProduct.getFilterCategory().equalsIgnoreCase(categoryId)) {
                                        isProductsAvailable = true;
                                        break;
                                    }
                                }
                            } else {
                                for (RestProduct restProduct : subcategory.getRestProducts()) {
                                    if (restProduct.getFilterCategory().equalsIgnoreCase(categoryId)) {
                                        subCategoryList.add(subcategory);
                                        isProductsAvailable = true;
                                        break;
                                    }
                                }
                            }
                            if (isProductsAvailable)
                                subCategoryList.add(subcategory);
                        }
                        Log.d(TAG, "Category Name <<::>> " + category.getCategoryName());
                        Log.d(TAG, "Subcategory List <<::>> " + subCategoryList.size());

                        if (subCategoryList.size() != 0)
                            sectionedAdapter.addSection(new ExpandableCategorySection(ResultDetailsTest.this,
                                    category.getCategoryName(), subCategoryList, clickListener, categoryId));
                    }
                    /*assert categoryId != null;
                    if (categoryId.equalsIgnoreCase("all"))
                        sectionedAdapter.addSection(new ExpandableCategorySection(ResultDetailActivity.this,
                                category.getCategoryName(),
                                category.getSubCategory(), clickListener, categoryId));
                    else if (categoryId.equalsIgnoreCase(category.getCategoryId())) {
                        sectionedAdapter.addSection(new ExpandableCategorySection(ResultDetailActivity.this,
                                category.getCategoryName(),
                                category.getSubCategory(), clickListener, categoryId));
                    }*/
                }
                rvCategory.setAdapter(sectionedAdapter);
                sectionedAdapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getReviveDetails();
        //insertFeedbackAnswers();
    }

    @Override
    public void onHeaderRootViewClicked(@NonNull ExpandableCategorySection section) {
        final SectionAdapter sectionAdapter = sectionedAdapter.getAdapterForSection(section);

        // store info of current section state before changing its state
        final boolean wasExpanded = section.isExpanded();
        final int previousItemsTotal = section.getContentItemsTotal();

        section.setExpanded(!wasExpanded);
        sectionAdapter.notifyHeaderChanged();

        if (wasExpanded) {
            sectionAdapter.notifyItemRangeRemoved(0, previousItemsTotal);
        } else {
            sectionAdapter.notifyAllItemsInserted();
        }
    }

    @Override
    public void onItemRootViewClicked(@NonNull ExpandableCategorySection section, int itemAdapterPosition, String name, String categoryId) {
        Log.d(TAG, "Get Adapter Position <<::>> " + itemAdapterPosition);
        Log.d(TAG, "Title " + section.getCategoryTitle());
        Log.d(TAG, "Sub Category Name " + name);
        Log.d(TAG, "Category Id " + categoryIDMap.get(section.getCategoryTitle()));

        String subCategoryId = section.getSubcategoryMap().get(name);
        Log.d(TAG, "Sub Category Id ==> " + subCategoryId);

        for (Category category : categoryList) {
            if (category.getCategoryId().equalsIgnoreCase(categoryIDMap.get(section.getCategoryTitle()))) {
                Log.d(TAG, "Category Id matched " + category.getCategoryId());

                for (SubCategory subCategory : category.getSubCategory()) {
                    if (subCategory.getSubCategoryId().equalsIgnoreCase(String.valueOf(subCategoryId))) {

                       /* Intent intent = new Intent(this, ViewAllProductsActivity.class);
                        intent.putExtra("subcategory", new Gson().toJson(subCategory));
                        intent.putExtra("sub_name", name);
                        intent.putExtra(Constants.CATEGORY_ID, categoryId);
                        startActivity(intent);*/
                    }
                }

                break;
            }
        }
    }

    //TODO: To Store the Feedback suboption questionId and optionId
    //  HashMap<String, String> feedbackSubOptionMap = new HashMap<>();

    private void showDialogCustom(List<SubQuestion> subQuestions) {
        dialog = new Dialog(ResultDetailsTest.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.feedback_radio_option_list);
        dialog.show();

        btnNext = (Button) dialog.findViewById(R.id.btnNext);
        btnBack = (Button) dialog.findViewById(R.id.btnBack);
        userFeedback= (EditText) dialog.findViewById(R.id.user_feedback);
        parentLl= (LinearLayout) dialog.findViewById(R.id.parent_ll);


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

                Log.d(TAG, "SUBQUESTION NUM >>> "+ questionCnt);
                Log.d(TAG, "FEEDBACK >>> "+ userFeedback.getText().toString());
                prodHashMap.put(String.valueOf(questionCnt), userFeedback.getText().toString());
                prodArrayList.add(prodHashMap);
                userFeedback.setText("");
                userFeedback.setVisibility(View.GONE);


                btnBack.setVisibility(questionCnt == -1 ? View.INVISIBLE : View.VISIBLE);

             /*   if (subQuestions.get(questionCnt - 1).getMandatory().equalsIgnoreCase("Y")){
                    Toast.makeText(ResultDetailActivity.this,"Please Select Option",Toast.LENGTH_LONG).show();
                }
*/

                if (subQuestions.get(questionCnt - 1).getSubQuestionId().equalsIgnoreCase("4")) {
                    insertFeedbackAnswers();
                    dialog.dismiss();
                    // Toast.makeText(ResultDetailActivity.this,"Last Item",Toast.LENGTH_LONG).show();
                } else {

                    optionMap.clear();
                    radioGroup.removeAllViews();
                    radioGroup.clearCheck();
                }
                if (questionCnt <= subQuestions.size())
                    // getQuestions();
                    questionCnt++;
                nextQuestionData();

                //dialog.dismiss();
            }
        });

        tvQuestion = dialog.findViewById(R.id.tvQuestion);
        radioGroup = dialog.findViewById(R.id.radiogroup);
        nextQuestionData();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    private void nextQuestionData() {

        if (questionCnt <= subQuestions.size()) {

            tvQuestion.setText(subQuestions.get(questionCnt - 1).getSubQuestion());


            List<SubQuestionOption> subQuestionOptions = subQuestions.get(questionCnt - 1).getSubQuestionOptions();

            for (SubQuestionOption option : subQuestionOptions) {


                optionMap.put(option.getSubOptionId(), option.getSubOptionsName());
                subQuestionOptionHashMap.put(option.getSubOptionId(), option);
            }

            addRadioButtons(optionMap);
        }

    }


    private void addRadioButtons(HashMap<String, String> optionMap) {

        String selectedId = "";
        // isChecked = false;
        //  Log.w(TAG, "Question Number <<::>> " + questionNum);

        radioGroup.setOrientation(LinearLayout.VERTICAL);

        for (String id : optionMap.keySet()) {

            AppCompatRadioButton radioButton = new AppCompatRadioButton(ResultDetailsTest.this);
            radioButton.setId(Integer.parseInt(id));
            radioButton.setText(optionMap.get(id));
            radioButton.setCompoundDrawablePadding(10);
            radioButton.setOnClickListener(this);
            radioGroup.addView(radioButton);

        }
        btnNext.setEnabled(isChecked);
        btnNext.setBackgroundColor(isChecked ? getResources().getColor(R.color.colorAccent) :
                Color.parseColor("#808185"));
    }


    @Override
    public void insertFeedback(Answer answer) {
        subQuestions = answer.getSubQuestion();


        if (answer.getOptionsName().equalsIgnoreCase("Average")) {
            selectedFeedbackID = answer.getOptionId();

            if (successData.equalsIgnoreCase("Data Saved Successfully!")){
                Toast.makeText(ResultDetailsTest.this, "Feedback already given by customer", Toast.LENGTH_SHORT).show();

            }
            else   if (errorCode ==400){
                Toast.makeText(ResultDetailsTest.this, "Feedback already given by customer", Toast.LENGTH_SHORT).show();
            }

            else {
                showRecyclerDialog(ResultDetailsTest.this,subQuestions);
            }

        }


        else if (answer.getOptionsName().equalsIgnoreCase("Poor")) {
            selectedFeedbackID = answer.getOptionId();

            if (successData.equalsIgnoreCase("Data Saved Successfully!")){
                Toast.makeText(ResultDetailsTest.this, "Feedback already given by customer", Toast.LENGTH_SHORT).show();

            }
            else if (errorCode ==400){
                Toast.makeText(ResultDetailsTest.this, "Feedback already given by customer", Toast.LENGTH_SHORT).show();
            }

            else {
                showRecyclerDialog(ResultDetailsTest.this,subQuestions);
            }

        }

        else if (answer.getOptionsName().equalsIgnoreCase("Excellent")) {
            selectedFeedbackID = answer.getOptionId();

            if (successData.equalsIgnoreCase("Data Saved Successfully!")){
                Toast.makeText(ResultDetailsTest.this, "Feedback already given by customer", Toast.LENGTH_SHORT).show();

            }

            else if(errorCode ==400){
                Toast.makeText(ResultDetailsTest.this, "Feedback already given by customer", Toast.LENGTH_SHORT).show();
            }
            else {
                // showExcellentFeedbackDialog();

                if(answer.getSubQuestionStatus().equalsIgnoreCase("N")){
                    showRecyclerDialog(ResultDetailsTest.this,subQuestions);
                }else {
                   // showExcellentFeedbackDialog();
                }


            }






            // insertFeedbackAnswers();


        }

    }

    HashMap<String, String> feedbackSubOptionMap = new HashMap<>();
    public void showRecyclerDialog(Context context, List<SubQuestion> subQuestions){

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.feedback_radio_option_list);

        Button btnNext = (Button) dialog.findViewById(R.id.btnNext);
        userFeedback= (EditText) dialog.findViewById(R.id.user_feedback);
        userFeedbackReycler= (EditText) dialog.findViewById(R.id.user_feedback_reycler);

        Button btnSubmit = (Button) dialog.findViewById(R.id.btndialog);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioButtonCheck!=null)



              /*  if (radioButtonCheck.size() <4){
                    Toast.makeText(ResultDetailsTest.this,"Please Select Radio Options",Toast.LENGTH_LONG).show();
                }
                else {*/
                    insertFeedbackAnswers();
                dialog.dismiss();
                //  }


            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        TextView   tvQuestion = dialog.findViewById(R.id.tvQuestion);

        if (questionCnt <= subQuestions.size()) {
            tvQuestion.setText(subQuestions.get(questionCnt -1).getSubQuestion());

        }

        List<SubQuestionOption> subQuestionOptions = subQuestions.get(questionCnt -1).getSubQuestionOptions();

        for (SubQuestionOption option : subQuestionOptions) {



            optionMap.put(option.getSubOptionId(), option.getSubOptionsName());
            subQuestionOptionHashMap.put(option.getSubOptionId(), option);
        }

        // addRadioButtons(optionMap);

        RecyclerView recyclerView = dialog.findViewById(R.id.recycler);
        LatestFeedbackTestAdapter adapterRe = new LatestFeedbackTestAdapter(ResultDetailsTest.this, subQuestions,custTransId,locationCode,selectedFeedbackID,this);
        recyclerView.setAdapter(adapterRe);
        recyclerView.setLayoutManager(new LinearLayoutManager(ResultDetailsTest.this, LinearLayoutManager.VERTICAL, false));

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dialog.show();

    }

    /*private void showExcellentFeedbackDialog() {
        dialog = new Dialog(ResultDetailsTest.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog_excellent_feedback);
        dialog.show();

        btnNext = (Button) dialog.findViewById(R.id.btnNext);

        excellentUserFeedback= (EditText) dialog.findViewById(R.id.excellent_user_feedback);
        parentLl= (LinearLayout) dialog.findViewById(R.id.parent_ll);
        excellentUserFeedback.setVisibility(View.VISIBLE);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertExcellentFeedbackAnswers();
            }
        });
        tvQuestion = dialog.findViewById(R.id.tvQuestion);
        radioGroup = dialog.findViewById(R.id.radiogroup);

    }*/


    public void insertExcellentFeedbackAnswers() {


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("custTransId", custTransId);
            jsonObject.put("locationCode", locationCode);


        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONArray optionsArray = new JSONArray();

        //Create json objects for two filter Ids
        JSONObject optionsObject = new JSONObject();
        //  JSONObject jsonParam2 =new JSONObject();

        try {

            optionsObject.put("questionNo", "1");
            optionsObject.put("optionId", selectedFeedbackID);
            optionsObject.put("remarks",excellentUserFeedback.getText().toString() );


            JSONArray jsonArray = new JSONArray();
            Log.d(TAG, "feedbackSubOptionMap >>>>> "+feedbackSubOptionMap.size());
            for (String subQuestion : feedbackSubOptionMap.keySet()) {
                JSONObject optionJsonObject = new JSONObject();
                optionJsonObject.put("questionNo", "1");
                optionJsonObject.put("optionId", selectedFeedbackID);
                optionJsonObject.put("remarks",userFeedback.getText().toString());
                optionJsonObject.put("subQuestionNo", subQuestion);

                optionJsonObject.put("subOptionId", feedbackSubOptionMap.get(subQuestion));
                jsonArray.put(optionJsonObject);
            }

            optionsObject.put("subOptions", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Add the filter Id object to array
        optionsArray.put(optionsObject);


        //Add array to main json object
        try {
            jsonObject.put("options", optionsArray);
            Log.d(TAG, "JSON OBJECT ------------------");
            Log.d(TAG, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        feedbackData = jsonObject.toString();
        Log.w(TAG, "Feedback Data <<::>> " + feedbackData);

        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json"), feedbackData);

        APIInterface apiInterface = RetrofitService.getRetrofit().create(APIInterface.class);
        apiInterface.postFeedback(requestBody).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response1>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response1 value) {
                        Log.e(TAG, "Value<<::>> " + value.toString());

                        successData =  value.getMessages();

                        if (value.getMessages() == null || value.getMessages().isEmpty())
                            Toast.makeText(ResultDetailsTest.this, value.getMessage(), Toast.LENGTH_SHORT).show();
                        else
                            dialog.dismiss();
                        Toast.makeText(ResultDetailsTest.this, value.getMessages(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(Throwable e) {
                        HttpException error = (HttpException) e;
                        errorBody = Objects.requireNonNull(error.response().errorBody()).toString();
                        errorCode = error.code();
                        dialog.dismiss();

                        Log.d(TAG, "Exception e <<::>> " + e);
                        Log.d(TAG, "errorBody <<::>> " + errorBody);
                        Toast.makeText(ResultDetailsTest.this, "Feedback already given by customer", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void insertFeedbackAnswers() {


       /* JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("custTransId", custTransId);
            jsonObject.put("ratingId", answer.getOptionsValue());
            jsonObject.put("ratingName", answer.getOptionsName());

        } catch (Exception e) {
            e.printStackTrace();
        }

        feedbackData = jsonObject.toString();
        Log.w(TAG, "Feedback Data <<::>> " + feedbackData);*/

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("custTransId", custTransId);
            jsonObject.put("locationCode", locationCode);


        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONArray optionsArray = new JSONArray();

        //Create json objects for two filter Ids
        JSONObject optionsObject = new JSONObject();



        //  JSONObject jsonParam2 =new JSONObject();



        try {

            optionsObject.put("questionNo", "1");
            optionsObject.put("optionId", selectedFeedbackID);
            optionsObject.put("remarks", "");


            JSONArray jsonArray = new JSONArray();
            Log.d(TAG, "feedbackSubOptionMap >>>>> "+feedbackSubOptionMap.size());
            //for (String subQuestion : feedbackSubOptionMap.keySet()) {
            for (String subQuestion : radioButtonCheck.keySet()) {
                String remark = "";
                if (remarkMap.containsKey(subQuestion))
                    remark = remarkMap.get(subQuestion);

                Log.d(TAG, "SUBQUES > "+subQuestion);

                ArrayList<String> suboptionIdList = radioButtonCheck.get(subQuestion);

                for (String suboptionId : suboptionIdList) {
                    // Log.d(TAG, "remark > "+remark);
                    JSONObject optionJsonObject = new JSONObject();
                    optionJsonObject.put("questionNo", "1");
                    optionJsonObject.put("optionId", selectedFeedbackID);
                    if (suboptionId.equalsIgnoreCase("5"))
                        optionJsonObject.put("remarks", remark);
                    else
                        optionJsonObject.put("remarks", "");
                    optionJsonObject.put("subQuestionNo", subQuestion);
                    optionJsonObject.put("subOptionId",suboptionId);
                    jsonArray.put(optionJsonObject);
                }
            }

            optionsObject.put("subOptions", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Add the filter Id object to array
        optionsArray.put(optionsObject);


        //Add array to main json object
        try {
            jsonObject.put("options", optionsArray);
            Log.d(TAG, "JSON OBJECT ------------------");
            Log.d(TAG, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        feedbackData = jsonObject.toString();
        Log.w(TAG, "InsertFeedback Answer Feedback Data <<::>> " + feedbackData);




        /*feedbackViewModel.postFeedbackDetails(jsonObject.toString());

        feedbackViewModel.getFeedbackResponse().observe(this, response1 -> {
            Toast.makeText(this, response1.getMessage(), Toast.LENGTH_SHORT).show();
        });*/

        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json"), feedbackData);

        APIInterface apiInterface = RetrofitService.getRetrofit().create(APIInterface.class);
        apiInterface.postFeedback(requestBody).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response1>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response1 value) {
                        Log.e(TAG, "Value<<::>> " + value.toString());

                        successData =  value.getMessages();

                        if (value.getMessages() == null || value.getMessages().isEmpty())
                            Toast.makeText(ResultDetailsTest.this, value.getMessage(), Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(ResultDetailsTest.this, value.getMessages(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(Throwable e) {
                        HttpException error = (HttpException) e;
                        errorBody = Objects.requireNonNull(error.response().errorBody()).toString();
                        errorCode = error.code();

                        Log.d(TAG, "Exception e <<::>> " + e);
                        Log.d(TAG, "errorBody <<::>> " + errorBody);
                        Toast.makeText(ResultDetailsTest.this, "Feedback already given by customer", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void insertRadioClickFeedback(TextView v, String radioOptionID, int radioButtonCheckValidation, HashMap<String,ArrayList<String>> selectedProducts) {
        Log.d(TAG, " Name " + ((TextView) v).getText() + " Id is " + v.getId());
        Log.d(TAG, "questionCnt " + questionCnt);


        radioButtonCheck = selectedProducts;





        if (questionCnt == 1) {
            gender = ((TextView) v).getText().toString();
        }







      /* for (SubQuestion subQuestion: subQuestions){
            feedbackSubOptionMap.put(subQuestion.getSubQuestionId(), Objects.requireNonNull(subQuestionOptionHashMap.get(String.valueOf(v.getId()))).getSubOptionId());

        }*/
        if (questionCnt <= subQuestions.size()){
            //  radioCheckValue =  v.isChecked();
            feedbackSubOptionMap.put(String.valueOf(questionCnt), Objects.requireNonNull(subQuestionOptionHashMap.get(String.valueOf(v.getId()))).getSubOptionId());
            questionCnt++;
        }




        Log.d(TAG, "SUB OPTION QUESTION ID "+subQuestionOptionHashMap.get(String.valueOf(v.getId())).getSubOptionId());

        //  questionCnt++;


        //TODO: Inserting feedback subquestionId and optionId on click of radio button

        // questionCnt++;

      /*  if (subQuestionOptionHashMap.get(String.valueOf(v.getId())).getSubOptionsValue().equalsIgnoreCase("5")){
            //  userFeedback.setText("");

            userFeedback.setVisibility(View.VISIBLE);

        }
        else {
            userFeedback.setVisibility(View.GONE);
        }*/
    }

    HashMap<String,String> remarkMap = new HashMap<>();
    @Override
    public void insertReamrk(HashMap<String, String> selectedProducts) {
        remarkMap = selectedProducts;
        for (String s: remarkMap.keySet())
        {
            Log.d("OptionalText",s + " "+remarkMap.get(s));
        }
    }


    @Override
    public void onClick(View v) {

    }


}




