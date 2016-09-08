package com.techbox.education;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by HP on 5/20/2016.
 */
public class TestClass {


    public void set_json(){
        JSONObject place_order_object = new JSONObject();
        JSONObject address_object = new JSONObject();
        JSONArray products_array = new JSONArray();


        try {
            address_object.put("area", "");
            address_object.put("block", "");
            address_object.put("street", "");
            address_object.put("building", "");
            address_object.put("floor", "");
            address_object.put("flat", "");
            address_object.put("mobile", "");

            place_order_object.put("address",address_object);


            place_order_object.put("comments","");
            place_order_object.put("coupon_code","");
            place_order_object.put("price","");
            place_order_object.put("delivery_charges","");
            place_order_object.put("total_proce","");
            place_order_object.put("payment_method","");
            place_order_object.put("restaurant_id","");

            for(int i =0; i<3; i++){
                JSONObject product = new JSONObject();
                product.put("product_id","");
                product.put("quantity","");
                product.put("price","");
                JSONArray addon_array = new JSONArray();
                for(int j = 0; j<4 ; j++){
                    JSONObject addon = new JSONObject();
                    addon.put("addon_id","");
                    addon.put("price","");
                    addon_array.put(addon);
                }
                product.put("addons",addon_array);

                JSONObject options = new JSONObject();
                options.put("options_id","");
                options.put("price","");

                product.put("options",options);
                products_array.put(product);

            }


            place_order_object.put("products",products_array);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
