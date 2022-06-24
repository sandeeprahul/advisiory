package com.example.advisoryservice.ui.questions;

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

import com.bumptech.glide.Glide;
import com.crashlytics.android.answers.Answers;
import com.example.advisoryservice.R;
import com.example.advisoryservice.base.BaseActivity;
import com.example.advisoryservice.data.model.Questions;
import com.example.advisoryservice.data.model.Response;
import com.example.advisoryservice.data.model.feedback.Answer;
import com.example.advisoryservice.data.model.instructionDetail.InstructionDetail;
import com.example.advisoryservice.data.model.questions.Option;
import com.example.advisoryservice.data.model.questions.SubQuestionOption;
import com.example.advisoryservice.data.model.revieve.Detail;
import com.example.advisoryservice.data.model.revieve.Eye;
import com.example.advisoryservice.data.model.revieve.RevieveResponse;
import com.example.advisoryservice.data.rest.APIInterface;
import com.example.advisoryservice.data.rest.RetrofitService;
import com.example.advisoryservice.ui.advisoryserviceselection.AdvisoryServiceSelection;
import com.example.advisoryservice.ui.main.MainActivity;
import com.example.advisoryservice.ui.main.MainViewModel;
import com.example.advisoryservice.util.Constants;
import com.example.advisoryservice.util.Log;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
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

