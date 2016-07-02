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


public class LoginActivity extends Activity {
    EditText et_uname;
    EditText et_password;
    String write;
    String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Session.forceRTLIfSupported(this);
        setContentView(R.layout.login_screen);
        TextView signup=(TextView)findViewById(R.id.signup);
        TextView forgotpassword=(TextView)findViewById(R.id.forgotpass);
        LinearLayout signin=(LinearLayout)findViewById(R.id.ll_submit_pass);
         et_uname=(EditText)findViewById(R.id.et_login_uname);
         et_password=(EditText)findViewById(R.id.et_login_pass);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            logindetails();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                intent.putExtra("type","normal");
                startActivity(intent);
            }
        });
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                alert.setTitle("forgot_password_sent");
                final EditText input = new EditText(LoginActivity.this);
                input.setHint("Enter your email id");
                input.setMinLines(5);
                input.setVerticalScrollBarEnabled(true);
//                input.setBackgroundResource(R.drawable.comments_bg);
                input.setPadding(10, 10, 10, 10);
                alert.setView(input);
                alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        write = input.getText().toString();
                        if (write.equals(""))
                            Toast.makeText(LoginActivity.this, "please_enter_email", Toast.LENGTH_SHORT).show();
                        else if (!write.matches(emailPattern))
                            Toast.makeText(LoginActivity.this, "Please Enter Valid Email id", Toast.LENGTH_SHORT).show();
                        else
                            forgot_pass();
                    }
                });
                alert.show();
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
    private void logindetails() {
        String uname = et_uname.getText().toString();
        String password = et_password.getText().toString();
        if (uname.equals(""))
            Toast.makeText(LoginActivity.this, "Please Enter UserName", Toast.LENGTH_SHORT).show();
        else if (password.equals(""))
            Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        else if (password.length() < 6)
            Toast.makeText(LoginActivity.this, "Password Lenth should be grether than 6 charcters", Toast.LENGTH_SHORT).show();
        else {
            String url = Session.SERVERURL+"login.php?";
            try {
                url = url + "username="+ URLEncoder.encode(uname, "utf-8")+
                        "&password="+URLEncoder.encode(password,"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
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
                        String reply=jsonObject.getJSONArray("response").getJSONObject(0).getString("status");
                        if(reply.equals("Failure")) {
                            String msg = jsonObject.getJSONArray("response").getJSONObject(0).getString("message");
                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String mem_id=jsonObject.getJSONArray("response").getJSONObject(0).getString("member_id");
                            String name=jsonObject.getJSONArray("response").getJSONObject(0).getString("name");
                            Session.setUserid(LoginActivity.this,mem_id,name);
                            Intent mainIntent= new Intent(getApplicationContext(),HomeActivity.class);
                            mainIntent.putExtra("uid",mem_id);
                            startActivity(mainIntent);
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
                    Toast.makeText(LoginActivity.this, "Server not connected", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            });

// Access the RequestQueue through your singleton class.
            AppController.getInstance().addToRequestQueue(jsObjRequest);

        }

    }
    public void forgot_pass(){
        String url = Session.SERVERURL+"forget-password.php?email="+write;
        Log.e("url--->", url);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
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
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(LoginActivity.this, "Server not connected", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });

// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }

}
