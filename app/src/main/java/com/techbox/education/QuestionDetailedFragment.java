package com.techbox.education;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

public class QuestionDetailedFragment extends Fragment{
    public  static QuestionDetailedFragment newInstaceses(Questiondetails questiondetails) {
        QuestionDetailedFragment temp=new QuestionDetailedFragment();
        Bundle args = new Bundle();
        args.putString("id", questiondetails.id);
        args.putString("position", questiondetails.position);
        args.putString("quecount", questiondetails.quecount);
        args.putString("correct", questiondetails.correct);
        args.putString("question", questiondetails.question);
        args.putString("answer1", questiondetails.answer1);
        args.putString("answer2", questiondetails.answer2);
        args.putString("answer3", questiondetails.answer3);
        args.putString("reference", questiondetails.reference);
        args.putString("youtube", questiondetails.youtube);

        temp.setArguments(args);
        return temp;


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.answer_screen, container, false);
        TextView position=(TextView)rootView.findViewById(R.id.question_number);
        TextView question=(TextView)rootView.findViewById(R.id.question);
        TextView answer1=(TextView)rootView.findViewById(R.id.ans1);
        TextView answer2=(TextView)rootView.findViewById(R.id.ans2);
        TextView answer3=(TextView)rootView.findViewById(R.id.ans3);
        TextView que_count=(TextView)rootView.findViewById(R.id.que_count);
        TextView ref=(TextView)rootView.findViewById(R.id.reference);

        position.setText("QUESTION - "+ getArguments().getString("position"));
        que_count.setText(getArguments().getString("position")+" OUT OF "+ getArguments().getString("quecount"));
        question.setText(getArguments().getString("question"));
        answer1.setText(getArguments().getString("answer1"));
        answer2.setText(getArguments().getString("answer2"));
        answer3.setText(getArguments().getString("answer3"));
        ref.setText(getArguments().getString("reference"));
        final RadioButton radioButton = (RadioButton) rootView.findViewById(R.id.one);
        final RadioButton radioButton2 = (RadioButton) rootView.findViewById(R.id.two);
        final RadioButton radioButton3 = (RadioButton) rootView.findViewById(R.id.three);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radioButton.isChecked()){
                    radioButton.setChecked(true);
                    radioButton2.setChecked(false);
                    radioButton3.setChecked(false);
                }
            }
        });
        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radioButton2.isChecked()){
                    radioButton2.setChecked(true);
                    radioButton.setChecked(false);
                    radioButton3.setChecked(false);
                }
            }
        });
        radioButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radioButton3.isChecked()){
                    radioButton3.setChecked(true);
                    radioButton2.setChecked(false);
                    radioButton.setChecked(false);
                }
            }
        });

        return  rootView;
    }


}
