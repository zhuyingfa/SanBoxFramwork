package com.yvan.sanboxframwork;

import android.os.Environment;

import com.yvan.sanboxframwork.impl.FileStoreImpl;
import com.yvan.sanboxframwork.impl.MediaStoreAccessImp;

public class FileAccessFactory {
    public static IFile getIFile(BaseRequest baseRequest) {
        // 29 30 31 32
        if (!Environment.isExternalStorageLegacy()) {
            setFileType(baseRequest);
            return MediaStoreAccessImp.getInstance();
        } else {
            return new FileStoreImpl();
        }
    }

    private static void setFileType(BaseRequest request) {
        // 根据后缀名 判断类型
        if (request.getFile().getAbsolutePath().endsWith(".mp3")
                || request.getFile().getAbsolutePath().endsWith(".wav")) {
            request.setType(MediaStoreAccessImp.AUDIO);
        } else if (request.getFile().getAbsolutePath().startsWith(MediaStoreAccessImp.VIDEO)
                || request.getFile().getAbsolutePath().endsWith(".mp4")
                || request.getFile().getAbsolutePath().endsWith(".rmvb")
                || request.getFile().getAbsolutePath().endsWith(".avi")) {
            request.setType(MediaStoreAccessImp.VIDEO);
        } else if (request.getFile().getAbsolutePath().startsWith(MediaStoreAccessImp.IMAGE)
                || request.getFile().getAbsolutePath().endsWith(".jpg")
                || request.getFile().getAbsolutePath().endsWith(".png")) {
            request.setType(MediaStoreAccessImp.IMAGE);
        } else {
            request.setType(MediaStoreAccessImp.DOWNLOADS);
        }
    }
}
