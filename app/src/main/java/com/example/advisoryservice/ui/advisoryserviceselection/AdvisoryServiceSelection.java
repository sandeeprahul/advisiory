package com.example.advisoryservice.ui.advisoryserviceselection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.advisoryservice.R;
import com.example.advisoryservice.base.BaseActivity;
import com.example.advisoryservice.data.model.CustomerInsertResponse;
import com.example.advisoryservice.data.model.CustomerPostRequest;
import com.example.advisoryservice.data.model.Data;
import com.example.advisoryservice.data.model.Questions;
import com.example.advisoryservice.data.model.Service;
import com.example.advisoryservice.data.rest.APIInterface;
import com.example.advisoryservice.data.rest.RetrofitService;
import com.example.advisoryservice.ui.customerdetail.CustomerInsertDetailsActivity;
import com.example.advisoryservice.ui.main.MainActivity;
import com.example.advisoryservice.ui.main.MainViewModel;
import com.example.advisoryservice.ui.selectedadvisory.AdvisorySelectionAdapter;
import com.example.advisoryservice.ui.selectedadvisory.SelectedAdvisoryActivity;
import com.example.advisoryservice.util.Constants;
import com.example.advisoryservice.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AdvisoryServiceSelection extends BaseActivity implements AdvisorySelectionAdapter.ClickListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private static final int PERMISSION_CODE_STORAGE = 3001;
    private static final int PERMISSION_CODE_READ_STORAGE = 3011;

    RecyclerView recyclerAdvisorySelection;
    ImageView introImage,introPageImg;
    ImageView imageview1, imageview2, imageview3, imageview4;
    TextView text1, text2, text3, text4,version;
    LinearLayout layout4;
    TextView tvTitle, tvIntroduction, tvIntroTitle,advisoryText;
    Button btnStartAnalysis;
    ProgressBar progressBar;
    ImageView ivLogo;
    boolean isTablet;

    private MainViewModel viewModel;
    LinearLayoutManager HorizontalLayout;
    String versionName;
    private APIInterface apiInterface;
    String  locationCode = "",gender,age,advisory_mobile_number,advisory_first_name,advisory_last_name,advisory_email;
    ProgressDialog progressDialog;
    AdvisorySelectionAdapter.ClickListener clickListener;
    String SelectedService,ServiceImage;
    RelativeLayout relativeParentBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isTablet = getSharedPreferences().getBoolean(Constants.TABLET, false);

        setRequestedOrientation(isTablet ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        locationCode = getSharedPreferences().getString(Constants.LOCATION_CODE, "");

        if (isTablet)
            Log.d(TAG, "Detected... You're using a Tablet");
        else
            Log.d(TAG, "Detected... You're using a Phone");
        setContentView(R.layout.activity_advisory_selection);

        init();

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        clickListener = this;

       /* Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
           *//* customerTransId = bundle.getString(Constants.CUSTOMER_TRANS_ID);
            customerCode = bundle.getString(Constants.CUSTOMER_CODE);
            Log.w(TAG, "Customer Code <<::>> " + customerCode);
            Log.w(TAG, "Customer TransId <<::>> " + customerTransId);*//*

            advisory_mobile_number = bundle.getString(Constants.ADVISORY_USER_MOBILE_NUMBER);
            advisory_first_name = bundle.getString(Constants.ADVISORY_USER_FIRST_NAME);
            advisory_last_name = bundle.getString(Constants.ADVISORY_USER_LAST_NAME);
            advisory_email = bundle.getString(Constants.ADVISORY_USER_AGE);
            gender = bundle.getString(Constants.ADVISORY_USER_GENDER);
            age = bundle.getString(Constants.ADVISORY_USER_AGE);

        }*/



        btnStartAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SelectedService==null){
                    Toast.makeText(AdvisoryServiceSelection.this,"Please Select Service",Toast.LENGTH_LONG).show();
                }

                else {
                   // postCustomerData();

                    Intent intent = new Intent(AdvisoryServiceSelection.this, CustomerInsertDetailsActivity.class);
                    intent.putExtra(Constants.ADVISORY_SERVICE_NUMBER,  SelectedService);
                    /*intent.putExtra(Constants.ADVISORY_CUSTOMER_TRANSCTION_ID, value.getCustTransId());
                    intent.putExtra(Constants.ADVISORY_SERVICE_IMAGE, ServiceImage);
                    intent.putExtra(Constants.ADVISORY_USER_GENDER,gender);*/
                    startActivity(intent);
                }





                // checkVersionTest(locationCode, versionName);


            }
        });
    }





    private void postCustomerData() {

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("appKey", "TUFTRVRIVU5HMTIzNDU=");

            JSONObject jObj = new JSONObject();
            jObj.put("firstName", advisory_first_name);
            jObj.put("lastName",advisory_last_name);
            jObj.put("mobileNo", advisory_mobile_number);
            jObj.put("email", advisory_email);
            jObj.put("locationCode", locationCode);
            jObj.put("platform", "Offline");
            jObj.put("application", "Android");
            jObj.put("advisoryService", "1");

            jsonObject.put("data", jObj);

            CustomerPostRequest customerPostRequest = new CustomerPostRequest();
            customerPostRequest.setAppKey("TUFTRVRIVU5HMTIzNDU=");
            Data data = new Data();
            data.setFirstName(advisory_first_name);
            data.setLastName(advisory_last_name);
            data.setMobileNo(advisory_mobile_number);
            data.setEmail(advisory_email);
            data.setLocationCode(locationCode);
            data.setPlatform("Offline");
            data.setApplication("Android");
            data.setGender(gender);
            data.setAge(age);
            data.setAdvisoryService(SelectedService);
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
                                Intent intent = new Intent(AdvisoryServiceSelection.this, SelectedAdvisoryActivity.class);
                                intent.putExtra(Constants.ADVISORY_SERVICE_NUMBER,  SelectedService);
                                intent.putExtra(Constants.ADVISORY_CUSTOMER_TRANSCTION_ID, value.getCustTransId());
                                intent.putExtra(Constants.ADVISORY_SERVICE_IMAGE, ServiceImage);
                                intent.putExtra(Constants.ADVISORY_USER_GENDER,gender);
                                startActivity(intent);
                                // moveToNextScreen(value);

                            } else
                                messages = value.getMessages();
                            Toast.makeText(AdvisoryServiceSelection.this, messages, Toast.LENGTH_SHORT).show();

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

    private void observableViewModelIntroPage() {
        viewModel.getIntroDetailsPage().observe(this, introDetail -> {

            if (introDetail != null) {
                //introPageImg.setImageDrawable(getResources().getDrawable(R.drawable.intro_page_test));
                 // Glide.with(this).load(introDetail.getData().get(0).getServiceBackgroundImage()).into(introPageImg);
               // relativeParentBg.setBackgroundResource(String.valueOf(introDetail.getData().get(0).getServiceBackgroundImage()));
                relativeParentBg.setBackground(getResources().getDrawable(R.drawable.intro_services_type));
               // relativeParentBg.setBackground(Drawable.createFromPath(introDetail.getData().get(0).getServiceBackgroundImage()));
                /*Bitmap myImage = getBitmapFromURL(introDetail.getData().get(0).getServiceBackgroundImage());
                Drawable dr = new BitmapDrawable(myImage);
                relativeParentBg.setBackgroundDrawable(dr);*/
               // List<Questions> test =     introDetail.getData().get(0).getQuestion();
              //  advisoryText.setText(introDetail.getData().get(0).getServiceTittle());



                if (isTablet) {
                    relativeParentBg.setBackground(getResources().getDrawable(R.drawable.intro_services_type));
                    List<Service> serviceDataModel = introDetail.getData().get(0).getService();
                    AdvisorySelectionAdapter advisorySelectionAdapter = new AdvisorySelectionAdapter(this,serviceDataModel,this);
                    GridLayoutManager gridLayoutManager =
                            new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false);
                    gridLayoutManager.setReverseLayout(true);
                    recyclerAdvisorySelection.setLayoutManager(gridLayoutManager);
                    recyclerAdvisorySelection.setNestedScrollingEnabled(false);
                    recyclerAdvisorySelection.setAdapter(advisorySelectionAdapter);
                    // tvIntroduction.setText(introDetail.getData().get(0).getIntroImage().replaceAll("<br />", ""));
                } else {
                    List<Service> serviceDataModel = introDetail.getData().get(0).getService();
                    AdvisorySelectionAdapter advisorySelectionAdapter = new AdvisorySelectionAdapter(this,serviceDataModel,this);
                    GridLayoutManager gridLayoutManager =
                            new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false);
                    gridLayoutManager.setReverseLayout(true);
                    recyclerAdvisorySelection.setLayoutManager(gridLayoutManager);
                    recyclerAdvisorySelection.setNestedScrollingEnabled(false);
                    recyclerAdvisorySelection.setAdapter(advisorySelectionAdapter);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        relativeParentBg.setBackground(getResources().getDrawable(R.drawable.intro_services_type));


                        // Glide.with(this).load(introDetail.getData().get(0).getIntroImage()).into(introPageImg);
                    } else {
                        relativeParentBg.setBackground(getResources().getDrawable(R.drawable.intro_services_type));

                        /*Bitmap myImage = getBitmapFromURL(introDetail.getData().get(0).getServiceBackgroundImage());
                        Drawable dr = new BitmapDrawable(myImage);
                        relativeParentBg.setBackgroundDrawable(dr);*/
                        // Glide.with(this).load(introDetail.getData().get(0).getServiceBackgroundImage()).into(introPageImg);
                       // introPageImg.setImageDrawable(getResources().getDrawable(R.drawable.intro_page_test));
                       // advisoryText.setText(introDetail.getData().get(0).getServiceTittle());
                       // relativeParentBg.setBackground(ContextCompat.getDrawable(this, Integer.parseInt(introDetail.getData().get(0).getServiceBackgroundImage())));

                        //  Glide.with(this).load(introDetail.getData().get(0).getIntroImage()).into(introPageImg);
                    }
                }
                //   tvIntroTitle.setText(introDetail.getData().get(0).getIntroTitle());

                /*SharedPreferences.Editor spe = getSharedPreferences().edit();
                Log.d(TAG, "Skin Image Url <<::>> " + introDetail.getData().get(0).getSkinexpertLogo());
                spe.putString(Constants.LOGO_URL, introDetail.getData().get(0).getSkinexpertLogo());
                spe.apply();

                setImageDetails(introDetail.getData().get(0).getImageDetail());*/

            }
        });

        viewModel.getError().observe(this, isError -> {
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

        viewModel.getLoading().observe(this, isLoading -> {
            Log.d(TAG, "Progress Bar loading <<::>> " + isLoading);
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if (isLoading) {
                    // errorTextView.setVisibility(View.GONE);
                }
            }
        });
    }


   /* private void setImageDetails(List<ImageDetail> imageDetails) {

        Log.d(TAG, "ImageDetail Size <<::>> " + imageDetails.size());

        Glide.with(this).load(imageDetails.get(0).getImageUrl()).into(imageview1);
        Glide.with(this).load(imageDetails.get(1).getImageUrl()).into(imageview2);
        Glide.with(this).load(imageDetails.get(2).getImageUrl()).into(imageview3);

        text1.setText(imageDetails.get(0).getImageTitle());
        text2.setText(imageDetails.get(1).getImageTitle());
        text3.setText(imageDetails.get(2).getImageTitle());

        layout4.setVisibility(imageDetails.size() > 3 ? View.VISIBLE : View.GONE);
        if (imageDetails.size() > 3) {
            Glide.with(this).load(imageDetails.get(3).getImageUrl()).into(imageview4);
            text4.setText(imageDetails.get(3).getImageTitle());
        }
    }*/


    public Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void init() {

        setActionBarTitle(getResources().getString(R.string.introduction), true);



        if (!getSharedPreferences().getString(Constants.FILE_URL, "").isEmpty())
            deleteImage(getSharedPreferences().getString(Constants.FILE_URL, ""), Constants.FILE_URL);
        if (!getSharedPreferences().getString(Constants.THUMB_IMAGE_FILE_URL, "").isEmpty())
            deleteImage(getSharedPreferences().getString(Constants.THUMB_IMAGE_FILE_URL, ""), Constants.THUMB_IMAGE_FILE_URL);
        apiInterface = RetrofitService.getRetrofit().create(APIInterface.class);

        advisoryText = findViewById(R.id.advisory_text);
        relativeParentBg = findViewById(R.id.relative_parent_bg);

        recyclerAdvisorySelection = findViewById(R.id.recycler_advisory_selection);
        btnStartAnalysis = findViewById(R.id.btnStartAnalysis);
        introPageImg = findViewById(R.id.intro_page_img);
        progressBar = findViewById(R.id.progress_circular);

    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        observableViewModelIntroPage();
       /* if (!PermissionUtils.isStorageGranted(this)) {
            Log.d(TAG, "WRITE_EXTERNAL_STORAGE permission not granted..");
            PermissionUtils.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    PERMISSION_CODE_STORAGE);
        } else if (!PermissionUtils.isReadStorageGranted(this)) {
            Log.d(TAG, "READ_EXTERNAL_STORAGE permission not granted..");
            PermissionUtils.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE,
                    PERMISSION_CODE_READ_STORAGE);
        } else
            Log.d(TAG, "Write and Read permission granted..");*/

    }


    @Override
    public void selectedadvisory(String serviceId, String serviceImage) {

         SelectedService=  serviceId;
        ServiceImage = serviceImage;


    }
}


