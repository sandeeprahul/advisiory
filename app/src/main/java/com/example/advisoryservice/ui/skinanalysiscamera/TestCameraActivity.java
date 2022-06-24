package com.example.advisoryservice.ui.skinanalysiscamera;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.advisoryservice.R;
import com.example.advisoryservice.base.BaseActivity;
import com.example.advisoryservice.data.model.instructionDetail.ImageDetail;
import com.example.advisoryservice.data.model.instructionDetail.InstructionDetail;
import com.example.advisoryservice.data.model.revieveDetail.ApiResponse;
import com.example.advisoryservice.data.rest.APIInterface;
import com.example.advisoryservice.data.rest.RetrofitService;
import com.example.advisoryservice.ui.genderquestions.GenderQuestionsActivity;
import com.example.advisoryservice.ui.productquestions.CustomerProductQuestionActivity;
import com.example.advisoryservice.util.Constants;
import com.example.advisoryservice.util.Log;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.fotoapparat.Fotoapparat;
import io.fotoapparat.configuration.CameraConfiguration;
import io.fotoapparat.configuration.UpdateConfiguration;
import io.fotoapparat.error.CameraErrorListener;
import io.fotoapparat.exception.camera.CameraException;
import io.fotoapparat.preview.Frame;
import io.fotoapparat.preview.FrameProcessor;
import io.fotoapparat.result.BitmapPhoto;
import io.fotoapparat.result.PhotoResult;
import io.fotoapparat.result.WhenDoneListener;
import io.fotoapparat.view.CameraView;
import io.fotoapparat.view.FocusView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static io.fotoapparat.log.LoggersKt.fileLogger;
import static io.fotoapparat.log.LoggersKt.logcat;
import static io.fotoapparat.log.LoggersKt.loggers;
import static io.fotoapparat.result.transformer.ResolutionTransformersKt.scaled;
import static io.fotoapparat.selector.AspectRatioSelectorsKt.standardRatio;
import static io.fotoapparat.selector.FlashSelectorsKt.autoFlash;
import static io.fotoapparat.selector.FlashSelectorsKt.autoRedEye;
import static io.fotoapparat.selector.FlashSelectorsKt.off;
import static io.fotoapparat.selector.FlashSelectorsKt.torch;
import static io.fotoapparat.selector.FocusModeSelectorsKt.autoFocus;
import static io.fotoapparat.selector.FocusModeSelectorsKt.continuousFocusPicture;
import static io.fotoapparat.selector.FocusModeSelectorsKt.fixed;
import static io.fotoapparat.selector.LensPositionSelectorsKt.back;
import static io.fotoapparat.selector.LensPositionSelectorsKt.front;
import static io.fotoapparat.selector.PreviewFpsRangeSelectorsKt.highestFps;
import static io.fotoapparat.selector.ResolutionSelectorsKt.highestResolution;
import static io.fotoapparat.selector.SelectorsKt.firstAvailable;
import static io.fotoapparat.selector.SensorSensitivitySelectorsKt.highestSensorSensitivity;

public class TestCameraActivity extends BaseActivity {

    private static final String TAG = TestCameraActivity.class.getSimpleName();

    private final PermissionsDelegate permissionsDelegate = new PermissionsDelegate(this);
    private boolean hasCameraPermission;
    private CameraView cameraView;
    private View background_view;
    private FocusView focusView;
    private View capture;
    View switchCameraButton;
    ImageView imageView, ivOval;
    RecyclerView rvApiResponse;

    String custTransId = "2768";

    List<ApiResponse> apiResponseList = new ArrayList<>();

    private Fotoapparat fotoapparat;

    boolean activeCameraBack = false;

    private CameraConfiguration cameraConfiguration = CameraConfiguration
            .builder()
            .photoResolution(standardRatio(
                    highestResolution()
            ))
            .focusMode(firstAvailable(
                    continuousFocusPicture(),
                    autoFocus(),
                    fixed()
            ))
            .flash(firstAvailable(
                    autoRedEye(),
                    autoFlash(),
                    torch(),
                    off()
            ))
            .previewFpsRange(highestFps())
            .sensorSensitivity(highestSensorSensitivity())
            .frameProcessor(new SampleFrameProcessor())
            .build();

    APIInterface apiInterface;
    String gender = "female", instructionDetailData = "";
    Gson gson;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spe;

