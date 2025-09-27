package com.msc.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户会员等级表实体类
 *
 * @author MultiShopCore
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@TableName("user_member_level")
public class UserMemberLevel {
    
    /**
     * 等级ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 等级代码
     */
    @TableField("level_code")
    private String levelCode;
    
    /**
     * 等级名称
     */
    @TableField("level_name")
    private String levelName;
    
    /**
     * 最低消费金额
     */
    @TableField("min_amount")
    private BigDecimal minAmount;
    
    /**
     * 最高消费金额
     */
    @TableField("max_amount")
    private BigDecimal maxAmount;
    
    /**
     * 折扣率
     */
    @TableField("discount_rate")
    private BigDecimal discountRate;
    
    /**
     * 积分倍率
     */
    @TableField("points_rate")
    private BigDecimal pointsRate;
    
    /**
     * 是否包邮:0-否,1-是
     */
    @TableField("free_shipping")
    private Integer freeShipping;
    
    /**
     * 生日折扣
     */
    @TableField("birthday_discount")
    private BigDecimal birthdayDiscount;
    
    /**
     * 等级图标
     */
    @TableField("level_icon")
    private String levelIcon;
    
    /**
     * 等级颜色
     */
    @TableField("level_color")
    private String levelColor;
    
    /**
     * 会员特权(JSON格式)
     */
    @TableField("privileges")
    private String privileges;
    
    /**
     * 排序
     */
    @TableField("sort_order")
    private Integer sortOrder;
    
    /**
     * 是否启用:0-否,1-是
     */
    @TableField("is_active")
    private Integer isActive;
    
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
    
    /**
     * 自定义构造方法
     */
    public UserMemberLevel(String levelCode, String levelName, BigDecimal minAmount) {
        this.levelCode = levelCode;
        this.levelName = levelName;
        this.minAmount = minAmount;
        this.discountRate = BigDecimal.ONE;
        this.pointsRate = BigDecimal.ONE;
        this.freeShipping = 0;
        this.sortOrder = 0;
        this.isActive = 1;
    }
    
    /**
     * 判断指定金额是否在当前等级范围内
     */
    public boolean isInRange(BigDecimal amount) {
        if (amount == null) {
            return false;
        }
        
        boolean minCheck = minAmount == null || amount.compareTo(minAmount) >= 0;
        boolean maxCheck = maxAmount == null || amount.compareTo(maxAmount) <= 0;
        
        return minCheck && maxCheck;
    }
}