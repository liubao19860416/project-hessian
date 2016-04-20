package com.hessian.client;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import com.caucho.hessian.client.HessianProxyFactory;
import com.hessian.api.FileUploadService;

/**
 * 文件上传客户端
 */
public class FileUploaderClient1 {
    
     //Hessian服务的url
     private static final String url = "http://localhost:8080/project-hessian-server/remote/uploadFile";

    public static void main(String[] args) throws Exception {
        //创建HessianProxyFactory实例
        HessianProxyFactory factory = new HessianProxyFactory();
        //获得Hessian服务的远程引用
        FileUploadService uploader = (FileUploadService) factory.create(FileUploadService.class, url);
        //读取需要上传的文件
        InputStream data = new BufferedInputStream(new FileInputStream("D:/temp/VerifyCode.jpg"));
        //调用远程服务上传文件。
        uploader.uploadFile("VerifyCode_hession.jpg", data);
    }
}