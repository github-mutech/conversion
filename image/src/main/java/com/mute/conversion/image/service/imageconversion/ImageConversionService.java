package com.mute.conversion.image.service.imageconversion;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author H
 */
public interface ImageConversionService {

    /**
     * getImageUrl
     *
     * @param file file
     * @return String
     */
    String getImageUrl(MultipartFile file);

    /**
     * getShowAndHideImageUrl
     *
     * @param showImageUrl showImageUrl
     * @param hideImageUrl hideImageUrl
     * @return String
     */
    String getShowAndHideImageUrl(String showImageUrl, String hideImageUrl);

}
