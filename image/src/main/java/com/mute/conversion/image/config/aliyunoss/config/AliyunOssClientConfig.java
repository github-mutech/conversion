package com.mute.conversion.image.config.aliyunoss.config;

import com.mute.conversion.image.config.aliyunoss.AliyunOssClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author H
 */
@Configuration
public class AliyunOssClientConfig {

    @Value("${aliyun.OSS.endpoint}")
    private String endpoint;

    @Value("${aliyun.OSS.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.OSS.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.OSS.backetName}")
    private String backetName;

    @Value("${aliyun.OSS.defaultFolder}")
    private String defaultFolder;

    @Bean("AliyunOSSClient")
    public AliyunOssClient aliyunOSSClient() {
        return new AliyunOssClient(endpoint, accessKeyId, accessKeySecret, backetName, defaultFolder);
    }
}
