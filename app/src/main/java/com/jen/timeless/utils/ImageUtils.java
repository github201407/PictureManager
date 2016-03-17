package com.jen.timeless.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadBatchListener;
import com.jen.timeless.App;
import com.jen.timeless.bean.Res;

import java.util.ArrayList;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by chenmingqun on 2016/3/16.
 */
public class ImageUtils {

    public static final String CAMERA_IMAGE_BUCKET_NAME =
            Environment.getExternalStorageDirectory().toString()
                    + "/DCIM/Camera";
    public static final String CAMERA_IMAGE_BUCKET_ID =
            getBucketId(CAMERA_IMAGE_BUCKET_NAME);

    /**
     * Matches code in MediaProvider.computeBucketValues. Should be a common
     * function.
     */
    public static String getBucketId(String path) {
        return String.valueOf(path.toLowerCase().hashCode());
    }
/*only get camerea picture*/
    public static ArrayList<String> getCameraImages1(Context context) {
        final String[] projection = {MediaStore.Images.Media.DATA};
        final String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        final String[] selectionArgs = {CAMERA_IMAGE_BUCKET_ID};
        final Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
        ArrayList<String> result = new ArrayList<String>(cursor.getCount());
        if (cursor.moveToFirst()) {
            final int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            do {
                final String data = cursor.getString(dataColumn);
                result.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }
/*get all picture*/
    public static ArrayList<String> getCameraImages(Context context) {
        final String[] projection = {MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.LATITUDE,
                MediaStore.Images.Media.LONGITUDE, MediaStore.Images.Media._ID};
        final Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null);
        ArrayList<String> result = new ArrayList<>();
        if(cursor == null) return result;
        while (cursor.moveToNext()){
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    .buildUpon().appendPath(Long.toString(id)).build();/* 图片对应的uri */
            result.add(uri.toString());
        }
        cursor.close();
        return result;
    }

    public static void uploadImgs(final Handler mHandler, final Activity activity, ArrayList<String> imgUrls) {
        ProgressUtils.showProgress2(activity);
        // TODO 代码优化，是否限定必须有图片，显示具体的进度
        BmobProFile.getInstance(activity).uploadBatch(imgUrls.toArray(new String[0]), new UploadBatchListener() {
            @Override
            public void onSuccess(boolean isFinish, String[] fileNames, String[] urls, BmobFile[] files) {
                Log.i("bmob", "onProgress :" + isFinish + "---" + fileNames + "---" + urls + "----" + files);
                if(isFinish) {
                    String imgUrl = null;
                    for (BmobFile file : files) {
                        if(file != null)
                            imgUrl = file.getUrl();
                    }
                    doSubmit(mHandler, activity,imgUrl);
                }
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                ProgressUtils.setProgress(curIndex, curPercent, total, totalPercent);
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


    private static void doSubmit(final Handler mHandler, Activity activitie, String imgUrl) {
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
        res.save(activitie, new SaveListener() {
            @Override
            public void onSuccess() {
                ProgressUtils.hideProgress();
                showToast("提交成功");
                Log.e("info", "Success");
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void onFailure(int i, String s) {
                ProgressUtils.hideProgress();
                Log.e("info", "Failure");
                showToast("提交失败，请重试！");
                mHandler.sendEmptyMessage(1);
            }
        });
    }

    private static void showToast(String msg){
        Toast.makeText(App.getApplication(), msg, Toast.LENGTH_SHORT).show();
    }
}
