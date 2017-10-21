package com.mute.conversion.image.config.aliyunoss.service.imageconversion;

/**
 * @author H
 * @date 2017/10/21
 */
public interface ImageConversionService {
    /**
     * getShowAndHideImageUrl
     *
     * @param showImageUrl showImageUrl
     * @param hideImageUrl hideImageUrl
     * @return String
     */
    String getShowAndHideImageUrl(String showImageUrl, String hideImageUrl);

}
