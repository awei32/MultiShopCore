package com.msc.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 用户详细资料实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {
    
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 真实姓名
     */
    private String realName;
    
    /**
     * 身份证号
     */
    private String idCard;
    
    /**
     * 职业
     */
    private String profession;
    
    /**
     * 公司
     */
    private String company;
    
    /**
     * 学历
     */
    private String education;
    
    /**
     * 收入水平
     */
    private String incomeLevel;
    
    /**
     * 婚姻状况:0-未知,1-未婚,2-已婚,3-离异
     */
    private Integer maritalStatus;
    
    /**
     * 兴趣爱好
     */
    private String hobby;
    
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
     * 区县
     */
    private String district;
    
    /**
     * 详细地址
     */
    private String address;
    
    /**
     * 紧急联系人
     */
    private String emergencyContact;
    
    /**
     * 紧急联系电话
     */
    private String emergencyPhone;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 自定义构造方法
     */
    public UserProfile(Long userId) {
        this.userId = userId;
        this.maritalStatus = 0;
    }
}