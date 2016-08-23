package com.yellowsoft.education;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SelectSubjectsActivity extends RootActivity {
    ArrayAdapter<String> adapter;
    HashMap<String, String> choices;
    ArrayList<String> sub_id;
    ArrayList<String> sub_title;
    String type;
    Intent intent=getIntent();
    String level,semister,grade,uname,password,fullname,email,mobile,gove,classs;
    String img_path;
    String mem_id;
    TextView chose_subject,save_changes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Session.forceRTLIfSupported(this);
        setContentView(R.layout.select_subjects);
        choices = new HashMap<>();
        sub_id = new ArrayList<String>();
        sub_title = new ArrayList<String>();
        chose_subject = (TextView)findViewById(R.id.choose_sub_heading);
        chose_subject.setText(Session.getword(this,"choose_subject"));
        save_changes = (TextView)findViewById(R.id.select_changes);
        save_changes.setText(Session.getword(this,"savechanges"));

        Intent intent=getIntent();
        type = getIntent().getStringExtra("type");
         if(type.equals("normal")) {
            level = intent.getStringExtra("level_id");
            grade = intent.getStringExtra("grade_id");
            semister = intent.getStringExtra("sem_id");
            uname = intent.getStringExtra("usname");
            password = intent.getStringExtra("psword");
            fullname = intent.getStringExtra("fname");
            email = intent.getStringExtra("email");
            mobile = intent.getStringExtra("mobi");
            gove = intent.getStringExtra("gover");
            classs = intent.getStringExtra("clas");
            img_path = intent.getStringExtra("image_path");
        }
        else{

         }
        LinearLayout save_changes=(LinearLayout)findViewById(R.id.save_change_fin);
        save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("change")){
                    edit_profile();
                }else{
                    if(choices.size()>5)
                        Toast.makeText(SelectSubjectsActivity.this, Session.getword(SelectSubjectsActivity.this,"select_minimum5"), Toast.LENGTH_SHORT).show();

                    else
                        register();
                }

            }
        });

        adapter = new ArrayAdapter<String>(this, R.layout.list_item, sub_title);
        LinearLayout sub_ly = (LinearLayout) findViewById(R.id.ly_subjects);
        sub_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView lv = (ListView) findViewById(R.id.lv1);
                lv.setAdapter(adapter);
                lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (choices.containsKey(String.valueOf(position)))
                            choices.remove(String.valueOf(position));
                        else {
                            choices.put(String.valueOf(position), sub_id.get(position));
                        }
                        JSONObject jsonObject = new JSONObject(choices);
                        Log.e("choice", jsonObject.toString());
                    }
                });
            }
        });
        get_sub();
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
                Toast.makeText(SelectSubjectsActivity.this, "Server not connected", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });

// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsObjRequest);

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
                            if(jsonObject.getString("status").equals("Failed")){
                                Toast.makeText(SelectSubjectsActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                // Toast.makeText(SelectSubjectsActivity.this,response , Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getApplicationContext(), Session.getword(SelectSubjectsActivity.this,"updated_successfully"), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(SelectSubjectsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                String csv = "-1";
                for (Map.Entry<String, String> entry : choices.entrySet()) {
                    if (csv.equals("-1"))
                        csv = entry.getValue();
                    else
                        csv = csv + "," + entry.getValue();
                }
                params.put("subject", csv);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
        String csv = "-1";
        for (Map.Entry<String, String> entry : choices.entrySet()){
            if(csv.equals("-1"))
                csv  = entry.getValue();
            else
                csv=csv+","+entry.getValue();
        }
        Log.e("subjects", csv);

    }


    private void register(){

        final JSONObject jsonObject = new JSONObject(choices);
        Log.e("sub_arra", jsonObject.toString());
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait..");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String url = Session.SERVERURL+"add-member.php?";
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
                                Toast.makeText(SelectSubjectsActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                    // Toast.makeText(SelectSubjectsActivity.this,response , Toast.LENGTH_SHORT).show();
                                    mem_id = jsonObject.getString("member_id");
                                    if (img_path != null)
                                        encodeImagetoString();
                                    else {
                                        Toast.makeText(getApplicationContext(), Session.getword(SelectSubjectsActivity.this,"register_successfull"),
                                                Toast.LENGTH_LONG).show();
                                        Intent in_login = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(in_login);
                                        finish();

                                    }
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
                        Toast.makeText(SelectSubjectsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                    params.put("username", uname);
                    params.put("password", password);
                    params.put("name", fullname);
                    params.put("email", email);
                    params.put("phone", mobile);
                    params.put("governorate", gove);
                    params.put("class", classs);
                    params.put("level", level);
                    params.put("grade", grade);
                    params.put("semister", semister);
                    String csv = "-1";
                    for (Map.Entry<String, String> entry : choices.entrySet()) {
                        if (csv.equals("-1"))
                            csv = entry.getValue();
                        else
                            csv = csv + "," + entry.getValue();
                    }
                    params.put("subject", csv);
                return params;
            }

        };
      AppController.getInstance().addToRequestQueue(stringRequest);
        Log.e("username", uname);
        Log.e("password", password);
        Log.e("name", fullname);
        Log.e("email", email);
        Log.e("phone", mobile);
        Log.e("governorate", gove);
        Log.e("class", classs);
        Log.e("level", level);
        Log.e("grade", grade);
        Log.e("semister", semister);
        String csv = "-1";
        for (Map.Entry<String, String> entry : choices.entrySet()) {
            if (csv.equals("-1"))
                csv = entry.getValue();
            else
                csv = csv + "," + entry.getValue();
        }
        Log.e("subject", csv);


    }



    RequestParams params = new RequestParams();
    public void makeHTTPCall() {


        AsyncHttpClient client = new AsyncHttpClient();
        // Don't forget to change the IP address to your LAN address. Port no as well.
        params.put("file", encodedString);
        params.put("ext_str", "jpg");
        params.put("member_id", mem_id);

        client.post(Session.SERVERURL + "add-member-image.php",
                params, new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http
                    // response code '200'
                    @Override
                    public void onSuccess(String response) {
                        // Hide Progress Dialog
                        Log.e("success", response);

                        Toast.makeText(getApplicationContext(), Session.getword(SelectSubjectsActivity.this,"register_successfull"),
                                Toast.LENGTH_LONG).show();
                        Intent in_login = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(in_login);
                        finish();

                    }


                    // When the response returned by REST has Http
                    // response code other than '200' such as '404',
                    // '500' or '403' etc
                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        // Hide Progress Dialog

                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(),
                                    "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(getApplicationContext(),
                                    "Something went wrong at server end",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Error Occured \n Most Common Error: \n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : "
                                            + statusCode, Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
    }
    Bitmap bitmap;
    String encodedString;

    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            };

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                encodedString = "";
                bitmap = BitmapFactory.decodeFile(img_path, options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, Base64.NO_WRAP);


                return "";
            }

            @Override
            protected void onPostExecute(String msg) {

                // Put converted Image string into Async Http Post param
                // Trigger Image upload
                makeHTTPCall();
            }
        }.execute(null, null, null);
    }


    private void get_user_details() {
        String url = Session.SERVERURL + "members-list.php?";
        try {
            url = url + "member_id="+ URLEncoder.encode(Session.getUserid(this), "utf-8");
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
                    Session.setUserdetails(SelectSubjectsActivity.this,jsonObject.toString());
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("response is:", error.toString());
                Toast.makeText(SelectSubjectsActivity.this, "Server not connected", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });

// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }


}

