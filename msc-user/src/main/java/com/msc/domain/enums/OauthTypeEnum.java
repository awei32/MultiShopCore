package com.msc.domain.enums;

/**
 * 第三方授权类型枚举
 */
public enum OauthTypeEnum {
    WECHAT("wechat", "微信"),
    ALIPAY("alipay", "支付宝"),
    QQ("qq", "QQ"),
    WEIBO("weibo", "微博");
    
    private final String code;
    private final String desc;
    
    OauthTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public static OauthTypeEnum getByCode(String code) {
        for (OauthTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}