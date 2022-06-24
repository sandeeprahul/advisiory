package com.example.advisoryservice.ui.result;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.advisoryservice.R;
import com.example.advisoryservice.data.model.questions.Option;
import com.example.advisoryservice.data.model.questions.SubQuestion;
import com.example.advisoryservice.data.model.questions.SubQuestionOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class LatestFeedbackTestAdapter extends RecyclerView.Adapter<LatestFeedbackTestAdapter.MyViewHolder> implements View.OnClickListener {
    public static final String TAG = LatestFeedbackTestAdapter.class.getSimpleName();
    private LayoutInflater inflater;
    private List<SubQuestion> subQuestionsList;
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
    HashMap<String, ArrayList<String>> selectedProducts = new HashMap<>();
    HashMap<String, String> remarkMap = new HashMap<>();
   // ArrayMap<String, Option> allOptionMap = new ArrayMap<>();
    AppCompatRadioButton radioButton;
    ArrayList<String> rbTest;
    String questionTest;

    int indexCnt = 0;
    TextView name, optionsText;
    LinearLayout parentLl;
    TextView textView;

    ArrayList<String> test = new ArrayList<>();

    ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String,String>>();


    public LatestFeedbackTestAdapter(Context ctx, List<SubQuestion> subQuestions, String custTransId, String locationCode, String selectedFeedbackID, ClickListener clickListener) {

        inflater = LayoutInflater.from(ctx);
        this.subQuestionsList = subQuestions;

        this.context = ctx;
        this.clickListener = clickListener;
        this.custTransId = this.custTransId;
        this.locationCode = this.locationCode;
        this.selectedFeedbackID = this.selectedFeedbackID;

    }


    @Override
    public LatestFeedbackTestAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.feedback_radio_list_items_view_test, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(LatestFeedbackTestAdapter.MyViewHolder holder, int position) {

        name.setText(subQuestionsList.get(position).getSubQuestion());
        questionTest = subQuestionsList.get(position).getSubQuestion();

        List<SubQuestionOption> subQuestionOptions = subQuestionsList.get(position).getSubQuestionOptions();


        radioButtonCheckValidation = String.valueOf(subQuestionsList.get(position));


        for (int i = 0; i < subQuestionOptions.size(); i++) {
           /* textView = new TextView(context);
            textView.setText(subQuestionOptions.get(i).getSubOptionsName());*/


            TextView tv = new TextView(context);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                    ((int) ViewGroup.LayoutParams.MATCH_PARENT, (int) ViewGroup.LayoutParams.MATCH_PARENT);
            params.leftMargin = 20;
            params.rightMargin = 50;
            params.topMargin = 15;

            tv.setBackground(context.getResources().getDrawable(R.drawable.radio_button_bg));

            // params.topMargin=i*50;
            tv.setText(subQuestionOptions.get(i).getSubOptionsName());
            tv.setId(Integer.parseInt(subQuestionOptions.get(i).getSubOptionId()));
            tv.setTextSize((float) 16);
            tv.setTypeface(tv.getTypeface(), Typeface.NORMAL);
            tv.setTextColor(context.getResources().getColor(R.color.black));
            tv.setPadding(20, 10, 20, 10);
            tv.setLayoutParams(params);

            parentLl.addView(tv);

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //  Toast.makeText(context, "Clicked", Toast.LENGTH_LONG).show();
                    // tv.setBackgroundColor(context.getResources().getColor(R.color.orange));

                    String quesId = subQuestionsList.get(holder.getAdapterPosition()).getSubQuestionId();
                    ArrayList<String> list = new ArrayList<>();

                    if (selectedProducts.containsKey(quesId)){
                        list = selectedProducts.get(quesId);

                        if (list.contains(String.valueOf(tv.getId()))){
                            list.remove(String.valueOf(tv.getId()));
                            tv.setBackground(context.getResources().getDrawable(R.drawable.radio_button_bg));
                            tv.setTextColor(context.getResources().getColor(R.color.black));
                        }else {
                            list.add(String.valueOf(tv.getId()));
                            tv.setBackground(context.getResources().getDrawable(R.drawable.feedback_onclick_option_bg));
                            tv.setTextColor(context.getResources().getColor(R.color.white));
                        }
                    }else {
                        list.add(String.valueOf(tv.getId()));
                        tv.setBackground(context.getResources().getDrawable(R.drawable.feedback_onclick_option_bg));
                        tv.setTextColor(context.getResources().getColor(R.color.white));
                    }
                    selectedProducts.put(quesId,list);

                    //   arrayList.add(selectedProducts);
                    //selectedProducts.add(String.valueOf(checkedRadioButton.getId()));


                    if (String.valueOf(tv.getId()).equals("5")) {
                        holder.userFeedback.setVisibility(View.VISIBLE);
                    } else {
                        holder.userFeedback.setVisibility(View.GONE);
                    }
                    //notifyItemChanged(holder.getAdapterPosition());

                    Log.e("CheckPosition", tv.getText().toString());
                    //  Log.e("CheckPositionID", "" + id);
                    Log.e("CheckPositionPos", holder.getAdapterPosition() + "");

                    //position =id;

                    clickListener.insertRadioClickFeedback(tv, radioOptionID, radioBtnChecked, selectedProducts);

                    //  selectedProducts.put(String.valueOf(checkedRadioButton.getId()),String.valueOf(checkedRadioButton.getId()));
                    // Changes the textview's text to "Checked: example radiobutton text"




                }
            });
        }


        // parentLl.addView(textView);
      /*  for (SubQuestionOption option : subQuestionOptions) {

            optionMap.put(option.getSubOptionId(), option.getSubOptionsName());
            subQuestionOptionHashMap.put(option.getSubOptionId(), option);
        }*/

        holder.userFeedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    Log.d("TAG", "editable.toString() " + editable.toString());
                    remarkMap.put(subQuestionsList.get(holder.getAdapterPosition()).getSubQuestionId(), editable.toString());
                    Log.d("TAG", "remarkMap " + remarkMap.size());
                    clickListener.insertReamrk(remarkMap);
                }
            }
        });

       /* radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();

                int id = checkedRadioButton.getId();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked) {




                    selectedProducts.put(subQuestionsList.get(holder.getAdapterPosition()).getSubQuestionId(), String.valueOf(checkedRadioButton.getId()));
                    //selectedProducts.add(String.valueOf(checkedRadioButton.getId()));


                    if (String.valueOf(checkedRadioButton.getId()).equals("5")) {
                        holder.userFeedback.setVisibility(View.VISIBLE);
                    } else {
                        holder.userFeedback.setVisibility(View.GONE);
                    }
                    //notifyItemChanged(holder.getAdapterPosition());

                    Log.e("CheckPosition", checkedRadioButton.getText().toString());
                    Log.e("CheckPositionID", "" + id);
                    Log.e("CheckPositionPos", holder.getAdapterPosition() + "");

                    //position =id;

                    clickListener.insertRadioClickFeedback(checkedRadioButton, radioOptionID, radioBtnChecked, selectedProducts);

                    //  selectedProducts.put(String.valueOf(checkedRadioButton.getId()),String.valueOf(checkedRadioButton.getId()));
                    // Changes the textview's text to "Checked: example radiobutton text"

                } else {
                    //  selectedProducts.remove(String.valueOf(checkedRadioButton.getId()));

                    Toast.makeText(context, "Not Selected ", Toast.LENGTH_LONG).show();
                }


            }
        });*/

        Log.e("CheckPositionIndex", "" + indexCnt);
        Log.e("CheckPositionIndex---", "" + subQuestionsList.size());
        //  if (indexCnt <= subQuestionsList.size())
        // addRadioButtons(optionMap, holder);


    }

    private void addRadioButtons(HashMap<String, String> optionMap, MyViewHolder holder) {

        String selectedId = "";
        // isChecked = false;
        //  Log.w(TAG, "Question Number <<::>> " + questionNum);

        //  indexCnt++;

        /*radioGroup.setOrientation(LinearLayout.VERTICAL);
        radioBtnChecked = radioGroup.getCheckedRadioButtonId();*/


        for (String id : optionMap.keySet()) {


            /*radioButton = new AppCompatRadioButton(context);

            radioButton.setId(Integer.parseInt(id));
            Log.e("radioPosition", (id));

            radioButton.setText(optionMap.get(id));
            radioOptionID = optionMap.get(id);

            radioButton.setBackground(context.getResources().getDrawable(R.drawable.radio_button_bg));
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.setMargins(20,20,30,10);



            radioButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            radioButton.setLayoutParams(params);

            radioButton.setOnClickListener(this);
            radioGroup.addView(radioButton);*/


        }

    }


    @Override
    public int getItemCount() {
        return subQuestionsList.size();
    }

    @Override
    public void onClick(View v) {

    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        EditText userFeedback;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            optionsText = (TextView) itemView.findViewById(R.id.options_text);
            radioGroup = itemView.findViewById(R.id.radiogroup);
            userFeedback = (EditText) itemView.findViewById(R.id.user_feedback);
            parentLl = (LinearLayout) itemView.findViewById(R.id.parent_ll);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  /*  MainActivity.textView.setText("You have selected : "+myImageNameList[getAdapterPosition()]);
                    MainActivity.dialog.dismiss();*/
                }
            });

        }

    }


    interface ClickListener {

        void insertRadioClickFeedback(TextView v, String radioOptionID, int radioButtonCheckValidation, HashMap<String, ArrayList<String>> selectedProducts);

        void insertReamrk(HashMap<String, String> selectedProducts);
    }
}

