package com.yvan.sanboxframwork.impl;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.yvan.sanboxframwork.BaseRequest;
import com.yvan.sanboxframwork.FileResponce;
import com.yvan.sanboxframwork.IFile;
import com.yvan.sanboxframwork.annotion.DbField;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MediaStoreAccessImp implements IFile {
    HashMap<String, Uri> uriMap = new HashMap<>();
    // 限制  内置目录   data/data/
    public static final String AUDIO = "Audio";
    public static final String VIDEO = "Video";
    public static final String IMAGE = "Pictures";
    public static final String DOWNLOADS = "Downloads";//什么都可以放
    public static final String DOCUMENTS = "Documents";
    private static MediaStoreAccessImp sInstance;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static MediaStoreAccessImp getInstance() {
        if (sInstance == null) {
            synchronized (MediaStoreAccessImp.class) {
                if (sInstance == null) {
                    sInstance = new MediaStoreAccessImp();
                }
            }
        }
        return sInstance;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private MediaStoreAccessImp() {
        uriMap.put(AUDIO, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        uriMap.put(VIDEO, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        uriMap.put(IMAGE, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        uriMap.put(DOWNLOADS, MediaStore.Downloads.EXTERNAL_CONTENT_URI);
    }

    @Override
    public <T extends BaseRequest> FileResponce delete(Context context, T baseRequest) {
        Uri uri = query(context, baseRequest).getUri();
        int code = context.getContentResolver().delete(uri, null, null);
        if (code > 0) {
            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    // 图片
    @Override
    public <T extends BaseRequest> FileResponce newCreateFile(Context context, T baseRequest) {
        Uri uri = uriMap.get(baseRequest.getType());

//        baseRequest 对象 --》contentValues
//        键值对
//        baseRequest
        ContentValues contentValues = objectConvertValues(baseRequest);

//        ContentResolver contentResolver = context.getContentResolver();
//        ContentValues contentValues = new ContentValues();
//        String path = Environment.DIRECTORY_DOWNLOADS + "/yvan";
////        relative_path      Download/yvan
//        contentValues.put(MediaStore.Downloads.RELATIVE_PATH,path);
//        contentValues.put(MediaStore.Downloads.DISPLAY_NAME, path);
//        contentValues.put(MediaStore.Downloads.TITLE, path);
//        Uri resultUri =contentResolver.insert(uri, contentValues);
//        if (resultUri != null) {
//            Toast.makeText(context, "添加文件成功", Toast.LENGTH_SHORT).show();
//        }
        Uri resultUri = context.getContentResolver().insert(uri, contentValues);
        FileResponce fileResponse = new FileResponce();
        fileResponse.setUri(resultUri);
        if (resultUri != null) {
            Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
        }
        return fileResponse;
    }

    private <T extends BaseRequest> ContentValues objectConvertValues(T baseRequest) {
        ContentValues contentValues = new ContentValues();
        Field[] fields = baseRequest.getClass().getDeclaredFields();
//        处理
        for (Field field : fields) {
            DbField dbField = field.getAnnotation(DbField.class);
            if (dbField == null) {
                continue;
            }
            String name = dbField.value();

            String value = null;
            String fieldName = field.getName();

            char firstLetter = Character.toUpperCase(fieldName.charAt(0));
            String theRest = fieldName.substring(1);
            String methodName = "get" + firstLetter + theRest;
            try {
                Method getMethod = baseRequest.getClass().getMethod(methodName);
                value = (String) getMethod.invoke(baseRequest);

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(value)) {
                contentValues.put(name, value);
            }
        }
        return contentValues;

    }

    @Override
    public <T extends BaseRequest> FileResponce copyFile(Context context, T baseRequest) {
        return null;
    }

    @Override
    public <T extends BaseRequest> FileResponce query(Context context, T baseRequest) {
        // 最终 yim
        Uri uri = uriMap.get(baseRequest.getType());
        ContentValues contentValues = objectConvertValues(baseRequest);

        Condition condition = new Condition(contentValues);
        String[] projection = new String[]{MediaStore.Images.Media._ID};
        //name=?   new String[]{}
        Cursor cursor = context.getContentResolver().query(uri, projection, condition.getWhereCasue(), condition.getWhereArgs(), null);
        Uri queryUri = null;
        if (cursor != null && cursor.moveToFirst()) {
            queryUri = ContentUris.withAppendedId(uri, cursor.getLong(0));
            cursor.close();
            Toast.makeText(context, "查询成功", Toast.LENGTH_SHORT).show();
        }
        FileResponce fileResponse = new FileResponce();
        fileResponse.setUri(queryUri);
        return fileResponse;
    }

    private class Condition {
        //dispalyName =?  1=1 and name=?
        private String whereCasue;
        private String[] whereArgs;//修改前的图片.jpg

        public Condition(ContentValues contentValues) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("1=1");
            ArrayList list = new ArrayList();

            Iterator<Map.Entry<String, Object>> set = contentValues.valueSet().iterator();
            while (set.hasNext()) {
                Map.Entry<String, Object> entry = set.next();
                String key = entry.getKey();
                String value = (String) entry.getValue();
                if (value != null) {
                    stringBuilder.append(" and " + key + " =? ");
                    list.add(value);
                }
            }
            this.whereCasue = stringBuilder.toString();
            this.whereArgs = (String[]) list.toArray(new String[list.size()]);
        }

        public String getWhereCasue() {
            return whereCasue;
        }

        public String[] getWhereArgs() {
            return whereArgs;
        }
    }

    @Override
    public <T extends BaseRequest> FileResponce renameTo(Context context, T where, T request) {
        // 查出来
        Uri uri = query(context, where).getUri();
        // ContentValues contentValues=objectConvertValues(where);
        // 应用
        // Condition condition = new Condition(contentValues);
        ContentValues contentValues = objectConvertValues(request);
        int code = context.getContentResolver().update(uri, contentValues, null, null);
        if (code > 0) {
            Toast.makeText(context, "更改成功", Toast.LENGTH_SHORT).show();
        }
        FileResponce fileResponse = new FileResponce();
        fileResponse.setUri(uri);
        return fileResponse;
    }
}
