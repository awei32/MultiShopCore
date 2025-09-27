package com.msc.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

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
    @NotBlank(message = "登录账号不能为空")
    @Size(min = 3, max = 50, message = "登录账号长度必须在3-50个字符之间")
    private String account;
    
    /**
     * 密码
     */
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    private String password;
    
    /**
     * 登录类型:1-密码登录,2-短信登录,3-第三方登录
     */
    @NotNull(message = "登录类型不能为空")
    @Min(value = 1, message = "登录类型值不正确")
    @Max(value = 3, message = "登录类型值不正确")
    private Integer loginType;
    
    /**
     * 验证码
     */
    @Size(min = 4, max = 6, message = "验证码长度不正确")
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