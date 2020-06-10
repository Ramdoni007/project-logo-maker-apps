package com.coolapps.logomaker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.coolapps.logomaker.R;
import com.coolapps.logomaker.beans.LogoDataBean;

import java.util.ArrayList;

/**
 * Created by apple on 3/3/18.
 */

public class LogoGridviewAdapter extends ArrayAdapter<LogoDataBean> {

    private Context context;
    private ArrayList<LogoDataBean> logoImageArray;
    private LayoutInflater inflater;
    private int layout;

    public LogoGridviewAdapter(Context context, int layout, ArrayList<LogoDataBean> images){
        super(context, layout,  images);
        this.context = context;
        this.logoImageArray = images;
        this.layout = layout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layout, parent, false);
            viewHolder.logo_image = convertView.findViewById(R.id.logo_image);
            viewHolder.logo_name = convertView.findViewById(R.id.logo_name);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.logo_image.setImageResource(logoImageArray.get(position).getImage());
        viewHolder.logo_name.setText(logoImageArray.get(position).getName());

        return convertView;

    }

    public class ViewHolder{
        public ImageView logo_image;
        public TextView logo_name;

    }


}
