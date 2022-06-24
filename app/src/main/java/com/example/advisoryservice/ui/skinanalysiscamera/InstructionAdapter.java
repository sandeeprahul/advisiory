package com.example.advisoryservice.ui.skinanalysiscamera;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.advisoryservice.R;
import com.example.advisoryservice.data.model.instructionDetail.ImageDetail;

import java.util.ArrayList;
import java.util.List;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.ItemViewHolder> {

    private static final String TAG = InstructionAdapter.class.getSimpleName();

    List<ImageDetail> imageDetailList = new ArrayList<>();

    public InstructionAdapter(List<ImageDetail> imageDetailList) {
        this.imageDetailList = imageDetailList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.instruction_item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(imageDetailList.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return imageDetailList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView tvInstructionName;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.ivImage);
            tvInstructionName = itemView.findViewById(R.id.tvInstructionName);
        }

        void bind(ImageDetail imageDetail) {
            Glide.with(itemView.getContext()).load(imageDetail.getImageUrl()).into(ivImage);
            tvInstructionName.setText(imageDetail.getInstructionName());
        }
    }
}

