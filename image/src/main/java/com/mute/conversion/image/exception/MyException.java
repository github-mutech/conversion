package com.mute.conversion.image.exception;


import com.mute.conversion.image.vo.result.ResultEnum;
import lombok.Data;

/**
 * Created by H on 2017/4/19.
 *
 * @author H
 */
@Data
public class MyException extends RuntimeException {

    private Integer code;

    public MyException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }
}
