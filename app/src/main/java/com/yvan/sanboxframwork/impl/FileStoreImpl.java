package com.yvan.sanboxframwork.impl;

import android.content.Context;

import com.yvan.sanboxframwork.BaseRequest;
import com.yvan.sanboxframwork.FileResponce;
import com.yvan.sanboxframwork.IFile;

public class FileStoreImpl implements IFile {

    @Override
    public <T extends BaseRequest> FileResponce delete(Context context, T baseRequest) {
        baseRequest.getFile().delete();
        return null;
    }

    @Override
    public <T extends BaseRequest> FileResponce newCreateFile(Context context, T baseRequest) {
        return null;
    }

    @Override
    public <T extends BaseRequest> FileResponce copyFile(Context context, T baseRequest) {
        return null;
    }

    @Override
    public <T extends BaseRequest> FileResponce query(Context context, T baseRequest) {
        return null;
    }

    @Override
    public <T extends BaseRequest> FileResponce renameTo(Context context, T where, T request) {
        return null;
    }
}
