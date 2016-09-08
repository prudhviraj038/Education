package com.techbox.education;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class SubjectsListAdapter extends BaseAdapter{
    String [] result;
    Context context;
    int [] imageId;
    ArrayList<String> titles;
    ArrayList<String> s_ids;
    ArrayList<String> ids;
    private static LayoutInflater inflater=null;
    public SubjectsListAdapter(Context mainActivity, ArrayList<String> ids,ArrayList<String> s_ids,ArrayList<String> titles) {
        // TODO Auto-generated constructor stub
        //  result=prgmNameList;
        context=mainActivity;
        this.titles = titles;
        this.ids = ids;
        this.s_ids = s_ids;
        //  imageId=prgmImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return titles.size();
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
        CheckedTextView tv;
        ImageView img;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.list_item, null);
        holder.tv=(CheckedTextView) rowView.findViewById(R.id.checkedTextView2);
        holder.tv.setText(titles.get(position));
        for(int i=0;i<ids.size();i++){
                if(ids.get(i).equals(s_ids.get(position))){
                    Log.e("id",ids.get(i));
                    ((ListView) parent).setItemChecked(position, true);
//                     holder.tv.setEnabled(true);
            }
        }
        return rowView;
    }

}