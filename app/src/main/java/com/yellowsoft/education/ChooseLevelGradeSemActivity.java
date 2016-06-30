package com.yellowsoft.education;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ChooseLevelGradeSemActivity extends Activity {
    String levels_id="0";
    String grade_id="0";
    String sem_sec_id="0";
    String type;
    ArrayList<String> level_id;
    ArrayList<String> level_title;
    ArrayList<String> grade;
    ArrayList<String> semsect_id;
    ArrayList<String> semsection_title;
    ArrayList<String> quer_id;
    ArrayList<String> que_count;
    ArrayList<String> mSelectedItems;

    TextView choose_level,choose_grade,choose_section;
    String uname,pword,fullname,emaill,mobile,gove,cls,imgpath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_subject_level);
        type = getIntent().getStringExtra("type");
        LinearLayout level_lv=(LinearLayout)findViewById(R.id.level);
        LinearLayout grade_lv=(LinearLayout)findViewById(R.id.grade);
        LinearLayout semsect_lv=(LinearLayout)findViewById(R.id.semsection);
        choose_level = (TextView) findViewById(R.id.choose_level);
        choose_grade = (TextView) findViewById(R.id.choose_grade);
        choose_section = (TextView) findViewById(R.id.choose_section);
        final LinearLayout save_changes=(LinearLayout)findViewById(R.id.save_change);
        level_id= new ArrayList<String>();
        level_title=new ArrayList<String>();
        semsect_id= new ArrayList<String>();
        semsection_title=new ArrayList<String>();
        quer_id= new ArrayList<String>();
        que_count=new ArrayList<String>();
        mSelectedItems=new ArrayList<String>();

        if(type.equals("")) {
            Intent intent = getIntent();
             uname = intent.getStringExtra("username");
             pword = intent.getStringExtra("password");
            fullname = intent.getStringExtra("fname");
            emaill = intent.getStringExtra("email");
            mobile = intent.getStringExtra("mobile");
            gove = intent.getStringExtra("gover");
            cls = intent.getStringExtra("class");
            imgpath = intent.getStringExtra("image_path");
        }

        if(type.equals("change"))
            try {
                JSONObject jsonObject=new JSONObject(Session.getUserdetails(ChooseLevelGradeSemActivity.this));
                choose_level.setText(jsonObject.getJSONObject("level").getString("title"));
                choose_grade.setText(jsonObject.getString("grade"));
                choose_section.setText(jsonObject.getJSONObject("semister").getString("title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        level_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChooseLevelGradeSemActivity.this);
                builder.setTitle("CHOOSE LEVELS");
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ChooseLevelGradeSemActivity.this, android.R.layout.simple_dropdown_item_1line, level_title);
                builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(ChooseSubjectActivity.this, level_title.get(which), Toast.LENGTH_SHORT).show();
                        levels_id = level_id.get(which);
                        choose_level.setText(level_title.get(which));
                    }
                });

                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        get_level();


        grade_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grade = new ArrayList<String>();
                grade.add("1");
                grade.add("2");
                grade.add("3");
                grade.add("4");
                grade.add("5");
                grade.add("6");

                AlertDialog.Builder builder = new AlertDialog.Builder(ChooseLevelGradeSemActivity.this);
                builder.setTitle("CHOOSE GRADE");
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ChooseLevelGradeSemActivity.this, android.R.layout.select_dialog_item, grade);
                builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(ChooseSubjectActivity.this, grade.get(which), Toast.LENGTH_SHORT).show();
                        grade_id = grade.get(which);
                        choose_grade.setText(grade.get(which));
                    }
                });

                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        semsect_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChooseLevelGradeSemActivity.this);
                builder.setTitle("SEMESTER SECTIONS");
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ChooseLevelGradeSemActivity.this, android.R.layout.select_dialog_item, semsection_title);
                builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Toast.makeText(ChooseSubjectActivity.this, semsection_title.get(which), Toast.LENGTH_SHORT).show();
                        sem_sec_id = semsect_id.get(which);
                        choose_section.setText(semsection_title.get(which));
                        save_changes.performClick();

                    }
                });

                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        get_semsection();

        ImageView back=(ImageView)findViewById(R.id.back_sub_scr);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (levels_id.equals(""))
                    Toast.makeText(ChooseLevelGradeSemActivity.this, "Please Select Level", Toast.LENGTH_SHORT).show();
                else if (grade_id.equals(""))
                    Toast.makeText(ChooseLevelGradeSemActivity.this, "Please Select Grade", Toast.LENGTH_SHORT).show();
                else if (semsection_title.equals(""))
                    Toast.makeText(ChooseLevelGradeSemActivity.this, "Please Select Semester", Toast.LENGTH_SHORT).show();
                else{
                    if(type.equals("change")){
                        edit_profile();
                    }else{
                        Intent intent = new Intent(getApplicationContext(), SelectSubjectsActivity.class);
                        intent.putExtra("type","normal");
                        intent.putExtra("level_id", levels_id);
                        intent.putExtra("grade_id", grade_id);
                        intent.putExtra("sem_id", sem_sec_id);
                        intent.putExtra("usname", uname);
                        intent.putExtra("psword", pword);
                        intent.putExtra("fname", fullname);
                        intent.putExtra("email", emaill);
                        intent.putExtra("mobi", mobile);
                        intent.putExtra("gover", gove);
                        intent.putExtra("clas", cls);
                        intent.putExtra("image_path",imgpath);
                        startActivity(intent);
                    }


            }}
      });
    }
    private void edit_profile(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait..");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String url = Session.SERVERURL+"edit-member.php?member_id="+Session.getUserid(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(progressDialog!=null)
                            progressDialog.dismiss();
                        Log.e("signup_res",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            jsonObject = jsonArray.getJSONObject(0);
                            if(jsonObject.getString("status").equals("Failed")){
                                Toast.makeText(ChooseLevelGradeSemActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Academics updated Succesfully", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(progressDialog!=null)
                            progressDialog.dismiss();
                        Toast.makeText(ChooseLevelGradeSemActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("level",levels_id);
                params.put("grade",grade_id);
                params.put("semister",sem_sec_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
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



    private void get_level(){
        String url=Session.SERVERURL+"levels.php";
        Log.e("url--->", url);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setCancelable(false);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                progressDialog.dismiss();
                Log.e("response is: ", jsonObject.toString());
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("levels");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject sub = jsonArray.getJSONObject(i);
                        String level_name = sub.getString("title");
                        String levels_id = sub.getString("id");
                        level_id.add(levels_id);
                        level_title.add(level_name);
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
                Toast.makeText(ChooseLevelGradeSemActivity.this,"Server not connected",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });

// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }

    private void get_semsection(){
        String url=Session.SERVERURL+"semisters.php";
        Log.e("url--->", url);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setCancelable(false);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                progressDialog.dismiss();
                Log.e("response is: ", jsonObject.toString());
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("semisters");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject sub = jsonArray.getJSONObject(i);
                        String semsec_name = sub.getString("title");
                        String semsec_id = sub.getString("id");
                        semsect_id.add(semsec_id);
                        semsection_title.add(semsec_name);
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
                Toast.makeText(ChooseLevelGradeSemActivity.this,"Server not connected",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });

// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }

    private void get_questionaries(){


    }
}

