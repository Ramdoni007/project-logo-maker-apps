package com.coolapps.logomaker.adapter;



import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.coolapps.logomaker.R;
import com.coolapps.logomaker.utilities.Item;
import com.coolapps.logomaker.views.MyWorkActivity;

import java.util.ArrayList;


public class GridViewAdapter extends BaseAdapter {

	// Declare variables
	private Activity activity;
//	private String[] filepath;
//	private String[] filename;
	private ArrayList<Item> path;


	private  LayoutInflater inflater = null;

	public GridViewAdapter(Activity a, ArrayList<Item> path) {
		activity = a;
//		filepath = fpath;
//		filename = fname;
		this.path = path;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);



	}

	public int getCount() {
		return path.size();

	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		final Holder mHolder;
		if (row == null) {

			row = inflater.inflate(R.layout.gridview_item, null);
			mHolder = new Holder();
			// Locate the ImageView in gridview_item.xml
			mHolder.image = row.findViewById(R.id.img_tumb);
			row.setTag(mHolder);
		}else{
			mHolder = (Holder) row.getTag();
		}

		// Decode the filepath with BitmapFactory followed by the position



//		final Bitmap bmp = BitmapFactory.decodeFile(filepath[position]);
		// Set the decoded bitmap into ImageView
//		mHolder.image.setImageBitmap(bmp);

//		mHolder.image.setTag(position);

		Glide.with(activity).load("file://"+ MyWorkActivity.FilePathStrings[position]).placeholder(R.drawable.ic_stub).error(R.drawable.ic_stub)
				.into(mHolder.image);

		return row;
	}

	class Holder{
		ImageView image;
	}


}

