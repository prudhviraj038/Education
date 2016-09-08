package com.techbox.education;




import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by sriven on 8/22/2016.
 */
public class Advertisement_Activity extends RootActivity {
    ListView listView;
    ArrayList<Advertisement> advertses;
    AdvertisementListAdapter advertiseListAdapter;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Session.forceRTLIfSupported(this);
        setContentView(R.layout.advertisement);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Advertisement_Activity.this,Feedback.class);
                startActivity(intent);
            }
        });
        TextView advertisement_page_title = (TextView) findViewById(R.id.advertise_page_title);
         TextView advertisement_enquiry = (TextView) findViewById(R.id.advertisement_enquiry);

         advertisement_page_title.setText(Session.getword(this,"advetisements"));
         advertisement_enquiry.setText(Session.getword(this,"advertisement_enquiry"));

        ImageView back_btn = (ImageView) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        advertses = new ArrayList<>();
        advertiseListAdapter = new AdvertisementListAdapter(this,advertses);
        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(advertiseListAdapter);
        get_advertisement();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try{
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(advertses.get(position).link));
                    startActivity(browserIntent);
                }catch (Exception exception){
                    Toast.makeText(Advertisement_Activity.this,"invalid url",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void get_advertisement(){

        String url = Session.SERVERURL + "advertisements.php?";

        Log.e("url--->", url);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(Session.getword(this, "loading"));
        progressDialog.setCancelable(false);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest( url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray jsonArray) {
                try {
                    Log.e("res", jsonArray.toString());
                    for(int i =0;i<jsonArray.length();i++){
                        advertses.add(new Advertisement(jsonArray.getJSONObject(i),Advertisement_Activity.this));

                    }
                    advertiseListAdapter.notifyDataSetChanged();
                                    } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("response is:", error.toString());
                Toast.makeText(Advertisement_Activity.this, "Server not connected", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });

// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

    }
}
