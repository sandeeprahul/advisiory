package com.example.advisoryservice.ui.genderquestions;

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

public class MultipleOptionAdapter extends RecyclerView.Adapter<MultipleOptionAdapter.MyViewHolder> implements View.OnClickListener {
    public static final String TAG = MultipleOptionAdapter.class.getSimpleName();
    private LayoutInflater inflater;
    private List<SubQuestion> subQuestionsList;
 //   LinkedHashMap<String, String> optionMap = new LinkedHashMap<>();
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
  //  ArrayMap<String, Option> allOptionMap = new ArrayMap<>();
    AppCompatRadioButton radioButton;
    ArrayList<String> rbTest;
    String questionTest;

    int indexCnt = 0;
    TextView name, optionsText;
    LinearLayout parentLl;
    TextView textView;

    ArrayList<String> test = new ArrayList<>();

    ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String,String>>();
    List<Option>optionList;


    public MultipleOptionAdapter(Context ctx, List<Option> optionList,ClickListener clickListener) {

        inflater = LayoutInflater.from(ctx);
        this.context = ctx;
        this.optionList =optionList;
        this.clickListener =clickListener;


    }




    @Override
    public MultipleOptionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.multiple_options_list_items, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MultipleOptionAdapter.MyViewHolder holder, int position) {

       /* name.setText(subQuestionsList.get(position).getSubQuestion());
        questionTest = subQuestionsList.get(position).getSubQuestion();

        List<SubQuestionOption> subQuestionOptions = subQuestionsList.get(position).getSubQuestionOptions();


        radioButtonCheckValidation = String.valueOf(subQuestionsList.get(position));*/



            /*textView = new TextView(context);
            name.setText(optionList.get(position).getOptionsName());*/


            TextView tv = new TextView(context);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                    ((int) ViewGroup.LayoutParams.MATCH_PARENT, 60);
            params.leftMargin = 10;
            params.rightMargin = 10;
            params.topMargin = 10;

            tv.setBackground(context.getResources().getDrawable(R.drawable.radio_button_bg));

            // params.topMargin=i*50;
            tv.setText(optionList.get(position).getOptionsName());
            tv.setId(Integer.parseInt(optionList.get(position).getOptionId()));
            tv.setTextSize((float) 16);
            tv.setTypeface(tv.getTypeface(), Typeface.NORMAL);
            tv.setTextColor(context.getResources().getColor(R.color.black));
            tv.setPadding(10, 10, 10, 10);
            tv.setLayoutParams(params);
            parentLl.addView(tv);
          //  optionList.clear();


//        notifyItemChanged(holder.getAdapterPosition());

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //  Toast.makeText(context, "Clicked", Toast.LENGTH_LONG).show();
                    // tv.setBackgroundColor(context.getResources().getColor(R.color.orange));
                  /*  tv.setBackground(context.getResources().getDrawable(R.drawable.feedback_onclick_option_bg));
                    tv.setTextColor(context.getResources().getColor(R.color.white));*/

                    String quesId = optionList.get(holder.getAdapterPosition()).getOptionId();
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


                    clickListener.insertRadioClickFeedback(tv,  selectedProducts);

                    //position =id;

                  //  clickListener.insertRadioClickFeedback(tv, radioOptionID, radioBtnChecked, selectedProducts);

                    //  selectedProducts.put(String.valueOf(checkedRadioButton.getId()),String.valueOf(checkedRadioButton.getId()));
                    // Changes the textview's text to "Checked: example radiobutton text"




                }
            });


       /* holder.userFeedback.addTextChangedListener(new TextWatcher() {
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

        Log.e("CheckPositionIndex", "" + indexCnt);
        Log.e("CheckPositionIndex---", "" + subQuestionsList.size());*/
        //  if (indexCnt <= subQuestionsList.size())
        // addRadioButtons(optionMap, holder);


    }



    @Override
    public int getItemCount() {
        return optionList.size();
    }

    @Override
    public void onClick(View v) {

    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        EditText userFeedback;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
          //  optionsText = (TextView) itemView.findViewById(R.id.options_text);


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



        void insertRadioClickFeedback(TextView v,  HashMap<String, ArrayList<String>> selectedProducts);

       // void insertReamrk(HashMap<String, String> selectedProducts);
    }
}

