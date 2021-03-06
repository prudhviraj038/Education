package com.techbox.education;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInstaller;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SignupActivity extends RootActivity {

    EditText et_uname;
    EditText et_password;
    EditText et_fullname;
    EditText et_email;
    EditText et_mobile;
    TextView et_gove,area_tv,class_tv,level_tv,grade_tv;
    EditText et_class;
    ImageView profile_image;
    LinearLayout gove_ll,area_ll,class_ll,level_ll,grade_ll;
    String type;
    TextView signup_txt;
    String area_id,gove_id,class_id,area_name,gove_name;
    int posi;
    ArrayList<Governorate> governorates;
    ArrayList<String> gove_titles;
    ArrayList<String> area_titles;
    ArrayList<String> class_titles;
    ArrayList<String> class_ids;
    ArrayList<String> grade;
    ImageView gov_drop,school_drop,class_drop,grade_drop,level_drop;
    LinearLayout academic_details;
    TextView leve_h,level_v,grade_v,grade_h,semister_h,semister_v,other_heading,sub_heading,sub_value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Session.forceRTLIfSupported(this);
        setContentView(R.layout.signup_screen);
        final int RESULT_LOAD_IMAGE = 1;
        type = getIntent().getStringExtra("type");
        gov_drop = (ImageView) findViewById(R.id.gov_drop);
        school_drop = (ImageView) findViewById(R.id.school_drop);
        class_drop = (ImageView) findViewById(R.id.class_drop);
        grade_drop = (ImageView) findViewById(R.id.grade_drop);
        level_drop = (ImageView) findViewById(R.id.level_drop);

        governorates=new ArrayList<>();
        gove_titles=new ArrayList<>();
        area_titles=new ArrayList<>();
        class_titles = new ArrayList<>();
        class_ids = new ArrayList<>();
        gove_id = "";
        area_id= "";
        class_id = "";
        classs = "";

        gove_ll=(LinearLayout)findViewById(R.id.gove_ll);
        area_ll=(LinearLayout)findViewById(R.id.area_ll);
        class_ll=(LinearLayout) findViewById(R.id.class_ll);
        level_ll = (LinearLayout) findViewById(R.id.level_ll);
        grade_ll = (LinearLayout) findViewById(R.id.grade_ll);

        ImageView back=(ImageView)findViewById(R.id.back_signup_scr);
        TextView selectimg=(TextView)findViewById(R.id.select_image);
        selectimg.setText(Session.getword(this,"selct_image"));
        TextView choose_sch_img = (TextView)findViewById(R.id.choose_image_heading);
        choose_sch_img.setText(Session.getword(this,"choose_your_school_image_only"));
        LinearLayout signup=(LinearLayout)findViewById(R.id.ll_signup);
        LinearLayout pass_ll=(LinearLayout)findViewById(R.id.pass_ll);
        LinearLayout user_ll=(LinearLayout)findViewById(R.id.user_name_ll);
        LinearLayout email_ll=(LinearLayout)findViewById(R.id.email_ll);
        signup_txt = (TextView) findViewById(R.id.signup2);

         et_uname = (EditText)findViewById(R.id.et_username);
         et_uname.setHint(Session.getword(this, "username"));
         et_password=(EditText)findViewById(R.id.et_password);
        et_password.setHint(Session.getword(this, "password"));
         et_fullname=(EditText)findViewById(R.id.et_fullname);
        et_fullname.setHint(Session.getword(this, "fullname"));
         et_email=(EditText)findViewById(R.id.et_email);
        et_email.setHint(Session.getword(this, "email"));
         et_mobile=(EditText)findViewById(R.id.et_mobile);
        et_mobile.setHint(Session.getword(this, "mobile"));
         et_gove=(TextView)findViewById(R.id.et_gove);
        et_gove.setText(Session.getword(this, "governorate"));
        area_tv=(TextView)findViewById(R.id.area_tv);
        area_tv.setText(Session.getword(this,"school"));
         et_class=(EditText)findViewById(R.id.et_class);
        et_class.setHint(Session.getword(this, "class"));

        class_tv = (TextView) findViewById(R.id.class_tv);
        class_tv.setText(Session.getword(this, "class"));

        level_tv = (TextView) findViewById(R.id.level_tv);
        level_tv.setText(Session.getword(this, "level"));

        grade_tv = (TextView) findViewById(R.id.grade_tv);
        grade_tv.setText(Session.getword(this, "grade"));

        academic_details = (LinearLayout) findViewById(R.id.other_details);
        other_heading = (TextView) findViewById(R.id.other_heading);

        leve_h = (TextView) findViewById(R.id.level_heading);
        level_v = (TextView) findViewById(R.id.level_value);

        grade_h = (TextView) findViewById(R.id.grrade_heading);
        grade_v = (TextView) findViewById(R.id.grade_value);


        semister_h = (TextView) findViewById(R.id.semister_heading);
        semister_v = (TextView) findViewById(R.id.semister_value);

        sub_heading = (TextView) findViewById(R.id.subjects_heading);
        sub_value = (TextView) findViewById(R.id.subjects_value);


        other_heading.setText(Session.getword(this,"academic_details"));

        leve_h.setText(Session.getword(this,"level"));
        grade_h.setText(Session.getword(this,"grade"));
        semister_h.setText(Session.getword(this,"semister"));
        sub_heading.setText(Session.getword(this,"subjects"));

        profile_image = (ImageView) findViewById(R.id.profile_image);

        selectimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //startActivityForResult(i, RESULT_LOAD_IMAGE);
                selectphotos();

            }
        });
        //llkk
        get_area();
        gove_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SignupActivity.this);
                builder.setTitle("Governorates");
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SignupActivity.this, android.R.layout.select_dialog_item, gove_titles);
                builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(SignupActivity.this, book_title.get(which), Toast.LENGTH_SHORT).show();
                        posi = which;
                        gove_id = governorates.get(which).id;
                        area_id = "";
                        et_gove.setText(governorates.get(which).getTitle(SignupActivity.this));
                        area_tv.setText(Session.getword(SignupActivity.this, "school"));
                        area_titles.clear();
                        for (int i = 0; i < governorates.get(which).are.size(); i++) {
                            area_titles.add(governorates.get(which).are.get(i).getATitle(SignupActivity.this));
                        }
                    }
                });
                final android.app.AlertDialog dialog = builder.create();
                dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                if(!type.equals("change"))
                dialog.show();
            }
        });

        area_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!gove_id.equals("")) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SignupActivity.this);
                    builder.setTitle("Select School");
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SignupActivity.this, android.R.layout.select_dialog_item, area_titles);
                    builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(SignupActivity.this, book_title.get(which), Toast.LENGTH_SHORT).show();
                            area_id = governorates.get(posi).are.get(which).a_id;
                            area_tv.setText(governorates.get(posi).are.get(which).getATitle(SignupActivity.this));
                            Picasso.with(SignupActivity.this).load(governorates.get(posi).are.get(which).school_image).into(profile_image);
                        }
                    });
                    final android.app.AlertDialog dialog = builder.create();
                    dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                    if(!type.equals("change"))
                    dialog.show();
                }
                else{
                    if(!type.equals("change"))
                    Toast.makeText(SignupActivity.this, Session.getword(SignupActivity.this,"Pls_ent_gover"), Toast.LENGTH_SHORT).show();

                }
            }
        });


        class_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SignupActivity.this);
                builder.setTitle("Classes");
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SignupActivity.this,
                        android.R.layout.select_dialog_item, class_titles);
                builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(SignupActivity.this, book_title.get(which), Toast.LENGTH_SHORT).show();
                        classs = class_ids.get(which);
                        class_tv.setText(class_titles.get(which));
                    }
                });
                final android.app.AlertDialog dialog = builder.create();
                dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                if(!type.equals("change"))
                dialog.show();
            }

        });

        level_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_level();
            }
        });

        grade_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grade = new ArrayList<String>();
                grade.add("1");
                grade.add("2");
                grade.add("3");
                grade.add("4");
                grade.add("5");
                grade.add("6");

                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                builder.setTitle("CHOOSE GRADE");
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SignupActivity.this, android.R.layout.select_dialog_item, grade);
                builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(ChooseSubjectActivity.this, grade.get(which), Toast.LENGTH_SHORT).show();
                        grade_id = grade.get(which);
                        grade_tv.setText(grade.get(which));
                    }
                });

                final AlertDialog dialog = builder.create();
                dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                dialog.show();

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(type.equals("change")){
            pass_ll.setVisibility(View.GONE);
            user_ll.setVisibility(View.GONE);
            email_ll.setVisibility(View.GONE);
            signup_txt.setText(Session.getword(this, "save"));
            signup.setVisibility(View.GONE);
           // academic_details.setVisibility(View.VISIBLE);


        }else{
            pass_ll.setVisibility(View.VISIBLE);
            user_ll.setVisibility(View.VISIBLE);
            email_ll.setVisibility(View.VISIBLE);
            signup_txt.setText(Session.getword(this,"signUp"));
            academic_details.setVisibility(View.GONE);
        }
        if(type.equals("change"))
        try {
            JSONObject jsonObject=new JSONObject(Session.getUserdetails(SignupActivity.this));
            et_uname.setText(jsonObject.getString("username"));
            et_fullname.setText(jsonObject.getString("name"));
            et_email.setText(jsonObject.getString("email"));
            et_mobile.setText(jsonObject.getString("phone"));
            et_gove.setText(jsonObject.getJSONObject("school").getString("governate" + Session.get_append(this)));
            area_tv.setText(jsonObject.getJSONObject("school").getString("title" + Session.get_append(this)));
            class_tv.setText(jsonObject.getString("class_name"+ Session.get_append(this)));
            et_fullname.setEnabled(false);
            et_mobile.setEnabled(false);
            et_uname.setEnabled(false);
            et_email.setEnabled(false);
            level_tv.setText(jsonObject.getJSONObject("level").getString("title" + Session.get_append(this)));
            grade_tv.setText(jsonObject.getString("grade"));
            semister_v.setText(jsonObject.getJSONObject("semister").getString("title" + Session.get_append(this)));
            class_drop.setVisibility(View.INVISIBLE);
            level_drop.setVisibility(View.INVISIBLE);
            grade_drop.setVisibility(View.INVISIBLE);
            gov_drop.setVisibility(View.INVISIBLE);
            school_drop.setVisibility(View.INVISIBLE);
            Picasso.with(SignupActivity.this).load(jsonObject.getString("image")).into(profile_image);
          //  sub_value.setText(Html.fromHtml(jsonObject.getString("subjects_csv"+ Session.get_append(this))));


            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register();
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
    String uname,password,fullname,email,mobile,gove,classs;
    private void register() {
         uname = et_uname.getText().toString();
         password = et_password.getText().toString();
         fullname = et_fullname.getText().toString();
         email = et_email.getText().toString();
         mobile = et_mobile.getText().toString();

         if (gove_id.equals(""))
            Toast.makeText(SignupActivity.this, Session.getword(this,"Pls_ent_gover"), Toast.LENGTH_SHORT).show();
        else if (area_id.equals(""))
            Toast.makeText(SignupActivity.this,Session.getword(this,"Pls_ent_school"), Toast.LENGTH_SHORT).show();
         else if (levels_id.equals(""))
             Toast.makeText(SignupActivity.this,Session.getword(this,"Pls_sele_level"), Toast.LENGTH_SHORT).show();
         else if (classs.equals(""))
            Toast.makeText(SignupActivity.this,Session.getword(this,"Pls_ent_class"), Toast.LENGTH_SHORT).show();
         /*else if (grade_id.equals(""))
             Toast.makeText(SignupActivity.this,Session.getword(this,"Pls_sele_grade"), Toast.LENGTH_SHORT).show();
         */else if (fullname.equals(""))
            Toast.makeText(SignupActivity.this,Session.getword(this,"pls_ent_fullname"), Toast.LENGTH_SHORT).show();
        else if (email.equals(""))
            Toast.makeText(SignupActivity.this, Session.getword(this,"Please enter EmailID"), Toast.LENGTH_SHORT).show();
        else if (mobile.equals(""))
            Toast.makeText(SignupActivity.this,Session.getword(this,"Pls_ent_Mob_Num"), Toast.LENGTH_SHORT).show();
        else if (uname.equals(""))
            Toast.makeText(SignupActivity.this, Session.getword(this,"Pls_ent_username"), Toast.LENGTH_SHORT).show();
        else if (type.equals("normal")&&password.equals(""))
            Toast.makeText(SignupActivity.this, Session.getword(this,"Pls_ent_Password"),Toast.LENGTH_SHORT).show();
        else if (type.equals("normal")&&password.length() < 6)
            Toast.makeText(SignupActivity.this, Session.getword(this,"password_length"), Toast.LENGTH_SHORT).show();
         else{
             if(true){
                edit_profile();
            }else{
            Intent signup_intent = new Intent(getApplicationContext(), ChooseLevelGradeSemActivity.class);
                signup_intent.putExtra("type","normal");
            signup_intent.putExtra("username", uname);
            signup_intent.putExtra("password", password);
            signup_intent.putExtra("fname", fullname);
            signup_intent.putExtra("email", email);
            signup_intent.putExtra("mobile", mobile);
            signup_intent.putExtra("gover", area_id);
            signup_intent.putExtra("class", classs);
            signup_intent.putExtra("image_path", imgPath);
            startActivity(signup_intent);
            }
        }

    }
    private void edit_profile(){
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
                                Toast.makeText(SignupActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            else {


                                Toast.makeText(getApplicationContext(), Session.getword(SignupActivity.this,"register_successfull"),
                                        Toast.LENGTH_LONG).show();
                                Intent in_login = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(in_login);
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
                        Toast.makeText(SignupActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("name",fullname);
                params.put("phone",mobile);
                params.put("governorate",area_id);
                params.put("class",classs);
                params.put("username", uname);
                params.put("password", password);
                params.put("email", email);
                params.put("level", levels_id);
                params.put("grade", "0");
                params.put("semister", "0");
                params.put("subject", "0");

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
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
                    Session.setUserdetails(SignupActivity.this,jsonObject.toString());
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
                Toast.makeText(SignupActivity.this, "Server not connected", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });

// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsObjRequest);
    }
    private void get_area() {
        String url = null;
        try {
            url = Session.SERVERURL+"schools.php?";
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("url--->", url);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please_wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                progressDialog.dismiss();
                Log.e("orders response is: ", jsonArray.toString());
                get_classes();
                for(int i=0;i<jsonArray.length();i++){
                    Governorate governorate= null;
                    try {
                        governorate = new Governorate(jsonArray.getJSONObject(i));
                        governorates.add(governorate);
                        gove_titles.add(governorates.get(i).getTitle(SignupActivity.this));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("response is:", error.toString());
//                Toast.makeText(getActivity(), Settings.getword(getActivity(), "server_not_connected"), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                get_classes();
            }

        });
// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }
    private void get_classes() {
        String url = null;
        try {
            url = Session.SERVERURL+"classes.php?";
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("url--->", url);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please_wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                progressDialog.dismiss();
                Log.e("orders response is: ", jsonArray.toString());
                class_titles = new ArrayList<>();
                for(int i=0;i<jsonArray.length();i++){
                    try {

                        class_titles.add(jsonArray.getJSONObject(i).getString("title"+Session.get_append(SignupActivity.this)));
                        class_ids.add(jsonArray.getJSONObject(i).getString("id"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("response is:", error.toString());
//                Toast.makeText(getActivity(), Settings.getword(getActivity(), "server_not_connected"), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });
// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }
    ArrayList<String> level_id;
    ArrayList<String> level_title;
    String levels_id="";
    String grade_id="";
    private void get_level(){
        level_id = new ArrayList<>();
        level_title = new ArrayList<>();
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
                    show_level_alert();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("response is:", error.toString());
                Toast.makeText(SignupActivity.this,"Server not connected",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });

// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }

    private void show_level_alert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
        builder.setTitle(Session.getword(SignupActivity.this,"choose_levels"));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SignupActivity.this, android.R.layout.simple_dropdown_item_1line, level_title);
        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(ChooseSubjectActivity.this, level_title.get(which), Toast.LENGTH_SHORT).show();
                levels_id = level_id.get(which);
                level_tv.setText(level_title.get(which));
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.show();

    }

    Bitmap bitmap;
    String encodedString;
    private Uri mImageCaptureUri;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    Bitmap sample;
    String imgPath;

    public void selectphotos() {
        final String[] items = new String[]{"camera", "gallery"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("select_image");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory(),
                            "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                    mImageCaptureUri = Uri.fromFile(file);
                    try {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    dialog.cancel();
                } else {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(galleryIntent, PICK_FROM_FILE);
                }
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.show();
    }


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
                        bitmap = BitmapFactory.decodeFile(imgPath, options);
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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FROM_FILE && resultCode == RESULT_OK ) {
            mImageCaptureUri = data.getData();
            String path = getRealPathFromURI(mImageCaptureUri); //from Gallery

            if (path == null)
                path = mImageCaptureUri.getPath();
            //from File Manag\\
            if (path != null)
                imgPath = path;
            Intent intent = new Intent(this, ImageEditActivity.class);
            intent.putExtra("image_path", imgPath);
            intent.putExtra("image_source", "gallery");
            intent.putExtra("rotation", String.valueOf(getCameraPhotoOrientation(this,mImageCaptureUri,imgPath)));
            startActivityForResult(intent, 4);

        } else if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK ) {
            String path = mImageCaptureUri.getPath();
            imgPath = path;
            Intent intent = new Intent(this, ImageEditActivity.class);
            intent.putExtra("image_path", imgPath);
            intent.putExtra("image_source", "device_cam");
            intent.putExtra("rotation", String.valueOf(getCameraPhotoOrientation(this,mImageCaptureUri,imgPath)));
            startActivityForResult(intent, 4);

        }
        else if (requestCode == 4) {
            String file_path = data.getStringExtra("image_path");
            Log.e("ile_path", file_path);
            sample = BitmapFactory.decodeFile(file_path);

            //Picasso.with(this).load(new File(file_path)).rotate(getCameraPhotoOrientation(this,mImageCaptureUri,file_path))
            //  .into(profile_image);
            imgPath = file_path;
            profile_image.setImageBitmap(sample);
        }
                else{
            Log.e("activity","not returned");
        }
    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);

        if (cursor == null) return null;

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(column_index);
    }
    public int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath){
        int rotate = 0;
        try {
            //  context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            Log.i("RotateImage", "Exif orientation: " + orientation);
            Log.i("RotateImage", "Rotate value: " + rotate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    RequestParams params = new RequestParams();
    public void makeHTTPCall() {


        AsyncHttpClient client = new AsyncHttpClient();
        // Don't forget to change the IP address to your LAN address. Port no as well.
        params.put("file", encodedString);
        params.put("ext_str", "jpg");
        params.put("member_id", Session.getUserid(this));

        client.post(Session.SERVERURL + "add-member-image.php",
                params, new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http
                    // response code '200'
                    @Override
                    public void onSuccess(String response) {
                        // Hide Progress Dialog
                        Log.e("success", response);

                        Toast.makeText(getApplicationContext(), Session.getword(SignupActivity.this,"updated_successfully"),
                                Toast.LENGTH_LONG).show();
                        get_user_details();

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

}