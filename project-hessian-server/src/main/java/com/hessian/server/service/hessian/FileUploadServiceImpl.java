package com.hessian.server.service.hessian;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;

import com.hessian.api.FileUploadService;

/**
 * FileUploadServiceI文件上传接口的具体实现类
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {

    /*
     * (non-Javadoc)
     * 
     * @MethodName upload
     * 
     * @Description 上传文件
     * 
     * @author xudp
     * 
     * @param filename 上传的文件名
     * 
     * @param data 上传的文件的输入流
     * 
     * @see file.upload.service.FileUploadServiceI#upload(java.lang.String,
     * java.io.InputStream)
     */
    public void uploadFile(String filename, InputStream data) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            // 获取客户端传递的InputStream
            bis = new BufferedInputStream(data);
            // 创建文件输出流
            bos = new BufferedOutputStream(new FileOutputStream(
                    "D:/temp/" + filename));
            byte[] buffer = new byte[8192];
            int r = bis.read(buffer, 0, buffer.length);
            while (r > 0) {
                bos.write(buffer, 0, r);
                r = bis.read(buffer, 0, buffer.length);
            }
            System.out.println("-------文件上传成功！-------------");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}