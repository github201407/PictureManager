package com.jen.timeless.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jen.timeless.R;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2016/3/16.
 */
public class PhotoViewActivity extends AppCompatActivity {

    public static void Instance(Context context, Bundle bundle) {
        Intent intent = new Intent(context, PhotoViewActivity.class);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setTitle("图片预览");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
