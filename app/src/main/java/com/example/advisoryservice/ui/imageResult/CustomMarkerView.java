package com.example.advisoryservice.ui.imageResult;

import android.content.Context;
import android.widget.TextView;

import com.example.advisoryservice.R;
import com.example.advisoryservice.util.Log;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.HashMap;

public class CustomMarkerView extends MarkerView {
    private TextView tvContent;

    HashMap<Integer, String> chartData;

    public CustomMarkerView(Context context, int layoutResource, HashMap<Integer, String> data) {
        super(context, layoutResource);

        this.chartData = data;
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
// content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        Log.d("CustomMarkerView", "X value " + e.getX());
        int val = Integer.parseInt(String.format("%.0f", e.getX()));
        Log.d("CustomMarkerView", "Integer val " + val);

        tvContent.setText(chartData.get(val));

        super.refreshContent(e, highlight);   // this will perform necessary layouting
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {
        if (mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }
        return mOffset;
    }

    /*@Override
    public int getXOffset(float xpos) {
        // this will center the marker-view horizontally
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset(float ypos) {
        // this will cause the marker-view to be above the selected value
        return -getHeight();
    }*/
}