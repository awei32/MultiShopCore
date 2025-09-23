package com.msc.domain.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户注册DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterDTO {
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 确认密码
     */
    private String confirmPassword;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 性别:0-未知,1-男,2-女
     */
    private Integer gender;
    
    /**
     * 生日
     */
    private LocalDate birthday;
    
    /**
     * 注册来源:web,app,wechat等
     */
    private String registerSource;
    
    /**
     * 验证码
     */
    private String verifyCode;
    
    /**
     * 邀请码
     */
    private String inviteCode;
    
    /**
     * 是否同意用户协议:0-否,1-是
     */
    private Integer agreeTerms;
    
    // 自定义构造方法
    public UserRegisterDTO(String username, String password, String email, String phone) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }
    
    /**
     * 性别枚举
     */
    public enum Gender {
        UNKNOWN(0, "未知"),
        MALE(1, "男"),
        FEMALE(2, "女");
        
        private final Integer code;
        private final String desc;
        
        Gender(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        
        public Integer getCode() {
            return code;
        }
        
        public String getDesc() {
            return desc;
        }
        
        public static Gender getByCode(Integer code) {
            for (Gender gender : values()) {
                if (gender.getCode().equals(code)) {
                    return gender;
                }
            }
            return null;
        }
    }
    
    /**
     * 注册来源枚举
     */
    public enum RegisterSource {
        WEB("web", "网页"),
        APP("app", "手机应用"),
        WECHAT("wechat", "微信"),
        ALIPAY("alipay", "支付宝"),
        QQ("qq", "QQ");
        
        private final String code;
        private final String desc;
        
        RegisterSource(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        
        public String getCode() {
            return code;
        }
        
        public String getDesc() {
            return desc;
        }
        
        public static RegisterSource getByCode(String code) {
            for (RegisterSource source : values()) {
                if (source.getCode().equals(code)) {
                    return source;
                }
            }
            return null;
        }
    }
}