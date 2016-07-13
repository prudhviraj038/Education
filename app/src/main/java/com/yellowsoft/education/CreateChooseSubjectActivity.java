package com.yellowsoft.education;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import javax.security.auth.Subject;


public class CreateChooseSubjectActivity extends RootActivity {
    String subect_id="0";
    String levels_id="0";
    String grade_id="0";
    String sem_sec_id="0";

    ArrayList<String> sub_id;
    ArrayList<String> sub_title;
    ArrayList<String> level_id;
    ArrayList<String> level_title;
    ArrayList<String> grade;
    ArrayList<String> semsect_id;
    ArrayList<String> semsection_title;
    ArrayList<String> quer_id;
    ArrayList<String> que_count;

    TextView choose_subject,choose_level,choose_grade,choose_section,choose_savechanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Session.forceRTLIfSupported(this);
        setContentView(R.layout.choose_level_grade_for_create);
        LinearLayout sub_lv=(LinearLayout)findViewById(R.id.cre_choose_subject);
        LinearLayout level_lv=(LinearLayout)findViewById(R.id.cre_choose_level);
        LinearLayout grade_lv=(LinearLayout)findViewById(R.id.cre_choose_grade);
        LinearLayout semsect_lv=(LinearLayout)findViewById(R.id.cre_choose_section);
        choose_subject = (TextView) findViewById(R.id.cre_subject);
        choose_subject.setText(Session.getword(this,"choose_subject"));
        choose_level = (TextView) findViewById(R.id.cre_level);
        choose_level.setText(Session.getword(this, "choose_levels"));
        choose_grade = (TextView) findViewById(R.id.cre_grade);
        choose_grade.setText(Session.getword(this,"choose_grade"));
        choose_section = (TextView) findViewById(R.id.cre_section);
        choose_section.setText(Session.getword(this, "semester_section"));
        choose_savechanges =(TextView)findViewById(R.id.savechanges);
        choose_savechanges.setText(Session.getword(this,"savechanges"));

        final LinearLayout save_changes=(LinearLayout)findViewById(R.id.cre_save);
        sub_id= new ArrayList<String>();
        sub_title=new ArrayList<String>();
        level_id= new ArrayList<String>();
        level_title=new ArrayList<String>();
        semsect_id= new ArrayList<String>();
        semsection_title=new ArrayList<String>();
        quer_id= new ArrayList<String>();
        que_count=new ArrayList<String>();


        sub_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateChooseSubjectActivity.this);
                builder.setTitle("CHOOSE SUBJECT");
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CreateChooseSubjectActivity.this, android.R.layout.select_dialog_item, sub_title);
                builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(ChooseSubjectActivity.this, sub_title.get(which), Toast.LENGTH_SHORT).show();
                        subect_id = sub_id.get(which);
                        choose_subject.setText(sub_title.get(which));

                    }
                });

                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        get_sub();



        level_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateChooseSubjectActivity.this);
                builder.setTitle(Session.getword(CreateChooseSubjectActivity.this,"choose_levels"));
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CreateChooseSubjectActivity.this, android.R.layout.select_dialog_item, level_title);
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

                AlertDialog.Builder builder = new AlertDialog.Builder(CreateChooseSubjectActivity.this);
                builder.setTitle("CHOOSE GRADE");
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CreateChooseSubjectActivity.this, android.R.layout.select_dialog_item, grade);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateChooseSubjectActivity.this);
                builder.setTitle(Session.getword(CreateChooseSubjectActivity.this,"semester_section"));
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CreateChooseSubjectActivity.this, android.R.layout.select_dialog_item, semsection_title);
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


//
//        ImageView back=(ImageView)findViewById(R.id.back_sub_scr);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quer_id.clear();
                que_count.clear();
                String url = Session.SERVERURL + "questionaries-list.php?";
                try {
                    url = url + "subject=" + URLEncoder.encode(subect_id, "utf-8") +
                            "&level=" + URLEncoder.encode(levels_id, "utf-8") +
                            "&grade=" + URLEncoder.encode(grade_id, "utf-8") +
                            "&semister=" + URLEncoder.encode(sem_sec_id, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("url--->", url);
                final ProgressDialog progressDialog = new ProgressDialog(CreateChooseSubjectActivity.this);
                progressDialog.setMessage("Please wait....");
                progressDialog.setCancelable(false);
                progressDialog.show();
                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();
                        Log.e("response is: ", jsonObject.toString());
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("questionarieslist");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject sub = jsonArray.getJSONObject(i);
                                String ques_count = sub.getString("questions_count");
                                String quers_id = sub.getString("id");
                                quer_id.add(quers_id);
                                que_count.add(ques_count);
                                Intent intent= new Intent(getApplicationContext(),CreateChooseSubjectActivity.class);
                                intent.putExtra("questionaire_id", quer_id);
                                startActivity(intent);
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
                        Toast.makeText(CreateChooseSubjectActivity.this, "Server not connected", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                });

// Access the RequestQueue through your singleton class.
                AppController.getInstance().addToRequestQueue(jsObjRequest);

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

    private void get_sub(){
        String url=Session.SERVERURL+"subjects.php";
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
                Toast.makeText(CreateChooseSubjectActivity.this,"Server not connected",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });

// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsObjRequest);

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
                Toast.makeText(CreateChooseSubjectActivity.this,"Server not connected",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CreateChooseSubjectActivity.this,"Server not connected",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });

// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }


}

