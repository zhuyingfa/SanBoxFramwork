package com.yvan.sanboxframwork;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission(this);
    }

    public static boolean checkPermission(
            Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);

        }
        return false;
    }

    /**
     * 创建文件夹 SD卡的Downloads文件夹，里面什么都可以放
     *
     * @param view
     */
    public void create(View view) {
        // SD卡的Downloads文件夹里
        // 创建一个名为yvanDirs的文件夹
        FileRequest request = new FileRequest(new File("yvanDirs"));
        request.setPath("yvanDirs");
        request.setDisplayName("yvanDirs");
        request.setTitle("yvanDirs");
        try {
            FileAccessFactory.getIFile(request).newCreateFile(this, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建图片 根据后缀判断图片为类型
     *
     * @param view
     */
    public void createImg(View view) {
        ImageRequest imageRequest = new ImageRequest(new File("修改前的图片.jpg"));
        imageRequest.setDisplayName("修改前的图片.jpg");
        try {
            FileResponce fileResponce = FileAccessFactory.getIFile(imageRequest).newCreateFile(this, imageRequest);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.die);
            OutputStream outputStream = getContentResolver().openOutputStream(fileResponce.getUri());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 图片查询
     *
     * @param view
     */
    public void queryFile(View view) {
        ImageRequest imageRequest = new ImageRequest(new File("修改前的图片.jpg"));
        imageRequest.setDisplayName("修改前的图片.jpg");
        try {
            FileAccessFactory.getIFile(imageRequest).query(this, imageRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 图片删除
     *
     * @param view
     */
    public void deleteFile(View view) {
        // 查询    file.delete   查到
        ImageRequest imageRequest = new ImageRequest(new File("修改前的图片.jpg"));
        imageRequest.setDisplayName("修改前的图片.jpg");
        try {
            FileAccessFactory.getIFile(imageRequest).delete(this, imageRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 图片信息更新
     *
     * @param view
     */
    public void updateFile(View view) {
        ImageRequest where = new ImageRequest(new File("修改前的图片.jpg"));
        where.setDisplayName("修改前的图片.jpg");
        ImageRequest item = new ImageRequest(new File("yvan.jpg"));
        item.setDisplayName("yvan.jpg");
        try {
            FileAccessFactory.getIFile(where).renameTo(this, where, item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}