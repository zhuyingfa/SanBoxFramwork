package com.yvan.sanboxframwork;

import java.io.File;

public class BaseRequest {

    // Picture/yvan.mp3
    // Movie/yvan
    // file   承载相对路径
    private File file;
    private String type;

    public BaseRequest() {
    }
    public BaseRequest(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
