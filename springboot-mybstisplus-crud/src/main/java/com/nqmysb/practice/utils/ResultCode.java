package com.nqmysb.practice.utils;

/**
 * 错误代码编号
 *
 * @author liaocan
 * 2019-05-15
 */
public enum ResultCode {
	
    SUCCESS(200, "成功"),
    
    NOT_LOGIN(400, "没有登录"),
    
    EXCEPTION(500, "发生异常"),
    
    UNAUTHORIZED(401, "未授权"),
    
    SYS_ERROR(402, "系统错误"),
    
    PARAMS_ERROR(403, "参数错误 "),
    
    NOT_SUPPORTED(410, "不支持或已经废弃"),
    
    INVALID_AUTHCODE(444, "无效的AuthCode"),
    
    TOO_FREQUENT(445, "太频繁的调用"),
    
    UNKNOWN_ERROR(499, "未知错误");

    private int val;
    private String msg;

    ResultCode(int val, String msg) {
        this.val = val;
        this.msg = msg;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean equalVal(int codeVal) {
        return this.val == codeVal;
    }

}
