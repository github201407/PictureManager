package com.jen.timeless.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.jen.timeless.R;

/**
 * Created by Administrator on 2015/12/18.
 */
public class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    protected int layoutResID = R.layout.activity_base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResID);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
