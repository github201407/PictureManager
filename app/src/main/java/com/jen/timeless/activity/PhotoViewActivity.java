package com.jen.timeless.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jen.timeless.App;
import com.jen.timeless.R;
import com.jen.timeless.utils.ImageUtils;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2016/3/16.
 */
public class PhotoViewActivity extends BaseActivity {

    private String url;
    private String flag;

    public static void Instance(Context context, Bundle bundle) {
        Intent intent = new Intent(context, PhotoViewActivity.class);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);

    }

    private static Activity photoViewActivity;
    public static Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0 && photoViewActivity != null){
                photoViewActivity.finish();
                Log.e("info", "Success");
            } else if (msg.what == 1){
                Log.e("info", "Failure");
            }
        }
    };

    private static void showToast(String msg){
        Toast.makeText(App.getApplication(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResID = R.layout.activity_photo_view;
        super.onCreate(savedInstanceState);
        photoViewActivity = this;
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
            url = bundle.getString("url");
            flag = bundle.getString("flag");
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem upLoadMenu = menu.findItem(R.id.upload);
        if(flag != null && flag.equals("upload")) {
            upLoadMenu.setVisible(true);
        } else {
            upLoadMenu.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if(id == R.id.upload) {
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(url);
            ImageUtils.uploadImgs(mHandler, PhotoViewActivity.this, arrayList);
        }

        return super.onOptionsItemSelected(item);
    }



}
