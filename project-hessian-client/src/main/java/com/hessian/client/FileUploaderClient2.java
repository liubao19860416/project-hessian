package com.hessian.client;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hessian.BaseTest;
import com.hessian.api.FileUploadService;

public class FileUploaderClient2 extends BaseTest{
    
    @Autowired
    private FileUploadService fileUploadService ;

    @Test
    public void testGetAllByHessian() throws Exception {
        //读取需要上传的文件
        InputStream data = new BufferedInputStream(new FileInputStream("D:/temp/VerifyCode.jpg"));
        //调用远程服务上传文件。
        fileUploadService.uploadFile("VerifyCode_hession.jpg", data);
        System.out.println("finished...");
    }
    
    
}