    InstructionDetail instructionDetail;
    List<ImageDetail> imageDetailList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(getSharedPreferences().getBoolean(Constants.TABLET, false) ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_camera);

        setActionBarTitle("Take a Selfie");

        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);

        sharedPreferences = getSharedPreferences(Constants.SP_PREF, Context.MODE_PRIVATE);

        apiInterface = RetrofitService.getRetrofitForSkinAnalysis().create(APIInterface.class);
        gson = new Gson();
        progressDialog = new ProgressDialog(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Intent intent = getIntent();
            if (intent.hasExtra("gender")) {
                gender = bundle.getString("gender");
                custTransId = bundle.getString(Constants.ADVISORY_CUSTOMER_TRANSCTION_ID);

                Log.d(TAG, "Gender <<::>> " + gender);
                Log.d(TAG, "custTransId <<::>> " + custTransId);

                instructionDetailData = bundle.getString(Constants.INSTRUCTION_DETAIL);
                Log.d(TAG, "instruction Details  <<::>> " + instructionDetailData);

                instructionDetail = gson.fromJson(instructionDetailData, InstructionDetail.class);
               imageDetailList.addAll(instructionDetail.getData().get(0).getImageDetail());
                Log.d(TAG, "ImageDetail size <<<::>> " + imageDetailList.size());
            }
        }

        background_view = findViewById(R.id.background_view);
        cameraView = findViewById(R.id.cameraView);
        focusView = findViewById(R.id.focusView);
        capture = findViewById(R.id.capture);
        imageView = findViewById(R.id.result);
        ivOval = findViewById(R.id.ivOval);
        switchCameraButton = findViewById(R.id.switchCamera);
        hasCameraPermission = permissionsDelegate.hasCameraPermission();

        rvApiResponse = findViewById(R.id.rvApiResponse);

        capture.setVisibility(View.GONE);
        ivOval.setVisibility(View.GONE);
        switchCameraButton.setVisibility(View.GONE);

        if (hasCameraPermission) {
            cameraView.setVisibility(View.VISIBLE);
            background_view.setVisibility(View.GONE);
        } else {
            permissionsDelegate.requestCameraPermission();
        }

        fotoapparat = createFotoapparat();

        showInstructionDialog();
        takePictureOnClick();
        switchCameraOnClick();
        toggleTorchOnSwitch();
        zoomSeekBar();
    }

    private Fotoapparat createFotoapparat() {
        return Fotoapparat
                .with(this)
                .into(cameraView)
                .focusView(focusView)
                //.previewScaleType(ScaleType.CenterCrop)
                .lensPosition(back())
                .frameProcessor(new TestCameraActivity.SampleFrameProcessor())
                .logger(loggers(
                        logcat(),
                        fileLogger(this)
                ))
                .cameraErrorCallback(new CameraErrorListener() {
                    @Override
                    public void onError(@NonNull CameraException e) {
                        Toast.makeText(TestCameraActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                })
                .build();
    }

    private void zoomSeekBar() {
        SeekBar seekBar = findViewById(R.id.zoomSeekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                fotoapparat.setZoom(progress / (float) seekBar.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void switchCameraOnClick() {
        View switchCameraButton = findViewById(R.id.switchCamera);

        boolean hasFrontCamera = fotoapparat.isAvailable(front());
        Log.d(TAG, "Front camera available <<::>> " + hasFrontCamera);

        switchCameraButton.setVisibility(
                hasFrontCamera ? View.VISIBLE : View.GONE
        );

        if (hasFrontCamera) {
            switchCameraOnClick(switchCameraButton);

            fotoapparat.switchTo(front(),
                    cameraConfiguration
            );
        }
    }

    private void toggleTorchOnSwitch() {
        SwitchCompat torchSwitch = findViewById(R.id.torchSwitch);

        torchSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fotoapparat.updateConfiguration(
                        UpdateConfiguration.builder()
                                .flash(
                                        isChecked ? torch() : off()
                                )
                                .build()
                );
            }
        });
    }

    private void switchCameraOnClick(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeCameraBack = !activeCameraBack;

                Log.d(TAG, "ActiveCameraBack <<::>> " + activeCameraBack);

                fotoapparat.switchTo(
                        activeCameraBack ? back() : front(),
                        cameraConfiguration
                );
            }
        });
    }

    private void takePictureOnClick() {
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Capture button clicked");

                progressDialog.setMessage("Capturing Image..");
                progressDialog.setCancelable(false);
                progressDialog.show();

                capture.setVisibility(View.GONE);
               /* Intent intent = new Intent(TestCameraActivity.this, CustomerProductQuestionActivity.class);

                startActivity(intent);*/

                takePicture();
            }
        });
    }


    File file;

    private void takePicture() {

        Log.d(TAG, "Capturing photo");
        PhotoResult photoResult = fotoapparat.takePicture();

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String imageName = "ImgSK" + timeStamp + ".jpeg";

        try {
            File storageDir = new File(Environment.getExternalStorageDirectory() + "/mBOSPictures");
            if (!storageDir.exists()) {
                File wallpaperDirectory = new File("/sdcard/mBOSPictures/");
                wallpaperDirectory.mkdirs();
            }

            //file = File.createTempFile(imageName, ".jpeg", storageDir);

            //File file = new File(getExternalFilesDir("photos"), imageName);
            File file = new File(Environment.getExternalStorageDirectory() + "/mBOSPictures", imageName);
            photoResult.saveToFile(file);

            photoResult
                    .toBitmap(scaled(0.25f))
                    .whenDone(new WhenDoneListener<BitmapPhoto>() {
                        @Override
                        public void whenDone(@Nullable BitmapPhoto bitmapPhoto) {
                            if (bitmapPhoto == null) {
                                Log.e(TAG, "Couldn't capture photo.");
                                return;
                            }

                            final BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            options.inSampleSize = 2;
                            options.inJustDecodeBounds = false;
                            //options.inTempStorage = new byte[16 * 1024];

                            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                            //imageView.setImageBitmap(myBitmap);
                            //imageView.setRotation(-bitmapPhoto.rotationDegrees);
                            Log.d(TAG, "File Path <<::>> " + file.getAbsolutePath());

                            Uri tempUri = getImageUri(getApplicationContext(), bitmapPhoto.bitmap);
                            File thumbImageFile = new File(getRealPathFromURI(tempUri));
                            Log.d(TAG, "thumbImageFile File Path <<::>> " + thumbImageFile.getAbsolutePath());

                            spe = sharedPreferences.edit();
                            spe.putString(Constants.FILE_URL, file.getAbsolutePath());
                            spe.putString(Constants.THUMB_IMAGE_FILE_URL, thumbImageFile.getAbsolutePath());
                            spe.apply();

                            ExifInterface exif = null;
                            try {
                                exif = new ExifInterface(file.getAbsolutePath());
                            } catch (IOException e) {
                                e.printStackTrace();
                                if (progressDialog != null && progressDialog.isShowing())
                                    progressDialog.dismiss();
                            }
                            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                    ExifInterface.ORIENTATION_UNDEFINED);
                            Bitmap bmRotated = rotateBitmap(myBitmap, orientation);
                            imageView.setImageBitmap(bmRotated);

                            if (progressDialog != null && progressDialog.isShowing())
                                progressDialog.dismiss();

                            imageUpload();

                            //moveScreen(file.getAbsolutePath());
                            //uploadFile(file.getAbsolutePath());

                            // CALL THIS METHOD TO GET THE ACTUAL PATH
                            //File finalFile = new File(getRealPathFromURI(uri));

                        }
                    });
        } catch (Exception ex) {

            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();

            ex.printStackTrace();
            capture.setVisibility(View.VISIBLE);
            cameraView.setVisibility(View.VISIBLE);
            Log.e(TAG, "Take Picture Exception " + ex);
        }
    }

    private void moveScreen(String path) {
        Intent resultIntent = getIntent();
        Bundle mBundle = new Bundle();
        mBundle.putString(Constants.FILE_URL, path);
        resultIntent.putExtras(mBundle);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    ProgressDialog progressDialog;
    boolean isError = false;
    String errorMessage = "";
    String base64Image = "";

    //retrofit2.Response<ResponseBody> response;
    Response response;

    private void imageUpload() {

        progressDialog.setMessage("Analyzing..Please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        isError = false;
        errorMessage = "";

        try {
            Bitmap depositSlipBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
            depositSlipBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos1);
            byte[] imageBytes1 = baos1.toByteArray();
            base64Image = Base64.encodeToString(imageBytes1, Base64.DEFAULT).trim();

            cameraView.setVisibility(View.GONE);
            background_view.setVisibility(View.VISIBLE);

            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("custTransId", custTransId);
            jsonObject1.put("locationCode", getSharedPreferences().getString(Constants.LOCATION_CODE, ""));
            jsonObject1.put("fileFormat", "jpeg");
            jsonObject1.put("gender", gender);
            Log.e(TAG, "JSON Data <<::>> " + jsonObject1.toString());
            Log.d(TAG, "base64Image size <<::>> " + base64Image.length());
            jsonObject1.put("imageFile", base64Image.replaceAll("\\\\", ""));

            RequestBody requestBody =
                    RequestBody.create(MediaType.parse("application/json"), jsonObject1.toString());

            APIInterface apiInterface = RetrofitService.getRetrofit().create(APIInterface.class);

            Log.d(TAG, "Calling API revieve postimage api");
            Call<ResponseBody> call = apiInterface.revieveImage(requestBody);
            Log.d(TAG, "Post RequestBody <<::>>  " + requestBody);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {

                    progressDialog.dismiss();
                    Log.e(TAG, "response.code() " + response.code());

                    if (response.code() == 400) {

                        try {

                            JSONObject jsonObj = new JSONObject(response.errorBody().string());
                            Log.d(TAG, "RevieveDetail Json data <<::>>  " + jsonObj.toString());

                            String json1 = jsonObj.toString().replaceAll("\\\\n", "");

                            Log.d(TAG, "Removed backslash n <<<:::>> " + json1);

                            String json = json1.replaceAll("\\\\", "");
                            json = json.replaceAll("\"\\{", "{");
                            json = json.replaceAll("\\}\"", "}");
                            JSONObject jsonObject1 = new JSONObject(json);

                            JSONObject jsonObject = jsonObject1.getJSONObject("Message");

                            Log.d(TAG, "After removing slash <<::>> " + jsonObject.toString());

                            if (jsonObject.has("status")) {
                                String messageString = jsonObject.getString("status");
                                Log.d(TAG, "RevieveDetail Response data <<::>>  " + messageString);

                                JSONArray jsonArray = jsonObject.getJSONArray("status");

                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jObj = jsonArray.getJSONObject(i);
                                    stringBuilder.append(jObj.getString("description")).append("\n");
                                }

                                Log.d(TAG, "Error Message <<::>> " + stringBuilder);

                                AlertDialog.Builder builder = new AlertDialog.Builder(TestCameraActivity.this);
                                builder.setTitle("Image Analysis Error");
                                builder.setMessage(stringBuilder + "Please Retake..")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                deleteSavedImages();

                                                imageView.setImageBitmap(null);
                                                cameraView.setVisibility(View.VISIBLE);
                                                background_view.setVisibility(View.GONE);
                                                capture.setVisibility(View.VISIBLE);
                                                takePictureOnClick();

                                                dialog.dismiss();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                            } else {
                                Toast.makeText(TestCameraActivity.this, "Error occured while analysing" + "\n" +
                                        "Please retake..", Toast.LENGTH_SHORT).show();

                                deleteSavedImages();

                                imageView.setImageBitmap(null);
                                cameraView.setVisibility(View.VISIBLE);
                                background_view.setVisibility(View.GONE);
                                capture.setVisibility(View.VISIBLE);
                                takePictureOnClick();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.e(TAG, "Exception ex <<::>> " + ex);
                            Toast.makeText(TestCameraActivity.this, "Error occured" + "\n" +
                                    "Please retake..", Toast.LENGTH_SHORT).show();

                            deleteSavedImages();

                            imageView.setImageBitmap(null);
                            cameraView.setVisibility(View.VISIBLE);
                            background_view.setVisibility(View.GONE);
                            capture.setVisibility(View.VISIBLE);
                            takePictureOnClick();
                        }

                    } else {

                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();

                        if (response.body() != null) {

                            JSONObject jsonObject = null;
                            try {

                                jsonObject = new JSONObject(response.body().string());
                                String messageString = jsonObject.getString("message");

                                Log.d(TAG, "Response body messageString <<::>> " + messageString);

                                Toast.makeText(TestCameraActivity.this, messageString, Toast.LENGTH_SHORT).show();

                                Log.w(TAG, "JSON Object " + jsonObject);

                                Intent resultIntent = getIntent();
                                Bundle mBundle = new Bundle();
                                mBundle.putString(Constants.ANALYZE_IMAGE, jsonObject.toString());
                                mBundle.putBoolean(Constants.TAKE_SELFIE, true);
                                resultIntent.putExtras(mBundle);
                                setResult(Activity.RESULT_OK, resultIntent);
                                finish();

                            } catch (JSONException | IOException e) {
                                e.printStackTrace();

                                if (progressDialog != null && progressDialog.isShowing())
                                    progressDialog.dismiss();
                                Log.e(TAG, "Response code exception <<::>>> " + e);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                    Log.e(TAG, "Error thrown <<::>> " + t);

                    if (t instanceof SocketTimeoutException) {
                        // "Connection Timeout";
                        Toast.makeText(TestCameraActivity.this, "Connection Timeout", Toast.LENGTH_SHORT).show();
                    } else if (t instanceof IOException) {
                        // "Timeout";
                        Toast.makeText(TestCameraActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "IOException Error thrown <<::>> " + t);
                    } else {
                        //Call was cancelled by user
                        if (call.isCanceled()) {
                            Log.e(TAG, "Call was cancelled forcefully");
                        } else {
                            //Generic error handling
                            Toast.makeText(TestCameraActivity.this, "Network error occurred.. Please retake", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Network Error :: " + t.getLocalizedMessage());
                        }
                    }

                    deleteSavedImages();

                    imageView.setImageBitmap(null);
                    cameraView.setVisibility(View.VISIBLE);
                    capture.setVisibility(View.VISIBLE);
                    background_view.setVisibility(View.GONE);
                    takePictureOnClick();
                    progressDialog.dismiss();
                }
            });

            /*Log.d(TAG, "base64Image size <<::>> " + base64Image.replaceAll("\\\\", "").length());
            String jData = "{\"custTransId\":\"" + custTransId + "\", \"gender\":\"" + gender + "\", " +
                    "\"locationCode\":\"" + getSharedPreferences().getString(Constants.LOCATION_CODE, "") + "\", " +
                    "\"fileFormat\":\"jpg\",\"imageFile\":\"" + base64Image.replaceAll("\\\\", "") + " \"}";
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, jData);
            Request request = new Request.Builder()
                    .url("https://apiservices1.healthandglowonline.co.in/skinapi_test/api/revieve/PostImage")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Observable.fromCallable(() -> {
                try {
                    response = client.newCall(request).execute();

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Exception <<::>> " + e);
                }
                return false;
            }).subscribeOn(Schedulers.io())
                    .observeOn(mainThread())
                    .subscribe((result) -> {
                        //Use result for something
                        if (response != null) {

                            progressDialog.dismiss();
                            Log.e(TAG, "response.code() " + response.code());

                            if (response.code() == 400) {

                                try {

                                    JSONObject jsonObj = new JSONObject(response.body().string());
                                    Log.d(TAG, "RevieveDetail Json data <<::>>  " + jsonObj.toString());

                                    String json1 = jsonObj.toString().replace("\n", "");
                                    String json = json1.replaceAll("\\\\", "");
                                    JSONObject jsonObject = new JSONObject(json);

                                    if (jsonObject.has("status")) {
                                        String messageString = jsonObject.getString("status");
                                        Log.d(TAG, "RevieveDetail Response data <<::>>  " + messageString);

                                        JSONArray jsonArray = jsonObject.getJSONArray("status");

                                        StringBuilder stringBuilder = new StringBuilder();
                                        stringBuilder.append("Error Message").append("\n\n");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jObj = jsonArray.getJSONObject(i);
                                            stringBuilder.append(jObj.getString("description")).append("\n");
                                        }

                                        Toast.makeText(TestCameraActivity.this, stringBuilder + "\n" +
                                                "Please retake..", Toast.LENGTH_SHORT).show();

                                    } else
                                        Toast.makeText(TestCameraActivity.this, "Error occured while analysing" + "\n" +
                                                "Please retake..", Toast.LENGTH_SHORT).show();

                                    deleteSavedImages();

                                    imageView.setImageBitmap(null);
                                    cameraView.setVisibility(View.VISIBLE);
                                    background_view.setVisibility(View.GONE);
                                    takePictureOnClick();

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    Log.e(TAG, "Exception ex <<::>> " + ex);
                                    Toast.makeText(TestCameraActivity.this, "Error occured" + "\n" +
                                            "Please retake..", Toast.LENGTH_SHORT).show();

                                    deleteSavedImages();

                                    imageView.setImageBitmap(null);
                                    cameraView.setVisibility(View.VISIBLE);
                                    background_view.setVisibility(View.GONE);
                                    takePictureOnClick();
                                }

                            } else {

                                if (response.body() != null) {

                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(response.body().string());
                                        String messageString = jsonObject.getString("message");

                                        Log.d(TAG, "Response body messageString <<::>> " + messageString);

                                        Toast.makeText(TestCameraActivity.this, messageString, Toast.LENGTH_SHORT).show();

                                        Log.w(TAG, "JSON Object " + jsonObject);

                                        if (progressDialog != null && progressDialog.isShowing())
                                            progressDialog.dismiss();

                                        Intent resultIntent = getIntent();
                                        Bundle mBundle = new Bundle();
                                        mBundle.putString(Constants.ANALYZE_IMAGE, jsonObject.toString());
                                        mBundle.putBoolean(Constants.TAKE_SELFIE, true);
                                        resultIntent.putExtras(mBundle);
                                        setResult(Activity.RESULT_OK, resultIntent);
                                        finish();

                                    } catch (JSONException | IOException e) {
                                        e.printStackTrace();

                                        if (progressDialog != null && progressDialog.isShowing())
                                            progressDialog.dismiss();
                                        Log.e(TAG, "Response code exception <<::>>> " + e);
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(this, "Unable to fetch data", Toast.LENGTH_SHORT).show();
                            if (progressDialog != null && progressDialog.isShowing())
                                progressDialog.dismiss();
                            takePictureOnClick();
                        }
                    });*/

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, "Exception <<::>> " + ex);
            capture.setVisibility(View.VISIBLE);
        }
    }

    private class UploadFile extends AsyncTask<String, Void, Void> {

        private String Content;
        private String Error = null;
        private BufferedReader reader;

        protected void onPreExecute() {
            Log.w(TAG, "Upload File Execution started");

            progressDialog.setMessage("Analyzing..Please wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();
            isError = false;
            errorMessage = "";

            Bitmap depositSlipBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
            depositSlipBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos1);
            byte[] imageBytes1 = baos1.toByteArray();
            base64Image = Base64.encodeToString(imageBytes1, Base64.DEFAULT);

            cameraView.setVisibility(View.GONE);
            background_view.setVisibility(View.VISIBLE);

        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected Void doInBackground(String... urls) {

            HttpURLConnection connection = null;
            try {
                URL url = new URL(urls[0]);
                Log.d(TAG, "Url <<::>> " + url);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");
                con.setUseCaches(false);
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setReadTimeout(10000);
                con.setConnectTimeout(6 * 10 * 5000);
                con.setRequestProperty("Connection", "Keep-Alive");
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);

                /*JSONObject jsonObject = new JSONObject();
                jsonObject.put("custTransId", custTransId);
                jsonObject.put("locationCode", getSharedPreferences().getString(Constants.LOCATION_CODE, ""));
                jsonObject.put("fileFormat", "jpg");
                jsonObject.put("gender", gender);
                Log.e(TAG, "JSON Data <<::>> " + jsonObject.toString());
                Log.d(TAG, "base64Image size <<::>> " + base64Image.length());
                jsonObject.put("imageFile", base64Image);*/

                String json = "{\"custTransId\":" + custTransId + ", \"gender\":" + gender + ", " +
                        "\"locationCode\":" + getSharedPreferences().getString(Constants.LOCATION_CODE, "") + ", " +
                        "\"fileFormat\":\"jpg\", " +
                        "\"imageFile\":" + base64Image.replaceAll("\\\\", "") + "}";

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("custTransId", custTransId);
                parameters.put("locationCode", getSharedPreferences().getString(Constants.LOCATION_CODE, ""));
                parameters.put("fileFormat", "jpg");
                parameters.put("gender", gender);
                Log.e(TAG, "JSON Data <<::>> " + parameters.toString());
                Log.d(TAG, "base64Image size <<::>> " + base64Image.length());
                parameters.put("imageFile", base64Image);

                JSONObject jsonObject = new JSONObject(parameters);

                String jsonInputString = jsonObject.toString();
                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int code = con.getResponseCode();
                Log.w(TAG, "COde <<<:::>>> " + code);
                Log.w(TAG, "Message <<<:::>>> " + con.getResponseMessage());

                try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    //try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    Log.w(TAG, "REPSONSE <<<<:::>>> " + response.toString());
                    Content = response.toString();
                }
                //longLog(jsonObject.toString());

            } catch (Exception ex) {
                Error = ex.getMessage();
                Log.e(TAG, "Error <<::>> " + Error);
                Log.w(TAG, "DoInBackgroundTask Error <<<:::>>> " + ex);
                ex.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.
            progressDialog.dismiss();
            try {

                Log.w(TAG, "Post Execute Content " + Content);
                if (Content != null) {
                    JSONObject jsonResponse = new JSONObject(Content);
                    Log.d(TAG, "Response after uploading base64 <<::>> " + jsonResponse);

                    Intent resultIntent = getIntent();
                    Bundle mBundle = new Bundle();
                    mBundle.putString(Constants.ANALYZE_IMAGE, jsonResponse.toString());
                    resultIntent.putExtras(mBundle);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();

                } else
                    Toast.makeText(TestCameraActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e(TAG, "Exception e " + e);
            }
        }

    }


    //private void uploadFile(Uri fileUri) {

    Dialog dialog;

    private void showInstructionDialog() {

        Log.d(TAG, "Showing Dialog <<::>> ");

        dialog = new Dialog(this, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.camera_instruction_dialog);
        dialog.setCancelable(false);

        dialog.show();

        WebView webView = dialog.findViewById(R.id.webView);

        String htmlAsString = getString(R.string.data);      // used by WebView
        Spanned htmlAsSpanned = Html.fromHtml(htmlAsString);

        webView.setVisibility(View.GONE);
        if (isTablet())
            webView.loadUrl("file:///android_asset/index_tab.html");
        else
            webView.loadUrl("file:///android_asset/index.html");

       // Log.d(TAG, "Image Detail List <<::>> " + imageDetailList.size());

        TextView tvInstructions = dialog.findViewById(R.id.tvInstructions);
        TextView tvFooter = dialog.findViewById(R.id.tvFooter);
        RecyclerView rvInstructions = dialog.findViewById(R.id.rvInstructions);
        Button btnTakeSelfie = dialog.findViewById(R.id.btnTakeSelfie);
        Button btnSkipSelfie = dialog.findViewById(R.id.btnSkipSelfie);

        tvInstructions.setText(instructionDetail.getData().get(0).getHeaderText());
        tvFooter.setText(instructionDetail.getData().get(0).getFooterText());

        InstructionAdapter instructionAdapter = new InstructionAdapter(imageDetailList);
        rvInstructions.setLayoutManager(new GridLayoutManager(this, 2));
        rvInstructions.setNestedScrollingEnabled(false);
        rvInstructions.setAdapter(instructionAdapter);
        instructionAdapter.notifyDataSetChanged();

        btnTakeSelfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                capture.setVisibility(View.VISIBLE);
                ivOval.setVisibility(View.VISIBLE);
                switchCameraButton.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });

        btnSkipSelfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSkipSelfieDialog();
            }
        });

        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        /*WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM | Gravity.END;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);*/
    }

    private void showSkipSelfieDialog() {

        Dialog skipDialog = new Dialog(this);
        skipDialog.setContentView(R.layout.skip_selfie_dialog);
        skipDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        skipDialog.setCancelable(false);

        TextView tvYes = skipDialog.findViewById(R.id.tvYes);
        TextView tvNo = skipDialog.findViewById(R.id.tvNo);

        skipDialog.show();

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                capture.setVisibility(View.VISIBLE);
                ivOval.setVisibility(View.VISIBLE);
                switchCameraButton.setVisibility(View.VISIBLE);

                dialog.dismiss();
                skipDialog.dismiss();
            }
        });

        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = getIntent();
                Bundle mBundle = new Bundle();
                mBundle.putBoolean(Constants.TAKE_SELFIE, false);
                resultIntent.putExtras(mBundle);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                dialog.dismiss();
                skipDialog.dismiss();
            }
        });

        Window window = skipDialog.getWindow();
        assert window != null;
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }


    /*private void showInstructionDialog() {
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

    }*/

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {

        try {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Unable to fetch path";
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (hasCameraPermission) {
            fotoapparat.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (hasCameraPermission) {
            fotoapparat.stop();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionsDelegate.resultGranted(requestCode, permissions, grantResults)) {
            hasCameraPermission = true;
            fotoapparat.start();
            cameraView.setVisibility(View.VISIBLE);
        }
    }


    private void deleteSavedImages() {
        if (!getSharedPreferences().getString(Constants.FILE_URL, "").isEmpty())
            deleteImage(getSharedPreferences().getString(Constants.FILE_URL, ""), Constants.FILE_URL);
        if (!getSharedPreferences().getString(Constants.THUMB_IMAGE_FILE_URL, "").isEmpty())
            deleteImage(getSharedPreferences().getString(Constants.THUMB_IMAGE_FILE_URL, ""), Constants.THUMB_IMAGE_FILE_URL);
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    private class SampleFrameProcessor implements FrameProcessor {
        @Override
        public void process(@NonNull Frame frame) {
            // Perform frame processing, if needed
        }
    }
}

/*Observable.fromCallable(() -> {
                try {
                    response = call.execute();

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Exception <<::>> " + e);
                }
                return false;
            }).subscribeOn(Schedulers.io())
                    .observeOn(mainThread())
                    .subscribe((result) -> {
                        //Use result for something
                        if (response != null) {

                            progressDialog.dismiss();
                            Log.e(TAG, "response.code() " + response.code());

                            if (response.code() == 400) {

                                try {

                                    JSONObject jsonObj = new JSONObject(response.errorBody().string());
                                    Log.d(TAG, "RevieveDetail Json data <<::>>  " + jsonObj.toString());

                                    String json1 = jsonObj.toString().replace("\n", "");
                                    String json = json1.replaceAll("\\\\", "");
                                    JSONObject jsonObject = new JSONObject(json);

                                    if (jsonObject.has("status")) {
                                        String messageString = jsonObject.getString("status");
                                        Log.d(TAG, "RevieveDetail Response data <<::>>  " + messageString);

                                        JSONArray jsonArray = jsonObject.getJSONArray("status");

                                        StringBuilder stringBuilder = new StringBuilder();
                                        stringBuilder.append("Error Message").append("\n\n");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jObj = jsonArray.getJSONObject(i);
                                            stringBuilder.append(jObj.getString("description")).append("\n");
                                        }

                                        Toast.makeText(TestCameraActivity.this, stringBuilder + "\n" +
                                                "Please retake..", Toast.LENGTH_SHORT).show();

                                    } else
                                        Toast.makeText(TestCameraActivity.this, "Error occured while analysing" + "\n" +
                                                "Please retake..", Toast.LENGTH_SHORT).show();

                                    deleteSavedImages();

                                    imageView.setImageBitmap(null);
                                    cameraView.setVisibility(View.VISIBLE);
                                    background_view.setVisibility(View.GONE);
                                    takePictureOnClick();

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    Log.e(TAG, "Exception ex <<::>> " + ex);
                                    Toast.makeText(TestCameraActivity.this, "Error occured" + "\n" +
                                            "Please retake..", Toast.LENGTH_SHORT).show();

                                    deleteSavedImages();

                                    imageView.setImageBitmap(null);
                                    cameraView.setVisibility(View.VISIBLE);
                                    background_view.setVisibility(View.GONE);
                                    takePictureOnClick();
                                }

                            } else {

                                if (response.body() != null) {

                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(response.body().string());
                                        String messageString = jsonObject.getString("message");

                                        Log.d(TAG, "Response body messageString <<::>> " + messageString);

                                        Toast.makeText(TestCameraActivity.this, messageString, Toast.LENGTH_SHORT).show();

                                        Log.w(TAG, "JSON Object " + jsonObject);

                                        if (progressDialog != null && progressDialog.isShowing())
                                            progressDialog.dismiss();

                                        Intent resultIntent = getIntent();
                                        Bundle mBundle = new Bundle();
                                        mBundle.putString(Constants.ANALYZE_IMAGE, jsonObject.toString());
                                        mBundle.putBoolean(Constants.TAKE_SELFIE, true);
                                        resultIntent.putExtras(mBundle);
                                        setResult(Activity.RESULT_OK, resultIntent);
                                        finish();

                                    } catch (JSONException | IOException e) {
                                        e.printStackTrace();

                                        if (progressDialog != null && progressDialog.isShowing())
                                            progressDialog.dismiss();
                                        Log.e(TAG, "Response code exception <<::>>> " + e);
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(this, "Unable to fetch data", Toast.LENGTH_SHORT).show();
                            if (progressDialog != null && progressDialog.isShowing())
                                progressDialog.dismiss();
                            takePictureOnClick();
                        }
                    });*/
