package com.jen.timeless.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadBatchListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jen.timeless.R;
import com.jen.timeless.bean.Res;
import com.jen.timeless.utils.ProgressUtils;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
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
        if(flag != null && flag.equals("upload"))
            menu.findItem(R.id.upload).setVisible(true);
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
            uploadImgs(url);
        }

        return super.onOptionsItemSelected(item);
    }

    private void uploadImgs(String imgUrl) {
        ProgressUtils.showProgress(this);
        // TODO 代码优化，是否限定必须有图片，显示具体的进度
        BmobProFile.getInstance(PhotoViewActivity.this).uploadBatch(new String[]{imgUrl}, new UploadBatchListener() {

            @Override
            public void onSuccess(boolean isFinish, String[] fileNames, String[] urls, BmobFile[] files) {
                Log.i("bmob", "onProgress :" + isFinish + "---" + fileNames + "---" + urls + "----" + files);
                if(isFinish) {
                    String imgUrl = null;
                    for (BmobFile file : files) {
                        if(file != null)
                            imgUrl = file.getUrl();
                    }
                    doSubmit(imgUrl);
                }
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                // curIndex    :表示当前第几个文件正在上传
                // curPercent  :表示当前上传文件的进度值（百分比）
                // total       :表示总的上传文件数
                // totalPercent:表示总的上传进度（百分比）
                Log.i("bmob", "onProgress :" + curIndex + "---" + curPercent + "---" + total + "----" + totalPercent);
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                ProgressUtils.hideProgress();
                // TODO Auto-generated method stub
                Log.i("bmob", "批量上传出错：" + statuscode + "--" + errormsg);
            }
        });
    }


    private void doSubmit(String imgUrl) {
        // ToDo 相关字段空值限制和判断
        String name = "";
        String desc = "";
        String type = "";
        String want = "";
        String chargeType = "";
        Res res = new Res();
        res.setName(name);
        res.setLocation(desc);
        res.setType(chargeType);
        res.setChangeType(type);
        res.setWantRes(want);
        res.setImgUrl(imgUrl);
        res.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                ProgressUtils.hideProgress();
//                showToast(actionView, "提交成功");
                Log.e("info", "Success");
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                ProgressUtils.hideProgress();
                Log.e("info", "Failure");
//                showToast(actionView, "提交失败，请重试！");
            }
        });
    }

    private void showToast(View view, String msg){
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

}
