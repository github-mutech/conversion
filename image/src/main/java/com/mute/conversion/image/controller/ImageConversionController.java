package com.mute.conversion.image.controller;

import com.mute.conversion.image.config.aliyunoss.AliyunOSSClient;
import com.mute.conversion.image.config.aliyunoss.service.imageconversion.ImageConversionService;
import com.mute.conversion.image.vo.result.Result;
import com.mute.conversion.image.vo.result.ResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author H
 * @date 2017/10/22
 */
@RestController
@RequestMapping("/ImageConversion")
public class ImageConversionController {

    @Value("${aliyun.OSS.defaultFolder}")
    private String defaultFolder;

    @Autowired
    private AliyunOSSClient aliyunOSSClient;

    @Autowired
    private ImageConversionService imageConversionService;

    @CrossOrigin
    @PostMapping(path = "/getImageUrl")
    public Result getImageUrl(@RequestParam("files") MultipartFile file) {
        if (!file.isEmpty()) {
            Map<String, Object> result = aliyunOSSClient.uploadImage2OSS(file, defaultFolder);
            String imageUrl = (String) result.get("imageUrl");
            if (StringUtils.isNotEmpty(imageUrl)) {
                return ResultUtil.success(imageUrl);
            } else {
                return ResultUtil.success("上传失败");
            }
        } else {
            return ResultUtil.error(1, "上传失败，请选择要上传的图片!");
        }
    }

    @CrossOrigin
    @GetMapping(path = "/getShowAndHideImageUrl")
    public Result getShowAndHideImageUrl(@RequestParam String showImageUrl, @RequestParam String hideImageUrl) {
        return ResultUtil.success(imageConversionService.getShowAndHideImageUrl(showImageUrl, hideImageUrl));
    }
}
