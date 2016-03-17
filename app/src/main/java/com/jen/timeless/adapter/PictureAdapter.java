package com.jen.timeless.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

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
    private ArrayList<String> arrayListChecked;
    private boolean checkboxIsShow = false;

    public PictureAdapter(Context context) {
        this.context = context;
        arrayList = new ArrayList<>();
        arrayListChecked = new ArrayList<>();
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
                String url = getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                bundle.putString("flag", "show");
                PhotoViewActivity.Instance(context, bundle);
            }

            @Override
            public void onOverflowClick(View v, int position) {
                //Toast.makeText(context, "checkout:" + String.valueOf(position), Toast.LENGTH_SHORT).show();
                String url = getItem(position);
                CheckBox checkBox = (CheckBox)v;
                if(checkBox.isChecked()){
                    if(!arrayListChecked.contains(url))
                        arrayListChecked.add(url);
                }else {
                    if(arrayListChecked.contains(url))
                        arrayListChecked.remove(url);
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String url = getItem(position);
        CardViewHolder cardViewHolder = (CardViewHolder) holder;
        Glide.with(context).load(url)
                .placeholder(android.R.drawable.stat_notify_error)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(android.R.drawable.stat_notify_error)
                .into(cardViewHolder.imageView);
        cardViewHolder.checkbox.setVisibility(checkboxIsShow ? View.VISIBLE : View.GONE);
        boolean isChecked = arrayListChecked.contains(url);
        cardViewHolder.checkbox.setChecked(isChecked);
    }

    public String getItem(int position){
        return arrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void addPhotoList(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    public void setPickPhoto(boolean isShowCheckbox){
        this.checkboxIsShow = isShowCheckbox;
        notifyDataSetChanged();
    }

    public ArrayList<String> getArrayListChecked() {
        return arrayListChecked;
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
