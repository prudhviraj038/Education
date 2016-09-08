package com.techbox.education;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Chinni on 10-06-2016.
 */
public class Advertisement implements java.io.Serializable{
    String image,title,link,id;
    Context context;
    String append ;

    Advertisement(JSONObject jsonObject, Context context){
        this.context=context;
        try {

            image = jsonObject.getString("image");
            title = jsonObject.getString("title");
            link = jsonObject.getString("link");
            id = jsonObject.getString("id");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
