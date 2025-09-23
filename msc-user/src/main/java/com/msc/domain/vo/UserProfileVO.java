package com.msc.domain.vo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户详细信息VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileVO {
    
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
     * 手机号
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
     * 真实姓名
     */
    private String realName;
    
    /**
     * 身份证号
     */
    private String idCard;
    
    /**
     * 性别:0-未知,1-男,2-女
     */
    private Integer gender;
    
    /**
     * 生日
     */
    private LocalDate birthday;
    
    /**
     * 个人简介
     */
    private String bio;
    
    /**
     * 省份
     */
    private String province;
    
    /**
     * 城市
     */
    private String city;
    
    /**
     * 地址
     */
    private String address;
    
    /**
     * 职业
     */
    private String occupation;
    
    /**
     * 公司
     */
    private String company;
    
    /**
     * 学历
     */
    private String education;
    
    /**
     * 兴趣爱好
     */
    private String interests;
    
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
     * 冻结积分
     */
    private Integer frozenPoints;
    
    /**
     * 余额
     */
    private BigDecimal balance;
    
    /**
     * 冻结余额
     */
    private BigDecimal frozenBalance;
    
    /**
     * 注册来源
     */
    private String registerSource;
    
    /**
     * 是否实名认证:0-否,1-是
     */
    private Integer verified;
    
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
     * 注册时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}