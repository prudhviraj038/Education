package com.yellowsoft.education;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdvertisementListAdapter extends BaseAdapter{
    Context context;
    ArrayList<Advertisement> freelancers;
    private static LayoutInflater inflater=null;
    public AdvertisementListAdapter(Activity mainActivity, ArrayList<Advertisement> freelancers) {
        // TODO Auto-generated constructor stu
        context=mainActivity;
        this.freelancers=freelancers;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return freelancers.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {

        ImageView userimage;
     //   TextView name,field,experience,gender;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        if(convertView==null)
        rowView = inflater.inflate(R.layout.advertisement_list_item, parent,false);
        else
        rowView = convertView;
        holder.userimage=(ImageView) rowView.findViewById(R.id.adv_image);

         Picasso.with(context).load(freelancers.get(position).image).into(holder.userimage);
       //  holder.name.setText(users.get(position).name);
        // holder.field.setText(users.get(position).field);
        // holder.experience.setText(users.get(position).experience);
        // holder.name.setText(users.get(position).name);

        return rowView;
    }

}