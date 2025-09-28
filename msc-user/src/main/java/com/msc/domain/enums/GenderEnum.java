package com.msc.domain.enums;

/**
 * 性别枚举
 */
public enum GenderEnum {
    UNKNOWN(0, "未知"),
    MALE(1, "男"),
    FEMALE(2, "女");
    
    private final Integer code;
    private final String desc;
    
    GenderEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public static GenderEnum getByCode(Integer code) {
        for (GenderEnum gender : values()) {
            if (gender.getCode().equals(code)) {
                return gender;
            }
        }
        return null;
    }
}