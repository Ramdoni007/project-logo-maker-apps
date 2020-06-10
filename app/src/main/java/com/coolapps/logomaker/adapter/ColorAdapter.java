package com.coolapps.logomaker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build.VERSION;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.coolapps.logomaker.R;
import com.coolapps.logomaker.utilities.ColorCircleDrawable;

public class ColorAdapter extends Adapter<ColorAdapter.ViewHolder> implements OnClickListener {
    private Context context;
    private String[] listItem;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String str);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public ViewHolder(View view) {
            super(view);
            this.image = view.findViewById(R.id.image);
        }
    }

    public ColorAdapter(String[] listItem, Context ct) {
        this.listItem = listItem;
        this.context = ct;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_color, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (VERSION.SDK_INT < 16) {
            viewHolder.image.setBackgroundDrawable(new ColorCircleDrawable(Color.parseColor(this.listItem[position])));
        } else {
            viewHolder.image.setBackground(new ColorCircleDrawable(Color.parseColor(this.listItem[position])));
        }
        viewHolder.itemView.setTag(this.listItem[position]);
    }

    public void onClick(View v) {
        if (this.mOnItemClickListener != null) {
            this.mOnItemClickListener.onItemClick(v, (String) v.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public int getItemCount() {
        return this.listItem.length;
    }
}
