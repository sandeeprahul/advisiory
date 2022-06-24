package com.example.advisoryservice.ui.login;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.advisoryservice.BuildConfig;
import com.example.advisoryservice.R;
import com.example.advisoryservice.base.BaseActivity;
import com.example.advisoryservice.data.rest.APIInterface;
import com.example.advisoryservice.data.rest.RetrofitService;
import com.example.advisoryservice.ui.advisoryserviceselection.AdvisoryServiceSelection;
import com.example.advisoryservice.ui.main.MainActivity;
import com.example.advisoryservice.ui.test.PermissionUtils;
import com.example.advisoryservice.util.Constants;
import com.example.advisoryservice.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class LoginActivity extends BaseActivity {

    public static final String TAG = LoginActivity.class.getSimpleName();

    private static final int PERMISSION_CODE_STORAGE = 3001;
    private static final int PERMISSION_CODE_READ_STORAGE = 3011;

    private LoginViewModel loginViewModel;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spe;

    TextView version;
    private long enqueue = -1L;
    private DownloadManager dm;
    EditText usernameEditText, passwordEditText;
    ProgressBar loadingProgressBar;
    String versionName;
    private APIInterface apiInterface;
    ProgressDialog pDialog;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private Button loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        if (!isTablet())
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);*/

        setRequestedOrientation(getSharedPreferences().getBoolean(Constants.TABLET, false) ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#3c3c3c"));
        }


       /* if (checkPermission()) {
            Log.w(TAG, "Permission to read and write file is not granted... Requesting for permission");
            requestPermissionAndContinue();
        }


        if (isPackageExisted("in.hng.skinexpert")) {

            new AlertDialog.Builder(LoginActivity.this)
                    .setMessage("Please click on OK button in the next message box to complete the installation of the app")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Intent intent = new Intent(Intent.ACTION_DELETE);
                            //Enter app package name that app you wan to install
                            intent.setData(Uri.parse("package:com.foodowrld.stock.stock"));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            int pid = android.os.Process.myPid();
                            android.os.Process.killProcess(pid);
                        }
                    }).show();


        }*/

        /*if (isTablet())
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);*/

        sharedPreferences = getSharedPreferences();
        apiInterface = RetrofitService.getRetrofit().create(APIInterface.class);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("New Version is Downloading.. Please wait");
        pDialog.dismiss();

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        final ImageView ivSkinExpertLogo = findViewById(R.id.ivSkinExpertLogo);
        loadingProgressBar = findViewById(R.id.loading);
        version = findViewById(R.id.version);

        versionName = BuildConfig.VERSION_NAME;
        String versionCode = String.valueOf(BuildConfig.VERSION_CODE);
        Log.d(TAG, "Version name <<::>> " + versionName);
        Log.d(TAG, "Version Code <<::>> " + versionCode);
        version.setText("v" + versionCode + " Version : " + versionName);
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        registerReceiver(onComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        registerReceiver(onNotificationClick,
                new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));

        loginViewModel.fetchIntro();
        loginViewModel.getIntroDetails().observe(this, introDetail -> {
            Glide.with(this).load(introDetail.getData().get(0).getSkinexpertLogo())
                    .placeholder(getCircularProgressDrawable())
                    .into(ivSkinExpertLogo);
        });

        loginViewModel.getError().observe(this, isError -> {
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

        loginViewModel.getLoading().observe(this, isLoading -> {
            Log.d(TAG, "Progress Bar loading <<::>> " + isLoading);
            if (isLoading != null) {
                loadingProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if (isLoading) {
                    // errorTextView.setVisibility(View.GONE);
                }
            }
        });

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                //    CallCheckForVersionUpdateAPI();
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });


        loginViewModel.getLoginResponse().observe(this, loginResponse -> {

            if (loginResponse != null) {

                //  Toast.makeText(this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();

                if (loginResponse.getStatusCode().equalsIgnoreCase("200")) {

                    spe = sharedPreferences.edit();
                    spe.putString(Constants.LOCATION_CODE, loginResponse.getLocationCode());
                    spe.putBoolean(Constants.LOGGED_IN, true);
                    spe.apply();
                    loginButton.setClickable(false);
                    //  checkVersion(loginResponse.getLocationCode(), versionName);
                  //  checkVersionTest(loginResponse.getLocationCode(), versionName);
                    Intent intent = new Intent(LoginActivity.this, AdvisoryServiceSelection.class);
                    startActivity(intent);
                    finish();
                } else
                    Toast.makeText(this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

   /* private void checkVersion(String locationCode, String versionName) {
        loginViewModel.LatestVersion(locationCode, versionName);
        loginViewModel.getLatestVersion().observe(this, response -> {

            if (response.getMessage().equalsIgnoreCase("version upto date")) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                if (isFileExists("hht.apk")) {
                    if (deleteFileName("hht.apk")) {
                        //DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://35.200.223.104/hng/applications/hht.apk"));
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(RetrofitService.BASE_URL + "/hng/applications/hht.apk"));
                        request.setMimeType("application/vnd.android.package-archive");
                        request.setDestinationInExternalPublicDir("/download/", "hht.apk");
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        enqueue = dm.enqueue(request);
                    }
                } else {
                    //DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://35.200.223.104/hng/applications/hht.apk"));
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(RetrofitService.BASE_URL + "/hng/applications/hht.apk"));
                    request.setMimeType("application/vnd.android.package-archive");
                    request.setDestinationInExternalPublicDir("/download/", "hht.apk");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    enqueue = dm.enqueue(request);
                }
            }

        });

    }*/

    private boolean checkPermission() {

        return ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ;
    }

    private void requestPermissionAndContinue() {
        if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("Permission Required");
                alertBuilder.setMessage("Allow permission to READ and WRITE file to you device");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{WRITE_EXTERNAL_STORAGE
                                , READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
                //  Log.e("", "permission denied, show dialog");
            } else {
                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        } else {

        }
    }

    public boolean isPackageExisted(String targetPackage) {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
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
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        //}
                    }

                }

                if (response.code() == 400) {
                    loginButton.setClickable(true);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                   // newAppUpdateshowDialog(getResources().getString(R.string.new_app_update_text));
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




    @Override
    protected void onResume() {
        super.onResume();
        if (!PermissionUtils.isStorageGranted(this)) {
            Log.d(TAG, "WRITE_EXTERNAL_STORAGE permission not granted..");
            PermissionUtils.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    PERMISSION_CODE_STORAGE);
        } else if (!PermissionUtils.isReadStorageGranted(this)) {
            Log.d(TAG, "READ_EXTERNAL_STORAGE permission not granted..");
            PermissionUtils.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE,
                    PERMISSION_CODE_READ_STORAGE);
        } else
            Log.d(TAG, "Write and Read permission granted..");

    }



/*
    private void CallCheckForVersionUpdateAPI() {

      //  Log.w(TAG, "Checking device ID and version " + DEV_ID + " Version Number " + versionName);

        String url = RetrofitService.BASE_URL + "/intro/check_version?loctionCode=" + "106" + "&version=" + versionName;

        Log.w(TAG, "Calling API to check for Updates in Login Page");
        Log.w(TAG, "URL ==> " + url);

       */
/* loadingProgressBar = ProgressDialog.show(LoginActivity.this, "",
                "Validating Device ID and version.. Please wait..", true);
        loadingProgressBar.show();*//*

        loadingProgressBar.setVisibility(View.VISIBLE);

        ApiCall.make(this, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            loadingProgressBar.setVisibility(View.GONE);

                            Log.d(TAG, "CallCheckForUpdateAPI onResponse: " + response);

                            JSONObject responseObject = new JSONObject(response);
                            String StatusCode = responseObject.getString("statusCode");
                            if (StatusCode.equalsIgnoreCase("200")) {
                               // userLogin();

                                loginViewModel.login(usernameEditText.getText().toString(),
                                        passwordEditText.getText().toString());
                            } else if (StatusCode.equalsIgnoreCase("201")) {

                                Toast.makeText(LoginActivity.this, "New update is available", Toast.LENGTH_SHORT).show();

                                if (isFileExists("hht.apk")) {
                                    if (deleteFileName("hht.apk")) {
                                        //DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://35.200.223.104/hng/applications/hht.apk"));
                                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(RetrofitService.BASE_URL + "/hng/applications/hht.apk"));
                                        request.setMimeType("application/vnd.android.package-archive");
                                        request.setDestinationInExternalPublicDir("/download/", "hht.apk");
                                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                        enqueue = dm.enqueue(request);
                                    }
                                } else {
                                    //DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://35.200.223.104/hng/applications/hht.apk"));
                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(RetrofitService.BASE_URL + "/hng/applications/hht.apk"));
                                    request.setMimeType("application/vnd.android.package-archive");
                                    request.setDestinationInExternalPublicDir("/download/", "hht.apk");
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    enqueue = dm.enqueue(request);
                                }


                            } else
                                showFailedAlert(responseObject.getString("Message") + " Please Contact the IT Support team..");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "JSONException " + e.getLocalizedMessage());
                            */
/*if (idialog != null && idialog.isShowing())
                                idialog.dismiss();*//*

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w(TAG, "Volley Error Occured Validating Login " + error.toString());
                        */
/*if (idialog != null && idialog.isShowing())
                            idialog.dismiss();
                        userLogin();*//*

                        error.getMessage();
                        volleyError(error);
                    }
                }
        );

    }
*/


    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {

                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(enqueue);
                Cursor c = dm.query(query);

                if (c != null && c.moveToFirst()) {
                    int columnIndex = c
                            .getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (DownloadManager.STATUS_SUCCESSFUL == c
                            .getInt(columnIndex)) {

                        String uriString = c
                                .getString(c
                                        .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));

                        Log.d(TAG, "ONComplete Screen " + uriString);

                        String app = uriString.substring(uriString.indexOf("/download/") + 10);
                        /*Intent i_intent = new Intent(Intent.ACTION_VIEW);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                       i_intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + app)), "application/vnd.android.package-archive");
                        //intent.setDataAndType(FileProvider.getUriForFile(this,"in.hng.skinexpert",photoFile));
                        i_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i_intent);*/

                        Intent i_intent = new Intent(Intent.ACTION_VIEW);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        i_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        Uri apkURI = FileProvider.getUriForFile(
                                LoginActivity.this,
                                LoginActivity.this.getApplicationContext()
                                        .getPackageName() + ".fileprovider", new File(Environment.getExternalStorageDirectory() + "/download/" + app));
                        i_intent.setDataAndType(apkURI, "application/vnd.android.package-archive");
                        i_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
// End New Approach
                        startActivity(i_intent);
                        pDialog.dismiss();
                       /* usernameEditText.setText("");
                        passwordEditText.setText("");*/
                        Intent intentMain = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intentMain);
                        finish();
                    }

                } else
                    openFile();
            }
        }
    };

    BroadcastReceiver onNotificationClick = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            openFile();
        }
    };


    protected void openFile() {
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + "Skinexpert.apk")), "application/vnd.android.package-archive");
        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(install);
    }

    private void showFailedAlert(final String msg) {

        Log.w(TAG, "ShowFailedAlert " + msg);

        this.runOnUiThread(new Runnable() {
            public void run() {

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("HnGmBOS");
                builder.setMessage(msg)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void volleyError(VolleyError error) {
        if (error instanceof TimeoutError) {
            showLoginFailedAlert("Time out error occurred.Please click on OK and try again");
            Log.e(TAG, "Time out error occurred.");
            //Time out error

        } else if (error instanceof NoConnectionError) {
            showLoginFailedAlert("Network error occurred.Please click on OK and try again");
            Log.e(TAG, "Network error occurred.");
            //net work error

        } else if (error instanceof AuthFailureError) {
            showLoginFailedAlert("Authentication error occurred.Please click on OK and try again");
            Log.e(TAG, "Authentication error occurred.");
            //error

        } else if (error instanceof ServerError) {
            showLoginFailedAlert("Server error occurred.Please click on OK and try again");
            Log.e(TAG, "Server error occurred.");
            //Error
        } else if (error instanceof NetworkError) {
            showLoginFailedAlert("Network error occurred.Please click on OK and try again");
            Log.e(TAG, "Network error occurred.");
            //Error

        } else if (error instanceof ParseError) {
            //Error
            showLoginFailedAlert("An error occurred.Please click on OK and try again");
            Log.e(TAG, "An error occurred.");
        } else {

            showLoginFailedAlert("An error occurred.Please click on OK and try again");
            Log.e(TAG, "An error occurred.");
            //Error
        }

    }

    private void showLoginFailedAlert(final String msg) {

        this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                Log.i(TAG, msg);
                builder.setTitle("Login Failed");
                builder.setMessage(msg)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //on click event
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }


    private boolean isFileExists(String filename) {
        File folder1 = new File(Environment.getExternalStorageDirectory() + "/download/" + filename);
        return folder1.exists();

    }

    private boolean deleteFileName(String filename) {

        File folder1 = new File(Environment.getExternalStorageDirectory() + "/download/" + filename);
        return folder1.delete();



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
}
