package com.jen.timeless;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jen.timeless.activity.PhotoViewActivity;
import com.jen.timeless.adapter.PictureAdapter;
import com.jen.timeless.bean.Res;
import com.jen.timeless.utils.CameraUtil;
import com.jen.timeless.utils.ProgressUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends AppCompatActivity {

    private PictureAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                PhotoSubmitActivity.Instance(MainActivity.this);
                showMenuDialog();
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new PictureAdapter(this);
        recyclerView.setAdapter(adapter);

    }

    private void showMenuDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("照片来源于：");
        final CharSequence[] items = {"相册", "拍照"};
        builder.setItems(items
                , new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                dispatchPickPictureIntent(ACTION_PICK_PHOTO);
                                break;
                            case 1:
                                dispatchTakePictureIntent(ACTION_TAKE_PHOTO);
                                break;
                            default:
                                break;
                        }
                        dialog.dismiss();
                    }
                });
        AlertDialog alter = builder.create();
        alter.show();
    }


    private void dispatchPickPictureIntent(int actionCode) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
        startActivityForResult(chooserIntent, actionCode);
    }

    public final static int ACTION_TAKE_PHOTO = 1;
    public final static int ACTION_PICK_PHOTO = 2;
    private void dispatchTakePictureIntent(int actionCode) {
        CameraUtil cameraUtil = CameraUtil.getInstance();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        switch(actionCode) {
            case ACTION_TAKE_PHOTO:
                File f;
                try {
                    f = cameraUtil.setUpPhotoFile();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                } catch (IOException e) {
                    e.printStackTrace();
                    cameraUtil.setmCurrentPhotoPath("");
                }
                break;
            default:
                break;
        }
        startActivityForResult(takePictureIntent, actionCode);
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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTION_TAKE_PHOTO: {
                if (resultCode == RESULT_OK) {
                    handleBigCameraPhoto();
                }
            }
            break;
            case ACTION_PICK_PHOTO: {
                if (resultCode == RESULT_OK) {
                    handlePickPhoto(data);
                }
            }
            break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handlePickPhoto(Intent data) {
        if(data != null){
            Uri selectedImage = data.getData();
            String picturePath = null;
            try {
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
//            mAdapter.addImage(TextUtils.isEmpty(picturePath) ? selectedImage.getPath() : picturePath);
            switchPhotoView(picturePath);
        }
    }

    private void handleBigCameraPhoto() {
        CameraUtil cameraUtil = CameraUtil.getInstance();
        String mCurrentPhotoPath = cameraUtil.getmCurrentPhotoPath();
        if (mCurrentPhotoPath != null) {
//            mAdapter.addImage(mCurrentPhotoPath);
            cameraUtil.galleryAddPic(mCurrentPhotoPath);
            switchPhotoView(mCurrentPhotoPath);
        }
    }

    private void switchPhotoView(String mCurrentPhotoPath) {
        Bundle bundle = new Bundle();
        bundle.putString("url", mCurrentPhotoPath);
        bundle.putString("flag", "upload");
        PhotoViewActivity.Instance(MainActivity.this, bundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.addPhotoList(new ArrayList<Res>());
    }

    private void downLoadData() {
        ProgressUtils.showProgress(this);
        BmobQuery<Res> query = new BmobQuery<>();
        query.findObjects(this, new FindListener<Res>() {
            @Override
            public void onSuccess(List<Res> list) {
                ProgressUtils.hideProgress();
                Log.e("bmob", "success:");
                adapter.addPhotoList(list);
            }

            @Override
            public void onError(int i, String s) {
                ProgressUtils.hideProgress();
                Log.e("bmob", "error:" + i + "," + s);
            }
        });
    }
}
