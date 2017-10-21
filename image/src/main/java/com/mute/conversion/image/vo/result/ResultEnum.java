package com.mute.conversion.image.vo.result;

/**
 * Created by H on 2017/4/19.
 * 错误信息枚举
 *
 * @author H
 */
public enum ResultEnum {
    /**
     * SUCCESS
     */
    SUCCESS(0, "成功"),
    /**
     * UNKONW_ERROR
     */
    UNKONW_ERROR(-1, "未知错误，请联系管理员！"),
    /**
     * DATEFORMAT_ERROR
     */
    DATEFORMAT_ERROR(1, "开票日期格式校验失败【yyyy-MM-dd】");


    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
