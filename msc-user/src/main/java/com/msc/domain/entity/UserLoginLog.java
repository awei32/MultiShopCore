package com.msc.domain.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录日志实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginLog {
    
    /**
     * 日志ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 登录类型:1-密码登录,2-短信登录,3-第三方登录
     */
    private Integer loginType;
    
    /**
     * 登录IP
     */
    private String loginIp;
    
    /**
     * 登录地址
     */
    private String loginAddress;
    
    /**
     * 设备类型:web,ios,android,wechat
     */
    private String deviceType;
    
    /**
     * 设备信息
     */
    private String deviceInfo;
    
    /**
     * 浏览器信息
     */
    private String browserInfo;
    
    /**
     * 操作系统
     */
    private String osInfo;
    
    /**
     * 登录状态:1-成功,0-失败
     */
    private Integer loginStatus;
    
    /**
     * 失败原因
     */
    private String failReason;
    
    /**
     * 登录时间
     */
    private LocalDateTime loginTime;
    
    /**
     * 登出时间
     */
    private LocalDateTime logoutTime;
    
    /**
     * 在线时长(秒)
     */
    private Long onlineDuration;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    // 自定义构造方法
    public UserLoginLog(Long userId, Integer loginType, String loginIp, 
                       String deviceType, Integer loginStatus) {
        this.userId = userId;
        this.loginType = loginType;
        this.loginIp = loginIp;
        this.deviceType = deviceType;
        this.loginStatus = loginStatus;
        this.loginTime = LocalDateTime.now();
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
        IOS("ios", "iOS"),
        ANDROID("android", "Android"),
        WECHAT("wechat", "微信"),
        MINI_PROGRAM("mini_program", "小程序");
        
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
    
    /**
     * 计算在线时长
     */
    public void calculateOnlineDuration() {
        if (loginTime != null && logoutTime != null) {
            this.onlineDuration = java.time.Duration.between(loginTime, logoutTime).getSeconds();
        }
    }
}