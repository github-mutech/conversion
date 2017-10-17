package com.mute.conversion.image.config.aliyunoss.config;

import com.mute.conversion.image.config.aliyunoss.AliyunOSSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliyunOSSClientConfig {

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
    public AliyunOSSClient aliyunOSSClient() {
        return new AliyunOSSClient(endpoint, accessKeyId, accessKeySecret, backetName, defaultFolder);
    }
}
