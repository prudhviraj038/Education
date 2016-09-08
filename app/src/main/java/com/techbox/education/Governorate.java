package com.techbox.education;


import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Chinni on 07-07-2016.
 */
public class Governorate {
    String id,title,title_ar;
    JSONArray jsonArray1;
    ArrayList<Area> are;
    Governorate(JSONObject jsonObject){
        are=new ArrayList<>();
            try {
                id=jsonObject.getString("id");
                title=jsonObject.getString("title");
                title_ar=jsonObject.getString("title_ar");
                jsonArray1=jsonObject.getJSONArray("schools");
                for(int j=0;j<jsonArray1.length();j++) {
                        Area ar = new Area(jsonArray1.getJSONObject(j));
                        are.add(ar);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    public  String getTitle(Context context) {
        if(Session.get_user_language(context).equals("ar"))
            return title_ar;
        else
            return  title;
    }
    public class Area {
        String a_id,a_title,a_title_ar,school_image;
        Area(JSONObject jsonObject1){
            try {
                a_id=jsonObject1.getString("id");
                a_title=jsonObject1.getString("title");
                a_title_ar=jsonObject1.getString("title_ar");
                school_image=jsonObject1.getString("image");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public  String getATitle(Context context) {
            if(Session.get_user_language(context).equals("ar"))
                return a_title_ar;
            else
                return  a_title;
        }
    }
}
