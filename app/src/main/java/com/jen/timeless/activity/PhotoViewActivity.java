package com.jen.timeless.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jen.timeless.R;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2016/3/16.
 */
public class PhotoViewActivity extends BaseActivity {

    public static void Instance(Context context, Bundle bundle) {
        Intent intent = new Intent(context, PhotoViewActivity.class);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResID = R.layout.activity_photo_view;
        super.onCreate(savedInstanceState);

        // Add the Up button
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("图片预览");
            ab.setDisplayHomeAsUpEnabled(true);
        }

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setNavigationIcon(R.mipmap.back);
//        toolbar.setTitle("图片预览");
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        ImageView imageView = (ImageView) findViewById(R.id.photoView);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if(bundle != null) {
            String url = bundle.getString("url");
            Glide.with(this).load(url)
                    .placeholder(android.R.drawable.stat_notify_error)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .error(android.R.drawable.stat_notify_error)
                    .into(imageView);
        }
        PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);
        attacher.update();

    }
}
