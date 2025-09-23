package com.msc.domain.vo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录日志VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginLogVO {
    
    /**
     * 日志ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 登录类型:password,sms,email,oauth
     */
    private String loginType;
    
    /**
     * 登录类型描述
     */
    private String loginTypeDesc;
    
    /**
     * 设备类型:web,mobile,tablet,desktop
     */
    private String deviceType;
    
    /**
     * 设备类型描述
     */
    private String deviceTypeDesc;
    
    /**
     * 设备信息
     */
    private String deviceInfo;
    
    /**
     * 登录IP
     */
    private String loginIp;
    
    /**
     * IP归属地
     */
    private String ipLocation;
    
    /**
     * 浏览器信息
     */
    private String userAgent;
    
    /**
     * 登录状态:success,failed
     */
    private String loginStatus;
    
    /**
     * 登录状态描述
     */
    private String loginStatusDesc;
    
    /**
     * 失败原因
     */
    private String failureReason;
    
    /**
     * 登录时间
     */
    private LocalDateTime loginTime;
    
    /**
     * 登出时间
     */
    private LocalDateTime logoutTime;
    
    /**
     * 在线时长(分钟)
     */
    private Long onlineDuration;
    
    /**
     * 在线时长描述
     */
    private String onlineDurationDesc;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}