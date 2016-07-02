package com.yellowsoft.education;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class AnswerAQuestionActivity extends AppCompatActivity {
    ArrayList<Questiondetails> questions;
    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Session.forceRTLIfSupported(this);
        setContentView(R.layout.questionarie_silding_activity);
        getSupportActionBar().hide();
        Intent intent=getIntent();
        String subject_id=intent.getStringExtra("subj_id");
        questions=new ArrayList<Questiondetails>();
        mPager = (ViewPager) findViewById(R.id.pager);

        String url=Session.SERVERURL+"questions-list.php?";
        Log.e("url--->", url);
        try {
            url = url + "question_id=" + URLEncoder.encode(subject_id, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                progressDialog.dismiss();
                Log.e("response is: ", jsonObject.toString());
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("questionslist");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject sub = jsonArray.getJSONObject(i);
                        String que_id = sub.getString("id");
                        String question = sub.getString("question");
                        String answer1 = sub.getString("answer1");
                        String answer2 = sub.getString("answer2");
                        String answer3 = sub.getString("answer3");
                        String answer4 = sub.getString("answer4");
                        String video = sub.getString("youtube");
                        String reference = sub.getString("reference");
                        String correct = sub.getString("correct");
                        Questiondetails questiondetails=new Questiondetails(que_id,correct,
                                question,answer1,answer2,answer3,answer4,reference,video,String.valueOf(i+1),String.valueOf(jsonArray.length()));
                        questions.add(questiondetails);
                    }
                    mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
                    mPager.setAdapter(mPagerAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("response is:", error.toString());
                Toast.makeText(AnswerAQuestionActivity.this, "Server not connected", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });

// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return  QuestionDetailedFragment.newInstaceses(questions.get(position));
        }

        @Override
        public int getCount() {
            return questions.size();
        }
    }
 }

