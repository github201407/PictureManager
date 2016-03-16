package com.jen.timeless.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jen.timeless.R;
import com.jen.timeless.activity.PhotoViewActivity;

import java.util.ArrayList;

/**
 * Created by jen on 2016/3/16.
 */
public class PictureAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private ArrayList<String> arrayList;
    public PictureAdapter(Context context) {
        this.context = context;
        arrayList = new ArrayList<>();
        arrayList.add("http://b.hiphotos.baidu.com/image/h%3D200/sign=8c8dc382d239b60052ce08b7d9513526/b58f8c5494eef01fa36ad8a4e7fe9925bc317d51.jpg");
        arrayList.add("http://b.hiphotos.baidu.com/image/h%3D200/sign=8c8dc382d239b60052ce08b7d9513526/b58f8c5494eef01fa36ad8a4e7fe9925bc317d51.jpg");
        arrayList.add("http://b.hiphotos.baidu.com/image/h%3D200/sign=8c8dc382d239b60052ce08b7d9513526/b58f8c5494eef01fa36ad8a4e7fe9925bc317d51.jpg");
        arrayList.add("http://b.hiphotos.baidu.com/image/h%3D200/sign=8c8dc382d239b60052ce08b7d9513526/b58f8c5494eef01fa36ad8a4e7fe9925bc317d51.jpg");
        arrayList.add("http://b.hiphotos.baidu.com/image/h%3D200/sign=8c8dc382d239b60052ce08b7d9513526/b58f8c5494eef01fa36ad8a4e7fe9925bc317d51.jpg");
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();

        View itemView = LayoutInflater
                .from(context)
                .inflate(R.layout.recycler_view_item, parent, false);

        return new CardViewHolder(itemView, new CardViewHolder.ClickResponseListener() {

            @Override
            public void onWholeClick(int position) {
//                Toast.makeText(context, "whole:" + String.valueOf(position), Toast.LENGTH_SHORT).show();
                String url = arrayList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                PhotoViewActivity.Instance(context, bundle);
            }

            @Override
            public void onOverflowClick(View v, int position) {
                Toast.makeText(context, "checkout:" + String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String url = arrayList.get(position);
        Glide.with(context).load(url)
                .placeholder(android.R.drawable.stat_notify_error)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(android.R.drawable.stat_notify_error)
                .into(((CardViewHolder)holder).imageView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CheckBox checkbox;
        public ImageView imageView;
        private ClickResponseListener mClickResponseListener;
        public CardViewHolder(View v, ClickResponseListener clickResponseListener) {
            super(v);
            this.mClickResponseListener = clickResponseListener;
            imageView = (ImageView) v.findViewById(R.id.image);
            checkbox = (CheckBox) v.findViewById(R.id.checkbox);
            v.setOnClickListener(this);
            checkbox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == checkbox) {
                mClickResponseListener.onOverflowClick(v, getAdapterPosition());
            } else {
                mClickResponseListener.onWholeClick(getAdapterPosition());
            }
        }

        public interface ClickResponseListener {
            void onWholeClick(int position);

            void onOverflowClick(View v, int position);
        }
    }
}
