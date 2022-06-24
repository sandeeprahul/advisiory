package com.example.advisoryservice.ui.customerdetail;

import android.content.Context;
import android.graphics.Color;
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
import com.example.advisoryservice.ui.selectedadvisory.AdvisorySelectionAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class AgeSelectionAdapter extends RecyclerView.Adapter<AgeSelectionAdapter.MyViewHolder> {
    public static final String TAG = AdvisorySelectionAdapter.class.getSimpleName();
    private LayoutInflater inflater;
    private ArrayList<String> ageListItems;
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
    private AgeSelectionAdapter.ClickListener clickListener;
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
    TextView ageText;
    int row_index = -1;

    public AgeSelectionAdapter(Context ctx, ArrayList<String> ageListItems, ClickListener clickListener) {


        inflater = LayoutInflater.from(ctx);
        this.ageListItems = ageListItems;

        this.context = ctx;
        this.clickListener = clickListener;

    }


    @Override
    public AgeSelectionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.age_selection_list_items, parent, false);
        AgeSelectionAdapter.MyViewHolder holder = new AgeSelectionAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(AgeSelectionAdapter.MyViewHolder holder, int position) {


        ageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position ==2){
                    ageText.setBackgroundColor(context.getResources().getColor(R.color.orange));
                }
            }
        });

        ageText.setText(ageListItems.get(position));

        // advisorySelectionData.setText(serviceDataModel.get(position).getServiceId());
        //  Glide.with(context).load(serviceDataModel.get(position).getImageUrl()).into(advisorySelectionImage);


    }


    @Override
    public int getItemCount() {
        return ageListItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        EditText userFeedback;

        public MyViewHolder(View itemView) {
            super(itemView);
            ageText = (TextView) itemView.findViewById(R.id.age_text);

        }

    }


    public interface ClickListener {
        void selectedageAdvisory(String ageSelection);

    }
}
