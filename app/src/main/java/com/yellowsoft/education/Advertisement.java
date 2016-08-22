package com.yellowsoft.education;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Chinni on 10-06-2016.
 */
public class Advertisement implements java.io.Serializable{
    String image;
    Context context;
    String append ;

    Advertisement(JSONObject jsonObject, Context context){
        this.context=context;
        append = "en";
        try {

            image = jsonObject.getString("image");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
