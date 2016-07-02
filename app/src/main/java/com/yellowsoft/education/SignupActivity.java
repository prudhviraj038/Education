package com.yellowsoft.education;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import java.util.HashMap;
import java.util.Map;


public class SignupActivity extends Activity {

    EditText et_uname;
    EditText et_password;
    EditText et_fullname;
    EditText et_email;
    EditText et_mobile;
    EditText et_gove;
    EditText et_class;
    ImageView profile_image;
    String type;
    TextView signup_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);
        final int RESULT_LOAD_IMAGE = 1;
        type = getIntent().getStringExtra("type");
        ImageView back=(ImageView)findViewById(R.id.back_signup_scr);
        TextView selectimg=(TextView)findViewById(R.id.select_image);
        LinearLayout signup=(LinearLayout)findViewById(R.id.ll_signup);
        LinearLayout pass_ll=(LinearLayout)findViewById(R.id.pass_ll);
        LinearLayout user_ll=(LinearLayout)findViewById(R.id.user_name_ll);
        LinearLayout email_ll=(LinearLayout)findViewById(R.id.email_ll);
        signup_txt = (TextView) findViewById(R.id.signup2);

         et_uname = (EditText)findViewById(R.id.et_username);
         et_password=(EditText)findViewById(R.id.et_password);
         et_fullname=(EditText)findViewById(R.id.et_fullname);
         et_email=(EditText)findViewById(R.id.et_email);
         et_mobile=(EditText)findViewById(R.id.et_mobile);
         et_gove=(EditText)findViewById(R.id.et_gove);
         et_class=(EditText)findViewById(R.id.et_class);
            profile_image = (ImageView) findViewById(R.id.profile_image);
        selectimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //startActivityForResult(i, RESULT_LOAD_IMAGE);
                selectphotos();

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
            signup_txt.setText("SAVE");

        }else{
            pass_ll.setVisibility(View.VISIBLE);
            user_ll.setVisibility(View.VISIBLE);
            email_ll.setVisibility(View.VISIBLE);
            signup_txt.setText("SIGN UP");

        }
        if(type.equals("change"))
        try {
            JSONObject jsonObject=new JSONObject(Session.getUserdetails(SignupActivity.this));
            et_uname.setText(jsonObject.getString("username"));
            et_fullname.setText(jsonObject.getString("name"));
            et_email.setText(jsonObject.getString("email"));
            et_mobile.setText(jsonObject.getString("phone"));
            et_gove.setText(jsonObject.getString("governorate"));
            et_class.setText(jsonObject.getString("class"));
            Picasso.with(SignupActivity.this).load(jsonObject.getString("image")).into(profile_image);
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
         gove = et_gove.getText().toString();
         classs = et_class.getText().toString();

        if (uname.equals(""))
            Toast.makeText(SignupActivity.this, "Please Enter UserName", Toast.LENGTH_SHORT).show();
        else if (type.equals("normal")&&password.equals(""))
            Toast.makeText(SignupActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        else if (type.equals("normal")&&password.length() < 6)
            Toast.makeText(SignupActivity.this, "Password Lenth should be grether than 6 charcters", Toast.LENGTH_SHORT).show();
        else if (fullname.equals(""))
            Toast.makeText(SignupActivity.this, "Please Enter Fullname", Toast.LENGTH_SHORT).show();
        else if (email.equals(""))
            Toast.makeText(SignupActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
        else if (mobile.equals(""))
            Toast.makeText(SignupActivity.this, "Please Enter Mobile", Toast.LENGTH_SHORT).show();
        else if (gove.equals(""))
            Toast.makeText(SignupActivity.this, "Please Enter Govenerate", Toast.LENGTH_SHORT).show();
        else if (classs.equals(""))
            Toast.makeText(SignupActivity.this, "Please Enter Class", Toast.LENGTH_SHORT).show();
        else {
            if(type.equals("change")){
                edit_profile();
            }else{
            Intent signup_intent = new Intent(getApplicationContext(), ChooseLevelGradeSemActivity.class);
                signup_intent.putExtra("type","normal");
            signup_intent.putExtra("username", uname);
            signup_intent.putExtra("password", password);
            signup_intent.putExtra("fname", fullname);
            signup_intent.putExtra("email", email);
            signup_intent.putExtra("mobile", mobile);
            signup_intent.putExtra("gover", gove);
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
        String url = Session.SERVERURL+"edit-member.php";

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
                                Toast.makeText(SignupActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                // Toast.makeText(SelectSubjectsActivity.this,response , Toast.LENGTH_SHORT).show();
                               // String mem_id = jsonObject.getString("member_id");

                                if(imgPath!=null)
                                    encodeImagetoString();
                                else{
                                    Toast.makeText(getApplicationContext(), "Profile updated Succesfully", Toast.LENGTH_LONG).show();
                                    get_user_details();

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
                        Toast.makeText(SignupActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("member_id",Session.getUserid(SignupActivity.this));
                params.put("name",fullname);
                params.put("phone",mobile);
                params.put("governorate",gove);
                params.put("class",classs);
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

                        Toast.makeText(getApplicationContext(), "Updated Succesfully",
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