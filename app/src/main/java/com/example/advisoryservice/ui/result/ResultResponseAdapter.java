package com.example.advisoryservice.ui.result;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.advisoryservice.R;
import com.example.advisoryservice.data.model.revieveDetail.ResultResponse;
import com.example.advisoryservice.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ResultResponseAdapter extends RecyclerView.Adapter<ResultResponseAdapter.ItemViewHolder> {

    public static final String TAG = ResultResponseAdapter.class.getSimpleName();

    List<ResultResponse> resultResponses = new ArrayList<>();

    public ResultResponseAdapter(List<ResultResponse> resultResponses) {
        this.resultResponses = resultResponses;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.revieve_detail_item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Log.d(TAG, "resultResponses " + resultResponses.size());
        holder.bind(resultResponses.get(position));
    }

    @Override
    public int getItemCount() {
        return resultResponses.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvDescription, tvValue;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvValue = itemView.findViewById(R.id.tvValue);
        }

        void bind(ResultResponse resultResponse) {
            tvDescription.setText(resultResponse.getDescription());
            tvValue.setText(resultResponse.getValue());
        }
    }
}
