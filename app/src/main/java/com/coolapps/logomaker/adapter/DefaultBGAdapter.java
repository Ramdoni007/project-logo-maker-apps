/**
 *
 */
package com.coolapps.logomaker.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.coolapps.logomaker.R;
import com.coolapps.logomaker.utilities.Item;


/**
 * @author Waqar MK
 */
public class DefaultBGAdapter extends BaseAdapter {


    int layoutResourceId;
    ArrayList<Item> data = new ArrayList<Item>();

    private Context mContext;
    private LayoutInflater infalter;


    public DefaultBGAdapter(Context c, ArrayList<Item> arrayList) {
        infalter = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = c;
        data = arrayList;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Item getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {

            convertView = infalter.inflate(R.layout.row_bggridview, null);
            holder = new ViewHolder();
            holder.imgQueue = convertView
                    .findViewById(R.id.defaultbg);


            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Glide.with(mContext).load(data.get(position).getImage()).placeholder(R.drawable.loading_image).error(R.drawable.ic_stub)
                .into(holder.imgQueue);

        return convertView;
    }

    public class ViewHolder {
        ImageView imgQueue;
    }


}
