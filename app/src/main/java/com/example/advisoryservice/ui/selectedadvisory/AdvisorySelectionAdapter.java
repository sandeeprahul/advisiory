package com.example.advisoryservice.ui.selectedadvisory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.advisoryservice.R;
import com.example.advisoryservice.data.model.Service;
import com.example.advisoryservice.data.model.questions.SubQuestion;
import com.example.advisoryservice.data.model.questions.SubQuestionOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class AdvisorySelectionAdapter extends RecyclerView.Adapter<AdvisorySelectionAdapter.MyViewHolder>  {
    public static final String TAG = AdvisorySelectionAdapter.class.getSimpleName();
    private LayoutInflater inflater;
    private List<Service> serviceDataModel;
    LinkedHashMap<String, String> optionMap = new LinkedHashMap<>();
    HashMap<String, SubQuestionOption> subQuestionOptionHashMap = new HashMap<>();
    RadioGroup radioGroup;
    //EditText userFeedback;
    Context context;
    String gender = "", custTransId, locationCode, selectedFeedbackID, feedbackData = "", errorBody, successData = "";
    int questionCnt = 1;
    HashMap<String, String> feedbackSubOptionMap = new HashMap<>();
    public int errorCode;
    HashMap<String, String> prodHashMap = new HashMap<String, String>();
    private ClickListener clickListener;
    List<SubQuestion> questions;
    String radioOptionID;
    String questionId;
    int position;
    String radioButtonCheckValidation;
    int radioBtnChecked;
    //ArrayList<String> selectedProducts = new ArrayList<>();
    HashMap<String, String> selectedProducts = new HashMap<>();
    HashMap<String, String> remarkMap = new HashMap<>();

    AppCompatRadioButton radioButton;
    ArrayList<String> rbTest;
    String questionTest;

    int indexCnt = 0;
   ImageView advisorySelectionImage;

    public AdvisorySelectionAdapter(Context ctx, List<Service> serviceDataModel,ClickListener clickListener) {


        inflater = LayoutInflater.from(ctx);
        this.serviceDataModel = serviceDataModel;

        this.context = ctx;
        this.clickListener = clickListener;

    }


    @Override
    public AdvisorySelectionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.advisory_selection_image_items, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(AdvisorySelectionAdapter.MyViewHolder holder, int position) {

       // advisorySelectionData.setText(serviceDataModel.get(position).getServiceId());
        Glide.with(context).load(serviceDataModel.get(position).getImageUrl()).into(advisorySelectionImage);

        advisorySelectionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.selectedadvisory(serviceDataModel.get(position).getServiceId(),serviceDataModel.get(position).getImageUrl());
            }
        });

    }



    @Override
    public int getItemCount() {
        return serviceDataModel.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        EditText userFeedback;

        public MyViewHolder(View itemView) {
            super(itemView);
            advisorySelectionImage = (ImageView) itemView.findViewById(R.id.advisory_selection_image);

        }

    }


    public interface ClickListener {
        void selectedadvisory(String serviceId, String serviceName);

    }
}

