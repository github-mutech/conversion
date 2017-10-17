package com.mute.conversion.image.controller;

import com.mute.conversion.image.config.aliyunoss.AliyunOSSClient;
import com.mute.conversion.image.util.OkHttpUtil;
import com.mute.conversion.image.vo.result.Result;
import com.mute.conversion.image.vo.result.ResultUtil;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ImageConversion")
public class ImageConversionController {

    @Value("${aliyun.OSS.defaultFolder}")
    private String defaultFolder;

    @Autowired
    private AliyunOSSClient aliyunOSSClient;

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
        Response showImageResponse = OkHttpUtil.getInstance().doGet(showImageUrl);
        Response hideImageResponse = OkHttpUtil.getInstance().doGet(hideImageUrl);
        InputStream showInputStream = showImageResponse.body().byteStream();
        InputStream hideInputStream = hideImageResponse.body().byteStream();
        BufferedImage showImage = null;
        BufferedImage hideImage = null;
        try {
            showImage = ImageIO.read(showInputStream);
            hideImage = ImageIO.read(hideInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (showImage != null) {
            int imageWith = showImage.getWidth();
            int imageHeight = showImage.getHeight();
            BufferedImage resImage = new BufferedImage(imageWith, imageHeight, BufferedImage.TYPE_4BYTE_ABGR);
//            BufferedImage resImage1 = new BufferedImage(imageWith, imageHeight, BufferedImage.TYPE_4BYTE_ABGR);
//            BufferedImage resImage2 = new BufferedImage(imageWith, imageHeight, BufferedImage.TYPE_4BYTE_ABGR);
//            for (int x = 0; x < imageWith; x++) {
//                for (int y = 0; y < imageHeight; y++) {
//                    if (x % 2 == 1) {
//                        if (y % 2 == 0) {
//                            int rgb = showImage.getRGB(x, y);
//                            System.out.println("x:"+x+"y:"+y+"rgb:"+rgb);
//                            resImage1.setRGB(x, y, rgb);
//                        } else {
//                            int rgb = showImage.getRGB(x, y);
//                            System.out.println("x:"+x+"y:"+y+"rgb:"+rgb);
//                            resImage2.setRGB(x, y, rgb);
//                        }
//                    } else {
//                        if (y % 2 == 0) {
//                            int rgb = showImage.getRGB(x, y);
//                            System.out.println("x:"+x+"y:"+y+"rgb:"+rgb);
//                            resImage2.setRGB(x, y, rgb);
//                        } else {
//                            int rgb = showImage.getRGB(x, y);
//                            System.out.println("x:"+x+"y:"+y+"rgb:"+rgb);
//                            resImage1.setRGB(x, y, rgb);
//                        }
//                    }
//                }
//            }
//            try {
//                ImageIO.write(resImage1, "png", new File("D:/temp1.png"));
//                ImageIO.write(resImage2, "png", new File("D:/temp2.png"));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            for (int x = 0; x < imageWith; x++) {
                for (int y = 0; y < imageHeight; y++) {
                    if (x % 2 == 1) {
                        if (y % 2 == 0) {
                            int rgb = hideImage.getRGB(x, y);
                            rgb = getRGBWithAlpla(rgb, 128);
                            resImage.setRGB(x, y, rgb);
                        } else {
                            int rgb = showImage.getRGB(x, y);
                            rgb = getRGBWithAlpla(rgb, 129);
                            resImage.setRGB(x, y, rgb);
                        }
                    } else {
                        if (y % 2 == 0) {
                            int rgb = showImage.getRGB(x, y);
                            rgb = getRGBWithAlpla(rgb, 129);
                            resImage.setRGB(x, y, rgb);
                        } else {
                            int rgb = hideImage.getRGB(x, y);
                            rgb = getRGBWithAlpla(rgb, 128);
                            resImage.setRGB(x, y, rgb);
                        }
                    }
                }
            }
            try {
                ImageIO.write(resImage, "png", new File("D:/temp.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private int getRGBWithAlpla(int rgb, int alpla) {
        int[] rgbArray = new int[3];
        rgbArray[0] = (rgb & 0xff0000) >> 16;
        rgbArray[1] = (rgb & 0xff00) >> 8;
        rgbArray[2] = (rgb & 0xff);
        if (alpla == 128) {
            int brightness = 150;
            rgbArray[0] = rgbArray[0] + brightness < 255 ? rgbArray[0] + brightness : 255;
            rgbArray[1] = rgbArray[1] + brightness < 255 ? rgbArray[1] + brightness : 255;
            rgbArray[2] = rgbArray[2] + brightness < 255 ? rgbArray[2] + brightness : 255;
        } else {


            int brightness = 30;
            rgbArray[0] = rgbArray[0] - brightness > 0 ? rgbArray[0] + brightness : 0;
            rgbArray[1] = rgbArray[1] - brightness > 0 ? rgbArray[1] + brightness : 0;
            rgbArray[2] = rgbArray[2] - brightness > 0 ? rgbArray[2] + brightness : 0;

            if (rgbArray[0] > 200 && rgbArray[1] > 200 && rgbArray[2] > 200) {
               return rgbArray[0] + (rgbArray[1] << 8) + (rgbArray[2] << 16) + (10 << 24);
            }
        }

        int rlt = rgbArray[0] + (rgbArray[1] << 8) + (rgbArray[2] << 16) + (alpla << 24);
        return rlt;
    }
}
