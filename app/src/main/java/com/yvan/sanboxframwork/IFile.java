package com.yvan.sanboxframwork;

import android.content.Context;

public interface IFile {
    <T extends BaseRequest> FileResponce delete(Context context, T baseRequest);

    <T extends BaseRequest> FileResponce newCreateFile(Context context, T baseRequest);

    <T extends BaseRequest> FileResponce copyFile(Context context, T baseRequest);

    <T extends BaseRequest> FileResponce query(Context context, T baseRequest);

    <T extends BaseRequest> FileResponce renameTo(Context context, T where, T request);
}
