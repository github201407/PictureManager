package com.jen.timeless.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jen.timeless.R;

/**
 * Created by Administrator on 2016/3/16.
 */
public class PhotoSubmitActivity extends BaseActivity {

    public static void Instance(Context context) {
        Intent intent = new Intent(context, PhotoSubmitActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResID = R.layout.activity_photo_submit;
        super.onCreate(savedInstanceState);
    }
}
