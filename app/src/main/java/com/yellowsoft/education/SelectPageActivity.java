package com.yellowsoft.education;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by sriven on 8/22/2016.
 */
public class SelectPageActivity extends RootActivity {
    ListView listView;
    TextView pre,next,title,select;
    ViewPager viewPager;
    ArrayList<GalleryImage> images;
    AdvertisementListAdapter advertiseListAdapter;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         Session.forceRTLIfSupported(this);
         setContentView(R.layout.select_page_activity);
         images = new ArrayList<>();
         pre=(TextView)findViewById(R.id.previous);
         next=(TextView)findViewById(R.id.next);
         select=(TextView)findViewById(R.id.select_page);
         title = (TextView) findViewById(R.id.gallery_title2);
         viewPager=(ViewPager)findViewById(R.id.pager);
         String json = getIntent().getStringExtra("pages_json");
         title.setText(getIntent().getStringExtra("title"));
         try {
             JSONArray pages_json_ar = new JSONArray(json);
             for(int i=0;i<pages_json_ar.length();i++){
                 images.add(new GalleryImage(pages_json_ar.getJSONObject(i)));
             }

         } catch (JSONException e) {
             e.printStackTrace();
         }
         viewPager.setAdapter(new SlidingImageAdapter(this,images));

         pre.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
             }
         });
         next.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
             }
         });
         select.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent();
                 intent.putExtra("page_id",images.get(viewPager.getCurrentItem()).id);
                 setResult(5,intent);
                 finish();
             }
         });

     }
}
