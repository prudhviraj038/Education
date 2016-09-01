package com.yellowsoft.education;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Chinni on 30-07-2016.
 */
public class GalleryImage implements Serializable {
    String id,title,title_ar,image,data,data_te,link,is_urgent,now;
   // Chanel chanels;

    GalleryImage(JSONObject jsonObject){
        title="";
        title_ar="";
        try {
            id=jsonObject.getString("id");
            title=jsonObject.getString("title");
            title_ar=jsonObject.getString("title_ar");
            image=jsonObject.getString("image");
          //  data=jsonObject.getString("description");
         //   data_te=jsonObject.getString("description_te");
//            link=jsonObject.getString("link");
//            is_urgent=jsonObject.getString("is_urgent");
//            now=jsonObject.getString("times");
//            JSONObject jsonObject2 =jsonObject.getJSONObject("chanel");
           // chanels=new Chanel(jsonObject2);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

//   public class Chanel implements Serializable{
//        String ch_id,ch_title,ch_title_ar,ch_title_fr,ch_image;
//        Chanel(JSONObject jsonObject1){
//            try {
//                ch_id=jsonObject1.getString("id");
//                ch_title=jsonObject1.getString("title");
//                ch_title_ar=jsonObject1.getString("title_ar");
//                ch_title_fr=jsonObject1.getString("title_fr");
//                ch_image=jsonObject1.getString("image");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }
//
//
}
