package com.mute.conversion.image.controller;

import com.mute.conversion.image.service.imageconversion.ImageConversionService;
import com.mute.conversion.image.vo.result.Result;
import com.mute.conversion.image.vo.result.ResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author H
 * @date 2017/10/22
 */
@RestController
@RequestMapping("/ImageConversion")
public class ImageConversionController {


    @Autowired
    private ImageConversionService imageConversionService;

    @CrossOrigin
    @PostMapping(path = "/getImageUrl")
    public Result getImageUrl(@RequestParam("image") MultipartFile file) {
        String imageUrl = imageConversionService.getImageUrl(file);
        if (StringUtils.isNotEmpty(imageUrl)) {
            return ResultUtil.success(imageUrl);
        } else {
            return ResultUtil.success("上传失败");
        }
    }

    @CrossOrigin
    @GetMapping(path = "/getShowAndHideImageUrl")
    public Result getShowAndHideImageUrl(@RequestParam String showImageUrl, @RequestParam String hideImageUrl) {
        return ResultUtil.success(imageConversionService.getShowAndHideImageUrl(showImageUrl, hideImageUrl));
    }
}
