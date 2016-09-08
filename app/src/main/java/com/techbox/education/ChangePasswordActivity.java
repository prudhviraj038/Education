package com.techbox.education;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class ChangePasswordActivity extends RootActivity {
    EditText new_pass,conf_pass;
    TextView sub_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Session.forceRTLIfSupported(this);
        setContentView(R.layout.change_password_screen);
        LinearLayout submit_ll=(LinearLayout)findViewById(R.id.ll_submit_pass);
        new_pass=(EditText)findViewById(R.id.new_password);

        conf_pass=(EditText)findViewById(R.id.confirm_password);
        sub_pass=(TextView)findViewById(R.id.submit_change_pass);
        sub_pass.setText(Session.getword(this,"submit"));


        submit_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_password();
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
    private void change_password() {
        String n_pass = new_pass.getText().toString();
        new_pass.setText(Session.getword(this, "new_password"));
        Log.e("pass",n_pass);
        String c_pass = conf_pass.getText().toString();
        if (n_pass.equals(""))
            Toast.makeText(ChangePasswordActivity.this, Session.getword(ChangePasswordActivity.this,"new_password"), Toast.LENGTH_SHORT).show();
        else if (n_pass.length() < 6)
            Toast.makeText(ChangePasswordActivity.this, Session.getword(ChangePasswordActivity.this,"password_length"), Toast.LENGTH_SHORT).show();
        else if (!c_pass.equals(n_pass))
            Toast.makeText(ChangePasswordActivity.this,Session.getword(ChangePasswordActivity.this,"confirm_password"), Toast.LENGTH_SHORT).show();

        else {
            String url = Session.SERVERURL+"change-password.php?";
            try {
                url = url +"member_id="+ URLEncoder.encode(Session.getUserid(this), "utf-8")+"&password="+ URLEncoder.encode(n_pass, "utf-8")+
                        "&cpassword="+URLEncoder.encode(c_pass,"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.e("url--->", url);
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(Session.getword(this,"loading"));
            progressDialog.setCancelable(false);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jsonObject) {
                    progressDialog.dismiss();
                    Log.e("response is: ", jsonObject.toString());
                    try {
                        String reply=jsonObject.getString("status");
                        if(reply.equals("Failed")) {
                            String msg = jsonObject.getString("message");
                            Toast.makeText(ChangePasswordActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String msg=jsonObject.getString("message");
                            Toast.makeText(ChangePasswordActivity.this, msg, Toast.LENGTH_SHORT).show();
                            finish();
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
                    Toast.makeText(ChangePasswordActivity.this, "Server not connected", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            });

// Access the RequestQueue through your singleton class.
            AppController.getInstance().addToRequestQueue(jsObjRequest);

        }

    }
}
