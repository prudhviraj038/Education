package com.yellowsoft.education;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


public class RewardActivity extends Activity {

    TextView worng,total,correct,user_name,user_adrress;
    TextView wrong,correctt,questions,top_stud;
    ImageView user_image,settings_btn,reward_home;
    JSONObject user_details ;

    ArrayList<Rewards> rewardses;
    RewardListAdapter rewardListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Session.forceRTLIfSupported(this);
        setContentView(R.layout.rewards_screen);
        rewardses = new ArrayList<>();
        wrong = (TextView)findViewById(R.id.wrong_ans);
        wrong.setText(Session.getword(this, "wrong_answers"));

        questions = (TextView)findViewById(R.id.no_ques);
        questions.setText(Session.getword(this, "no_of_questions"));

        correctt = (TextView)findViewById(R.id.correct_ans);
        correctt.setText(Session.getword(this, "correct_answers"));
        top_stud=(TextView)findViewById(R.id.top_students);
        top_stud.setText(Session.getword(this,"top_students"));

        ImageView back=(ImageView)findViewById(R.id.back_rewrds);
        GridView reward_gridView=(GridView)findViewById(R.id.gridView_reward);
        rewardListAdapter = new RewardListAdapter(this,rewardses);
        reward_gridView.setAdapter(rewardListAdapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        reward_home = (ImageView) findViewById(R.id.reward_home);
        reward_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        settings_btn = (ImageView) findViewById(R.id.reward_settings);
        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RewardActivity.this,SettingsActivity.class);
                startActivity(intent);
            }
        });

        worng = (TextView) findViewById(R.id.wrong_answers);
        total = (TextView) findViewById(R.id.no_of_questions);
        correct = (TextView) findViewById(R.id.correct_answers);
        user_name = (TextView) findViewById(R.id.user_name);
        user_adrress = (TextView) findViewById(R.id.reward_user_adr);

        user_image = (ImageView) findViewById(R.id.user_image);
        try {
            user_details = new JSONObject(Session.getUserdetails(this));
            user_name.setText(user_details.getString("name"));
            user_name.setText(user_details.getString("name"));
            user_adrress.setText(user_details.getJSONObject("area").getString("governate"+Session.get_append(this))
                            +" , "+ user_details.getJSONObject("area").getString("title"+Session.get_append(this))
            );
            Picasso.with(this).load(user_details.getString("image")).into(user_image);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        get_rewards();
        get_toppers();

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

    private void get_rewards(){

        String url = Session.SERVERURL + "dashboard.php?";
        try {
            url = url + "member_id="+ URLEncoder.encode(Session.getUserid(this), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.e("url--->", url);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(Session.getword(this,"loading"));
        progressDialog.setCancelable(false);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest( url,null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonArray) {

                try {
                    Log.e("res",jsonArray.toString());
                    worng.setText(jsonArray.getString("wrong"));
                    total.setText(jsonArray.getString("total"));
                    correct.setText(jsonArray.getString("correct"));
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("response is:", error.toString());
                Toast.makeText(RewardActivity.this, "Server not connected", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });

// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }

    private void get_toppers() {
        String url = Session.SERVERURL + "toppers.php";
        Log.e("url--->", url);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(Session.getword(this,"loading"));
        progressDialog.setCancelable(false);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest( url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray jsonArray) {

                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    Log.e("res",jsonObject.toString());
                    for(int i=0;i<jsonArray.length();i++){
                        rewardses.add(new Rewards(jsonArray.getJSONObject(i),RewardActivity.this));
                    }
                    rewardListAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("response is:", error.toString());
                Toast.makeText(RewardActivity.this, "Server not connected", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });

// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }


}
