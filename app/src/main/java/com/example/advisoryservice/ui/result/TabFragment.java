package com.example.advisoryservice.ui.result;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Polar;
import com.anychart.core.axes.Linear;
import com.anychart.core.polar.series.Column;
import com.anychart.data.Mapping;
import com.anychart.enums.PolarSeriesType;
import com.anychart.enums.ScaleStackMode;
import com.anychart.enums.ScaleTypes;
import com.anychart.graphics.vector.SolidFill;
import com.example.advisoryservice.R;
import com.example.advisoryservice.data.model.revieveDetail.ApiResponse;
import com.example.advisoryservice.data.model.revieveDetail.RevieveDetail;
import com.example.advisoryservice.data.model.testresult.Summary;
import com.example.advisoryservice.util.Log;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Set;

public class TabFragment extends Fragment {

    public static final String TAG = TabFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "position";
    private static final Boolean ARG_PARAM3 = false;
    private static final String ARG_PARAM4 = "customerId";
    private static final String ARG_PARAM5 = "chartData";


    // TODO: Rename and change types of parameters
    private String description;

    private int position;

    private TextView tvDescription, test;
    private Summary summary;
    private Spanned message;
    private RevieveDetail reviveData;

    private boolean isTakenSelfie = false;
    private String customerTransId,chartData;

    private AnyChartView polarChart;
    ArrayList<DataEntry> entries = new ArrayList<>();
    Polar polar;
    Set set;
    Gson gson;
    Column series1,series2;
    Mapping series1Data,series2Data;

    public TabFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TabFragment newInstance(Summary summary, int position, String custTransId, boolean isSelfieTaken, RevieveDetail revieveDetail) {
        TabFragment fragment = new TabFragment();
        Bundle args = new Bundle();

        Gson gson = new Gson();
        String json = gson.toJson(summary);
        String chartData = gson.toJson(revieveDetail);

        args.putString(ARG_PARAM1, json);
        args.putInt(ARG_PARAM2, position);
        args.putBoolean(String.valueOf(ARG_PARAM3), isSelfieTaken);
        args.putString(ARG_PARAM4, custTransId);
        args.putString(ARG_PARAM5, chartData);

        /*args.putString(ARG_PARAM3, custTransId);*/
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            // How to retrieve your Java object back from the string
            description = getArguments().getString(ARG_PARAM1);
            position = getArguments().getInt(ARG_PARAM2);
            isTakenSelfie = getArguments().getBoolean(String.valueOf(ARG_PARAM3));
            customerTransId = getArguments().getString(ARG_PARAM4);
            chartData =  getArguments().getString(ARG_PARAM5);
            gson = new Gson();
            reviveData = gson.fromJson(chartData, RevieveDetail.class);




            Log.d(TAG, "Description <<::>> " + chartData);
            Log.d(TAG, "Description <<::>> " + description);
            Log.d(TAG, "Position <<::>> " + position);

            summary = gson.fromJson(description, Summary.class);


        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab, container, false);

        tvDescription = view.findViewById(R.id.tvDescription);
        polarChart = (AnyChartView) view.findViewById(R.id.polarChart);


        polar = AnyChart.polar();
       // set = Set.instantiate();
        // set.data(getDataTest());
        //  getPolarChartDataDetails();
        //     getPolarChartData();
        if (position == 0 && isTakenSelfie) {
            polarChart.setVisibility(View.GONE);
            // getPolarChartDataDetails();

            // getPolarChartData();
            ArrayList<DataEntry> ChartData = new ArrayList<>();
            polar.removeAllSeries();
            ChartData.clear();



            if (reviveData!=null){
                for (ApiResponse apiResponse :reviveData.getApiResponse() ) {


                    ArrayList<DataEntry> entries = new ArrayList<>();
                    entries = getData(apiResponse.getDescription(), Integer.parseInt(apiResponse.getValue()));

                    ChartData.add(entries.get(0));
                    entries.clear();
                    //Create the ChartData in single collection (10 rows)
                    //set.data(getData(apiResponse.getDescription(), Integer.parseInt(apiResponse.getValue()), apiResponse.getResultColor()));
                }

                Log.e(TAG, "RevieveDetail Response Message <<::>> " + ChartData.size());
              /*  set.data(ChartData);


                series1Data = set.mapAs("{ x: 'x', value: 'value2' }");
                series2Data = set.mapAs("{ x: 'x', value: 'value' }");*/





                polar.legend().position("bottom");
                polar.legend().align("left");
                polar.legend().itemsLayout("vertical");

                series1 = polar.column(series1Data);
                series1.name("Areas that require attention");
                series2 = polar.column(series2Data);
                series2.name("Areas that do not require immediate attention");

               /* polar.column(series1Data);
               polar.column(series2Data);*/
                // polar.column(series3Data);
                polar.legend().enabled(true);
                polar.legend().align("left");
                polar.tooltip().background().fill("#0000FF");
                for (int i = 0; i < ChartData.size(); i++) {

                    if (i == 0) {
                        polar.palette().itemAt(i, new SolidFill("#FF4040", 30));

                        // polar.tooltip().background().fill("#FF4040");

                    } else if (i == ChartData.size()) {
                        polar.palette().itemAt(i, new SolidFill("#0000FF", 30));

                        // polar.tooltip().background().fill("#0000FF");
                    }
                }

                polar.sortPointsByX(true)
                        .defaultSeriesType(PolarSeriesType.COLUMN)
                        .yAxis(false)
                        .xScale(ScaleTypes.ORDINAL);
                polar.title().margin().bottom(20d);
                ((com.anychart.scales.Linear) polar.yScale(Linear.class)).stackMode(ScaleStackMode.VALUE);
                polar.yScale().minimum(0);
              /*  polar.tooltip()
                        .valuePrefix("")
                        .displayMode(TooltipDisplayMode.UNION);*/

                polarChart.setChart(polar);
                Log.w(TAG, "ChartVisible <<::>> " + position);


            } else {
                polarChart.setVisibility(View.GONE);
                Log.w(TAG, "ChartGone <<::>> " + position);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            message = Html.fromHtml(summary.getDescription(), Html.FROM_HTML_MODE_COMPACT);
        } else {
            message = Html.fromHtml(summary.getDescription());
        }
        tvDescription.setText(message);
        Log.w(TAG, "Message <<::>> " + summary.getDescription());


        return view;
    }


    private ArrayList getData(String description, int value) {
        // String StaticColor = "#FF4040";
        //Log.d(TAG, "Colour Value <<::>> " + resultColor);
        // polar.tooltip().background().fill(resultColor);
        int value2 = 100 - value;


        /*polar.palette().itemAt(value, new SolidFill(StaticColor, 30));*/
        entries.add(new CustomDataEntry(description, value, value2));

        return entries;
    }

    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value, int Value2) {
            super(x, value);
            setValue("value", value);
            setValue("value2", Value2);
            // setValue("colour", Colour);
            // setValue("value3", value3);


        }
    }

}

