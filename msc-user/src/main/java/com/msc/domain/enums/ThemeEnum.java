package com.msc.domain.enums;

/**
 * 主题枚举
 */
public enum ThemeEnum {
    LIGHT("light", "浅色主题"),
    DARK("dark", "深色主题"),
    AUTO("auto", "自动主题");
    
    private final String code;
    private final String desc;
    
    ThemeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public static ThemeEnum getByCode(String code) {
        for (ThemeEnum theme : values()) {
            if (theme.getCode().equals(code)) {
                return theme;
            }
        }
        return null;
    }
}