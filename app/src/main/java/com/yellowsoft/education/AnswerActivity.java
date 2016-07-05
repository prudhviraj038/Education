package com.yellowsoft.education;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AnswerActivity extends Activity {
    String subject_id = "1";
    TextView question,ans1,ans2,ans3,ans4,que_count,give_up,que_number;
    ImageView one,two,three,four;
    LinearLayout submit_layout;
    String user_correct,api_correct;
    JSONObject user_details ;
    int next_stage = 5;
    int correct_count=0;
    int question_count = 0;
    String answertype = "-1";
    String question_id = "-1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Session.forceRTLIfSupported(this);
        setContentView(R.layout.answer_screen);
        user_correct = "-1";
        submit_layout=(LinearLayout)findViewById(R.id.submit_ans);
        que_count = (TextView) findViewById(R.id.que_count);
        que_number = (TextView) findViewById(R.id.question_number);
        que_number.setText("QUESTION "+ String.valueOf(question_count));
        give_up = (TextView) findViewById(R.id.give_up);
        give_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answertype = "skipped";
                setanswer();
            }
        });
        submit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(user_correct.equals("-1")){
                    Toast.makeText(AnswerActivity.this, "Select An Answer", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (user_correct.equals(api_correct)) {
                        Toast.makeText(AnswerActivity.this, "Correct Answer", Toast.LENGTH_SHORT).show();
                        correct_count = correct_count + 1;
                      //  que_count.setText(String.valueOf(correct_count) + " / " + String.valueOf(next_stage));
                        answertype = "correct";
                    } else {
                        Toast.makeText(AnswerActivity.this, "Wrong Answer", Toast.LENGTH_SHORT).show();
                        answertype = "wrong";
                    }

                    setanswer();
                }
            }
        });
        one = (ImageView ) findViewById(R.id.one);
        two = (ImageView) findViewById(R.id.two);
        three = (ImageView) findViewById(R.id.three);
        four = (ImageView) findViewById(R.id.four);

        question = (TextView)findViewById(R.id.question);
        ans1 = (TextView)findViewById(R.id.ans1);
        ans2 = (TextView)findViewById(R.id.ans2);
        ans3 = (TextView)findViewById(R.id.ans3);
        ans4 = (TextView)findViewById(R.id.ans4);

        subject_id = getIntent().getStringExtra("subj_id");
        try {
            user_details = new JSONObject(Session.getUserdetails(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ImageView userimage = (ImageView)findViewById(R.id.user_image);

        TextView user_name = (TextView) findViewById(R.id.username);
        TextView user_level = (TextView) findViewById(R.id.userlevel);
        try {
            user_name.setText(user_details.getString("name"));
            user_level.setText("0"+user_details.getString("grade"));
            Picasso.with(this).load(user_details.getString("image")).into(userimage);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_correct = "1";
                one.setImageResource(R.drawable.radio_btn_selected);;
                two.setImageResource(R.drawable.radio_btn_unselected);
                three.setImageResource(R.drawable.radio_btn_unselected);;
                four.setImageResource(R.drawable.radio_btn_unselected);;
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_correct = "2";
                two.setImageResource(R.drawable.radio_btn_selected);;
                one.setImageResource(R.drawable.radio_btn_unselected);;
                three.setImageResource(R.drawable.radio_btn_unselected);;
                four.setImageResource(R.drawable.radio_btn_unselected);;
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_correct = "3";
                three.setImageResource(R.drawable.radio_btn_selected);;
                two.setImageResource(R.drawable.radio_btn_unselected);;
                one.setImageResource(R.drawable.radio_btn_unselected);;
                four.setImageResource(R.drawable.radio_btn_unselected);;
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_correct = "4";
                four.setImageResource(R.drawable.radio_btn_selected);;
                two.setImageResource(R.drawable.radio_btn_unselected);;
                three.setImageResource(R.drawable.radio_btn_unselected);;
                one.setImageResource(R.drawable.radio_btn_unselected);;
            }
        });

        getquestion();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getquestion(){
        user_correct = "-1";
        one.setImageResource(R.drawable.radio_btn_unselected);;
        two.setImageResource(R.drawable.radio_btn_unselected);;
        three.setImageResource(R.drawable.radio_btn_unselected);;
        four.setImageResource(R.drawable.radio_btn_unselected);;
        try {
          //  JSONObject user_details = new JSONObject(Session.getUserdetails(this));
            String url = Session.SERVERURL+"question.php?user_id="+Session.getUserid(this)+"&level="+user_details.getJSONObject("level").getString("id")
                    +"&grade="+user_details.getString("grade")
                    +"&semister="+user_details.getJSONObject("semister").getString("id")+"&subject="+subject_id;

            Log.e("url--->", url);
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait getting next question....");
            progressDialog.setCancelable(false);
            progressDialog.show();
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    progressDialog.dismiss();
                    Log.e("response is: ", jsonObject.toString());
                    try {
                        question_id = jsonObject.getString("id");
                        String questionstr = jsonObject.getString("question");
                        question.setText(questionstr);
                        ans1.setText(jsonObject.getString("answer1"));
                        ans2.setText(jsonObject.getString("answer2"));
                        ans3.setText(jsonObject.getString("answer3"));
                        ans4.setText(jsonObject.getString("answer4"));
                        api_correct = jsonObject.getString("correct");
                        question_count++;
                        que_number.setText("QUESTION "+ String.valueOf(question_count));
                        que_count.setText(String.valueOf(question_count) + " / " + String.valueOf(next_stage));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub
                    Log.e("response is:", error.toString());
                    Toast.makeText(AnswerActivity.this, "Server not connected", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            });

// Access the RequestQueue through your singleton class.
            AppController.getInstance().addToRequestQueue(jsObjRequest);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    private void setanswer(){

        one.setImageResource(R.drawable.radio_btn_unselected);;;
        two.setImageResource(R.drawable.radio_btn_unselected);;;
        three.setImageResource(R.drawable.radio_btn_unselected);;;
        four.setImageResource(R.drawable.radio_btn_unselected);;;
        try {
            JSONObject user_details = new JSONObject(Session.getUserdetails(this));
            String url = Session.SERVERURL+"answer.php?member_id="+Session.getUserid(this)+"&level="+user_details.getJSONObject("level").getString("id")
                    +"&grade="+user_details.getString("grade")
                    +"&semister="+user_details.getJSONObject("semister").getString("id")
                    +"&subject="+subject_id
                    +"&answer="+user_correct
                    +"&question_id="+question_id
                    +"&ans_result="+answertype;

            Log.e("url--->", url);
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait submiting your answer....");
            progressDialog.setCancelable(false);
            progressDialog.show();
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    progressDialog.dismiss();
                    Log.e("response is: ", jsonObject.toString());
                    try {

                        String response = jsonObject.getJSONArray("response").getJSONObject(0).getString("status");
                        if(response.equals("Success")) {
                            if (question_count == next_stage){
                                if(correct_count==question_count){
                                    show_alert();
                                  //  Toast.makeText(AnswerActivity.this,"You have cleared a level!",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                  //  Toast.makeText(AnswerActivity.this,"You have failed in this level!",Toast.LENGTH_SHORT).show();
                                   // finish();
                                    show_alert_fail();
                                }
                            }else{
                                getquestion();
                            }



                        }
                        else
                            Toast.makeText(AnswerActivity.this, "Your answer not submitted, please try again...", Toast.LENGTH_SHORT).show();
//                        question.setText(questionstr);
//                        ans1.setText(jsonObject.getString("answer1"));
//                        ans2.setText(jsonObject.getString("answer2"));
//                        ans3.setText(jsonObject.getString("answer3"));
//                        ans4.setText(jsonObject.getString("answer4"));
//                        api_correct = jsonObject.getString("correct");
//                        question_count++;
//                        que_number.setText("QUESTION "+ String.valueOf(question_count));


//
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub
                    Log.e("response is:", error.toString());
                    Toast.makeText(AnswerActivity.this, "Server not connected", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            });

// Access the RequestQueue through your singleton class.
            AppController.getInstance().addToRequestQueue(jsObjRequest);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

        private  void show_alert(){
        AlertDialogManager alert = new AlertDialogManager();
        alert.showAlertDialog(this, "Congratulations!", "You have cleared this level", false, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                next_stage = next_stage + 5;
                question_count=0;
                correct_count=0;
           //     que_count.setText(String.valueOf(correct_count) + " / " + String.valueOf(next_stage));
                getquestion();
            }
        });

        }

    private  void show_alert_fail(){
        AlertDialogManager alert = new AlertDialogManager();
        alert.showAlertDialog(this, "Sorry!", "You have failed in this level", false, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
    }


}
