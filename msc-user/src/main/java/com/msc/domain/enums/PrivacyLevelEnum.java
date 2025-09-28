package com.msc.domain.enums;

/**
 * 隐私级别枚举
 */
public enum PrivacyLevelEnum {
    PUBLIC("public", "公开"),
    FRIENDS("friends", "好友可见"),
    PRIVATE("private", "私密");
    
    private final String code;
    private final String desc;
    
    PrivacyLevelEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public static PrivacyLevelEnum getByCode(String code) {
        for (PrivacyLevelEnum level : values()) {
            if (level.getCode().equals(code)) {
                return level;
            }
        }
        return null;
    }
}