package com.techbox.education;


import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Chinni on 10-06-2016.
 */
public class Rewards implements java.io.Serializable{
    String id,title,grade,correctt,wrong,skiped,level,image,count;
    Context context;
    String append ;

    Rewards(JSONObject jsonObject, Context context){
        this.context=context;
        append = "en";
        try {
            id=jsonObject.getString("id");
            title=jsonObject.getString("name");
            image = jsonObject.getString("image");
            level = jsonObject.getString("level");
            grade = jsonObject.getString("class");
            correctt = jsonObject.optString("correct", "99");
            wrong = jsonObject.optString("wrong", "99");
            skiped = jsonObject.optString("skipped", "99");
            count = jsonObject.optString("count","99");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
