package com.example.advisoryservice.ui.genderquestions;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.advisoryservice.R;
import com.example.advisoryservice.base.BaseActivity;
import com.example.advisoryservice.data.model.Questions;
import com.example.advisoryservice.data.model.Response;
import com.example.advisoryservice.data.model.ServicePostRequest;
import com.example.advisoryservice.data.model.instructionDetail.InstructionDetail;
import com.example.advisoryservice.data.model.questions.Option;
import com.example.advisoryservice.data.model.questions.SubQuestionOption;
import com.example.advisoryservice.data.model.revieve.Detail;
import com.example.advisoryservice.data.model.revieve.Eye;
import com.example.advisoryservice.data.model.revieve.RevieveResponse;
import com.example.advisoryservice.data.rest.APIInterface;
import com.example.advisoryservice.data.rest.RetrofitService;
import com.example.advisoryservice.ui.customerdetail.CustomerInsertDetailsActivity;
import com.example.advisoryservice.ui.main.MainActivity;
import com.example.advisoryservice.ui.productquestions.CustomerProductQuestionActivity;
import com.example.advisoryservice.ui.selectedadvisory.AdvisorySelectionAdapter;
import com.example.advisoryservice.ui.selectedadvisory.SelectedAdvisoryActivity;
import com.example.advisoryservice.ui.skinanalysiscamera.TestCameraActivity;
import com.example.advisoryservice.util.Constants;
import com.example.advisoryservice.util.Log;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;

public class GenderQuestionsActivity extends BaseActivity implements View.OnClickListener,MultipleOptionAdapter.ClickListener {

    public static final String TAG = GenderQuestionsActivity.class.getSimpleName();

    TextView tvQuestion, tvErrorMessage;
    ImageView ivImage;
    RadioGroup radioGroup;
    ProgressBar progressBar;
    Button btnBack, btnNext;

    GenderQuestionViewModel viewModel;
    LinkedHashMap<String, String> optionMap = new LinkedHashMap<>();
    LinkedHashMap<String, String> imageMap = new LinkedHashMap<>();
    LinkedHashMap<String, String> questionAnswer = new LinkedHashMap<>();
    LinkedHashMap<String, Option> allOptionMap = new LinkedHashMap<>();
    LinkedHashMap<String, LinkedHashMap<String, String>> fullMap = new LinkedHashMap<>();

    Questions questionsModel = new Questions();
    Gson gson;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spe;
    ProgressDialog pDialog;
    InstructionDetail instructionDetail;

    int questionCnt = 1;
    String gender = "", customerTransId = "", customerCode = "", locationCode = "", advisory_service_number, advisory_customer_transction_id;
    boolean isChecked = false, hasInfoUrl = false, isSuccess = false, isTakeSelfie = false;

    private APIInterface apiInterface;
    RecyclerView recyclerMultipleOptionSelection;
    List<Option> optionList;
    String advisoryGender, advisoryGenderShort;
    HashMap<String, ArrayList<String>> selectMultiOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(getSharedPreferences().getBoolean(Constants.TABLET, false) ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_gender_based_question_view);

        init();

        gson = new Gson();
        sharedPreferences = getSharedPreferences();
        locationCode = sharedPreferences.getString(Constants.LOCATION_CODE, "");
        Log.d(TAG, "Location Code <<::>> " + locationCode);

        if (isTablet())
            setTheme(R.style.TabletAppTheme);
        else
            setTheme(R.style.AppTheme);

        apiInterface = RetrofitService.getRetrofit().create(APIInterface.class);
        viewModel = ViewModelProviders.of(this).get(GenderQuestionViewModel.class);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
           /* customerTransId = bundle.getString(Constants.CUSTOMER_TRANS_ID);
            customerCode = bundle.getString(Constants.CUSTOMER_CODE);
            Log.w(TAG, "Customer Code <<::>> " + customerCode);
            Log.w(TAG, "Customer TransId <<::>> " + customerTransId);*/

