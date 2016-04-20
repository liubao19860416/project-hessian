package com.hessian.api;

import java.io.InputStream;

/**
 * 文件上传服务接口
 */
public interface FileUploadService {

    public void uploadFile(String filename, InputStream data);
}