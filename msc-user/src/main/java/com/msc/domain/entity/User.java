package com.msc.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户基础信息实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    /**
     * 用户ID
     */
    private Long id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码(加密)
     */
    private String password;
    
    /**
     * 密码盐值
     */
    private String salt;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 头像URL
     */
    private String avatar;
    
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
     * 状态:0-禁用,1-正常,2-锁定
     */
    private Integer status;
    
    /**
     * 会员等级:1-普通,2-银牌,3-金牌,4-钻石
     */
    private Integer memberLevel;
    
    /**
     * 累计消费金额
     */
    private BigDecimal totalAmount;
    
    /**
     * 累计积分
     */
    private Integer totalPoints;
    
    /**
     * 可用积分
     */
    private Integer availablePoints;
    
    /**
     * 注册来源:web,app,wechat,alipay
     */
    private String registerSource;
    
    /**
     * 注册IP
     */
    private String registerIp;
    
    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
    
    /**
     * 最后登录IP
     */
    private String lastLoginIp;
    
    /**
     * 登录次数
     */
    private Integer loginCount;
    
    /**
     * 是否实名认证:0-否,1-是
     */
    private Integer verified;
    
    /**
     * 是否删除:0-否,1-是
     */
    private Integer deleted;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 版本号(乐观锁)
     */
    private Integer version;
    
    /**
     * 自定义构造方法用于创建新用户
     */
    public User(String username, String password, String salt) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.status = 1;
        this.memberLevel = 1;
        this.totalAmount = BigDecimal.ZERO;
        this.totalPoints = 0;
        this.availablePoints = 0;
        this.loginCount = 0;
        this.verified = 0;
        this.deleted = 0;
        this.version = 0;
    }
}