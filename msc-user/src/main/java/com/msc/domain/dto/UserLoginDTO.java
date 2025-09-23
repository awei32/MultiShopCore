package com.msc.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginDTO {
    
    /**
     * 登录账号(用户名/邮箱/手机号)
     */
    private String account;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 登录类型:1-密码登录,2-短信登录,3-第三方登录
     */
    private Integer loginType;
    
    /**
     * 验证码
     */
    private String verifyCode;
    
    /**
     * 设备类型:web,ios,android,wechat
     */
    private String deviceType;
    
    /**
     * 设备信息
     */
    private String deviceInfo;
    
    /**
     * 是否记住登录:0-否,1-是
     */
    private Integer rememberMe;
    
    /**
     * 第三方平台
     */
    private String platform;
    
    /**
     * 第三方授权码
     */
    private String authCode;
    
    /**
     * 第三方访问令牌
     */
    private String accessToken;
    
    // 自定义构造方法
    public UserLoginDTO(String account, String password, Integer loginType) {
        this.account = account;
        this.password = password;
        this.loginType = loginType;
    }
    
    /**
     * 登录类型枚举
     */
    public enum LoginType {
        PASSWORD(1, "密码登录"),
        SMS(2, "短信登录"),
        OAUTH(3, "第三方登录");
        
        private final Integer code;
        private final String desc;
        
        LoginType(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        
        public Integer getCode() {
            return code;
        }
        
        public String getDesc() {
            return desc;
        }
        
        public static LoginType getByCode(Integer code) {
            for (LoginType type : values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
            return null;
        }
    }
    
    /**
     * 设备类型枚举
     */
    public enum DeviceType {
        WEB("web", "网页"),
        IOS("ios", "苹果手机"),
        ANDROID("android", "安卓手机"),
        WECHAT("wechat", "微信小程序");
        
        private final String code;
        private final String desc;
        
        DeviceType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        
        public String getCode() {
            return code;
        }
        
        public String getDesc() {
            return desc;
        }
        
        public static DeviceType getByCode(String code) {
            for (DeviceType type : values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
            return null;
        }
    }
}