package com.yellowsoft.education;

/**
 * Created by HP on 8/22/2016.
 */
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Chinni on 18-08-2016.
 */
public class Feedback extends Activity {
    TextView submit,click_here;
    EditText name,email,msg,mobile,url;
    String namee,emaill,msgg,no,id,mobile_str,url_str;
    LinearLayout subit_ll;
    ImageView back;
    ImageView profile_image;
    String enquiry_id;
    String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    boolean image_selected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Session.forceRTLIfSupported(this);
        setContentView(R.layout.settings_actvity);
        profile_image = (ImageView) findViewById(R.id.profile_image);
        TextView advertisement_page_title = (TextView) findViewById(R.id.advertise_page_title);
        no="1";
        id=getIntent().getStringExtra("id");
        submit=(TextView)findViewById(R.id.sett_submit);
        submit.setText(Session.getword(this,"submit_enquiry"));
        click_here=(TextView)findViewById(R.id.select_here_tv);
        click_here.setText(Session.getword(this,"tap_here_to_select_image"));
        name=(EditText)findViewById(R.id.st_fullname_et);
        name.setHint(Session.getword(this,"fullname"));
        mobile=(EditText)findViewById(R.id.st_mobile_et);
        mobile.setHint(Session.getword(this,"mobile"));
        url=(EditText)findViewById(R.id.st_redirect_url);
        url.setHint(Session.getword(this,"redirect_url"));
        email=(EditText)findViewById(R.id.st_email_et);
        email.setHint(Session.getword(this,"email"));
        msg=(EditText)findViewById(R.id.st_message_et);
        click_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectphotos();
            }
        });
        back=(ImageView)findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        subit_ll=(LinearLayout)findViewById(R.id.st_submit_ll);
        subit_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                namee=name.getText().toString();
                emaill=email.getText().toString();
                msgg=msg.getText().toString();
                mobile_str=mobile.getText().toString();
                url_str=url.getText().toString();
                if (namee.equals(""))
                    Toast.makeText(Feedback.this,Session.getword(Feedback.this,"pls_ent_fullname"), Toast.LENGTH_SHORT).show();
                else if (!emaill.matches(emailPattern))
                    Toast.makeText(Feedback.this, Session.getword(Feedback.this,"Please enter EmailID"), Toast.LENGTH_SHORT).show();

                else if (mobile_str.equals(""))
                    Toast.makeText(Feedback.this, Session.getword(Feedback.this,"Pls_ent_Mob_Num"), Toast.LENGTH_SHORT).show();

                else if (url_str.equals(""))
                    Toast.makeText(Feedback.this, Session.getword(Feedback.this,"pls_ent_redirect_url"), Toast.LENGTH_SHORT).show();
                else if (!image_selected)
                    Toast.makeText(Feedback.this, Session.getword(Feedback.this,"pls_select_a_image"), Toast.LENGTH_SHORT).show();

                else {
                    final ProgressDialog progressDialog = new ProgressDialog(Feedback.this);
                    progressDialog.setMessage("please_wait");
                    progressDialog.show();
                    String url = null;
                    try {
                        if(no.equals("1")) {
                            url = Session.SERVERURL+"send_enquiry.php?" +
                                    "full_name=" + URLEncoder.encode(namee, "utf-8") +
                                    "&email=" + URLEncoder.encode(emaill, "utf-8") +
                                    "&contact_number=" + URLEncoder.encode(mobile_str, "utf-8")+
                                    "&url=" + URLEncoder.encode(url_str, "utf-8");
                        }else if(no.equals("2")){
                            url = "http://3ajelapp.com/nashrsdfs/api/bug_reports.php?" +
                                    "name=" + URLEncoder.encode(namee, "utf-8") +
                                    "&email=" + URLEncoder.encode(emaill, "utf-8")
                                    + "&message=" + URLEncoder.encode(msgg, "utf-8");
                        }else {
                            url = "http://3ajelapp.com/nashrsdf/api/report_abuse.php?" +
                                    "name=" + URLEncoder.encode(namee, "utf-8") +
                                    "&email=" + URLEncoder.encode(emaill, "utf-8")
                                    + "&message=" + URLEncoder.encode(msgg, "utf-8")
                                    + "&news_id=" + URLEncoder.encode(id, "utf-8");
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Log.e("register url", url);
                    JsonArrayRequest jsObjRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray jsonArray) {
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            Log.e("response is", jsonArray.toString());
                            try {
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
//                                Log.e("response is", jsonObject.getString("response"));
                                String result = jsonObject.getString("status");

                                if (result.equals("Success")) {

                                    enquiry_id = jsonObject.getString("enquiry_id");
                                    encodeImagetoString();

                                } else {
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(Feedback.this, message, Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                            Log.e("error response is:", error.toString());
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            Toast.makeText(Feedback.this, "please try again", Toast.LENGTH_SHORT).show();

                        }
                    });

                    AppController.getInstance().addToRequestQueue(jsObjRequest);
                }

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
            image_selected=true;
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
        params.put("image", encodedString);
        params.put("ext", "jpg");
        params.put("id", enquiry_id);

        client.post(Session.SERVERURL + "send_enquiry_image.php",
                params, new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http
                    // response code '200'
                    @Override
                    public void onSuccess(String response) {
                        // Hide Progress Dialog
                        Log.e("success", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("status").equals("Success")){
                                Toast.makeText(getApplicationContext(),"Requested submited successfully",Toast.LENGTH_LONG).show();
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(),"try agian",Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"try agin",Toast.LENGTH_LONG).show();
                            // get_user_details();
                        }

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
