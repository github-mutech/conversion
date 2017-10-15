package com.mute.conversion.image.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ImageConversion")
public class ImageConversionController {
    @RequestMapping("/getShowImageUrl")
    public String getShowImageUrl() {
        return null;
    }
}
