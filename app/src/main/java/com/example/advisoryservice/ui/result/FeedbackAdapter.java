package com.example.advisoryservice.ui.result;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.advisoryservice.R;
import com.example.advisoryservice.data.model.feedback.Answer;
import com.example.advisoryservice.data.model.questions.SubQuestion;
import com.example.advisoryservice.data.model.questions.SubQuestionOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ItemViewHolder> {

    private final List<Answer> answerList = new ArrayList<>();
    private Context context;
    private ClickListener clickListener;
    List<SubQuestion> subQuestions;
    public static TextView textView;
    public static Dialog dialog;
    private Button dialogBtn;
    private String[] myImageNameList = new String[]{"Benz", "Bike",
            "Car","Carrera"
            ,"Ferrari","Harly",
            "Lamborghini","Silver"};
    int questionCnt = 1;
    LinkedHashMap<String, String> optionMap = new LinkedHashMap<>();
    HashMap<String, SubQuestionOption> subQuestionOptionHashMap = new HashMap<>();
    RadioGroup radioGroup;


    FeedbackAdapter(FeedbackViewModel viewModel, LifecycleOwner lifecycleOwner, Context context, ClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
        viewModel.getFeedbackDetail().observe(lifecycleOwner, details -> {
            answerList.clear();
            if (details != null) {
                answerList.addAll(details.getData().get(0).getAnswer());
                notifyDataSetChanged();
            }
        });
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        holder.bind(answerList.get(holder.getAdapterPosition()));
        subQuestions =   answerList.get(position).getSubQuestion();

        holder.llFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  if (answerList.get(position).getOptionsName().equalsIgnoreCase("Average")){
                    showRecyclerDialog(context,subQuestions);

                }

                else if (answerList.get(position).getOptionsName().equalsIgnoreCase("Poor")){
                    showRecyclerDialog(context,subQuestions);
                }*/
                clickListener.insertFeedback(answerList.get(position));

                // clickListener.insertFeedback(answerList.get(position));
            }
        });
    }


  /*  public void showRecyclerDialog(Context context, List<SubQuestion> subQuestions){

        dialog = new Dialog(context);
         dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.feedback_radio_option_list);

        Button btnNext = (Button) dialog.findViewById(R.id.btnNext);
        Button btnSubmit = (Button) dialog.findViewById(R.id.btndialog);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        TextView   tvQuestion = dialog.findViewById(R.id.tvQuestion);

        if (questionCnt <= subQuestions.size()) {
            tvQuestion.setText(subQuestions.get(questionCnt -1).getSubQuestion());

        }

        List<SubQuestionOption> subQuestionOptions = subQuestions.get(questionCnt -1).getSubQuestionOptions();

        for (SubQuestionOption option : subQuestionOptions) {



            optionMap.put(option.getSubOptionId(), option.getSubOptionsName());
            subQuestionOptionHashMap.put(option.getSubOptionId(), option);
        }

       // addRadioButtons(optionMap);




        RecyclerView recyclerView = dialog.findViewById(R.id.recycler);
        FeedbackRadioAdater adapterRe = new FeedbackRadioAdater(this.context, subQuestions);
        recyclerView.setAdapter(adapterRe);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false));

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dialog.show();

    }*/

    private void addRadioButtons(HashMap<String, String> optionMap) {

        String selectedId = "";
        // isChecked = false;
        //  Log.w(TAG, "Question Number <<::>> " + questionNum);

        radioGroup.setOrientation(LinearLayout.VERTICAL);

        for (String id : optionMap.keySet()) {

            AppCompatRadioButton radioButton = new AppCompatRadioButton(context);
            radioButton.setId(Integer.parseInt(id));
            radioButton.setText(optionMap.get(id));
            radioButton.setCompoundDrawablePadding(10);
            /*if (questionsModel.getData().get(questionCnt - 1).getIsInfoFlag().equalsIgnoreCase("Y") && questionNum != 1)
                radioButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.info, 0);*/
            //radioButton.setOnClickListener(context);

            /*if (backPressData.containsKey(String.valueOf(questionNum))) {

                Log.w(TAG, "QuestionAnswer contains questionNumber " + questionNum);
                if (Objects.requireNonNull(backPressData.get(String.valueOf(questionNum))).equalsIgnoreCase(allOptionMap.get(id).getOptionsValue())) {

                    Log.w(TAG, "Selected ID <<::>> " + id);
                    selectedId = id;
                    radioButton.setChecked(true);
                    isChecked = true;
                } else
                    radioButton.setChecked(false);
            }*/

            radioGroup.addView(radioButton);
        }

   /*     btnNext.setEnabled(isChecked);
        btnNext.setBackgroundColor(isChecked ? getResources().getColor(R.color.colorAccent) :
                Color.parseColor("#808185"));
        if (isChecked)
            Glide.with(this).load(imageMap.get(selectedId))
                    .placeholder(getCircularProgressDrawable()).into(ivImage);*/

    }

    @Override
    public int getItemCount() {
        return answerList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView imageview;
        TextView tvFeedback;
        LinearLayout llFeedback;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageview = itemView.findViewById(R.id.imageSmiley);
            tvFeedback = itemView.findViewById(R.id.tvFeedback);
            llFeedback = itemView.findViewById(R.id.llFeedback);
        }

        void bind(Answer imageDetail) {
            //this.imageDetail = imageDetail;
            tvFeedback.setText(imageDetail.getOptionsName());
            if (imageDetail.getOptionsName().equalsIgnoreCase("poor"))
                imageview.setImageResource(R.drawable.ic_poor);
            else if (imageDetail.getOptionsName().equalsIgnoreCase("average"))
                imageview.setImageResource(R.drawable.ic_good);
            else
                imageview.setImageResource(R.drawable.ic_excellent);

            //Glide.with(context).load(imageDetail.getImageUrl()).into(imageview);
        }
    }

    interface ClickListener {
        void insertFeedback(Answer answer);
    }


}

