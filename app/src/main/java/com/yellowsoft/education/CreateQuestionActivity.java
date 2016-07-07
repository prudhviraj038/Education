package com.yellowsoft.education;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CreateQuestionActivity extends AppCompatActivity {
    TextView write_que,book_ref,upload_video,write_ans,submit;
    EditText type_question;

    ArrayList<Questiondetails> questions;
    String correct="-1";
    RadioButton one,two,three,four;
    JSONObject user_details ;
    String subject_id = "1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Session.forceRTLIfSupported(this);
        setContentView(R.layout.create_question);
        write_que = (TextView)findViewById(R.id.write_que_heading);
        write_que.setText(Session.getword(this, "write_y_question"));
        type_question=(EditText)findViewById(R.id.et_que);
        type_question.setText(Session.getword(this,"type_your_ques"));
        book_ref = (TextView)findViewById(R.id.book_ref_heading);
        book_ref.setText(Session.getword(this,"reference"));
        upload_video = (TextView)findViewById(R.id.upload_vid_heading);
        upload_video.setText(Session.getword(this,""));
        write_ans = (TextView)findViewById(R.id.write_answer_heading);
        write_ans.setText(Session.getword(this,"write_y_answer"));

        submit = (TextView)findViewById(R.id.create_submit);
        submit.setText(Session.getword(this, "submit"));

        getSupportActionBar().hide();

        one = (RadioButton ) findViewById(R.id.one);
        two = (RadioButton) findViewById(R.id.two);
        three = (RadioButton) findViewById(R.id.three);
        four = (RadioButton) findViewById(R.id.four);
        try {
            user_details = new JSONObject(Session.getUserdetails(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correct = "1";
                two.setChecked(false);
                three.setChecked(false);
                four.setChecked(false);
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correct = "2";
                one.setChecked(false);
                three.setChecked(false);
                four.setChecked(false);
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correct = "3";
                two.setChecked(false);
                one.setChecked(false);
                four.setChecked(false);
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correct = "4";
                two.setChecked(false);
                three.setChecked(false);
                one.setChecked(false);
            }
        });

        EditText question = (EditText) findViewById(R.id.et_que);
        EditText answer1 = (EditText) findViewById(R.id.et_ans1);
        EditText answer2 = (EditText) findViewById(R.id.et_ans2);
        EditText answer3 = (EditText) findViewById(R.id.et_ans3);
        EditText answer4 = (EditText) findViewById(R.id.et_ans4);

        String questions = question.getText().toString();
        String answ1 = answer1.getText().toString();
        String answ2 = answer2.getText().toString();
        String answ3 = answer3.getText().toString();
        String answ4 = answer4.getText().toString();


        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.submit);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_question();
            }
        });
        subject_id = getIntent().getStringExtra("subj_id");
    }

    private void add_question() {
        EditText question = (EditText) findViewById(R.id.et_que);
        EditText answer1 = (EditText) findViewById(R.id.et_ans1);
        EditText answer2 = (EditText) findViewById(R.id.et_ans2);
        EditText answer3 = (EditText) findViewById(R.id.et_ans3);
        EditText answer4 = (EditText) findViewById(R.id.et_ans4);

        final String questions = question.getText().toString();
        final String answ1 = answer1.getText().toString();
        final String answ2 = answer2.getText().toString();
        final String answ3 = answer3.getText().toString();
        final String answ4 = answer4.getText().toString();

        if (questions.equals(""))
            Toast.makeText(CreateQuestionActivity.this, Session.getword(CreateQuestionActivity.this,"Pls_ent_question"), Toast.LENGTH_SHORT).show();
        else if (answ1.equals(""))
            Toast.makeText(CreateQuestionActivity.this, Session.getword(CreateQuestionActivity.this,"Pls_sel_first_ans"), Toast.LENGTH_SHORT).show();
        else if (answ2.equals(""))
            Toast.makeText(CreateQuestionActivity.this, Session.getword(CreateQuestionActivity.this,"Pls_sel_sec_ans"), Toast.LENGTH_SHORT).show();
        else if (answ3.equals(""))
            Toast.makeText(CreateQuestionActivity.this, Session.getword(CreateQuestionActivity.this,"Pls_ent_third_ans"), Toast.LENGTH_SHORT).show();
        else if (answ4.equals(""))
            Toast.makeText(CreateQuestionActivity.this, Session.getword(CreateQuestionActivity.this,"Pls_sel_fourth_ans"), Toast.LENGTH_SHORT).show();
        else if (correct.equals("-1"))
            Toast.makeText(CreateQuestionActivity.this, Session.getword(CreateQuestionActivity.this,"Pls_sel_corr_ans"), Toast.LENGTH_SHORT).show();


        else {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("please wait...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            String url = Session.SERVERURL + "add-question.php?";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            if (response.equals("failed")) {
                                Toast.makeText(CreateQuestionActivity.this, response, Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(CreateQuestionActivity.this, response, Toast.LENGTH_SHORT).show();

                                after_question();
                                                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            Toast.makeText(CreateQuestionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    try {
                        params.put("user_id", Session.getUserid(CreateQuestionActivity.this));
                        params.put("level", user_details.getJSONObject("level").getString("id"));
                        params.put("grade", user_details.getString("grade"));
                        params.put("semister", user_details.getJSONObject("semister").getString("id"));
                        params.put("subject",subject_id);
                        params.put("question", questions);
                        params.put("answer1", answ1);
                        params.put("answer2", answ2);
                        params.put("answer3", answ3);
                        params.put("answer4", answ4);
                        params.put("reference", "ref");
                        params.put("correct", correct);
                        params.put("youtube", "youtube");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest);
        }


    }

    private void after_question(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Session.getword(this,"add_another_ques"))
                .setCancelable(false)
                .setPositiveButton(Session.getword(this, "yes"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        EditText question = (EditText) findViewById(R.id.et_que);
                        EditText answer1 = (EditText) findViewById(R.id.et_ans1);
                        EditText answer2 = (EditText) findViewById(R.id.et_ans2);
                        EditText answer3 = (EditText) findViewById(R.id.et_ans3);
                        EditText answer4 = (EditText) findViewById(R.id.et_ans4);
                        question.setText("");
                        answer1.setText("");
                        answer2.setText("");
                        answer3.setText("");
                        answer4.setText("");
                        one.setChecked(false);
                        two.setChecked(false);
                        three.setChecked(false);
                        four.setChecked(false);
                        correct = "-1";

                    }
                })
                .setNegativeButton(Session.getword(this, "no"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
