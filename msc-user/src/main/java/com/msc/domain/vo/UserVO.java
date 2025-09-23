package com.msc.domain.vo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息展示VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVO {
    
    /**
     * 用户ID
     */
    private Long id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 手机号(脱敏)
     */
    private String phone;
    
    /**
     * 头像
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
     * 性别描述
     */
    private String genderDesc;
    
    /**
     * 生日
     */
    private LocalDate birthday;
    
    /**
     * 状态:0-禁用,1-正常,2-锁定
     */
    private Integer status;
    
    /**
     * 状态描述
     */
    private String statusDesc;
    
    /**
     * 会员等级
     */
    private String memberLevel;
    
    /**
     * 会员等级名称
     */
    private String memberLevelName;
    
    /**
     * 累计消费金额
     */
    private BigDecimal totalAmount;
    
    /**
     * 总积分
     */
    private Integer totalPoints;
    
    /**
     * 可用积分
     */
    private Integer availablePoints;
    
    /**
     * 注册来源
     */
    private String registerSource;
    
    /**
     * 注册来源描述
     */
    private String registerSourceDesc;
    
    /**
     * 是否实名认证:0-否,1-是
     */
    private Integer verified;
    
    /**
     * 实名认证描述
     */
    private String verifiedDesc;
    
    /**
     * 省份
     */
    private String province;
    
    /**
     * 城市
     */
    private String city;
    
    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
    
    /**
     * 最后登录IP
     */
    private String lastLoginIp;
    
    /**
     * 注册时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 自定义构造方法
     */
    public UserVO(Long id, String username, String nickname, String avatar) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.avatar = avatar;
    }
}