            advisory_service_number = bundle.getString(Constants.ADVISORY_SERVICE_NUMBER);
            advisory_customer_transction_id = bundle.getString(Constants.ADVISORY_CUSTOMER_TRANSCTION_ID);
            advisoryGender = bundle.getString(Constants.ADVISORY_USER_GENDER);
            /*if (advisoryGender.equalsIgnoreCase("MALE")){
                advisoryGenderShort = "M";
            }
            else if (advisoryGender.equalsIgnoreCase("FEMALE")){
                advisoryGenderShort = "F";

            }*/


        }

        //customerTransId = "2738";

        // observeQuestionViewModel();
        observeQuestionViewModelInstructions();
        postServiceData();
    }


    private void postServiceData() {

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("advisoryService", advisory_service_number);
            jsonObject.put("custTransId", advisory_customer_transction_id);
            jsonObject.put("locationCode", locationCode);
            jsonObject.put("gender", advisoryGender);

            ServicePostRequest servicePostRequest = new ServicePostRequest();

            servicePostRequest.setAdvisoryService(advisory_service_number);
            servicePostRequest.setCustTransId(advisory_customer_transction_id);
            servicePostRequest.setLocationCode(locationCode);
            servicePostRequest.setGender(advisoryGender);
            Log.e("postServiceData", "ServicePost Request <<::>> " + jsonObject.toString());

            Call<Questions> call = apiInterface.insertServiceDetails(servicePostRequest);
            call.enqueue(new Callback<Questions>() {
                @Override
                public void onResponse(Call<Questions> call, retrofit2.Response<Questions> response) {
                    JSONObject jsonObject = null;
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    if (response.code() == 200) {
                        Log.e("onRes@PSD", ""+json);
                        progressBar.setVisibility(View.GONE);
                        questionsModel = gson.fromJson(json, Questions.class);
                        getQuestions();
                    } else {
                        Toast.makeText(GenderQuestionsActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }

                 /*   String messageString = null;
                    try {
                        messageString = jsonObject.getString("Message");
                        Log.d(TAG, "messageString<<::>> " + messageString);
                        Toast.makeText(GenderQuestionsActivity.this, messageString, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/


                }

                @Override
                public void onFailure(Call<Questions> call, Throwable t) {
                    Log.e(TAG, "Exception <<::>> " + t);
                }
            });


        } catch (Exception e) {
            Log.e(TAG, "Exception <<::>> " + e);
            e.printStackTrace();
            hideProgressDialog();
        }
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void addRadioButtons(HashMap<String, String> optionMap, int questionNum, boolean hasInfoUrl) {

        String selectedId = "";
        isChecked = false;
        Log.w(TAG, "Question Number <<::>> " + questionNum);

        radioGroup.setOrientation(LinearLayout.VERTICAL);

        for (String id : optionMap.keySet()) {

            AppCompatRadioButton radioButton = new AppCompatRadioButton(this);
            radioButton.setId(Integer.parseInt(id));
            radioButton.setText(optionMap.get(id));
            radioButton.setCompoundDrawablePadding(10);
            if (questionsModel.getData().get(questionCnt - 1).getIsInfoFlag().equalsIgnoreCase("Y") && questionNum != 1)
                radioButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.info, 0);
            radioButton.setOnClickListener(this);

            if (backPressData.containsKey(String.valueOf(questionNum))) {

                Log.w(TAG, "QuestionAnswer contains questionNumber " + questionNum);
                if (Objects.requireNonNull(backPressData.get(String.valueOf(questionNum))).equalsIgnoreCase(allOptionMap.get(id).getOptionsValue())) {

                    Log.w(TAG, "Selected ID <<::>> " + id);
                    selectedId = id;
                    radioButton.setChecked(true);
                    isChecked = true;
                } else
                    radioButton.setChecked(false);
            }

            radioGroup.addView(radioButton);
        }

       /* btnNext.setEnabled(isChecked);
        btnNext.setBackgroundColor(isChecked ? getResources().getColor(R.color.colorAccent) :
                Color.parseColor("#808185"));
        if (isChecked)
            Glide.with(this).load(imageMap.get(selectedId))
                    .placeholder(getCircularProgressDrawable()).into(ivImage);*/

    }

    HashMap<String, SubQuestionOption> subQuestionOptionHashMap = new HashMap<>();
    HashMap<String, String> backPressData = new HashMap<>();
    boolean hasSubQuestions = false;

    @Override
    public void onClick(View v) {

        Log.d(TAG, " Name " + ((RadioButton) v).getText() + " Id is " + v.getId());
        if (questionCnt == 1) {
            gender = ((RadioButton) v).getText().toString();
        }

        Log.d(TAG, "Has Sub Questions <<::>> " + hasSubQuestions);

        if (!hasSubQuestions) {
            if (imageMap.size() > 0) {
                Log.w(TAG, "Radio Button Id <<::>> " + v.getId());
                Glide.with(this).load(imageMap.get(String.valueOf(v.getId())))
                        .placeholder(getCircularProgressDrawable()).into(ivImage);
            }
        }

        Log.d(TAG, "Question Number being inserted " + questionCnt);

        backPressData.put(String.valueOf(questionCnt), Objects.requireNonNull(allOptionMap.get(String.valueOf(v.getId()))).getOptionsValue());

        if (!hasSubQuestions)
            questionAnswer.put(String.valueOf(questionCnt), Objects.requireNonNull(allOptionMap.get(String.valueOf(v.getId()))).getOptionsValue());
        else {
            questionAnswer.put(String.valueOf(questionCnt), Objects.requireNonNull(subQuestionOptionHashMap.get(String.valueOf(v.getId()))).getSubOptionsValue());
            hasSubQuestions = false;
        }

   /*     btnNext.setEnabled(true);
        btnNext.setBackgroundColor(getResources().getColor(R.color.colorAccent));*/


        if (allOptionMap.get(String.valueOf(v.getId())).getSubQuestionStatus().equalsIgnoreCase("N") ||
                allOptionMap.get(String.valueOf(v.getId())).getSubQuestionStatus().isEmpty()) {

            hasSubQuestions = false;
            if (questionsModel.getData().get(questionCnt - 1).getIsInfoFlag().equalsIgnoreCase("Y") && questionCnt != 1) {
                if (advisoryGender.equalsIgnoreCase("male"))
                    popUpDialog(Objects.requireNonNull(allOptionMap.get(String.valueOf(v.getId()))).getInfoText(),
                            Objects.requireNonNull(allOptionMap.get(String.valueOf(v.getId()))).getInfoImgUrl());
                else
                    popUpDialog(Objects.requireNonNull(allOptionMap.get(String.valueOf(v.getId()))).getInfoText(),
                            Objects.requireNonNull(allOptionMap.get(String.valueOf(v.getId()))).getInfoImgUrl());

            }


        } else {

            hasSubQuestions = true;
            /*btnNext.setEnabled(false);
            btnNext.setBackgroundColor(Color.parseColor("#808185"));*/

            optionMap.clear();
            radioGroup.removeAllViews();
            radioGroup.clearCheck();
            hasInfoUrl = false;

            Log.w(TAG, "Question Cnt <<::>> " + questionCnt);

            int position = v.getId();
            position = position - 1;
            int index = radioGroup.indexOfChild(v);
            Log.d(TAG, "Radio Button Index <<::>> " + index);

            List<SubQuestionOption> subQuestionOptionList = questionsModel.getData().get(questionCnt - 1).getOptions()
                    .get(position).getSubQuestion().get(0).getSubQuestionOptions();

            tvQuestion.setText(questionsModel.getData().get(questionCnt - 1).getOptions()
                    .get(position).getSubQuestion().get(0).getSubQuestion());
            String questionNum = questionsModel.getData().get(questionCnt - 1).getQuestionId();

            for (SubQuestionOption option : subQuestionOptionList) {

                hasInfoUrl = false;

                optionMap.put(option.getSubOptionId(), option.getSubOptionsName());
                subQuestionOptionHashMap.put(option.getSubOptionId(), option);
            }

            addRadioButtons(optionMap, Integer.parseInt(questionNum), hasInfoUrl);

        }

    }


    String optionValue;

    private void getQuestions() {

        imageMap.clear();
        optionMap.clear();
        radioGroup.removeAllViews();
        radioGroup.clearCheck();
        hasInfoUrl = false;

        Log.e("getQuestions", "Question Cnt <<::>> " + questionCnt);


       /* if (questionCnt == 9) {

            optionValue = questionAnswer.get("8");
            Log.d(TAG, "Option Value <<::>> " + optionValue);

            if (optionValue.equalsIgnoreCase("7")) {

                questionAnswer.put(String.valueOf(questionCnt), "");
                questionCnt++;


            }
        }*/

        if (questionCnt <= questionsModel.getData().size()) {

            optionList = questionsModel.getData().get(questionCnt - 1).getOptions();

            if (questionCnt == 1)
                Glide.with(this).load(questionsModel.getData().get(questionCnt - 1).getDefaultImage())
                        .placeholder(getCircularProgressDrawable()).into(ivImage);
            else {
                if (gender.equalsIgnoreCase("Male")) {
                    Glide.with(this).load(questionsModel.getData().get(questionCnt - 1).getDefaultImage())
                            .placeholder(getCircularProgressDrawable()).into(ivImage);
                } else {
                    Glide.with(this).load(questionsModel.getData().get(questionCnt - 1).getDefaultImage())
                            .placeholder(getCircularProgressDrawable()).into(ivImage);
                }
            }


            tvQuestion.setText(questionsModel.getData().get(questionCnt - 1).getQuestion());
            String questionNum = questionsModel.getData().get(questionCnt - 1).getQuestionId();


            if (questionCnt == 7) {

                optionValue = questionAnswer.get("6");
                Log.d(TAG, "Option Value <<::>> " + optionValue);

                for (Option option : optionList) {

                    // hasInfoUrl = !option.getMaleInfoText().isEmpty();

                    if (gender.equalsIgnoreCase("Male")) {
                        imageMap.put(option.getOptionId(), option.getImgUrl());
                    } else {
                        imageMap.put(option.getOptionId(), option.getImgUrl());
                    }
                    if (!option.getOptionsValue().equalsIgnoreCase(optionValue)) {
                        optionMap.put(option.getOptionId(), option.getOptionsName());
                        allOptionMap.put(option.getOptionId(), option);
                    }
                }

            } else if (questionCnt != 1) {

                for (Option option : optionList) {

                    //   hasInfoUrl = !option.getMaleInfoText().isEmpty();

                    if (gender.equalsIgnoreCase("Male")) {
                        imageMap.put(option.getOptionId(), option.getImgUrl());
                    } else {
                        imageMap.put(option.getOptionId(), option.getImgUrl());
                    }
                    optionMap.put(option.getOptionId(), option.getOptionsName());
                    allOptionMap.put(option.getOptionId(), option);
                }
            } else {

                for (Option option : optionList) {

//                    hasInfoUrl = !option.getMaleInfoText().isEmpty();

                    if (option.getOptionsName().equalsIgnoreCase("Male")) {
                        imageMap.put(option.getOptionId(), option.getImgUrl());
                    } else {
                        imageMap.put(option.getOptionId(), option.getImgUrl());
                    }
                    optionMap.put(option.getOptionId(), option.getOptionsName());
                    allOptionMap.put(option.getOptionId(), option);
                }
            }
            if (questionsModel.getData().get(questionCnt - 1).getMultipleOption().equalsIgnoreCase("Y") && questionCnt != 1) {
                radioGroup.setVisibility(View.GONE);


                // Toast.makeText(this,"Multi Options",Toast.LENGTH_LONG).show();
                /*if (gender.equalsIgnoreCase("male"))
                    popUpDialog(Objects.requireNonNull(allOptionMap.get(String.valueOf(v.getId()))).getMaleInfoText(),
                            Objects.requireNonNull(allOptionMap.get(String.valueOf(v.getId()))).getInfoImgUrl());
                else
                    popUpDialog(Objects.requireNonNull(allOptionMap.get(String.valueOf(v.getId()))).getFemaleInfoText(),
                            Objects.requireNonNull(allOptionMap.get(String.valueOf(v.getId()))).getInfoImgUrl());*/


                MultipleOptionAdapter multipleOptionAdapter = new MultipleOptionAdapter(GenderQuestionsActivity.this, optionList,this);
                GridLayoutManager gridLayoutManager =
                        new GridLayoutManager(GenderQuestionsActivity.this, 2, RecyclerView.VERTICAL, false);
                gridLayoutManager.setReverseLayout(true);
                recyclerMultipleOptionSelection.setLayoutManager(gridLayoutManager);
                //   recyclerMultipleOptionSelection.setNestedScrollingEnabled(false);
                recyclerMultipleOptionSelection.setAdapter(multipleOptionAdapter);

            }


            addRadioButtons(optionMap, Integer.parseInt(questionNum), hasInfoUrl);

        } else {
            Toast.makeText(this, "Question cnt " + questionCnt, Toast.LENGTH_SHORT).show();
        }
    }

    private void postQuestionData(String value, String revieveResponse) {

        try {
            isSuccess = false;

            if (!revieveResponse.isEmpty()) {

                RevieveResponse revieveValue = gson.fromJson(revieveResponse, RevieveResponse.class);

                List<Detail> data = revieveValue.getData().get(0).getDetail();
                List<Eye> dataEye = revieveValue.getData().get(0).getEyes();


                for (Detail detail : data) {

                    if (detail.getLINENO().equalsIgnoreCase("1")) {
                        questionAnswer.put("6", detail.getOPTIONVALUE());
                    }

                    if (detail.getLINENO().equalsIgnoreCase("1") &&
                            detail.getOPTIONVALUE().equalsIgnoreCase("7")) {
                        questionAnswer.put("8", "2");
                        break;
                    }

                    if (detail.getLINENO().equalsIgnoreCase("2"))
                        questionAnswer.put("7", detail.getOPTIONVALUE());
                }

               /* for (Eye eye : dataEye ){
                   if (eye.getLINENO().equalsIgnoreCase("1")){
                       questionAnswer.put("10", eye.getOPTIONVALUE());
                   }
                }*/
            }

            Log.w(TAG, "questionAnswer " + questionAnswer.size());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("custTransId", advisory_customer_transction_id);
            jsonObject.put("locationCode", locationCode);
            jsonObject.put("advisoryService", advisory_service_number);
            jsonObject.put("gender", advisoryGender);

            JSONArray jsonArraySubOptions = new JSONArray();

            JSONArray jsonArray = new JSONArray();
            for (String questionNum : questionAnswer.keySet()) {
                JSONObject optionJsonObject = new JSONObject();
                optionJsonObject.put("questionNo", questionNum);
                optionJsonObject.put("optionId", questionAnswer.get(questionNum));
                optionJsonObject.put("subOptions", jsonArraySubOptions);
                jsonArray.put(optionJsonObject);
            }

            jsonObject.put("options", jsonArray);

            Log.w(TAG, "Final JSON <<::>> ");
            Log.d(TAG, jsonObject.toString());

            if (value.equalsIgnoreCase("valid")) {
                Log.d(TAG, "Validate Question");
                validateQuestion(jsonObject.toString(), revieveResponse);

            } else {
                Log.d(TAG, "Insert Question");
                insertQuestionAnswers(jsonObject.toString());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            btnNext.setEnabled(true);
            if (pDialog != null && pDialog.isShowing())
                pDialog.dismiss();
        }
    }

    private void validateQuestion(String data, String revieveResponse) {

        Log.d(TAG, "Validate Question revieveResponse " + revieveResponse);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), data);

        Call<ResponseBody> call = apiInterface.validateQuestionAnswers(requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {

                Log.d(TAG, "response.code() " + response.code());

                if (response.code() == 400) {
                    btnNext.setEnabled(false);

                    if (pDialog != null && pDialog.isShowing())
                        pDialog.dismiss();

                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String messageString = jsonObject.getString("Message");
                        Log.d(TAG, "messageString<<::>> " + messageString);

                        Spanned message;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            message = Html.fromHtml(messageString, Html.FROM_HTML_MODE_COMPACT);
                        } else {
                            message = Html.fromHtml(messageString);
                        }
                        Log.w(TAG, "Message <<::>> " + message);
                        showDialog(message);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } else {
                    btnNext.setEnabled(true);

                    if (response.body() != null) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.body().string());
                            String messageString = jsonObject.getString("message");

                            Log.d(TAG, "Response body messageString <<::>> " + messageString);
                            Toast.makeText(GenderQuestionsActivity.this, messageString, Toast.LENGTH_SHORT).show();

                            if (!revieveResponse.isEmpty()) {
                                RevieveResponse revieveValue = gson.fromJson(revieveResponse, RevieveResponse.class);
                                for (Detail detail : revieveValue.getData().get(0).getDetail()) {

                                    questionAnswer.put("7", "");

                                    if (detail.getLINENO().equalsIgnoreCase("1") &&
                                            detail.getOPTIONVALUE().equalsIgnoreCase("7"))
                                        break;

                                    if (detail.getLINENO().equalsIgnoreCase("2"))
                                        questionAnswer.put("7", detail.getOPTIONVALUE());
                                }
                                for (Eye eye : revieveValue.getData().get(0).getEyes()) {
                                    questionAnswer.put("8", eye.getOPTIONVALUE());
                                }
                                questionCnt += 4;
                                postQuestionData("insert", "");

                                /*questionCnt += 3;
                                getQuestions();*/
                            }
                            //insertQuestionAnswers(data);

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                            if (pDialog != null && pDialog.isShowing())
                                pDialog.dismiss();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable e) {
                Log.d(TAG, "Error thrown " + e);
                if (e instanceof HttpException) {
                    HttpException exception = (HttpException) e;
                    retrofit2.Response<?> response = exception.response();
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Log.e("Error ", "" + jsonObject.optString("message"));
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

            }
        });
    }

    private void insertQuestionAnswers(String data) {

        Log.w(TAG, "Data <<::>> " + data);
        RequestBody requestBody1 =
                RequestBody.create(MediaType.parse("application/json"), data);

        Log.w(TAG, "requestBody1 <<::>> " + requestBody1.toString());

        apiInterface.postQuestionAnswers(requestBody1).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Response value) {

                        if (pDialog != null && pDialog.isShowing())
                            pDialog.dismiss();

                        try {
                            Toast.makeText(GenderQuestionsActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            btnNext.setEnabled(true);
                        }
                        moveToNextScreen();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Exception Thrown <<::>>> " + e);
                        btnNext.setEnabled(true);
                        if (pDialog != null && pDialog.isShowing())
                            pDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void moveToNextScreen() {
        Log.d(TAG, "Take Selfie <<::>> " + isTakeSelfie);
        Intent intent = new Intent(GenderQuestionsActivity.this, CustomerProductQuestionActivity.class);
        intent.putExtra(Constants.ADVISORY_CUSTOMER_TRANSCTION_ID, advisory_customer_transction_id);
        intent.putExtra(Constants.ADVISORY_SERVICE_NUMBER, advisory_service_number);
        intent.putExtra(Constants.TAKE_SELFIE, isTakeSelfie);
        startActivity(intent);
    }

    private void popUpDialog(String description, String url) {

        try {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.info_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);

            TextView tvDescription = dialog.findViewById(R.id.tvDescription);
            tvDescription.setText(description);
            ImageView ivInfoIcon = dialog.findViewById(R.id.ivInfoIcon);
            TextView tvGotIt = dialog.findViewById(R.id.tvGotIt);

            ivInfoIcon.setVisibility(url.isEmpty() ? View.GONE : View.VISIBLE);

            tvGotIt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();

            Window window = dialog.getWindow();
            assert window != null;
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showDialog(Spanned message) {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.error_dialog);
        dialog.setCancelable(false);

        Button btnRetakeTest = dialog.findViewById(R.id.btnRetakeTest);
        TextView textView = dialog.findViewById(R.id.tvText);
        textView.setText(message);

        dialog.show();

        btnRetakeTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!getSharedPreferences().getString(Constants.FILE_URL, "").isEmpty())
                    deleteImage(getSharedPreferences().getString(Constants.FILE_URL, ""), Constants.FILE_URL);
                if (!getSharedPreferences().getString(Constants.THUMB_IMAGE_FILE_URL, "").isEmpty())
                    deleteImage(getSharedPreferences().getString(Constants.THUMB_IMAGE_FILE_URL, ""), Constants.THUMB_IMAGE_FILE_URL);

                Intent intent = new Intent(GenderQuestionsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void init() {

        setActionBarTitle("Questions");

        tvQuestion = findViewById(R.id.tvQuestion);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        ivImage = findViewById(R.id.ivImage);
        radioGroup = findViewById(R.id.radiogroup);
        progressBar = findViewById(R.id.progressBar);
        btnBack = findViewById(R.id.btnBack);
        btnNext = findViewById(R.id.btnNext);
        recyclerMultipleOptionSelection = findViewById(R.id.recycler_multiple_option_selection);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "NEXT BUTTON CLICKED <<::>> QUESTION COUNT " + questionCnt);

                if (questionCnt == 5 && advisory_service_number.equalsIgnoreCase("1")) {
                    Intent intent = new Intent(GenderQuestionsActivity.this, TestCameraActivity.class);
                    intent.putExtra("gender", advisoryGender);
                    intent.putExtra(Constants.ADVISORY_CUSTOMER_TRANSCTION_ID, advisory_customer_transction_id);
                    intent.putExtra(Constants.INSTRUCTION_DETAIL, new Gson().toJson(instructionDetail));
                    startActivityForResult(intent, 101);


                    Toast.makeText(GenderQuestionsActivity.this, "submit", Toast.LENGTH_LONG).show();
                }else {
                    if (advisory_service_number.equalsIgnoreCase("1")){
                        if (questionCnt == 8)
                            postQuestionData("valid", "");

                        questionCnt++;
                        btnBack.setVisibility(questionCnt == 1 ? View.INVISIBLE : View.VISIBLE);
                        Log.d(TAG, "OnNext Question Count <<::>> " + questionCnt);

                        if (questionCnt <= questionsModel.getData().size())
                            getQuestions();
                        else
                            postQuestionData("insert", "");
                    }


                }

                if (advisory_service_number.equalsIgnoreCase("2")) {
                    questionCnt++;
                    btnBack.setVisibility(questionCnt == 1 ? View.INVISIBLE : View.VISIBLE);
                    Log.d(TAG, "OnNext Question Count <<::>> " + questionCnt);

                    if (questionCnt <= questionsModel.getData().size()){

                        getQuestions();
                    }

                    else
                        postQuestionData("insert", "");

                }


                if (advisory_service_number.equals("2")) {
                    questionCnt++;
                    btnBack.setVisibility(questionCnt == 1 ? View.INVISIBLE : View.VISIBLE);
                    Log.d(TAG, "OnNext Question Count <<::>> " + questionCnt);

                    if (questionCnt <= questionsModel.getData().size()){

                        getQuestions();
                    }

                    else
                        postQuestionData("insert", "");

                }
              /*  if (questionCnt == 10) {
                    Toast.makeText(GenderQuestionsActivity.this, "submit", Toast.LENGTH_LONG).show();



                   *//* Log.d(TAG, "Opening Camera Activity");
                    Log.d(TAG, "GENDER In Intent passed is " + gender);*//*

                    //Intent intent = new Intent(QuestionsActivity.this, CameraActivity.class);
                    *//*Intent intent = new Intent(GenderQuestionsActivity.this, TestCameraActivity.class);
                    intent.putExtra("gender", gender);
                    intent.putExtra(Constants.CUSTOMER_TRANS_ID, customerTransId);
                    intent.putExtra(Constants.INSTRUCTION_DETAIL, new Gson().toJson(instructionDetail));
                    startActivityForResult(intent, 101);*//*

                    try {
                        Log.w(TAG, "questionAnswer " + questionAnswer.size());
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("custTransId", advisory_customer_transction_id);
                        jsonObject.put("locationCode", locationCode);
                        jsonObject.put("advisoryService", advisory_service_number);

                        JSONArray jsonArray = new JSONArray();
                        for (String questionNum : questionAnswer.keySet()) {
                            JSONObject optionJsonObject = new JSONObject();
                            optionJsonObject.put("questionNo", questionNum);
                            optionJsonObject.put("optionId", questionAnswer.get(questionNum));
                            jsonArray.put(optionJsonObject);
                        }

                        jsonObject.put("options", jsonArray);
                        validateQuestion(jsonObject.toString(), "");
                    } catch (Exception e) {
                        e.getMessage();
                    }


                } else {
                    if (questionCnt == 18)
                        postQuestionData("valid", "");

                    questionCnt++;
                    btnBack.setVisibility(questionCnt == 1 ? View.INVISIBLE : View.VISIBLE);
                    Log.d(TAG, "OnNext Question Count <<::>> " + questionCnt);
                    if (questionsModel.getData().get(questionCnt - 1).getQuestionId().equalsIgnoreCase("8"))
                        getTest();
                    if (questionCnt <= questionsModel.getData().size())
                        getQuestions();

                    else
                        postQuestionData("insert", "");
                }*/
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* if (questionCnt == 10)
                    questionCnt -= 3;
                else if (questionAnswer.get(String.valueOf(questionCnt - 1)).equalsIgnoreCase(""))
                    questionCnt -= 2;
                else
                    questionCnt--;

                btnBack.setVisibility(questionCnt == 1 ? View.INVISIBLE : View.VISIBLE);
                getQuestions();

                if (questionCnt >= questionsModel.getData().size() || questionCnt == 0)*/
                onBackPressed();
            }
        });
    }

    private void getTest() {
        Toast.makeText(this, "Test", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "REQUEST CODE <<::>> " + requestCode);
        Log.d(TAG, "RESULT CODE <<::>> " + resultCode);

        if (requestCode == 101) {

            if (resultCode == Activity.RESULT_OK) {
                // TODO Extract the data returned from the child Activity.
                try {

                    isTakeSelfie = data.getExtras().getBoolean(Constants.TAKE_SELFIE);
                    Log.d(TAG, "Take Selfie by user <<::>> " + isTakeSelfie);

                    pDialog = new ProgressDialog(this);
                    pDialog.setCancelable(false);
                    pDialog.setMessage("Processing.. Please wait");

                    if (isTakeSelfie) {
                        pDialog.show();
                        String returnValue = data.getExtras().getString(Constants.ANALYZE_IMAGE);
                        postQuestionData("valid", returnValue);
                        //  moveToNextScreen();

                        // Log.d(TAG, "RETURN VALUE <<::>> " + returnValue);
                        // postQuestionData("valid", returnValue);

                    } else {
                        questionCnt++;
                        getQuestions();
                    }
                    /*Log.d(TAG, "RETURN VALUE <<::>> " + returnValue);

                    AnalyzeImage analyzeImage = gson.fromJson(returnValue, AnalyzeImage.class);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("custTransId", customerTransId);
                    jsonObject.put("locationCode", Integer.parseInt(locationCode));

                    JSONArray jsonArray = new JSONArray();
                    for (Result result : analyzeImage.getResults()) {

                        String gson = new Gson().toJson(result);
                        JSONObject gsonJson = new JSONObject(gson);
                        Log.d(TAG, "RESULT data <<::>> " + gsonJson);

                        String desc = result.getDescription();
                        double value = result.getValue();
                        JSONObject jObj = new JSONObject();
                        jObj.put("description", desc);
                        jObj.put("value", String.valueOf(value));
                        jsonArray.put(jObj);
                    }
                    jsonObject.put("details", jsonArray);

                    Log.d(TAG, "Revieve Post data <<::>> " + jsonObject.toString());

                    postReviveData(jsonObject.toString());*/

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    ProgressDialog progressDialog;
    boolean isError = false;
    String errorMessage = "";


    private void postReviveData(String data) {

        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json"), data);

        APIInterface apiInterface = RetrofitService.getRetrofit().create(APIInterface.class);

        Log.d(TAG, "Calling API revieve/insert");

        apiInterface.revieveInsert(requestBody).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RevieveResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RevieveResponse value) {

                        try {

                            Log.e(TAG, "Response <<::>> " + value.getMessage());

                            Log.d(TAG, "Size " + value.getData().get(0).getDetail());
                            Toast.makeText(GenderQuestionsActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();
                            if (value.getData().get(0).getDetail().size() != 0) {

                                String data = gson.toJson(value);
                                Log.w(TAG, "Revieve Insert Response data <<::>> " + value);
                                postQuestionData("valid", data);

                                /*for (Detail detail : value.getDetail()) {

                                    if (detail.getLINENO().equalsIgnoreCase("1")) {
                                        questionAnswer.put("8", detail.getOPTIONVALUE());
                                        postQuestionData("valid");
                                    }

                                    questionAnswer.put("9", "");
                                    if (detail.getLINENO().equalsIgnoreCase("2"))
                                        questionAnswer.put("9", detail.getOPTIONVALUE());
                                }

                                questionCnt += 3;
                                getQuestions();*/
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.e(TAG, "Exception " + ex);
                            Toast.makeText(GenderQuestionsActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        HttpException error = (HttpException) e;
                        String errorBody = Objects.requireNonNull(error.response().errorBody()).toString();
                        Log.d(TAG, "Exception e <<::>> " + e);
                        Log.d(TAG, "errorBody <<::>> " + errorBody);
                        Toast.makeText(GenderQuestionsActivity.this, errorBody, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        // observeQuestionViewModelInstructions();
    }

    private void observeQuestionViewModelInstructions() {
        HashMap<String, Object> jsonBody = new HashMap<>();
        jsonBody.put("advisoryService", advisory_service_number);
        viewModel.fetchInstructionDetail(jsonBody);


        viewModel.getInstructionDetail().observe(this, instructionDetail -> {
            this.instructionDetail = instructionDetail;
        });



       /* Call<ResponseBody> call = apiInterface.getInstructionDetailApi(advisory_service_number);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {


                Log.d(TAG, "response.code() " + response.code());
                JSONObject jsonObject = null;
                if (response.code()==200){
                    if(response.body()!=null){
                        //  jsonObject = new JSONObject(response.body().toString());


                    }

                }

                if (response.code() == 400) {

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable e) {
                Log.d(TAG, "Error thrown " + e);
                if (e instanceof HttpException) {
                    HttpException exception = (HttpException) e;
                    retrofit2.Response<?> response = exception.response();
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Log.e("Error ", "" + jsonObject.optString("message"));
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

            }
        });
*/

    }

    @Override
    public void insertRadioClickFeedback(TextView v, HashMap<String, ArrayList<String>> selectedProducts) {
        questionAnswer.put(String.valueOf(questionCnt),"2");
       // if (suboptionIdList!=null)
       /* JSONArray jsonArray = new JSONArray();
        JSONObject optionsObject = new JSONObject();

        for (String product : selectedProducts.keySet()){
            ArrayList<String> suboptionIdList = selectedProducts.get(product);

            for (String data : suboptionIdList){

                questionAnswer.put(String.valueOf(questionCnt),data);
                try {
                    optionsObject.put("subQuestionNo", data);
                    optionsObject.put("subOptionId",data);
                    jsonArray.put(optionsObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

        }
        try {
            optionsObject.put("subOptions", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

      //  selectMultiOptions = selectedProducts;


    }
}

