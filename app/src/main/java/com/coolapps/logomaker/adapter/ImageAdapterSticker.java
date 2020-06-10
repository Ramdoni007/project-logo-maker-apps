package com.coolapps.logomaker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.coolapps.logomaker.R;
import com.coolapps.logomaker.utilities.LOADERCACHE;
import com.coolapps.logomaker.utilities.ListSticker;

public class ImageAdapterSticker extends BaseAdapter {
    private Context context;
    private final String[] mThumbIds;

    public ImageAdapterSticker(Context context, String[] mThumbIds) {
        this.context = context;
        this.mThumbIds = mThumbIds;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = new View(this.context);
        view = inflater.inflate(R.layout.item_gridview, null);
        ImageView imageView = view.findViewById(R.id.imageView1);
        LOADERCACHE.getInstance(this.context).DisplayImage("ThumbFrame/" + this.mThumbIds[position], imageView, 300);
        imageView.getLayoutParams().height = (int) (((float) ListSticker.CAMERA_WIDTH) / 6.2f);
        imageView.getLayoutParams().width = (int) (((float) ListSticker.CAMERA_WIDTH) / 3.1f);
        return view;
    }

    public int getCount() {
        return this.mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
}
