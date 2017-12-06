package com.mute.conversion.txt.controller;

import com.mute.conversion.txt.service.TxtService;
import com.mute.conversion.util.reslut.Result;
import com.mute.conversion.util.reslut.ResultEnum;
import com.mute.conversion.util.reslut.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author H
 * @date 2017-12-06
 */
@RestController
@RequestMapping(path = "/txt")
public class TxtController {
    @Autowired
    private TxtService txtService;

    @RequestMapping(path = "/getMp3Url", method = RequestMethod.GET)
    @GetMapping
    public Result getMp3Url(@RequestParam String txt) {
        String mp3Url = txtService.getMp3Url(txt);
        if (mp3Url == null) {
            return ResultUtil.error(ResultEnum.UNKONW_ERROR);
        }
        return ResultUtil.success(mp3Url);
    }

}
