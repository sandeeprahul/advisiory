package com.example.advisoryservice.ui.result;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.advisoryservice.R;
import com.example.advisoryservice.data.model.revieveDetail.MappingResponse;
import com.example.advisoryservice.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MappingResponseAdapter extends RecyclerView.Adapter<MappingResponseAdapter.ItemViewHolder> {

    public static final String TAG = MappingResponseAdapter.class.getSimpleName();

    List<MappingResponse> mappingResponses = new ArrayList<>();

    public MappingResponseAdapter(List<MappingResponse> mappingResponses) {
        this.mappingResponses = mappingResponses;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.revieve_detail_item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Log.d(TAG, "MappingResponseAdapter " + mappingResponses.size());
        holder.bind(mappingResponses.get(position));
    }

    @Override
    public int getItemCount() {
        return mappingResponses.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvDescription, tvValue;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvValue = itemView.findViewById(R.id.tvValue);
        }

        void bind(MappingResponse mappingResponse) {
            tvDescription.setText(mappingResponse.getDescription());
            tvValue.setText(mappingResponse.getValue());
        }

    }
}
