package com.msc.domain.enums;

import com.msc.domain.entity.User;

/**
 * 登录类型枚举
 */
public enum LoginTypeEnum {
    PASSWORD("password", "密码登录"),
    SMS("sms", "短信登录"),
    WECHAT("wechat", "微信登录"),
    ALIPAY("alipay", "支付宝登录"),
    REGISTER("register", "注册登录");
    
    private final String code;
    private final String desc;
    
    LoginTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public static LoginTypeEnum getByCode(String code) {
        for (LoginTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}