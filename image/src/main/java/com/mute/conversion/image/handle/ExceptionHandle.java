package com.mute.conversion.image.handle;

import com.mute.conversion.image.exception.MyException;
import com.mute.conversion.image.vo.result.Result;
import com.mute.conversion.image.vo.result.ResultEnum;
import com.mute.conversion.image.vo.result.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by H on 2017/4/19.
 *
 * @author H
 */
@ControllerAdvice
public class ExceptionHandle {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);


    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handle(Exception e) {
        if (e instanceof MyException) {
            MyException myException = (MyException) e;
            return ResultUtil.error(myException.getCode(), myException.getMessage());
        } else {
            logger.error("[系统异常]", e);
            return ResultUtil.error(ResultEnum.UNKONW_ERROR);
        }
    }
}
