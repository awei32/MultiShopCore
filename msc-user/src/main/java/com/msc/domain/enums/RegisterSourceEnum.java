package com.msc.domain.enums;

/**
 * 注册来源枚举
 */
public enum RegisterSourceEnum {
    WEB("web", "网页"),
    APP("app", "手机应用"),
    WECHAT("wechat", "微信"),
    ALIPAY("alipay", "支付宝"),
    QQ("qq", "QQ");
    
    private final String code;
    private final String desc;
    
    RegisterSourceEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public static RegisterSourceEnum getByCode(String code) {
        for (RegisterSourceEnum source : values()) {
            if (source.getCode().equals(code)) {
                return source;
            }
        }
        return null;
    }
}