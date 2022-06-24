package com.example.advisoryservice.ui.result;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.advisoryservice.R;
import com.example.advisoryservice.data.model.revieveDetail.ApiResponse;
import com.example.advisoryservice.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ApiResponseAdapter extends RecyclerView.Adapter<ApiResponseAdapter.ItemViewHolder> {

    public static final String TAG = ApiResponseAdapter.class.getSimpleName();

    List<ApiResponse> apiResponses = new ArrayList<>();

    public ApiResponseAdapter(List<ApiResponse> apiResponses) {
        this.apiResponses = apiResponses;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.analyze_image_response_item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Log.d(TAG, "apiResponses " + apiResponses.size());
        holder.bind(apiResponses.get(position));
    }

    @Override
    public int getItemCount() {
        return apiResponses.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvDescription, tvValue;
        ProgressBar circularProgressBar;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvValue = itemView.findViewById(R.id.tvValue);
            circularProgressBar = itemView.findViewById(R.id.circularProgressBar);
        }

        void bind(ApiResponse apiResponse) {


            if (!apiResponse.getResultDescription().isEmpty()) {

                String[] word = apiResponse.getResultDescription().split(":");

                int len = 0;
                Log.d(TAG, "Word Split length " + word.length);
                if (word.length > 1) {
                    len = apiResponse.getResultDescription().length() - word[1].length();

                    /*String sourceString = word[0] + ":</br>"+ "<p style=color:#2196F3><b>" + word[1] + "</b></p> ";
                    tvDescription.setText(Html.fromHtml(sourceString));*/

                    Spannable wordtoSpan = new SpannableString(apiResponse.getResultDescription());

                    wordtoSpan.setSpan(new ForegroundColorSpan(Color.parseColor(apiResponse.getResultColor())),
                            len, apiResponse.getResultDescription().length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    wordtoSpan.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                            len, apiResponse.getResultDescription().length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    tvDescription.setText(wordtoSpan);

                } else
                    tvDescription.setText(apiResponse.getResultDescription());

            } else
                tvDescription.setText(apiResponse.getDescription());

            LayerDrawable progressBarDrawable = (LayerDrawable) circularProgressBar.getProgressDrawable();
            Drawable backgroundDrawable = progressBarDrawable.getDrawable(0);
            Drawable progressDrawable = progressBarDrawable.getDrawable(1);
            if (!apiResponse.getResultColor().isEmpty())
                DrawableCompat.setTint(progressDrawable, Color.parseColor(apiResponse.getResultColor()));
            else
                DrawableCompat.setTint(progressDrawable, Color.parseColor("#088AB5"));

            circularProgressBar.setProgress(Integer.parseInt(apiResponse.getValue()));
            tvValue.setText(apiResponse.getValue());
        }

    }

    static private String addChar(String str, char ch, int position) {
        return str.substring(0, position) + ch + str.substring(position);
    }
}

