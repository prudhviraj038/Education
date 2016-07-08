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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
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


public class ChooseQuestionaireActivity extends Activity {
    HomeActivity homeActivity=new HomeActivity();
    TextView choosequestion,save_changes;
    String sub_id=homeActivity.subect_id;
    ArrayList<String> que_count;
    ArrayList<String> quer_id;
    Intent intent=getIntent();
    final String levels_id=intent.getStringExtra("level_id");
    final String grade_id=intent.getStringExtra("grade_id");
    final String sem_sec_id=intent.getStringExtra("sem_sec_id");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Session.forceRTLIfSupported(this);
        setContentView(R.layout.choose_questionaire);
        quer_id= new ArrayList<String>();
        que_count= new ArrayList<String>();
        choosequestion = (TextView)findViewById(R.id.choose_que_heading);
        choosequestion.setText(Session.getword(this,"Type your Question"));
        save_changes = (TextView)findViewById(R.id.save_changes_tv);
        save_changes.setText(Session.getword(this,"savechanges"));
        LinearLayout choose_qr=(LinearLayout)findViewById(R.id.choose_qur);
        choose_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChooseQuestionaireActivity.this);
                builder.setTitle(Session.getword(ChooseQuestionaireActivity.this,"choose_question"));
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ChooseQuestionaireActivity.this, android.R.layout.select_dialog_item, quer_id);
                builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //     Toast.makeText(ChooseSubjectActivity.this, quer_id.get(which), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), AnswerAQuestionActivity.class);
                        intent.putExtra("questionair_id", quer_id.get(which));
                        startActivity(intent);

                    }
                });

                final AlertDialog dialog = builder.create();
                dialog.show();
            }});
        get_choose_quer_id();
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


    private void get_choose_quer_id(){
        quer_id.clear();
        que_count.clear();
        String url = Session.SERVERURL + "questionaries-list.php?";
        try {
            url = url + "subject=" + URLEncoder.encode(sub_id, "utf-8") +
                    "&level=" + URLEncoder.encode(levels_id, "utf-8") +
                    "&grade=" + URLEncoder.encode(grade_id, "utf-8") +
                    "&semister=" + URLEncoder.encode(sem_sec_id, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.e("url--->", url);
        final ProgressDialog progressDialog = new ProgressDialog(ChooseQuestionaireActivity.this);
        progressDialog.setMessage(Session.getword(this,"loading"));
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
                Toast.makeText(ChooseQuestionaireActivity.this, "Server not connected", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });

// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }

    }

