package com.yvan.sanboxframwork;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.yvan.sanboxframwork.annotion.DbField;

import java.io.File;

/**
 * 沙箱 文件
 */
public class ImageRequest extends BaseRequest {
    //重点文件 和文件夹区分
    //    文件   放那个地方
    @DbField(MediaStore.Images.ImageColumns.MIME_TYPE)
    private String mimeType;
    @DbField(MediaStore.Downloads.DISPLAY_NAME)
    private String displayName;
    @DbField(MediaStore.Downloads.RELATIVE_PATH)
    private String path;
    File file;

    public ImageRequest() {
    }

    public ImageRequest(File file) {
        super(file);
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        if (!TextUtils.isEmpty(path)) {
            return Environment.DIRECTORY_PICTURES + "/" + path;
        }
        return null;
    }
}