public class QuestionsActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = QuestionsActivity.class.getSimpleName();

    TextView tvQuestion, tvErrorMessage;
    ImageView ivImage;
    RadioGroup radioGroup;
    ProgressBar progressBar;
    Button btnBack, btnNext;

    QuestionsViewModel viewModel;
    LinkedHashMap<String, String> optionMap = new LinkedHashMap<>();
    LinkedHashMap<String, String> imageMap = new LinkedHashMap<>();
    LinkedHashMap<String, String> questionAnswer = new LinkedHashMap<>();
    LinkedHashMap<String, Option> allOptionMap = new LinkedHashMap<>();
    LinkedHashMap<String, Answer> allOptionMapTest = new LinkedHashMap<>();
    LinkedHashMap<String, LinkedHashMap<String, String>> fullMap = new LinkedHashMap<>();

    Questions questionsModel = new Questions();
    Gson gson;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spe;
    ProgressDialog pDialog;
    InstructionDetail instructionDetail;

    int questionCnt = 1;
    String gender = "", customerTransId = "", customerCode = "", locationCode = "",age,advisory_mobile_number,advisory_first_name,advisory_last_name,advisory_email;
    boolean isChecked = false, hasInfoUrl = false, isSuccess = false, isTakeSelfie = false;

    private APIInterface apiInterface;

    private MainViewModel mainViewModel;
    List<Questions> test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(getSharedPreferences().getBoolean(Constants.TABLET, false) ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_questions);


        init();
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        gson = new Gson();
        sharedPreferences = getSharedPreferences();
        locationCode = sharedPreferences.getString(Constants.LOCATION_CODE, "");
        Log.d(TAG, "Location Code <<::>> " + locationCode);

        if (isTablet())
            setTheme(R.style.TabletAppTheme);
        else
            setTheme(R.style.AppTheme);

        apiInterface = RetrofitService.getRetrofit().create(APIInterface.class);
        viewModel = ViewModelProviders.of(this).get(QuestionsViewModel.class);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
           /* customerTransId = bundle.getString(Constants.CUSTOMER_TRANS_ID);
            customerCode = bundle.getString(Constants.CUSTOMER_CODE);
            Log.w(TAG, "Customer Code <<::>> " + customerCode);
            Log.w(TAG, "Customer TransId <<::>> " + customerTransId);*/

            advisory_mobile_number = bundle.getString(Constants.ADVISORY_USER_MOBILE_NUMBER);
            advisory_first_name = bundle.getString(Constants.ADVISORY_USER_FIRST_NAME);
            advisory_last_name = bundle.getString(Constants.ADVISORY_USER_LAST_NAME);
            advisory_email = bundle.getString(Constants.ADVISORY_USER_AGE);

        }

        //customerTransId = "2738";

        // observeQuestionViewModel();

        observableViewModelIntroPage();

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
          //  if (questionsModel.getData().get(questionCnt - 1).getIsInfoFlag().equalsIgnoreCase("Y") && questionNum != 1)
                //radioButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.info, 0);
           radioButton.setOnClickListener(this);

           /* if (backPressData.containsKey(String.valueOf(questionNum))) {

                Log.w(TAG, "QuestionAnswer contains questionNumber " + questionNum);
                if (Objects.requireNonNull(backPressData.get(String.valueOf(questionNum))).equalsIgnoreCase(allOptionMapTest.get(id).getOptionsValue())) {

                    Log.w(TAG, "Selected ID <<::>> " + id);
                    selectedId = id;
                    radioButton.setChecked(true);
                    isChecked = true;
                } else
                    radioButton.setChecked(false);
            }*/

            radioGroup.addView(radioButton);
        }

       /* btnNext.setEnabled(isChecked);
        btnNext.setBackgroundColor(isChecked ? getResources().getColor(R.color.colorAccent) :
                Color.parseColor("#808185"));
        if (isChecked)
            Glide.with(this).load(imageMap.get(selectedId))
                    .placeholder(getCircularProgressDrawable()).into(ivImage);*/

    }

   /* HashMap<String, SubQuestionOption> subQuestionOptionHashMap = new HashMap<>();
    HashMap<String, String> backPressData = new HashMap<>();
    boolean hasSubQuestions = false;*/

    @Override
    public void onClick(View v) {

        Log.d(TAG, " Name " + ((RadioButton) v).getText() + " Id is " + v.getId());
        Log.d(TAG, "questionCnt " + questionCnt);
        if (questionCnt == 1) {
            gender = ((RadioButton) v).getText().toString();
        }
        if (questionCnt ==2){
            age =((RadioButton) v).getText().toString();
        }
      /*  Log.d(TAG, "SUB OPTION QUESTION ID "+subQuestionOptionHashMap.get(String.valueOf(v.getId())).getSubOptionId());
        //TODO: Inserting feedback subquestionId and optionId on click of radio button
        feedbackSubOptionMap.put(String.valueOf(questionCnt), Objects.requireNonNull(subQuestionOptionHashMap.get(String.valueOf(v.getId()))).getSubOptionId());

        if (subQuestionOptionHashMap.get(String.valueOf(v.getId())).getSubOptionsValue().equalsIgnoreCase("5")){
            //  userFeedback.setText("");

            userFeedback.setVisibility(View.VISIBLE);
            // userFeedback.setText("");

            // customerInputFeedback =userFeedback.getText().toString();
           *//* Log.d(TAG, "SUBQUESTION NUM >>> "+ questionCnt);
            Log.d(TAG, "FEEDBACK >>> "+ userFeedback.getText().toString());
            prodHashMap.put(String.valueOf(questionCnt), userFeedback.getText().toString());
            prodArrayList.add(prodHashMap);
            userFeedback.setText("");*//*

            // check data ///



        }
        else {
           // userFeedback.setVisibility(View.GONE);
        }*/





        btnNext.setEnabled(true);
        btnNext.setBackgroundColor(getResources().getColor(R.color.colorAccent));






    }

    private void observeQuestionViewModel() {


        viewModel.getInstructionDetail().observe(this, instructionDetail -> {
            this.instructionDetail = instructionDetail;
        });

        viewModel.getAllQuestions().observe(this, questions -> {

            questionsModel = questions;
            Log.w(TAG, "Question List <<::>>> " + questionsModel.getData().size());

            getQuestions();

        });

        viewModel.getError().observe(this, isError -> {
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

        viewModel.getLoading().observe(this, isLoading -> {
            Log.d(TAG, "Progress Bar loading <<::>> " + isLoading);
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if (isLoading) {
                    tvErrorMessage.setVisibility(View.GONE);
                }
            }
        });
    }

    String optionValue;


    private void observableViewModelIntroPage() {

        imageMap.clear();
        optionMap.clear();
        radioGroup.removeAllViews();
        radioGroup.clearCheck();
        hasInfoUrl = false;

        mainViewModel.getIntroDetailsPage().observe(this, introDetail -> {

            if (introDetail != null) {

                 test = introDetail.getData().get(0).getQuestion();

                if (questionCnt <= test.size()) {
                    List<Answer> optionList = test.get(questionCnt - 1).getAnswer();

                    if (questionCnt == 1)
                        Glide.with(this).load(test.get(questionCnt - 1).getDefaultImage())
                                .placeholder(getCircularProgressDrawable()).into(ivImage);
                    else {
                        if (gender.equalsIgnoreCase("Male")) {
                            Glide.with(this).load(test.get(questionCnt - 1).getDefaultImage())
                                    .placeholder(getCircularProgressDrawable()).into(ivImage);
                        } else {
                            Glide.with(this).load(test.get(questionCnt - 1).getDefaultImage())
                                    .placeholder(getCircularProgressDrawable()).into(ivImage);
                        }
                    }

                    tvQuestion.setText(test.get(questionCnt - 1).getQuestionName());
                    String questionNum = test.get(questionCnt - 1).getQuestionId();


                        for (Answer option : optionList) {

                            // hasInfoUrl = !option.getMaleInfoText().isEmpty();

                          /*  if (gender.equalsIgnoreCase("Male")) {
                                //imageMap.put(option.getOptionId(), option.getMaleImgUrl());
                            } else {
                                // imageMap.put(option.getOptionId(), option.getFemaleImgUrl());
                            }*/
                            optionMap.put(option.getOptionId(), option.getOptionsName());
                            allOptionMapTest.put(option.getOptionId(), option);


                        }
                    addRadioButtons(optionMap, Integer.parseInt(questionNum), hasInfoUrl);
                    }





                }
        });

        mainViewModel.getError().observe(this, isError -> {
            if (isError != null) if (isError) {
                //errorTextView.setVisibility(View.VISIBLE);
                Log.w(TAG, "An Error Occurred While Loading Data <<::>>");
                //errorTextView.setText("An Error Occurred While Loading Data!");
            } else {
                Log.w(TAG, "No Errors");
               /* errorTextView.setVisibility(View.GONE);
                errorTextView.setText(null);*/
            }
        });

        mainViewModel.getLoading().observe(this, isLoading -> {
            Log.d(TAG, "Progress Bar loading <<::>> " + isLoading);
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if (isLoading) {
                    // errorTextView.setVisibility(View.GONE);
                }
            }
        });
    }


    private void getQuestions() {

        imageMap.clear();
        optionMap.clear();
        radioGroup.removeAllViews();
        radioGroup.clearCheck();
        hasInfoUrl = false;

        Log.w(TAG, "Question Cnt <<::>> " + questionCnt);


       /* if (questionCnt == 9) {

            optionValue = questionAnswer.get("8");
            Log.d(TAG, "Option Value <<::>> " + optionValue);

            if (optionValue.equalsIgnoreCase("7")) {

                questionAnswer.put(String.valueOf(questionCnt), "");
                questionCnt++;


            }
        }*/

        if (questionCnt <= questionsModel.getData().size()) {

            List<Option> optionList = questionsModel.getData().get(questionCnt - 1).getOptions();

            if (questionCnt == 1)
                Glide.with(this).load(questionsModel.getData().get(questionCnt - 1).getDefaultImageMale())
                        .placeholder(getCircularProgressDrawable()).into(ivImage);
            else {
                if (gender.equalsIgnoreCase("Male")) {
                    Glide.with(this).load(questionsModel.getData().get(questionCnt - 1).getDefaultImageMale())
                            .placeholder(getCircularProgressDrawable()).into(ivImage);
                } else {
                    Glide.with(this).load(questionsModel.getData().get(questionCnt - 1).getDefaultImageFemale())
                            .placeholder(getCircularProgressDrawable()).into(ivImage);
                }
            }

            tvQuestion.setText(questionsModel.getData().get(questionCnt - 1).getQuestion());
            String questionNum = questionsModel.getData().get(questionCnt - 1).getQuestionId();


            if (questionCnt == 9) {

                optionValue = questionAnswer.get("8");
                Log.d(TAG, "Option Value <<::>> " + optionValue);

                for (Option option : optionList) {

                    hasInfoUrl = !option.getMaleInfoText().isEmpty();

                    if (gender.equalsIgnoreCase("Male")) {
                        imageMap.put(option.getOptionId(), option.getMaleImgUrl());
                    } else {
                        imageMap.put(option.getOptionId(), option.getFemaleImgUrl());
                    }
                    if (!option.getOptionsValue().equalsIgnoreCase(optionValue)) {
                        optionMap.put(option.getOptionId(), option.getOptionsName());
                        allOptionMap.put(option.getOptionId(), option);
                    }
                }

            } else if (questionCnt != 1) {

                for (Option option : optionList) {

                    hasInfoUrl = !option.getMaleInfoText().isEmpty();

                    if (gender.equalsIgnoreCase("Male")) {
                        imageMap.put(option.getOptionId(), option.getMaleImgUrl());
                    } else {
                        imageMap.put(option.getOptionId(), option.getFemaleImgUrl());
                    }
                    optionMap.put(option.getOptionId(), option.getOptionsName());
                    allOptionMap.put(option.getOptionId(), option);
                }
            } else {

                for (Option option : optionList) {

                    hasInfoUrl = !option.getMaleInfoText().isEmpty();

                    if (option.getOptionsName().equalsIgnoreCase("Male")) {
                        imageMap.put(option.getOptionId(), option.getMaleImgUrl());
                    } else {
                        imageMap.put(option.getOptionId(), option.getFemaleImgUrl());
                    }
                    optionMap.put(option.getOptionId(), option.getOptionsName());
                    allOptionMap.put(option.getOptionId(), option);
                }
            }


          //  addRadioButtons(optionMap, Integer.parseInt(questionNum), hasInfoUrl);

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


                for (Detail detail : data) {

                    if (detail.getLINENO().equalsIgnoreCase("1")) {
                        questionAnswer.put("8", detail.getOPTIONVALUE());
                    }

                    if (detail.getLINENO().equalsIgnoreCase("1") &&
                            detail.getOPTIONVALUE().equalsIgnoreCase("7")) {
                        questionAnswer.put("9", "");
                        break;
                    }

                    if (detail.getLINENO().equalsIgnoreCase("2"))
                        questionAnswer.put("9", detail.getOPTIONVALUE());
                }
            }

            Log.w(TAG, "questionAnswer " + questionAnswer.size());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("custTransId", customerTransId);
            jsonObject.put("locationCode", locationCode);

            JSONArray jsonArray = new JSONArray();
            for (String questionNum : questionAnswer.keySet()) {
                JSONObject optionJsonObject = new JSONObject();
                optionJsonObject.put("questionNo", questionNum);
                optionJsonObject.put("optionId", questionAnswer.get(questionNum));
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
                            Toast.makeText(QuestionsActivity.this, messageString, Toast.LENGTH_SHORT).show();

                            if (!revieveResponse.isEmpty()) {
                                RevieveResponse revieveValue = gson.fromJson(revieveResponse, RevieveResponse.class);
                                for (Detail detail : revieveValue.getData().get(0).getDetail()) {

                                    questionAnswer.put("9", "");

                                    if (detail.getLINENO().equalsIgnoreCase("1") &&
                                            detail.getOPTIONVALUE().equalsIgnoreCase("7"))
                                        break;

                                    if (detail.getLINENO().equalsIgnoreCase("2"))
                                        questionAnswer.put("9", detail.getOPTIONVALUE());
                                }
                                for (Eye eye : revieveValue.getData().get(0).getEyes()) {
                                    questionAnswer.put("10", eye.getOPTIONVALUE());
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
                            Toast.makeText(QuestionsActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();
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
      /*  Log.d(TAG, "Take Selfie <<::>> " + isTakeSelfie);
        Intent intent = new Intent(QuestionsActivity.this, CustomerProductQuestionActivity.class);
        intent.putExtra(Constants.CUSTOMER_TRANS_ID, customerTransId);
        intent.putExtra(Constants.TAKE_SELFIE, isTakeSelfie);
        startActivity(intent)*/
        ;
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

                Intent intent = new Intent(QuestionsActivity.this, MainActivity.class);
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

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBack.setVisibility(questionCnt == -1 ? View.INVISIBLE : View.VISIBLE);

             /*   if (subQuestions.get(questionCnt - 1).getMandatory().equalsIgnoreCase("Y")){
                    Toast.makeText(ResultDetailActivity.this,"Please Select Option",Toast.LENGTH_LONG).show();
                }
*/

                if (questionCnt <= test.size())
                    // getQuestions();
                    questionCnt++;
                observableViewModelIntroPage();
                if (questionCnt==3){
                    Intent intent = new Intent(QuestionsActivity.this, AdvisoryServiceSelection.class);
                    intent.putExtra(Constants.CUSTOMER_TRANS_ID, customerTransId);
                    intent.putExtra(Constants.ADVISORY_USER_MOBILE_NUMBER, advisory_mobile_number);
                    intent.putExtra(Constants.ADVISORY_USER_FIRST_NAME, advisory_first_name);
                    intent.putExtra(Constants.ADVISORY_USER_LAST_NAME, advisory_last_name);
                    intent.putExtra(Constants.ADVISORY_USER_EMAIL, advisory_email);
                    intent.putExtra(Constants.ADVISORY_USER_GENDER, gender);
                    intent.putExtra(Constants.ADVISORY_USER_AGE, age);
                    startActivity(intent);
                }

                //dialog.dismiss();
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
                    questionCnt--;*/

                btnBack.setVisibility(questionCnt == 1 ? View.INVISIBLE : View.VISIBLE);
              //  getQuestions();

              //  if (questionCnt >= questionsModel.getData().size() || questionCnt == 0)
                    onBackPressed();
            }
        });
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
                            Toast.makeText(QuestionsActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(QuestionsActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        HttpException error = (HttpException) e;
                        String errorBody = Objects.requireNonNull(error.response().errorBody()).toString();
                        Log.d(TAG, "Exception e <<::>> " + e);
                        Log.d(TAG, "errorBody <<::>> " + errorBody);
                        Toast.makeText(QuestionsActivity.this, errorBody, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
