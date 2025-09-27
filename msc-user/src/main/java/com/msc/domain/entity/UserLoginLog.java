package com.msc.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户登录日志实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@TableName("user_login_log")
public class UserLoginLog {
    
    /**
     * 日志ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    
    /**
     * 登录类型:password,sms,wechat,alipay
     */
    @TableField("login_type")
    private String loginType;
    
    /**
     * 登录设备
     */
    @TableField("login_device")
    private String loginDevice;
    
    /**
     * 登录IP
     */
    @TableField("login_ip")
    private String loginIp;
    
    /**
     * 登录地点
     */
    @TableField("login_location")
    private String loginLocation;
    
    /**
     * 用户代理
     */
    @TableField("user_agent")
    private String userAgent;
    
    /**
     * 登录状态:1-成功,0-失败
     */
    @TableField("login_status")
    private Integer loginStatus;
    
    /**
     * 失败原因
     */
    @TableField("fail_reason")
    private String failReason;
    
    /**
     * 会话ID
     */
    @TableField("session_id")
    private String sessionId;
    
    /**
     * 登录时间
     */
    @TableField("login_time")
    private LocalDateTime loginTime;
    
    /**
     * 退出时间
     */
    @TableField("logout_time")
    private LocalDateTime logoutTime;
    
    /**
     * 自定义构造方法
     */
    public UserLoginLog(Long userId, String loginType, String loginIp, 
                       String loginDevice, Integer loginStatus) {
        this.userId = userId;
        this.loginType = loginType;
        this.loginIp = loginIp;
        this.loginDevice = loginDevice;
        this.loginStatus = loginStatus;
        this.loginTime = LocalDateTime.now();
    }

    
    /**
     * 登录类型常量
     */
    public static final class LoginType {
        /** 密码登录 */
        public static final String PASSWORD = "password";
        /** 短信登录 */
        public static final String SMS = "sms";
        /** 微信登录 */
        public static final String WECHAT = "wechat";
        /** 支付宝登录 */
        public static final String ALIPAY = "alipay";
    }
    
    /**
     * 登录状态常量
     */
    public static final class LoginStatus {
        /** 登录成功 */
        public static final Integer SUCCESS = 1;
        /** 登录失败 */
        public static final Integer FAILED = 0;
    }
    
    /**
     * 计算在线时长(秒)
     */
    public Long calculateOnlineDuration() {
        if (loginTime != null && logoutTime != null) {
            return java.time.Duration.between(loginTime, logoutTime).getSeconds();
        }
        return null;
    }
    
    /**
     * 是否登录成功
     */
    public boolean isLoginSuccess() {
        return Integer.valueOf(1).equals(this.loginStatus);
    }
}