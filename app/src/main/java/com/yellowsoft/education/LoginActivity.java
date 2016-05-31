package com.yellowsoft.education;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        TextView signup=(TextView)findViewById(R.id.signup);
        TextView forgotpassword=(TextView)findViewById(R.id.forgotpass);
        LinearLayout signin=(LinearLayout)findViewById(R.id.ll_signin);
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
                startActivity(intent);
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
}
