package com.msc.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户详细资料实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@TableName("user_profile")
public class UserProfile {
    
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    
    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;
    
    /**
     * 身份证号
     */
    @TableField("id_card")
    private String idCard;
    
    /**
     * 职业
     */
    @TableField("profession")
    private String profession;
    
    /**
     * 公司
     */
    @TableField("company")
    private String company;
    
    /**
     * 学历
     */
    @TableField("education")
    private String education;
    
    /**
     * 收入水平
     */
    @TableField("income_level")
    private String incomeLevel;
    
    /**
     * 婚姻状况:0-未知,1-未婚,2-已婚,3-离异
     */
    @TableField("marital_status")
    private Integer maritalStatus;
    
    /**
     * 兴趣爱好
     */
    @TableField("hobby")
    private String hobby;
    
    /**
     * 个人简介
     */
    @TableField("bio")
    private String bio;
    
    /**
     * 省份
     */
    @TableField("province")
    private String province;
    
    /**
     * 城市
     */
    @TableField("city")
    private String city;
    
    /**
     * 区县
     */
    @TableField("district")
    private String district;
    
    /**
     * 详细地址
     */
    @TableField("address")
    private String address;
    
    /**
     * 紧急联系人
     */
    @TableField("emergency_contact")
    private String emergencyContact;
    
    /**
     * 紧急联系电话
     */
    @TableField("emergency_phone")
    private String emergencyPhone;
    
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    public UserProfile(Long userId) {
        this.userId = userId;
        this.maritalStatus = 0;
    }

    // 常量定义
    public static final class MaritalStatus {
        public static final int UNKNOWN = 0;
        public static final int SINGLE = 1;
        public static final int MARRIED = 2;
        public static final int DIVORCED = 3;
    }
}