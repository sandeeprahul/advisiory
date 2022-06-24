package com.example.advisoryservice.ui.customerdetail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.advisoryservice.R;
import com.example.advisoryservice.base.BaseActivity;
import com.example.advisoryservice.data.model.CompleteResultDetailsResponde;
import com.example.advisoryservice.data.model.CustomerInsertResponse;
import com.example.advisoryservice.data.model.CustomerPostRequest;
import com.example.advisoryservice.data.model.Data;
import com.example.advisoryservice.data.rest.APIInterface;
import com.example.advisoryservice.data.rest.RetrofitService;
import com.example.advisoryservice.ui.TestMakeupActivity;
import com.example.advisoryservice.ui.TestingModule;
import com.example.advisoryservice.ui.genderquestions.GenderQuestionsActivity;
import com.example.advisoryservice.ui.questions.QuestionsActivity;
import com.example.advisoryservice.ui.selectedadvisory.AdvisorySelectionAdapter;
import com.example.advisoryservice.util.Constants;
import com.example.advisoryservice.util.Log;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CustomerInsertDetailsActivity extends BaseActivity implements AgeSelectionAdapter.ClickListener{

    public static final String TAG = CustomerInsertDetailsActivity.class.getSimpleName();

    TextView tvName, tvChange, dateTimeText, nameText, resultText, recommendedText, genderMaleText;
    EditText etCustomerName, etCustomerLastName, etCustomerMobile, etCustomerEmail;
    Button btnBack, btnNext;
    ProgressDialog progressDialog;
    LinearLayout parentHeaderLayout, resultLayout, genderMaleLl, genderFemaleLl;
    ImageView genderMaleImg,genderFemaleImg;
    RecyclerView recyclerAgeSelection;

    private APIInterface apiInterface;
    CustomerViewModel viewModel;

    String tokenEncoded = "", token = "", locationCode = "";
    int storeId = 0, storeOutletId = 0, customerListSize = 0, customerId = 0, gender = 0;
    String genderSelction= "",selectionAge= "",advisoryServiceNumber;
    TextView ageText1,ageText2,ageText3,ageText4,ageText5;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(getSharedPreferences().getBoolean(Constants.TABLET, false) ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /*if (!isTablet())
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);*/

        setContentView(R.layout.activity_customer_insert_details);
        init();

        locationCode = getSharedPreferences().getString(Constants.LOCATION_CODE, "");
        Log.d(TAG, "Location Code <<::>> " + locationCode);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
           /* customerTransId = bundle.getString(Constants.CUSTOMER_TRANS_ID);
            customerCode = bundle.getString(Constants.CUSTOMER_CODE);
            Log.w(TAG, "Customer Code <<::>> " + customerCode);
            Log.w(TAG, "Customer TransId <<::>> " + customerTransId);*/

            advisoryServiceNumber = bundle.getString(Constants.ADVISORY_SERVICE_NUMBER);

        }

        apiInterface = RetrofitService.getRetrofit().create(APIInterface.class);
        viewModel = ViewModelProviders.of(this).get(CustomerViewModel.class);
        viewModel.getStoreLogin().observe(this, storeLoginResponse -> {
            try {
                token = storeLoginResponse.getToken();
                tokenEncoded = URLEncoder.encode(storeLoginResponse.getToken(), "utf-8");
                storeId = storeLoginResponse.getStoreId();
                storeOutletId = storeLoginResponse.getStoreOutletId();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "Token Encoded " + tokenEncoded);
        });


        viewModel.getCustomerDetail().observe(this, customerDetailsResponses -> {

            customerListSize = customerDetailsResponses.size();
            Log.d(TAG, "CUstomer List Size " + customerListSize);

            if (customerDetailsResponses.size() > 0) {

                customerId = customerDetailsResponses.get(0).getCustomerId();
                gender = customerDetailsResponses.get(0).getGender();

                etCustomerName.setText(customerDetailsResponses.get(0).getFirstName());
                etCustomerLastName.setText(customerDetailsResponses.get(0).getLastName());
                etCustomerEmail.setText(customerDetailsResponses.get(0).getEmail());

                etCustomerMobile.requestFocus();
                HashMap<String, Object> jsonBody = new HashMap<>();
                jsonBody.put("phoneNo", etCustomerMobile.getText().toString());
                viewModel.completeResultDetail(jsonBody);

            } else {
                customerId = gender = 0;
                etCustomerName.requestFocus();
                Toast.makeText(this, "Customer Details not found", Toast.LENGTH_SHORT).show();
                etCustomerName.setText(null);
                etCustomerEmail.setText(null);
                etCustomerLastName.setText(null);
                recommendedText.setVisibility(View.GONE);
                parentHeaderLayout.setVisibility(View.GONE);
                resultLayout.setVisibility(View.GONE);
                /*etCustomerName.setEnabled(true);
                etCustomerMobile.setEnabled(true);
                etCustomerEmail.setEnabled(true);*/
            }
        });

        viewModel.getCustomerInsertResponse().observe(this, createCustomerResponse -> {

            Log.d(TAG, "Customer Insert Response " + createCustomerResponse.getMessage());
            Toast.makeText(this, createCustomerResponse.getMessage(), Toast.LENGTH_SHORT).show();
            postCustomerData();
        });

        viewModel.getUpdateCustomerResponse().observe(this, response -> {
            Log.d(TAG, "Customer Update Response " + response);
            postCustomerData();
        });

        viewModel.getError().observe(this, isError -> {

            if (isError != null) if (isError) {
                hideProgressDialog();
                Log.w(TAG, "An Error Occurred While Loading Data <<::>>");
                // Toast.makeText(this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
            } else {
                Log.w(TAG, "No Errors");
            }
        });

        viewModel.getLoading().observe(this, isLoading -> {

            Log.d(TAG, "isLoading<<::>> " + isLoading);
            if (isLoading != null && isLoading)
                showProgressDialog("Loading..");
            else
                hideProgressDialog();
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("GenderSelction",selectionAge);
//                Intent intent = new Intent(CustomerInsertDetailsActivity.this, TestingModule.class);
//                        startActivity(intent);

                try {
                    boolean isValid = validate();
                    Log.d(TAG, "IsValid " + isValid);
                    if (isValid) {

                        postCustomerData();

                        Intent intent = new Intent(CustomerInsertDetailsActivity.this, GenderQuestionsActivity.class);
                        intent.putExtra(Constants.ADVISORY_USER_MOBILE_NUMBER, etCustomerMobile.getText().toString());
                        intent.putExtra(Constants.ADVISORY_USER_FIRST_NAME, etCustomerName.getText().toString());
                        intent.putExtra(Constants.ADVISORY_USER_LAST_NAME, etCustomerLastName.getText().toString());
                        intent.putExtra(Constants.ADVISORY_USER_EMAIL, etCustomerEmail.getText().toString());
                        startActivity(intent);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void postCustomerData() {

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("appKey", "TUFTRVRIVU5HMTIzNDU=");

            JSONObject jObj = new JSONObject();
            jObj.put("firstName", etCustomerName.getText().toString());
            jObj.put("lastName", etCustomerLastName.getText().toString());
            jObj.put("mobileNo", etCustomerMobile.getText().toString().trim());
            jObj.put("email", etCustomerEmail.getText().toString().trim());
            jObj.put("gender", genderSelction);
            jObj.put("age", selectionAge);
            jObj.put("locationCode", locationCode);
            jObj.put("platform", "Offline");
            jObj.put("application", "Android");
            jObj.put("advisoryService", advisoryServiceNumber);

            jsonObject.put("data", jObj);

            CustomerPostRequest customerPostRequest = new CustomerPostRequest();
            customerPostRequest.setAppKey("TUFTRVRIVU5HMTIzNDU=");
            Data data = new Data();
            data.setFirstName(etCustomerName.getText().toString());
            data.setLastName(etCustomerLastName.getText().toString());
            data.setMobileNo(etCustomerMobile.getText().toString().trim());
            data.setEmail(etCustomerEmail.getText().toString().trim());
            data.setGender(genderSelction);
            data.setAge(selectionAge);
            data.setLocationCode(locationCode);
            data.setAdvisoryService(advisoryServiceNumber);
            data.setPlatform("Offline");
            data.setApplication("Android");
            customerPostRequest.setData(data);

            Log.w(TAG, "Customer Request <<::>> " + jsonObject.toString());

            apiInterface.insertCustomerDetails(customerPostRequest).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<CustomerInsertResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(CustomerInsertResponse value) {
                            String messages = "";
                            if (value.getMessages() == null) {
                                Log.w(TAG, "Code <<::> " + value.getCustomerCode());
                                Log.w(TAG, "TransId <<::> " + value.getCustTransId());
                                Log.w(TAG, "Message <<::> " + value.getMessage());
                                messages = value.getMessage();

                                Intent intent = new Intent(CustomerInsertDetailsActivity.this, GenderQuestionsActivity.class);
                                intent.putExtra(Constants.ADVISORY_CUSTOMER_TRANSCTION_ID, value.getCustTransId());
                                intent.putExtra(Constants.ADVISORY_USER_GENDER, genderSelction);
                                intent.putExtra(Constants.ADVISORY_SERVICE_NUMBER, advisoryServiceNumber);

                                spe = sharedPreferences.edit();
                                spe.putString(Constants.MOBILE_NUM_PREF, etCustomerMobile.getText().toString());

                                spe.apply();

                                startActivity(intent);

                                //  moveToNextScreen(value);

                            } else
                                messages = value.getMessages();
                            //  Toast.makeText(CustomerInsertDetailsActivity.this, messages, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "Exception Thrown <<::>>> " + e);
                            hideProgressDialog();
                        }

                        @Override
                        public void onComplete() {
                        }
                    });

        } catch (Exception e) {
            Log.e(TAG, "Exception <<::>> " + e);
            e.printStackTrace();
            hideProgressDialog();
        }
    }


    private boolean validate() {
        if (etCustomerMobile.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter  Mobile Number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etCustomerMobile.getText().toString().length() > 10) {
            Toast.makeText(this, "Enter Valid mobile number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etCustomerName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter First name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etCustomerLastName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Last name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(etCustomerEmail.getText().toString()).matches()) {
            Toast.makeText(this, "Enter Valid  email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (genderSelction.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please Select Gender", Toast.LENGTH_SHORT).show();
            return false;
        }

        else if (selectionAge.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please Select Age", Toast.LENGTH_SHORT).show();
            return false;
        }


        else
            return true;
    }


    private void init() {
        sharedPreferences = getSharedPreferences();
        setActionBarTitle("Customer Details");
        ArrayList<String> ageArraylist =   new ArrayList<String>();
        ageArraylist.add("20 - 30 Years");
        ageArraylist.add("31 - 40 Years");
        ageArraylist.add("41 - 50 Years");
        ageArraylist.add("51 - 60 Years");
        ageArraylist.add("Above 60 Years");

        tvName = findViewById(R.id.tvName);
        etCustomerName = findViewById(R.id.etCustomerName);
        etCustomerLastName = findViewById(R.id.etCustomerLastName);
        etCustomerMobile = findViewById(R.id.etCustomerMobile);
        etCustomerEmail = findViewById(R.id.etCustomerEmail);
        tvChange = findViewById(R.id.tvChange);
        dateTimeText = findViewById(R.id.date_time_text);
        nameText = findViewById(R.id.name_text);
        resultText = findViewById(R.id.result_text);
        recommendedText = findViewById(R.id.recommended_text);
        parentHeaderLayout = findViewById(R.id.parent_header_layout);
        resultLayout = findViewById(R.id.result_layout);
        btnBack = findViewById(R.id.btnBack);
        btnNext = findViewById(R.id.btnNext);
        genderMaleText = findViewById(R.id.gender_male_text);
        genderMaleLl = findViewById(R.id.gender_male_ll);
        genderFemaleLl = findViewById(R.id.gender_female_ll);
        genderMaleImg = findViewById(R.id.gender_male_img);
        genderFemaleImg = findViewById(R.id.gender_female_img);
        recyclerAgeSelection = findViewById(R.id.recycler_age_selection);
        ageText1 = findViewById(R.id.age_text_1);
        ageText2 = findViewById(R.id.age_text_2);
        ageText3 = findViewById(R.id.age_text_3);
        ageText4 = findViewById(R.id.age_text_4);
        ageText5 = findViewById(R.id.age_text_5);

        etCustomerMobile.requestFocus();
        etCustomerName.setEnabled(true);
        etCustomerLastName.setEnabled(true);
        etCustomerEmail.setEnabled(true);

        /*etCustomerName.setEnabled(false);
        etCustomerLastName.setEnabled(false);
        etCustomerEmail.setEnabled(false);*/

       /* tvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etCustomerName.setEnabled(true);
                etCustomerLastName.setEnabled(true);
                etCustomerEmail.setEnabled(true);
                etCustomerMobile.setEnabled(false);
            }
        });

        etCustomerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty())
                    tvName.setText(editable.toString());
                else
                    tvName.setText(null);
            }
        });*/

        AgeSelectionAdapter advisorySelectionAdapter = new AgeSelectionAdapter(this,ageArraylist,this);
        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(this, 1, RecyclerView.VERTICAL, false);
        gridLayoutManager.setReverseLayout(true);
        recyclerAgeSelection.setLayoutManager(gridLayoutManager);
        recyclerAgeSelection.setNestedScrollingEnabled(false);
        recyclerAgeSelection.setAdapter(advisorySelectionAdapter);
        genderMaleLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // genderMaleLl.setBackground(getResources().getDrawable(R.drawable.gender_onclick_bg));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    genderMaleImg.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.orange)));
                    genderFemaleImg.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.black)));
                     genderSelction = "M";
                }
            }
        });

        genderFemaleLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // genderMaleLl.setBackground(getResources().getDrawable(R.drawable.gender_onclick_bg));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    genderFemaleImg.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.orange)));
                    genderMaleImg.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.black)));
                    genderSelction = "F";
                }
            }
        });


        ageText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionAge = "20-30";
                ageText1.setBackground(getResources().getDrawable(R.drawable.gender_onclick_bg));
                ageText1.setTextColor(getResources().getColor(R.color.white));


                ageText2.setBackground(getResources().getDrawable(R.drawable.gender_bg));
                ageText2.setTextColor(getResources().getColor(R.color.black));

                ageText3.setBackground(getResources().getDrawable(R.drawable.gender_bg));
                ageText3.setTextColor(getResources().getColor(R.color.black));

                ageText4.setBackground(getResources().getDrawable(R.drawable.gender_bg));
                ageText4.setTextColor(getResources().getColor(R.color.black));

                ageText5.setBackground(getResources().getDrawable(R.drawable.gender_bg));
                ageText5.setTextColor(getResources().getColor(R.color.black));

            }
        });

        ageText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionAge = "31-40";
                ageText2.setBackground(getResources().getDrawable(R.drawable.gender_onclick_bg));
                ageText2.setTextColor(getResources().getColor(R.color.white));

                ageText1.setBackground(getResources().getDrawable(R.drawable.gender_bg));
                ageText1.setTextColor(getResources().getColor(R.color.black));


                ageText3.setBackground(getResources().getDrawable(R.drawable.gender_bg));
                ageText3.setTextColor(getResources().getColor(R.color.black));

                ageText4.setBackground(getResources().getDrawable(R.drawable.gender_bg));
                ageText4.setTextColor(getResources().getColor(R.color.black));

                ageText5.setBackground(getResources().getDrawable(R.drawable.gender_bg));
                ageText5.setTextColor(getResources().getColor(R.color.black));
            }
        });
        ageText3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionAge = "41-50";
                ageText3.setBackground(getResources().getDrawable(R.drawable.gender_onclick_bg));
                ageText3.setTextColor(getResources().getColor(R.color.white));


                ageText1.setBackground(getResources().getDrawable(R.drawable.gender_bg));
                ageText1.setTextColor(getResources().getColor(R.color.black));

                ageText2.setBackground(getResources().getDrawable(R.drawable.gender_bg));
                ageText2.setTextColor(getResources().getColor(R.color.black));

                ageText4.setBackground(getResources().getDrawable(R.drawable.gender_bg));
                ageText4.setTextColor(getResources().getColor(R.color.black));

                ageText5.setBackground(getResources().getDrawable(R.drawable.gender_bg));
                ageText5.setTextColor(getResources().getColor(R.color.black));
            }
        });
        ageText4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionAge = "51-60";
                ageText4.setBackground(getResources().getDrawable(R.drawable.gender_onclick_bg));
                ageText4.setTextColor(getResources().getColor(R.color.white));

                ageText1.setBackground(getResources().getDrawable(R.drawable.gender_bg));
                ageText1.setTextColor(getResources().getColor(R.color.black));

                ageText2.setBackground(getResources().getDrawable(R.drawable.gender_bg));
                ageText2.setTextColor(getResources().getColor(R.color.black));

                ageText3.setBackground(getResources().getDrawable(R.drawable.gender_bg));
                ageText3.setTextColor(getResources().getColor(R.color.black));

                ageText5.setBackground(getResources().getDrawable(R.drawable.gender_bg));
                ageText5.setTextColor(getResources().getColor(R.color.black));
            }
        });
        ageText5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectionAge = ">60";
                ageText5.setBackground(getResources().getDrawable(R.drawable.gender_onclick_bg));
                ageText5.setTextColor(getResources().getColor(R.color.white));


                ageText1.setBackground(getResources().getDrawable(R.drawable.gender_bg));
                ageText1.setTextColor(getResources().getColor(R.color.black));

                ageText2.setBackground(getResources().getDrawable(R.drawable.gender_bg));
                ageText2.setTextColor(getResources().getColor(R.color.black));

                ageText3.setBackground(getResources().getDrawable(R.drawable.gender_bg));
                ageText3.setTextColor(getResources().getColor(R.color.black));

                ageText4.setBackground(getResources().getDrawable(R.drawable.gender_bg));
                ageText4.setTextColor(getResources().getColor(R.color.black));
            }
        });



        etCustomerMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!editable.toString().isEmpty()) {
                    if (editable.toString().length() < 10) {
                        Toast.makeText(CustomerInsertDetailsActivity.this, "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();

                    } else if (editable.toString().length() == 10) {
                        hideKeyboard(CustomerInsertDetailsActivity.this);
                        Log.d(TAG, "FetchCustomerDetail search");
                        viewModel.fetchCustomerDetails(tokenEncoded, etCustomerMobile.getText().toString());

                    }

                }
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    private void showProgressDialog(String substring) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        //progressDialog.setTitle(title);
        progressDialog.setMessage(substring);
        progressDialog.show();

    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }


    @Override
    public void selectedageAdvisory(String ageSelection) {
        //recyclerAgeSelection.setBackground(getResources().getDrawable(R.drawable.gender_onclick_bg));
    }
}

