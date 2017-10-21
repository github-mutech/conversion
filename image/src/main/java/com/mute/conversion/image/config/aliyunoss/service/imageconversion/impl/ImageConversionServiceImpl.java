package com.mute.conversion.image.config.aliyunoss.service.imageconversion.impl;

import com.mute.conversion.image.config.aliyunoss.service.imageconversion.ImageConversionService;
import com.mute.conversion.image.util.OkHttpUtil;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author H
 * @date 2017/10/22
 */
@Service
public class ImageConversionServiceImpl implements ImageConversionService {

    private static final Logger logger = LoggerFactory.getLogger(ImageConversionServiceImpl.class);

    @Override
    public String getShowAndHideImageUrl(String showImageUrl, String hideImageUrl) {
        BufferedImage showImage = getBufferedImageByUrl(showImageUrl);
        BufferedImage hideImage = getBufferedImageByUrl(hideImageUrl);
        if (showImage != null && hideImage != null) {
            int width = showImage.getWidth() > hideImage.getWidth() ? showImage.getWidth() : hideImage.getWidth();
            int height = showImage.getHeight() > hideImage.getHeight() ? showImage.getHeight() : hideImage.getHeight();
            showImage = getPreprocessedImage(showImage, width, height, Color.white.getRGB());
            hideImage = getPreprocessedImage(hideImage, width, height, Color.black.getRGB());
            BufferedImage resultImage = getResultImage(showImage, hideImage, width, height);
            try {
                ImageIO.write(resultImage, "png", new File("D:/temp.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private BufferedImage getBufferedImageByUrl(String imageUrl) {
        BufferedImage bufferedImage = null;
        Response imageResponse = OkHttpUtil.getInstance().doGet(imageUrl);
        InputStream inputStream = imageResponse.body().byteStream();
        try {
            bufferedImage = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedImage;
    }

    private BufferedImage getPreprocessedImage(BufferedImage image, int width, int height, int bjColor) {
        BufferedImage bufferedImage = null;
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        if (imageWidth == width && imageHeight == height) {
            return image;
        } else {
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
            // 背景
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bufferedImage.setRGB(x, y, bjColor);
                }
            }
            // 图片
            int startX = (width - imageWidth) / 2;
            int endX = startX + imageWidth;
            int startY = (height - imageHeight) / 2;
            int endY = startY + imageHeight;
            for (int x = startX; x < endX; x++) {
                for (int y = startY; y < endY; y++) {
                    bufferedImage.setRGB(x, y, image.getRGB(x - startX, y - startY));
                }
            }
        }
        return bufferedImage;
    }

    private BufferedImage getResultImage(BufferedImage showImage, BufferedImage hideImage, int width, int height) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (x % 2 == 0) {
                    if (y % 2 == 0) {
                        bufferedImage.setRGB(x, y, getGrayRgb(showImage.getRGB(x, y), 1));
                    } else {
                        bufferedImage.setRGB(x, y, getGrayRgb(hideImage.getRGB(x, y), 0));
                    }
                } else {
                    if (y % 2 == 1) {
                        bufferedImage.setRGB(x, y, getGrayRgb(showImage.getRGB(x, y), 1));
                    } else {
                        bufferedImage.setRGB(x, y, getGrayRgb(hideImage.getRGB(x, y), 0));
                    }
                }
            }
        }
        return bufferedImage;
    }

    private int getGrayRgb(int rgb, int type) {
        int alpha;
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;
        int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);
        if (type == 0) {
            // 白色背景看不见，黑色背景看得见
            alpha = gray;
            gray = gray == 0 ? 0 : 255;
        } else {
            // 黑色背景看不见，白色背景看得见
            alpha = 255 - gray;
            gray = 0;
        }

        int newRgb = 0;
        newRgb += alpha;
        newRgb = newRgb << 8;
        newRgb += gray;
        newRgb = newRgb << 8;
        newRgb += gray;
        newRgb = newRgb << 8;
        newRgb += gray;
        return newRgb;
    }
}
