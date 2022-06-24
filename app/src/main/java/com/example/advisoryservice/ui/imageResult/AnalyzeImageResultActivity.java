package com.example.advisoryservice.ui.imageResult;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Polar;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.PolarSeriesType;
import com.anychart.enums.ScaleStackMode;
import com.anychart.enums.ScaleTypes;
import com.anychart.enums.TooltipDisplayMode;
import com.anychart.graphics.vector.SolidFill;
import com.anychart.scales.Linear;
import com.bumptech.glide.Glide;
import com.example.advisoryservice.R;
import com.example.advisoryservice.base.BaseActivity;
import com.example.advisoryservice.data.model.Questions;
import com.example.advisoryservice.data.model.revieveDetail.ApiResponse;
import com.example.advisoryservice.data.model.revieveDetail.RevieveDetail;
import com.example.advisoryservice.data.model.testresult.Category;
import com.example.advisoryservice.data.model.testresult.Filter;
import com.example.advisoryservice.data.model.testresult.RegimeSchedule;
import com.example.advisoryservice.data.model.testresult.ResultDetailResponse;
import com.example.advisoryservice.data.model.testresult.Summary;
import com.example.advisoryservice.data.rest.APIInterface;
import com.example.advisoryservice.data.rest.RetrofitService;
import com.example.advisoryservice.ui.genderquestions.GenderQuestionsActivity;
import com.example.advisoryservice.ui.result.ApiResponseAdapter;
import com.example.advisoryservice.ui.result.ExpandableCategorySection;
import com.example.advisoryservice.ui.result.ResultDetailViewModel;
import com.example.advisoryservice.ui.result.ResultDetailViewModelTest;
import com.example.advisoryservice.ui.result.ResultDetailsTest;
import com.example.advisoryservice.util.Constants;
import com.example.advisoryservice.util.LinePagerIndicatorDecoration;
import com.example.advisoryservice.util.Log;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class AnalyzeImageResultActivity extends BaseActivity implements OnChartValueSelectedListener {

    public static final String TAG = AnalyzeImageResultActivity.class.getSimpleName();

    RecyclerView rvApiResponse;
    RelativeLayout rlApiResponse;
    Button btnRecommendedProducts;
    ImageView imageView;

    List<ApiResponse> apiResponseList = new ArrayList<>();

    ApiResponseAdapter apiResponseAdapter;
    APIInterface apiInterface;
    /*  String customerTransId = "3641";*/
    String customerTransId = "", filePath = "",advisoryServiceNumber,mobileNumberPref;
    boolean isSelfieTaken = false;
    Gson gson;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spe;

    private HorizontalBarChart chart;
    List<String> xValues = new ArrayList<>();
    List<Integer> colorList = new ArrayList<>();
    ArrayList<BarEntry> values = new ArrayList<>();

   /* LinkedList<String> xValues = new LinkedList<>();
    LinkedList<Integer> colorList = new LinkedList<>();
    LinkedList<BarEntry> values = new LinkedList<>();*/

    int count = 0;
    String desc = "";
    float barWidth = 9f;
    float spaceForBar = 10f;
    XAxis xAxis;

    HashMap<Integer, String> chartMap = new HashMap<>();
    LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();

    private AnyChartView polarChart;
    ArrayList<DataEntry> entries = new ArrayList<>();
    Polar polar;
    Set set;
    RevieveDetail revieveDetail;


    List<Summary> summaryList = new ArrayList<>();
    List<Category> categoryList = new ArrayList<>();
    List<String> recommendedProductsList = new ArrayList<>();
    ArrayMap<String, String> categoryMap = new ArrayMap<>();
    ArrayMap<String, String> categoryIDMap = new ArrayMap<>();
    List<RegimeSchedule> regimeScheduleList = new ArrayList<>();
    ResultDetailViewModelTest resultDetailViewModel;
    ResultDetailResponse resultDetailResponse;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(getSharedPreferences().getBoolean(Constants.TABLET, false) ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        /*if (!isTablet())
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);*/

        setContentView(R.layout.activity_analyze_image_result);
        polarChart = (AnyChartView) findViewById(R.id.polarChart);

        polar = AnyChart.polar();
        set = Set.instantiate();
        setActionBarTitle("Your Skin Statistics");

        sharedPreferences = getSharedPreferences(Constants.SP_PREF, Context.MODE_PRIVATE);
        mobileNumberPref = getSharedPreferences().getString(Constants.MOBILE_NUM_PREF, "");
        apiInterface = RetrofitService.getRetrofitForSkinAnalysis().create(APIInterface.class);
        gson = new Gson();

        filePath = sharedPreferences.getString(Constants.FILE_URL, "");
        Log.d(TAG, "Image File Path <<::>> " + filePath);

        //filePath = "/storage/emulated/0/Android/data/in.hng.skinexpert/files/photos/ImgSK20201014123042.jpg";
        apiInterface = RetrofitService.getRetrofit().create(APIInterface.class);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Intent intent = getIntent();
            if (intent.hasExtra(Constants.CUSTOMER_TRANS_ID)) {
                customerTransId = bundle.getString(Constants.CUSTOMER_TRANS_ID);
                isSelfieTaken = bundle.getBoolean(Constants.TAKE_SELFIE);
                advisoryServiceNumber = bundle.getString(Constants.ADVISORY_SERVICE_NUMBER);


                Log.d(TAG, "Is Selfie Taken <<::>> " + isSelfieTaken);
                Log.d(TAG, "customerTransId <<::>> " + customerTransId);
            }
        }

        chartInit();

        imageView = findViewById(R.id.result);
        rvApiResponse = findViewById(R.id.rvApiResponse);
        rlApiResponse = findViewById(R.id.rlApiResponse);
        btnRecommendedProducts = findViewById(R.id.btnRecommendedProducts);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvApiResponse.setLayoutManager(manager);
        rvApiResponse.setNestedScrollingEnabled(false);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvApiResponse);
        rvApiResponse.addItemDecoration(new LinePagerIndicatorDecoration());

        Bitmap myBitmap = BitmapFactory.decodeFile(filePath);
        imageView.setImageBitmap(myBitmap);

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = 2;
// Get the original bitmap from the filepath to which you want to change orientation
// fileName ist the filepath of the image
        Bitmap cachedImage = BitmapFactory.decodeFile(filePath, o2);
        int rotate = getCameraPhotoOrientation(this, filePath);
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        cachedImage = Bitmap.createBitmap(cachedImage, 0, 0, cachedImage.getWidth(), cachedImage.getHeight(), matrix, true);
        imageView.setImageBitmap(cachedImage);

        btnRecommendedProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AnalyzeImageResultActivity.this, ResultDetailsTest.class);

                intent.putExtra(Constants.CUSTOMER_TRANS_ID, customerTransId);
                intent.putExtra(Constants.TAKE_SELFIE, isSelfieTaken);
                intent.putExtra(Constants.ADVISORY_SERVICE_NUMBER,advisoryServiceNumber);
                /*intent.putExtra(Constants.CUSTOMER_TRANS_ID, customerTransId);
                intent.putExtra(Constants.TAKE_SELFIE, isSelfieTaken);
                intent.putExtra(Constants.CHART_DATA, new Gson().toJson(revieveDetail));*/

                startActivity(intent);
            }
        });
       // observeResultDetail();
        //getReviveDetails();

        getPolarChartDataDetails();
    }

    public void drawTriangle(Canvas canvas, Paint paint, int x, int y, int width) {
        int halfWidth = width / 2;

        Path path = new Path();
        path.moveTo(x, y - halfWidth); // Top
        path.lineTo(x - halfWidth, y + halfWidth); // Bottom left
        path.lineTo(x + halfWidth, y + halfWidth); // Bottom right
        path.lineTo(x, y - halfWidth); // Back to Top
        path.close();

        canvas.drawPath(path, paint);
    }



    private void getPolarChartDataDetails() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("custTransId", customerTransId);
            jsonObject.put("phoneNo", mobileNumberPref);
            jsonObject.put("advisoryService", advisoryServiceNumber);

            String data = jsonObject.toString();
            Log.d(TAG, "Revieve Detail request data " + data);

            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), data);

            APIInterface apiInterface = RetrofitService.getRetrofit().create(APIInterface.class);

            Log.d(TAG, "Calling API revieve detail api");


            Call<ResultDetailResponse> call = apiInterface.revieveDetailsTest(requestBody);
            call.enqueue(new Callback<ResultDetailResponse>() {
                @Override
                public void onResponse(Call<ResultDetailResponse> call, Response<ResultDetailResponse> response) {
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                  if (response.code() ==200){

                      resultDetailResponse = gson.fromJson(json, ResultDetailResponse.class);

                      if (resultDetailResponse.getMessage().equalsIgnoreCase("success")) {
                          List<ApiResponse> apiResponselist =   resultDetailResponse.getImageOutput().get(0).getApiResponse();

                          Collections.reverse(apiResponselist);

                          for (ApiResponse apiResponse : apiResponselist) {
                              xValues.add(apiResponse.getDescription());
                              values.add(new BarEntry(count, Float.parseFloat(apiResponse.getValue())));
                              if (apiResponse.getResultColor().isEmpty())
                                  colorList.add(getResources().getColor(R.color.blue));
                              else
                                  colorList.add(Color.parseColor(apiResponse.getResultColor()));

                              if (!apiResponse.getResultDescription().isEmpty()) {
                                  String[] val = apiResponse.getResultDescription().split(":");
                                  if (val.length > 1) {
                                      chartMap.put(count, val[1]);
                                      hashMap.put(apiResponse.getResultColor(), val[1]);
                                  }
                              } else
                                  chartMap.put(count, "Good");

                              Log.d(TAG, "Count<<<::>>> " + count);

                              count++;
                          }
                          Collections.reverse(colorList);
                          // Collections.reverse(xValues);
                          Collections.reverse(values);

                          for (int i : chartMap.keySet()) {
                              Log.d(TAG, "Count " + i);
                              Log.d(TAG, "Value  " + chartMap.get(i));
                          }

                          IMarker marker = new CustomMarkerView(AnalyzeImageResultActivity.this,
                                  R.layout.custom_marker_view_layout, chartMap);
                          chart.setMarker(marker);

                          Log.d(TAG, "values.size " + values.size());
                          Log.d(TAG, "xAxis value size " + xValues.size());

                          //xAxis.setAvoidFirstLastClipping(true);
                          xAxis.setCenterAxisLabels(false);
                          xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
                          xAxis.setLabelCount(xValues.size());

                          BarDataSet set1;

                          if (chart.getData() != null &&
                                  chart.getData().getDataSetCount() > 0) {
                              set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
                              set1.setColors(colorList);
                              set1.setValues(values);
                              chart.getData().notifyDataChanged();
                              chart.notifyDataSetChanged();
                          } else {
                              set1 = new BarDataSet(values, "");
                              set1.setColors(colorList);

                              set1.setDrawIcons(false);

                              ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                              dataSets.add(set1);

                              BarData data = new BarData(dataSets);
                              if (isTablet())
                                  data.setValueTextSize(15);
                              else
                                  data.setValueTextSize(10);
                              //data.setBarWidth(barWidth);
                              chart.setFitBars(true);
                              chart.setData(data);

                              //xAxis.setAxisMinimum(data.getXMin() - .5f);
                              //xAxis.setAxisMaximum(data.getXMax() + .5f);
                          }

                          Legend l = chart.getLegend();
                          if (isTablet())
                              l.setTextSize(15f);
                          else
                              l.setTextSize(12f);
                          l.setTextColor(Color.BLACK);
                          l.setXEntrySpace(15f); // set the space between the legend entries on the x-axis
                          l.setYEntrySpace(15f); // set the space between the legend entries on the y-axis
                          l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                          l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                          l.setOrientation(Legend.LegendOrientation.HORIZONTAL);

                          List<LegendEntry> legendEntryList = new ArrayList<>();
                          for (String str : hashMap.keySet()) {
                              legendEntryList.add(new LegendEntry(hashMap.get(str), Legend.LegendForm.SQUARE,
                                      10f, 1, null, Color.parseColor(str)));
                          }
                          Collections.reverse(legendEntryList);
                          l.setCustom(legendEntryList);

                          chart.invalidate();
                          chart.notifyDataSetChanged();
                          chart.setVisibleXRangeMaximum(10); // allow 20 values to be displayed at once on the x-axis, not more
                          chart.moveViewToX(10);


                          String gsonTest = new Gson().toJson(resultDetailResponse);
                          JSONObject gsonJson = null;
                          try {
                              gsonJson = new JSONObject(gsonTest);
                          } catch (JSONException e) {
                              e.printStackTrace();
                          }
                          Log.d(TAG, "Revieve Detail Response data <<::>> " + gsonJson);

                          Toast.makeText(AnalyzeImageResultActivity.this, resultDetailResponse.getMessage(), Toast.LENGTH_SHORT).show();

                          if (!resultDetailResponse.getImageOutput().get(0).getSkinScoreDescription().isEmpty()) {
                              String skinScoreDescription = resultDetailResponse.getImageOutput().get(0).getSkinScoreDescription();
                              String[] splitData = skinScoreDescription.split("is");
                              String[] val = splitData[1].trim().split("\\s+");

                              Log.w(TAG, "Value 1 <<::>> " + val[0]);
                              Log.w(TAG, "Value 2 <<::>> " + val[1]);

                              ApiResponse apiResponse = new ApiResponse();
                              apiResponse.setResultDescription(splitData[0] + " is : " + val[0]);
                              apiResponse.setValue(val[1]);
                              apiResponse.setResultColor("#36B8E3");

                              apiResponseList.add(apiResponse);
                          }
                          apiResponseList.addAll(resultDetailResponse.getImageOutput().get(0).getApiResponse());
                          apiResponseAdapter = new ApiResponseAdapter(apiResponseList);
                          rvApiResponse.setAdapter(apiResponseAdapter);
                          apiResponseAdapter.notifyDataSetChanged();



              /*  Glide.with(this).load(resultDetailResponse.getImageURL())
                        .placeholder(R.drawable.progress_animation).into(ivImage);*/


                      }else {
                          Toast.makeText(AnalyzeImageResultActivity.this, "Error", Toast.LENGTH_LONG).show();
                      }
                  }





                }

                @Override
                public void onFailure(Call<ResultDetailResponse> call, Throwable t) {
                    Log.e(TAG, "Reveieve Details Exception <<::>> " + t);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Reveieve Details Exception <<::>> " + e);
        }
    }

    private ArrayList getData(String description, int value , String resultColor) {
        // String StaticColor = "#FF4040";
        //Log.d(TAG, "Colour Value <<::>> " + resultColor);
        int value2 = 100 -value;
        /*polar.palette().itemAt(value, new SolidFill(StaticColor, 30));*/

        entries.add(new AnalyzeImageResultActivity.CustomDataEntry(description, value,value2,resultColor));

        return entries;
    }

    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value ,int Value2, String Colour) {
            super(x, value);
            setValue("value", value);
            setValue("value2",Value2);
            setValue("colour",Colour);
            // setValue("value3", value3);


        }
    }
        private void observeResultDetail() {

        try {
            resultDetailViewModel.getResultDetail().observe(this, resultDetailResponse -> {

                Log.d(TAG, "ResultDetail API Response <<::>> " + new Gson().toJson(resultDetailResponse));

                if (resultDetailResponse.getMessage().equalsIgnoreCase("success")) {
                    List<ApiResponse> apiResponselist =   resultDetailResponse.getImageOutput().get(0).getApiResponse();

                    Collections.reverse(apiResponselist);

                    for (ApiResponse apiResponse : apiResponselist) {
                        xValues.add(apiResponse.getDescription());
                        values.add(new BarEntry(count, Float.parseFloat(apiResponse.getValue())));
                        if (apiResponse.getResultColor().isEmpty())
                            colorList.add(getResources().getColor(R.color.blue));
                        else
                            colorList.add(Color.parseColor(apiResponse.getResultColor()));

                        if (!apiResponse.getResultDescription().isEmpty()) {
                            String[] val = apiResponse.getResultDescription().split(":");
                            if (val.length > 1) {
                                chartMap.put(count, val[1]);
                                hashMap.put(apiResponse.getResultColor(), val[1]);
                            }
                        } else
                            chartMap.put(count, "Good");

                        Log.d(TAG, "Count<<<::>>> " + count);

                        count++;
                    }
                    Collections.reverse(colorList);
                    // Collections.reverse(xValues);
                    Collections.reverse(values);

                    for (int i : chartMap.keySet()) {
                        Log.d(TAG, "Count " + i);
                        Log.d(TAG, "Value  " + chartMap.get(i));
                    }

                    IMarker marker = new CustomMarkerView(AnalyzeImageResultActivity.this,
                            R.layout.custom_marker_view_layout, chartMap);
                    chart.setMarker(marker);

                    Log.d(TAG, "values.size " + values.size());
                    Log.d(TAG, "xAxis value size " + xValues.size());

                    //xAxis.setAvoidFirstLastClipping(true);
                    xAxis.setCenterAxisLabels(false);
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
                    xAxis.setLabelCount(xValues.size());

                    BarDataSet set1;

                    if (chart.getData() != null &&
                            chart.getData().getDataSetCount() > 0) {
                        set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
                        set1.setColors(colorList);
                        set1.setValues(values);
                        chart.getData().notifyDataChanged();
                        chart.notifyDataSetChanged();
                    } else {
                        set1 = new BarDataSet(values, "");
                        set1.setColors(colorList);

                        set1.setDrawIcons(false);

                        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                        dataSets.add(set1);

                        BarData data = new BarData(dataSets);
                        if (isTablet())
                            data.setValueTextSize(15);
                        else
                            data.setValueTextSize(10);
                        //data.setBarWidth(barWidth);
                        chart.setFitBars(true);
                        chart.setData(data);

                        //xAxis.setAxisMinimum(data.getXMin() - .5f);
                        //xAxis.setAxisMaximum(data.getXMax() + .5f);
                    }

                    Legend l = chart.getLegend();
                    if (isTablet())
                        l.setTextSize(15f);
                    else
                        l.setTextSize(12f);
                    l.setTextColor(Color.BLACK);
                    l.setXEntrySpace(15f); // set the space between the legend entries on the x-axis
                    l.setYEntrySpace(15f); // set the space between the legend entries on the y-axis
                    l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                    l.setOrientation(Legend.LegendOrientation.HORIZONTAL);

                    List<LegendEntry> legendEntryList = new ArrayList<>();
                    for (String str : hashMap.keySet()) {
                        legendEntryList.add(new LegendEntry(hashMap.get(str), Legend.LegendForm.SQUARE,
                                10f, 1, null, Color.parseColor(str)));
                    }
                    Collections.reverse(legendEntryList);
                    l.setCustom(legendEntryList);

                    chart.invalidate();
                    chart.notifyDataSetChanged();
                    chart.setVisibleXRangeMaximum(10); // allow 20 values to be displayed at once on the x-axis, not more
                    chart.moveViewToX(10);


                    String gson = new Gson().toJson(resultDetailResponse);
                    JSONObject gsonJson = null;
                    try {
                        gsonJson = new JSONObject(gson);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "Revieve Detail Response data <<::>> " + gsonJson);

                    Toast.makeText(AnalyzeImageResultActivity.this, resultDetailResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    if (!resultDetailResponse.getImageOutput().get(0).getSkinScoreDescription().isEmpty()) {
                        String skinScoreDescription = resultDetailResponse.getImageOutput().get(0).getSkinScoreDescription();
                        String[] splitData = skinScoreDescription.split("is");
                        String[] val = splitData[1].trim().split("\\s+");

                        Log.w(TAG, "Value 1 <<::>> " + val[0]);
                        Log.w(TAG, "Value 2 <<::>> " + val[1]);

                        ApiResponse apiResponse = new ApiResponse();
                        apiResponse.setResultDescription(splitData[0] + " is : " + val[0]);
                        apiResponse.setValue(val[1]);
                        apiResponse.setResultColor("#36B8E3");

                        apiResponseList.add(apiResponse);
                    }
                    apiResponseList.addAll(resultDetailResponse.getImageOutput().get(0).getApiResponse());
                    apiResponseAdapter = new ApiResponseAdapter(apiResponseList);
                    rvApiResponse.setAdapter(apiResponseAdapter);
                    apiResponseAdapter.notifyDataSetChanged();



              /*  Glide.with(this).load(resultDetailResponse.getImageURL())
                        .placeholder(R.drawable.progress_animation).into(ivImage);*/


                }


            });

            resultDetailViewModel.getError().observe(this, isError -> {
                if (isError != null) if (isError) {
                    Log.w(TAG, "An Error Occurred While Loading Data <<::>>");
               /* tvErrorMessage.setVisibility(View.VISIBLE);
                tvErrorMessage.setText(getResources().getString(R.string.error));*/
                } else {
                    Log.w(TAG, "No Errors");
               /* tvErrorMessage.setVisibility(View.GONE);
                tvErrorMessage.setText(null);*/
                }
            });

            resultDetailViewModel.getLoading().observe(this, isLoading -> {
                Log.d(TAG, "Progress Bar loading <<::>> " + isLoading);
                if (isLoading != null) {
              /*  progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if (isLoading) {
                    tvErrorMessage.setVisibility(View.GONE);
                }*/
                }
            });
        }catch (Exception e){
            e.getMessage();
        }





    }



   /* private void getReviveDetails() {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("custTransId", customerTransId);

            String data = jsonObject.toString();
            Log.d(TAG, "Revieve Detail request data " + data);

            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), data);

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
                                revieveDetail = value;

                                //count = value.getApiResponse().size();
                                count = 0;
                                rlApiResponse.setVisibility(View.GONE);
                                xValues.clear();
                                colorList.clear();
                                chartMap.clear();


                                //chart.getDescription().setText(value.getSkinScoreDescription());
                                //chart.getDescription().setPosition(3f, 3f);
                                List<ApiResponse> apiResponselist = value.getApiResponse();
                                Collections.reverse(apiResponselist);

                                for (ApiResponse apiResponse : apiResponselist) {
                                    xValues.add(apiResponse.getDescription());
                                    values.add(new BarEntry(count, Float.parseFloat(apiResponse.getValue())));
                                    if (apiResponse.getResultColor().isEmpty())
                                        colorList.add(getResources().getColor(R.color.blue));
                                    else
                                        colorList.add(Color.parseColor(apiResponse.getResultColor()));

                                    if (!apiResponse.getResultDescription().isEmpty()) {
                                        String[] val = apiResponse.getResultDescription().split(":");
                                        if (val.length > 1) {
                                            chartMap.put(count, val[1]);
                                            hashMap.put(apiResponse.getResultColor(), val[1]);
                                        }
                                    } else
                                        chartMap.put(count, "Good");

                                    Log.d(TAG, "Count<<<::>>> " + count);

                                    count++;
                                }
                                Collections.reverse(colorList);
                                // Collections.reverse(xValues);
                                Collections.reverse(values);

                                for (int i : chartMap.keySet()) {
                                    Log.d(TAG, "Count " + i);
                                    Log.d(TAG, "Value  " + chartMap.get(i));
                                }

                                IMarker marker = new CustomMarkerView(AnalyzeImageResultActivity.this,
                                        R.layout.custom_marker_view_layout, chartMap);
                                chart.setMarker(marker);

                                Log.d(TAG, "values.size " + values.size());
                                Log.d(TAG, "xAxis value size " + xValues.size());

                                //xAxis.setAvoidFirstLastClipping(true);
                                xAxis.setCenterAxisLabels(false);
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
                                xAxis.setLabelCount(xValues.size());

                                BarDataSet set1;

                                if (chart.getData() != null &&
                                        chart.getData().getDataSetCount() > 0) {
                                    set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
                                    set1.setColors(colorList);
                                    set1.setValues(values);
                                    chart.getData().notifyDataChanged();
                                    chart.notifyDataSetChanged();
                                } else {
                                    set1 = new BarDataSet(values, "");
                                    set1.setColors(colorList);

                                    set1.setDrawIcons(false);

                                    ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                                    dataSets.add(set1);

                                    BarData data = new BarData(dataSets);
                                    if (isTablet())
                                        data.setValueTextSize(15);
                                    else
                                        data.setValueTextSize(10);
                                    //data.setBarWidth(barWidth);
                                    chart.setFitBars(true);
                                    chart.setData(data);

                                    //xAxis.setAxisMinimum(data.getXMin() - .5f);
                                    //xAxis.setAxisMaximum(data.getXMax() + .5f);
                                }

                                Legend l = chart.getLegend();
                                if (isTablet())
                                    l.setTextSize(15f);
                                else
                                    l.setTextSize(12f);
                                l.setTextColor(Color.BLACK);
                                l.setXEntrySpace(15f); // set the space between the legend entries on the x-axis
                                l.setYEntrySpace(15f); // set the space between the legend entries on the y-axis
                                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);

                                List<LegendEntry> legendEntryList = new ArrayList<>();
                                for (String str : hashMap.keySet()) {
                                    legendEntryList.add(new LegendEntry(hashMap.get(str), Legend.LegendForm.SQUARE,
                                            10f, 1, null, Color.parseColor(str)));
                                }
                                Collections.reverse(legendEntryList);
                                l.setCustom(legendEntryList);

                                chart.invalidate();
                                chart.notifyDataSetChanged();
                                chart.setVisibleXRangeMaximum(10); // allow 20 values to be displayed at once on the x-axis, not more
                                chart.moveViewToX(10);


                                String gson = new Gson().toJson(value);
                                JSONObject gsonJson = new JSONObject(gson);
                                Log.d(TAG, "Revieve Detail Response data <<::>> " + gsonJson);

                                Toast.makeText(AnalyzeImageResultActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();

                                if (!value.getSkinScoreDescription().isEmpty()) {
                                    String skinScoreDescription = value.getSkinScoreDescription();
                                    String[] splitData = skinScoreDescription.split("is");
                                    String[] val = splitData[1].trim().split("\\s+");

                                    Log.w(TAG, "Value 1 <<::>> " + val[0]);
                                    Log.w(TAG, "Value 2 <<::>> " + val[1]);

                                    ApiResponse apiResponse = new ApiResponse();
                                    apiResponse.setResultDescription(splitData[0] + " is : " + val[0]);
                                    apiResponse.setValue(val[1]);
                                    apiResponse.setResultColor("#36B8E3");

                                    apiResponseList.add(apiResponse);
                                }
                                apiResponseList.addAll(value.getApiResponse());
                                apiResponseAdapter = new ApiResponseAdapter(apiResponseList);
                                rvApiResponse.setAdapter(apiResponseAdapter);
                                apiResponseAdapter.notifyDataSetChanged();


                            } catch (Exception ex) {
                                ex.printStackTrace();
                                Log.e(TAG, "Revieve detail onNext Exception " + ex);
                                Toast.makeText(AnalyzeImageResultActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            HttpException error = (HttpException) e;
                            String errorBody = Objects.requireNonNull(error.response().errorBody()).toString();
                            Log.d(TAG, "Exception e <<::>> " + e);
                            Log.d(TAG, "errorBody <<::>> " + errorBody);
                            Toast.makeText(AnalyzeImageResultActivity.this, errorBody, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Reveieve Details Exception <<::>> " + e);
        }
    }*/

    private void chartInit() {
        chart = findViewById(R.id.chart1);
        chart.setOnChartValueSelectedListener(this);


        polarChart = (AnyChartView) findViewById(R.id.polarChart);

        polar = AnyChart.polar();
        set = Set.instantiate();
        // chart.setHighlightEnabled(false);

        chart.setDrawBarShadow(false);

        chart.setDrawValueAboveBar(true);

        chart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        //chart.setMaxVisibleValueCount(10);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);

        // draw shadows for each bar that show the maximum value
        // chart.setDrawBarShadow(true);

        chart.setDrawGridBackground(false);
        //chart.setViewPortOffsets(60, 0, 50, 60);

        xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xl.setTypeface(tfLight);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        if (isTablet())
            xAxis.setTextSize(14);
        else
            xAxis.setTextSize(11);
        xAxis.setGranularity(1f);

        YAxis yl = chart.getAxisLeft();
        //yl.setTypeface(tfLight);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);

        YAxis yr = chart.getAxisRight();
        //yr.setTypeface(tfLight);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yr.setInverted(true);

        chart.setTouchEnabled(true);
        /*CustomMarkerView mv = new CustomMarkerView(this, R.layout.custom_marker_view_layout);
        chart.setMarkerView(mv);*/

        chart.setHighlightPerTapEnabled(true);

        chart.setFitBars(true);
        chart.animateY(2500);
        chart.getLegend().setEnabled(true);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);
    }

    private int getCameraPhotoOrientation(Context context, String imagePath) {
        int rotate = 0;
        try {
            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(
                    imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        spe = sharedPreferences.edit();
        spe.remove(Constants.FILE_URL);
        spe.apply();
    }



    @Override
    public void onValueSelected(Entry e, Highlight h) {
        float x = h.getX();
        Log.i("Highlighted", "Actual highlight: " + x);

        //Getting the same exception as above
        //combinedChart.highlightValue(x, 0, false);

        //Does not works, draws to x=0 position with any given x
        chart.highlightValue(chart.getHighlighter().getHighlight(x, 0));
    }

    @Override
    public void onNothingSelected() {

    }
}
