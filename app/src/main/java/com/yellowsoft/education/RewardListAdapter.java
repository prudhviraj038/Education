package com.yellowsoft.education;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RewardListAdapter extends BaseAdapter{
    String [] result;
    Context context;
    int [] imageId;
    ArrayList<Rewards> rewardses;
    private static LayoutInflater inflater=null;
    public RewardListAdapter(Context mainActivity,ArrayList<Rewards> rewardses) {
        // TODO Auto-generated constructor stub
        //  result=prgmNameList;
        context=mainActivity;
        this.rewardses = rewardses;
        //  imageId=prgmImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return rewardses.size();
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
        TextView tv;
        ImageView img;
        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv4;
        TextView tv5;
        TextView tv6;
        TextView tv7;
        TextView tv8;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.reward_list_item, null);

        holder.tv=(TextView) rowView.findViewById(R.id.name_tv);
        holder.tv1=(TextView) rowView.findViewById(R.id.sec_tv);

        holder.tv2=(TextView) rowView.findViewById(R.id.grade_tv);
        holder.tv3=(TextView) rowView.findViewById(R.id.number_gave_tv);
        holder.tv4=(TextView) rowView.findViewById(R.id.number_wrong_tv);
        holder.tv5=(TextView) rowView.findViewById(R.id.number_correct_tv);
        holder.tv6=(TextView) rowView.findViewById(R.id.gave_tv);
        holder.tv6.setText(Session.getword(context,"giveup"));
        holder.tv7=(TextView) rowView.findViewById(R.id.wrong_tv);
        holder.tv7.setText(Session.getword(context,"wrong_answers"));
        holder.tv8=(TextView) rowView.findViewById(R.id.correct_tv);
        holder.tv8.setText(Session.getword(context,"correct_answers"));

        holder.img=(ImageView) rowView.findViewById(R.id.reward_list_image);
        Picasso.with(context).load(rewardses.get(position).image).into(holder.img);
        holder.tv.setText(rewardses.get(position).title);
        holder.tv1.setText(rewardses.get(position).level);
        holder.tv2.setText("GRADE 0"+rewardses.get(position).grade);
        holder.tv3.setText(rewardses.get(position).skiped);
        holder.tv4.setText(rewardses.get(position).count);
        holder.tv5.setText(rewardses.get(position).correctt);
        return rowView;
    }

}