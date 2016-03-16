package com.jen.timeless.utils;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * Created by Administrator on 2015/12/22.
 */
public class ProgressUtils {
    private static ProgressDialog progressDialog;

    public static void showProgress(Activity activity) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setIndeterminate(false);
        if (!activity.isFinishing()) {
            progressDialog.show();
        }
    }

    public static void showProgress2(Activity activity) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        if (!activity.isFinishing()) {
            progressDialog.show();
        }
    }

    public static void setProgress(int curIndex, int curPercent, int total, int totalPercent) {
        if (progressDialog != null) {
            progressDialog.setMax(total);
            progressDialog.setProgress(curIndex);
            progressDialog.setMessage(String.valueOf(totalPercent));
        }
    }

    public static void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
        progressDialog = null;
    }
}
