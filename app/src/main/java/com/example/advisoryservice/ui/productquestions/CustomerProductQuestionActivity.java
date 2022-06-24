package com.example.advisoryservice.ui.productquestions;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.advisoryservice.R;
import com.example.advisoryservice.base.BaseActivity;
import com.example.advisoryservice.data.model.Response;
import com.example.advisoryservice.data.model.questions.Option;
import com.example.advisoryservice.data.rest.APIInterface;
import com.example.advisoryservice.data.rest.RetrofitService;
import com.example.advisoryservice.ui.advisoryserviceselection.AdvisoryServiceSelection;
import com.example.advisoryservice.ui.imageResult.AnalyzeImageResultActivity;
import com.example.advisoryservice.ui.result.ResultDetailsTest;
import com.example.advisoryservice.util.Constants;
import com.example.advisoryservice.util.Log;
import com.example.advisoryservice.util.SkinViewModelFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class CustomerProductQuestionActivity extends BaseActivity {

    public static final String TAG = CustomerProductQuestionActivity.class.getSimpleName();

    ProgressBar progressBar;
    TextView tvErrorMessage, tvQuestion;
    ImageView ivImage;
    LinearLayout llCheckbox;
    Button btnSubmit;

    private ProductQuestionViewModel productQuestionViewModel;
    private APIInterface apiInterface;

    String customerTransId = "", locationCode = "",advisoryServiceNumber,mobileNumberPref;
    boolean isSelfieTaken = false;
    ArrayMap<String, String> selectedProducts = new ArrayMap<>();
    ArrayMap<String, Option> allOptionMap = new ArrayMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        if (!isTablet())
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);*/
        setRequestedOrientation(getSharedPreferences().getBoolean(Constants.TABLET, false) ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_customer_product_question);

        locationCode = getSharedPreferences().getString(Constants.LOCATION_CODE, "");
        mobileNumberPref = getSharedPreferences().getString(Constants.MOBILE_NUM_PREF, "");
        Log.d(TAG, "Location Code <<::>> " + locationCode);

        setUI();
        Log.d(TAG, "IS Tablet <<::>> " + isTablet());

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (selectedProducts.size() == 0)
                        Toast.makeText(CustomerProductQuestionActivity.this, "Please select a product", Toast.LENGTH_SHORT).show();
                    else {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("custTransId", customerTransId);
                        jsonObject.put("locationCode", locationCode);
                        jsonObject.put("advisoryService", advisoryServiceNumber);

                        JSONArray jsonArray = new JSONArray();

                        for (String optionData : selectedProducts.keySet()) {
                            JSONObject optionJsonObject = new JSONObject();
                            optionJsonObject.put("questionNo", "1");
                            optionJsonObject.put("optionId", selectedProducts.get(optionData));
                            jsonArray.put(optionJsonObject);
                        }

                        jsonObject.put("options", jsonArray);

                        Log.d(TAG, "Insert Regime <<::>> " + jsonObject.toString());
                        insertRegime(jsonObject.toString());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Exception on Next Button <<::>> " + e);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        observeData();
    }

    private void addCheckBox(ArrayMap<String, Option> dataMap) {

        for (String key : dataMap.keySet()) {

            Option option = dataMap.get(key);
            if (option != null) {

                AppCompatCheckBox checkBox = new AppCompatCheckBox(this);
                checkBox.setId(Integer.parseInt(option.getOptionId()));
                checkBox.setText(option.getOptionsName());
                if (isTablet())
                    checkBox.setTextSize(20);
                else
                    checkBox.setTextSize(15);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            selectedProducts.put(String.valueOf(compoundButton.getId()),allOptionMap.get(String.valueOf(compoundButton.getId())).getOptionsValue());
                        } else {
                            selectedProducts.remove(String.valueOf(compoundButton.getId()));
                        }

                    }
                });
                llCheckbox.addView(checkBox);
            }
        }

    }

    private void observeData() {

        allOptionMap.clear();
        selectedProducts.clear();
        llCheckbox.removeAllViews();

        productQuestionViewModel.getProductQuestion().observe(this, questions -> {

            Log.w(TAG, "Question List <<::>>> " + questions.getData().size());
            if (questions.getMessage().equalsIgnoreCase("success")) {

                for (int i = 0; i < questions.getData().size(); i++) {

                    Glide.with(this).load(questions.getDefaultImage())
                            .placeholder(getCircularProgressDrawable()).into(ivImage);
                    tvQuestion.setText(questions.getData().get(i).getQuestion());

                    for (Option option : questions.getData().get(i).getOptions()) {
                        allOptionMap.put(option.getOptionId(), option);
                    }
                }
                addCheckBox(allOptionMap);
            }

        });

        productQuestionViewModel.getError().observe(this, isError -> {
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

        productQuestionViewModel.getLoading().observe(this, isLoading -> {
            Log.d(TAG, "Progress Bar loading <<::>> " + isLoading);
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if (isLoading) {
                    tvErrorMessage.setVisibility(View.GONE);
                }
            }
        });
    }

    private void insertRegime(String data) {

        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json"), data);

        Log.w(TAG, "requestBody <<::>> " + requestBody.toString());

        apiInterface.postRegime(requestBody).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Response value) {
                        try {
                            Log.d(TAG, "Response value product question  <<::>> " + value.toString());
                            Log.d(TAG, "Regime response  <<::>> " + value.getMessage());
                            Toast.makeText(CustomerProductQuestionActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        moveToNextScreen();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Exception Thrown <<::>>> " + e);
                        Toast.makeText(CustomerProductQuestionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void moveToNextScreen() {

        Log.d(TAG, "Moving to next screen from regime " + isSelfieTaken);

        if (isSelfieTaken) {
            Log.d(TAG, "Moving to AnalyzeImageResultActivity");

           // Intent intent = new Intent(this, AnalyzeImageResultActivity.class);
            Intent intent = new Intent(this, AnalyzeImageResultActivity.class);

            // Intent intent = new Intent(this, AnalyzeTest.class);
            intent.putExtra(Constants.CUSTOMER_TRANS_ID, customerTransId);
            intent.putExtra(Constants.TAKE_SELFIE, isSelfieTaken);
            intent.putExtra(Constants.ADVISORY_SERVICE_NUMBER,advisoryServiceNumber);
            startActivity(intent);
        } else {
            Log.d(TAG, "Moving to ResultDetailActivity");
           // Intent intent = new Intent(this, ResultDetailsTest.class);
            Intent intent = new Intent(this, ResultDetailsTest.class);

            intent.putExtra(Constants.CUSTOMER_TRANS_ID, customerTransId);
            intent.putExtra(Constants.TAKE_SELFIE, isSelfieTaken);
            intent.putExtra(Constants.ADVISORY_SERVICE_NUMBER,advisoryServiceNumber);
            startActivity(intent);
        }
    }

    private void setUI() {

        setActionBarTitle("Skin Care Regime");

        tvQuestion = findViewById(R.id.tvQuestion);
        ivImage = findViewById(R.id.ivImage);
        llCheckbox = findViewById(R.id.llCheckbox);
        btnSubmit = findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBar);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            customerTransId = bundle.getString(Constants.ADVISORY_CUSTOMER_TRANSCTION_ID);
            isSelfieTaken = bundle.getBoolean(Constants.TAKE_SELFIE);
            advisoryServiceNumber = bundle.getString(Constants.ADVISORY_SERVICE_NUMBER);
            Log.d(TAG, "IS Selfie taken <<::>> " + isSelfieTaken);
            Log.d(TAG, "customerTransId <<::>> " + customerTransId);
        }

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("custTransId", customerTransId);
            jsonObject.put("advisoryService", advisoryServiceNumber);
            Log.d(TAG, "JSON Object <<::>> " + jsonObject.toString());

        } catch (Exception e) {
            Log.e(TAG, "Exception e ==> " + e);
        }

        SkinViewModelFactory factory = new SkinViewModelFactory(getApplication(), jsonObject.toString());
        productQuestionViewModel = ViewModelProviders.of(this, factory).get(ProductQuestionViewModel.class);
        apiInterface = RetrofitService.getRetrofit().create(APIInterface.class);
    }
}