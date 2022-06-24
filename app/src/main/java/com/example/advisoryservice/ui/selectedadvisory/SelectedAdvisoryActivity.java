package com.example.advisoryservice.ui.selectedadvisory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import com.example.advisoryservice.data.model.ServicePostRequest;
import com.example.advisoryservice.data.rest.APIInterface;
import com.example.advisoryservice.data.rest.RetrofitService;
import com.example.advisoryservice.ui.advisoryserviceselection.AdvisoryServiceSelection;
import com.example.advisoryservice.ui.genderquestions.GenderQuestionsActivity;
import com.example.advisoryservice.ui.main.MainActivity;
import com.example.advisoryservice.ui.main.MainViewModel;
import com.example.advisoryservice.util.Constants;
import com.example.advisoryservice.util.Log;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectedAdvisoryActivity extends BaseActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private static final int PERMISSION_CODE_STORAGE = 3001;
    private static final int PERMISSION_CODE_READ_STORAGE = 3011;

    RecyclerView recyclerAdvisorySelection;
    ImageView introImage, introPageImg;
    ImageView imageview1, imageview2, imageview3, imageview4;
    TextView text1, text2, text3, text4, version;
    LinearLayout layout4;
    TextView tvTitle, tvIntroduction, tvIntroTitle;
    Button btnStartAnalysis;
    ProgressBar progressBar;
    ImageView selectedAdvisorImage;
    boolean isTablet;

    private MainViewModel viewModel;
    LinearLayoutManager HorizontalLayout;
    String versionName;
    private APIInterface apiInterface;
    String locationCode = "", gender, age, advisory_service_number, advisory_customer_transction_id, advisory_service_image;
    ProgressDialog progressDialog;
    AdvisorySelectionAdapter.ClickListener clickListener;
    String SelectedService;
    String advisoryGender;

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
        setContentView(R.layout.selected_advisory);

        init();

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        observableViewModelIntroPage();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
           /* customerTransId = bundle.getString(Constants.CUSTOMER_TRANS_ID);
            customerCode = bundle.getString(Constants.CUSTOMER_CODE);
            Log.w(TAG, "Customer Code <<::>> " + customerCode);
            Log.w(TAG, "Customer TransId <<::>> " + customerTransId);*/

            advisory_service_number = bundle.getString(Constants.ADVISORY_SERVICE_NUMBER);
            advisory_customer_transction_id = bundle.getString(Constants.ADVISORY_CUSTOMER_TRANSCTION_ID);
            advisory_service_image = bundle.getString(Constants.ADVISORY_SERVICE_IMAGE);
            gender = bundle.getString(Constants.ADVISORY_USER_GENDER);


        }
        Glide.with(this).load(advisory_service_image).into(selectedAdvisorImage);


        btnStartAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(SelectedAdvisoryActivity.this, GenderQuestionsActivity.class);

                intent.putExtra(Constants.ADVISORY_SERVICE_NUMBER,  advisory_service_number);
                intent.putExtra(Constants.ADVISORY_CUSTOMER_TRANSCTION_ID, advisory_customer_transction_id);
                intent.putExtra(Constants.ADVISORY_USER_GENDER,gender);
                startActivity(intent);

              //  postServiceData();



            }
        });
    }


    private void postServiceData() {

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("advisoryService", advisory_service_number);
            jsonObject.put("custTransId", advisory_customer_transction_id);
            jsonObject.put("locationCode", locationCode);
            jsonObject.put("gender", gender);

            ServicePostRequest servicePostRequest = new ServicePostRequest();

            servicePostRequest.setAdvisoryService(advisory_service_number);
            servicePostRequest.setCustTransId(advisory_customer_transction_id);
            servicePostRequest.setLocationCode(locationCode);
            servicePostRequest.setGender(gender);
            Log.w(TAG, "ServicePost Request <<::>> " + jsonObject.toString());

            Call<Questions> call = apiInterface.insertServiceDetails(servicePostRequest);
            call.enqueue(new Callback<Questions>() {
                @Override
                public void onResponse(Call<Questions> call, Response<Questions> response) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String messageString = null;
                    try {
                        messageString = jsonObject.getString("Message");
                        Log.d(TAG, "messageString<<::>> " + messageString);
                        Toast.makeText(SelectedAdvisoryActivity.this, messageString, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


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

    private void init() {

          setActionBarTitle(getResources().getString(R.string.introduction), true);


        if (!getSharedPreferences().getString(Constants.FILE_URL, "").isEmpty())
            deleteImage(getSharedPreferences().getString(Constants.FILE_URL, ""), Constants.FILE_URL);
        if (!getSharedPreferences().getString(Constants.THUMB_IMAGE_FILE_URL, "").isEmpty())
            deleteImage(getSharedPreferences().getString(Constants.THUMB_IMAGE_FILE_URL, ""), Constants.THUMB_IMAGE_FILE_URL);
        apiInterface = RetrofitService.getRetrofit().create(APIInterface.class);

        selectedAdvisorImage = findViewById(R.id.selected_advisor_image);
        progressBar = findViewById(R.id.progress_circular);
        btnStartAnalysis = findViewById(R.id.btnStartAnalysis);
        introPageImg = findViewById(R.id.intro_page_img);

    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }


    private void observableViewModelIntroPage() {
        viewModel.getIntroDetailsPage().observe(this, introDetail -> {

            if (introDetail != null) {
                introPageImg.setImageDrawable(getResources().getDrawable(R.drawable.intro_page_test));
                //  Glide.with(this).load(introDetail.getData().get(0).getIntroImage()).into(introPageImg);

                List<Questions> test = introDetail.getData().get(0).getQuestion();


                if (isTablet) {
                    // tvIntroduction.setText(introDetail.getData().get(0).getIntroImage().replaceAll("<br />", ""));
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        introPageImg.setImageDrawable(getResources().getDrawable(R.drawable.intro_page_test));
                        // Glide.with(this).load(introDetail.getData().get(0).getIntroImage()).into(introPageImg);
                    } else {
                        introPageImg.setImageDrawable(getResources().getDrawable(R.drawable.intro_page_test));

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


    @Override
    protected void onResume() {
        super.onResume();
        // observableViewModelIntroPage();
    }
}



