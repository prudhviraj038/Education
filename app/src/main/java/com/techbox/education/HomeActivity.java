package com.techbox.education;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


public class HomeActivity extends RootActivity {

    String subect_id="0";
    String sem_id="0";
    ArrayList<String> sub_id;
    ArrayList<String> sub_title;
    TextView choose_subject;

    TextView choose_semester;
    ArrayList<String> semester_id;
    ArrayList<String> semester_title;

    String user_id;
    ImageView reward_btn,settings_btn;
    TextView answer,make_que,books,advertisement_tv;
    LinearLayout main_layout,advertise;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Session.forceRTLIfSupported(this);
        setContentView(R.layout.home_screen);
        main_layout = (LinearLayout) findViewById(R.id.main_layout);
        sub_id= new ArrayList<String>();
        sub_title=new ArrayList<String>();
        semester_id = new ArrayList<>();
        semester_title = new ArrayList<>();

        answer = (TextView)findViewById(R.id.answer_question);
        answer.setText(Session.getword(this, "answer_questions"));

        make_que= (TextView)findViewById(R.id.make_questiona);
        make_que.setText(Session.getword(this, "make_a_questionaire"));

        advertisement_tv = (TextView) findViewById(R.id.advertisements_tv);
        advertisement_tv.setText(Session.getword(this, "advetisements"));

        advertise = (LinearLayout)findViewById(R.id.home_advertisment);
        advertise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,Advertisement_Activity.class);
                startActivity(intent);
            }
        });

        // books = (TextView)findViewById(R.id.books_reviews);
        // books.setText(Session.getword(this, "books_and_reviews"));
        settings_btn = (ImageView) findViewById(R.id.settings_btn);
        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,SettingsActivity.class);
                startActivity(intent);
            }
        });

        reward_btn = (ImageView) findViewById(R.id.reward_btn);
        reward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,RewardActivity.class);
                startActivity(intent);
            }
        });
        Intent intent=getIntent();
        user_id=Session.getUserid(this);

        LinearLayout choose_sub=(LinearLayout)findViewById(R.id.choose_sub);
        choose_subject = (TextView) findViewById(R.id.tv1);
        choose_subject.setText(Session.getword(this, "choose_subjects"));
        choose_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle(Session.getword(HomeActivity.this,"choose_subjects"));
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(HomeActivity.this, android.R.layout.select_dialog_item, sub_title);
                builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(ChooseSubjectActivity.this, sub_title.get(which), Toast.LENGTH_SHORT).show();
                        subect_id = sub_id.get(which);
                        choose_subject.setText(sub_title.get(which));

                    }
                });

                final AlertDialog dialog = builder.create();
                dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                dialog.show();
            }
        });
        LinearLayout choose_sem=(LinearLayout)findViewById(R.id.choose_sem);
        choose_semester = (TextView) findViewById(R.id.sem_tv);
        choose_semester.setText(Session.getword(this,"choose_semister"));
        choose_sem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle(Session.getword(HomeActivity.this,"choose_semister"));
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(HomeActivity.this, android.R.layout.select_dialog_item, semester_title);
                builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(ChooseSubjectActivity.this, sub_title.get(which), Toast.LENGTH_SHORT).show();
                        sem_id = semester_id.get(which);
                        choose_semester.setText(semester_title.get(which));

                    }
                });

                final AlertDialog dialog = builder.create();
                dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                dialog.show();
            }
        });


        get_sub();

        RelativeLayout make_question=(RelativeLayout)findViewById(R.id.make_question);
        make_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subect_id.equals("0")) {
                    Toast.makeText(HomeActivity.this, Session.getword(HomeActivity.this, "please_select_subjects"), Toast.LENGTH_SHORT).show();
                }else  if (sem_id.equals("0")) {
                    Toast.makeText(HomeActivity.this, Session.getword(HomeActivity.this, "please_select_semister"), Toast.LENGTH_SHORT).show();
                } else {

                    Intent intent = new Intent(getApplicationContext(), CreateQuestionActivity.class);
                    intent.putExtra("subj_id",subect_id);
                    intent.putExtra("sem_id",sem_id);
                    startActivity(intent);
                }
            }
        });
        RelativeLayout answer_question=(RelativeLayout)findViewById(R.id.answer_a_question_layout);

        answer_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subect_id.equals("0")){
                    Toast.makeText(HomeActivity.this, Session.getword(HomeActivity.this,"please_select_subjects"), Toast.LENGTH_SHORT).show();
                }else  if (sem_id.equals("0")) {
                    Toast.makeText(HomeActivity.this, Session.getword(HomeActivity.this, "please_select_semester"), Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(getApplicationContext(), AnswerActivity.class);
                    intent.putExtra("subj_id",subect_id);
                    intent.putExtra("sem_id",sem_id);
                    startActivity(intent);
            }}
        });

        ImageView back=(ImageView)findViewById(R.id.back_home_scr);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

    private void get_sub() {
    String url = Session.SERVERURL + "subjects.php";
    Log.e("url--->", url);
    final ProgressDialog progressDialog = new ProgressDialog(this);
    progressDialog.setMessage("Please wait....");
    progressDialog.setCancelable(false);
    JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

        @Override
        public void onResponse(JSONObject jsonObject) {
            if(progressDialog!=null)
            progressDialog.dismiss();
            get_sems();
            Log.e("response is: ", jsonObject.toString());
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("subjects");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject sub = jsonArray.getJSONObject(i);
                    String sub_name = sub.getString("title");
                    String subj_id = sub.getString("id");
                    sub_id.add(subj_id);
                    sub_title.add(sub_name);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }, new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            // TODO Auto-generated method stub
            Log.e("response is:", error.toString());
            Toast.makeText(HomeActivity.this, "Server not connected", Toast.LENGTH_SHORT).show();
            if(progressDialog!=null)
                progressDialog.dismiss();
            get_sems();
        }

    });

// Access the RequestQueue through your singleton class.
    AppController.getInstance().addToRequestQueue(jsObjRequest);

}
    private void get_sems() {
        String url = Session.SERVERURL + "semisters.php?";

        Log.e("url--->", url);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setCancelable(false);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                if(progressDialog!=null)
                    progressDialog.dismiss();
                get_user_details();
                Log.e("response is: ", jsonObject.toString());
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("semisters");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject sub = jsonArray.getJSONObject(i);
                        String sub_name = sub.getString("title");
                        String subj_id = sub.getString("id");
                        semester_id.add(subj_id);
                        semester_title.add(sub_name);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("response is:", error.toString());
                Toast.makeText(HomeActivity.this, "Server not connected", Toast.LENGTH_SHORT).show();
                if(progressDialog!=null)
                    progressDialog.dismiss();
                get_user_details();
            }

        });

// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }


    private void get_user_details() {
        String url = Session.SERVERURL + "members-list.php?";
        try {
            url = url + "member_id="+ URLEncoder.encode(user_id, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.e("url--->", url);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setCancelable(false);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest( url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray jsonArray) {

                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    Session.setUserdetails(HomeActivity.this,jsonObject.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("response is:", error.toString());
                Toast.makeText(HomeActivity.this, "Server not connected", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });

// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsObjRequest);
    }



}