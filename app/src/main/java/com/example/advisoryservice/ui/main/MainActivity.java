package com.example.advisoryservice.ui.main;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.multidex.BuildConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
//import com.example.advisoryservice.BuildConfig;
import com.example.advisoryservice.R;
import com.example.advisoryservice.base.BaseActivity;
import com.example.advisoryservice.data.model.ImageDetail;
import com.example.advisoryservice.data.model.Questions;
import com.example.advisoryservice.data.model.instructionDetail.Data;
import com.example.advisoryservice.data.rest.APIInterface;
import com.example.advisoryservice.data.rest.RetrofitService;
import com.example.advisoryservice.ui.customerdetail.CustomerInsertDetailsActivity;
import com.example.advisoryservice.ui.test.PermissionUtils;
import com.example.advisoryservice.util.Constants;
import com.example.advisoryservice.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;

public class MainActivity extends BaseActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private static final int PERMISSION_CODE_STORAGE = 3001;
    private static final int PERMISSION_CODE_READ_STORAGE = 3011;

    RecyclerView recyclerview;
    ImageView introImage,introPageImg;
    ImageView imageview1, imageview2, imageview3, imageview4;
    TextView text1, text2, text3, text4,version;
    LinearLayout layout4;
    TextView tvTitle, tvIntroduction, tvIntroTitle;
    Button btnStartAnalysis;
    ProgressBar progressBar;
    ImageView ivLogo;
    boolean isTablet;

    private MainViewModel viewModel;
    LinearLayoutManager HorizontalLayout;
    String versionName;
    private APIInterface apiInterface;
    String  locationCode = "";


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
        setContentView(R.layout.activity_main);

        init();

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);


        btnStartAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, CustomerInsertDetailsActivity.class);
                startActivity(intent);

               // checkVersionTest(locationCode, versionName);


            }
        });
    }

    private void checkVersionTest(String locationCode, String versionName) {
        Call<ResponseBody> call = apiInterface.getVersionTest(locationCode,versionName);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {


                Log.d(TAG, "response.code() " + response.code());
                JSONObject jsonObject = null;
                if (response.code()==200){
                    if(response.body()!=null){
                        //  jsonObject = new JSONObject(response.body().toString());

                        // if (messageString.equalsIgnoreCase("version upto date")){
                        Intent intent = new Intent(MainActivity.this, CustomerInsertDetailsActivity.class);

                        /*Intent intent = new Intent(MainActivity.this, AnalyzeTest.class);*/

                        //Intent intent = new Intent(MainActivity.this, AnalyzeTest.class);
                        startActivity(intent);
                        //}
                    }

                }

                if (response.code() == 400) {

                    newAppUpdateshowDialog(getResources().getString(R.string.new_app_update_text));
                  /*  pDialog.show();
                    if (isFileExists("Skinexpert.apk")) {
                        if (deleteFileName("Skinexpert.apk")) {
                            //DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://35.200.223.104/hng/applications/hht.apk"));
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://36.255.252.199/skinexpertapk/skinexpertapk/Skinexpert.apk"));
                            request.setMimeType("application/vnd.android.package-archive");
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Skinexpert.apk");
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            enqueue = dm.enqueue(request);
                        }
                    } else {
                        //DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://35.200.223.104/hng/applications/hht.apk"));
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://36.255.252.199/skinexpertapk/skinexpertapk/Skinexpert.apk"));
                        request.setMimeType("application/vnd.android.package-archive");
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Skinexpert.apk");
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        enqueue = dm.enqueue(request);
                    }*/







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

    private void newAppUpdateshowDialog(String message) {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.new_app_update_view);
        dialog.setCancelable(false);

        Button btnOk = dialog.findViewById(R.id.btnok);
        TextView textView = dialog.findViewById(R.id.tvText);
        textView.setText(message);

        dialog.show();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }


    private void observableViewModelIntroPage() {
        viewModel.getIntroDetailsPage().observe(this, introDetail -> {

            if (introDetail != null) {
              //  introPageImg.setImageDrawable(getResources().getDrawable(R.drawable.intro_page_test));
                Glide.with(this).load(introDetail.getData().get(0).getIntroImage()).into(introPageImg);

             List<Questions> test =     introDetail.getData().get(0).getQuestion();



                if (isTablet) {
                    // tvIntroduction.setText(introDetail.getData().get(0).getIntroImage().replaceAll("<br />", ""));
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                       // introPageImg.setImageDrawable(getResources().getDrawable(R.drawable.intro_page_test));
                        Glide.with(this).load(introDetail.getData().get(0).getIntroImage()).into(introPageImg);
                    } else {
                       // introPageImg.setImageDrawable(getResources().getDrawable(R.drawable.intro_page_test));

                         Glide.with(this).load(introDetail.getData().get(0).getIntroImage()).into(introPageImg);
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

    private void init() {

        setActionBarTitle(getResources().getString(R.string.introduction), true);



        if (!getSharedPreferences().getString(Constants.FILE_URL, "").isEmpty())
            deleteImage(getSharedPreferences().getString(Constants.FILE_URL, ""), Constants.FILE_URL);
        if (!getSharedPreferences().getString(Constants.THUMB_IMAGE_FILE_URL, "").isEmpty())
            deleteImage(getSharedPreferences().getString(Constants.THUMB_IMAGE_FILE_URL, ""), Constants.THUMB_IMAGE_FILE_URL);
        apiInterface = RetrofitService.getRetrofit().create(APIInterface.class);



        recyclerview = findViewById(R.id.recyclerview);
        introImage = findViewById(R.id.iv_introImage);
        introPageImg = findViewById(R.id.intro_page_img);

        tvTitle = findViewById(R.id.tvTitle);
        tvIntroduction = findViewById(R.id.tvIntroduction);
        tvIntroTitle = findViewById(R.id.tvIntroTitle);
        /*imageview1 = findViewById(R.id.imageview1);
        imageview2 = findViewById(R.id.imageview2);
        imageview3 = findViewById(R.id.imageview3);
        imageview4 = findViewById(R.id.imageview4);*/
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        progressBar = findViewById(R.id.progress_circular);
        btnStartAnalysis = findViewById(R.id.btnStartAnalysis);
        layout4 = findViewById(R.id.layout4);
        ivLogo = findViewById(R.id.ivLogo);
        version = findViewById(R.id.version);


        versionName = BuildConfig.VERSION_NAME;
        String versionCode = String.valueOf(BuildConfig.VERSION_CODE);
        Log.d(TAG, "Version name <<::>> " + versionName);
        Log.d(TAG, "Version Code <<::>> " + versionCode);

       /* versionName = BuildConfig.VERSION_NAME;
        String versionCode = String.valueOf(BuildConfig.VERSION_CODE);
        version.setText("v" + versionCode + " Version : " + versionName);*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#3c3c3c"));
        }
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

   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_CODE_STORAGE:
            case PERMISSION_CODE_READ_STORAGE:
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.d(TAG, "onRequestPermissionsResult Permission not granted");
                    Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.d(TAG, "onRequestPermissionsResult Permission granted");
                }
                break;
        }
        if (requestCode != PERMISSION_CODE_STORAGE && requestCode != PERMISSION_CODE_READ_STORAGE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }*/

    private void showInstructionDialog() {
        Log.d(TAG, "Showing Dialog <<::>> ");
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.camera_instruction_dialog);
        dialog.setCancelable(false);

        dialog.show();

        TextView tvInstructions = dialog.findViewById(R.id.tvInstructions);
        WebView webView = dialog.findViewById(R.id.webView);

        String htmlAsString = getString(R.string.data);      // used by WebView
        Spanned htmlAsSpanned = Html.fromHtml(htmlAsString);
        //tvInstructions.setText(htmlAsSpanned);
        //webView.loadDataWithBaseURL(null, htmlAsString, "text/html", "utf-8", null);
        if (isTablet())
            webView.loadUrl("file:///android_asset/index_tab.html");
        else
            webView.loadUrl("file:///android_asset/index.html");

        Button btnTakeSelfie = dialog.findViewById(R.id.btnTakeSelfie);
        btnTakeSelfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        /*WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM | Gravity.END;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);*/
    }
}

