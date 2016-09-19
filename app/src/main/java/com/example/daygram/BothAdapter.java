package com.example.daygram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by yubin on 2016/9/15.
 */
public class BothAdapter extends BaseAdapter {

    //定义两个类别标志
    private static final int TYPE_B = 0;

    private static final int TYPE_A = 1;

    private Context mContext;

    private ArrayList<Object> mData = null;

    public BothAdapter(Context mContext,ArrayList<Object> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    //多布局的核心，通过这个判断类别
    @Override
    public int getItemViewType(int position) {
        if (mData.get(position) instanceof ContentList) {
            return TYPE_A;
        } else if (mData.get(position) == null) {
            return TYPE_B;
        } else {
                return super.getItemViewType(position);
            }

    }

    //类别数目
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        ViewHolder1 holder1 = null;
        if(convertView == null){
            switch (type){
                case TYPE_A:
                    holder1 = new ViewHolder1();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_for_content_list, parent, false);
                    holder1.week = (TextView) convertView.findViewById(R.id.week);
                    holder1.date = (TextView) convertView.findViewById(R.id.date);
                    holder1.content = (TextView) convertView.findViewById(R.id.content);
                    convertView.setTag(R.id.tag_A, holder1);
                    break;
                case TYPE_B:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_for_dot_list, parent, false);;
                    break;
            }
        }else{
            switch (type){
                case TYPE_A:
                    holder1 = (ViewHolder1) convertView.getTag(R.id.tag_A);
                    break;
                case TYPE_B:
                    break;
            }
        }

        Object obj = mData.get(position);
        //设置下控件的值
        switch (type){
            case TYPE_A:
                ContentList a = (ContentList) obj;
                if(a != null){
                    holder1.week.setText(a.getWeek());
                    holder1.date.setText(a.getDate());
                    holder1.content.setText(a.getContent());
                }
                break;
            case TYPE_B:
                break;
        }
        return convertView;
    }

    private static class ViewHolder1{
        TextView week;
        TextView date;
        TextView content;
    }

}
