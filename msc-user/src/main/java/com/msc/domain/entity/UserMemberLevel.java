package com.msc.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户会员等级实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMemberLevel {
    
    /**
     * 等级ID
     */
    private Long id;
    
    /**
     * 等级代码
     */
    private String levelCode;
    
    /**
     * 等级名称
     */
    private String levelName;
    
    /**
     * 最低消费金额
     */
    private BigDecimal minAmount;
    
    /**
     * 最高消费金额
     */
    private BigDecimal maxAmount;
    
    /**
     * 折扣率
     */
    private BigDecimal discountRate;
    
    /**
     * 积分倍率
     */
    private BigDecimal pointsRate;
    
    /**
     * 是否包邮:0-否,1-是
     */
    private Integer freeShipping;
    
    /**
     * 生日折扣
     */
    private BigDecimal birthdayDiscount;
    
    /**
     * 等级图标
     */
    private String levelIcon;
    
    /**
     * 等级颜色
     */
    private String levelColor;
    
    /**
     * 会员特权(JSON格式)
     */
    private String privileges;
    
    /**
     * 排序
     */
    private Integer sortOrder;
    
    /**
     * 是否启用:0-否,1-是
     */
    private Integer active;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    // 自定义构造方法
    public UserMemberLevel(String levelCode, String levelName, BigDecimal minAmount) {
        this.levelCode = levelCode;
        this.levelName = levelName;
        this.minAmount = minAmount;
        this.discountRate = BigDecimal.ONE;
        this.pointsRate = BigDecimal.ONE;
        this.freeShipping = 0;
        this.sortOrder = 0;
        this.active = 1;
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