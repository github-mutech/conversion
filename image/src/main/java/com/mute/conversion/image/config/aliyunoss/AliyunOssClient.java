package com.mute.conversion.image.config.aliyunoss;


import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author H
 */
public class AliyunOssClient {

    private static final Logger logger = LoggerFactory.getLogger(AliyunOssClient.class);

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String backetName;
    private String defaultFolder;
    private OSSClient ossClient;

    private AliyunOssClient() {
    }

    public AliyunOssClient(String endpoint, String accessKeyId, String accessKeySecret, String backetName, String defaultFolder) {
        this.endpoint = endpoint;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.backetName = backetName;
        this.defaultFolder = defaultFolder;
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 上传图片至OSS
     *
     * @param multipartFile 上传文件（文件全路径如：D:\\image\\cake.jpg）
     * @param folder        模拟文件夹名 如"qj_nanjing/"
     * @return String 图片链接地址
     */
    public Map<String, Object> uploadImage2OSS(MultipartFile multipartFile, String folder) {
        Map<String, Object> result = new HashMap<>(2);
        try {
            // 文件名
            String fileName = multipartFile.getOriginalFilename();
            // 文件大小
            Long fileSize = multipartFile.getSize();
            // 新文件名
            String newFileName = String.valueOf(System.currentTimeMillis()).concat(fileName);
            // 文件类型
            String contentType = multipartFile.getContentType();
            // 以输入流的形式上传文件
            InputStream inputStream = multipartFile.getInputStream();
            //创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            //上传的文件的长度
            metadata.setContentLength(inputStream.available());
            //指定该Object被下载时的网页的缓存行为
            metadata.setCacheControl("no-cache");
            //指定该Object下设置Header
            metadata.setHeader("Pragma", "no-cache");
            //指定该Object被下载时的内容编码格式
            metadata.setContentEncoding("utf-8");
            //文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
            //如果没有扩展名则填默认值application/octet-stream
            metadata.setContentType(contentType);
            //指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
            metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");
            String key = folder + newFileName;
            //上传文件   (上传文件流的形式)
            PutObjectResult putResult = ossClient.putObject(backetName, key, inputStream, metadata);
            //解析结果
            result.put("eTag", putResult.getETag());

            Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000);
            URL url = ossClient.generatePresignedUrl(backetName, key, expiration);
            if (url != null) {
                result.put("imageUrl", url.toString());
            }
        } catch (Exception e) {
            logger.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
        }
        return result;
    }
}
