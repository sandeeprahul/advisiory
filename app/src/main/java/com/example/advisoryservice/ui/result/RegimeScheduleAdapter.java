package com.example.advisoryservice.ui.result;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.advisoryservice.R;
import com.example.advisoryservice.data.model.testresult.RegimeSchedule;
import com.example.advisoryservice.data.model.testresult.RegimeSubCategory;

import java.util.List;

public class RegimeScheduleAdapter extends RecyclerView.Adapter<RegimeScheduleAdapter.ItemViewHolder> {

    public static final String TAG = RegimeScheduleAdapter.class.getSimpleName();

    private List<RegimeSchedule> regimeScheduleList;
    private Context context;

    RegimeScheduleAdapter(Context context, List<RegimeSchedule> regimeScheduleList) {
        this.context = context;
        this.regimeScheduleList = regimeScheduleList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_header_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {

        holder.tvHeaderText.setText(regimeScheduleList.get(position).getRegimeCategoryName());
        holder.tvHeaderText.setBackgroundColor(Color.parseColor(regimeScheduleList.get(position).getHeaderColorCode()));

        if (position == 2)
            holder.tvHeaderText.setTextColor(Color.parseColor("#FFFFFF"));
        else
            holder.tvHeaderText.setTextColor(Color.parseColor("#000000"));

        for (RegimeSubCategory data : regimeScheduleList.get(position).getRegimeSubCategory()) {

            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.regime_schedule_list_item, null);

            LinearLayout llScheduleLayout = v.findViewById(R.id.llScheduleLayout);
            TextView tvCategoryId = v.findViewById(R.id.tvCategoryId);
            TextView tvCategoryName = v.findViewById(R.id.tvCategoryName);
            TextView tvCategoryImage = v.findViewById(R.id.tvCategoryImage);

            llScheduleLayout.setBackgroundColor(Color.parseColor(regimeScheduleList.get(position).getSubSectionColorCode()));
            tvCategoryId.setText(data.getRegimeSubCategoryId());
            tvCategoryName.setText(data.getRegimeSubCategoryName());

            Spanned message;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                message = Html.fromHtml(data.getImageUrl(), Html.FROM_HTML_MODE_COMPACT);
            } else {
                message = Html.fromHtml(data.getImageUrl());
            }
            tvCategoryImage.setText(message);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            holder.llLayout.addView(v, layoutParams);
        }

    }

    @Override
    public int getItemCount() {
        return regimeScheduleList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvHeaderText;
        LinearLayout llLayout;

        ItemViewHolder(View view) {
            super(view);
            tvHeaderText = view.findViewById(R.id.tvHeaderText);
            llLayout = view.findViewById(R.id.llLayout);
        }

    }
}
