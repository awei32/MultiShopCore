package com.msc.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户积分记录表实体类
 *
 * @author MultiShopCore
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@TableName("user_points_log")
public class UserPointsLog {
    
    /**
     * 记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    
    /**
     * 关联订单ID
     */
    @TableField("order_id")
    private Long orderId;
    
    /**
     * 积分类型:1-获得,2-消费,3-过期,4-退还
     */
    @TableField("points_type")
    private Integer pointsType;
    
    /**
     * 积分数量
     */
    @TableField("points_amount")
    private Integer pointsAmount;
    
    /**
     * 积分余额
     */
    @TableField("points_balance")
    private Integer pointsBalance;
    
    /**
     * 来源类型:order,sign,activity,refund等
     */
    @TableField("source_type")
    private String sourceType;
    
    /**
     * 来源ID
     */
    @TableField("source_id")
    private Long sourceId;
    
    /**
     * 描述
     */
    @TableField("description")
    private String description;
    
    /**
     * 过期时间
     */
    @TableField("expire_time")
    private LocalDateTime expireTime;
    
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 自定义构造方法
     */
    public UserPointsLog(Long userId, Integer pointsType, Integer pointsAmount, 
                        Integer pointsBalance, String sourceType) {
        this.userId = userId;
        this.pointsType = pointsType;
        this.pointsAmount = pointsAmount;
        this.pointsBalance = pointsBalance;
        this.sourceType = sourceType;
    }
    
    /**
     * 积分类型常量
     */
    public static final class PointsType {
        /** 获得积分 */
        public static final Integer EARN = 1;
        /** 消费积分 */
        public static final Integer CONSUME = 2;
        /** 过期积分 */
        public static final Integer EXPIRE = 3;
        /** 退还积分 */
        public static final Integer REFUND = 4;
    }

    /**
     * 来源类型常量
     */
    public static final class SourceType {
        /** 订单获得 */
        public static final String ORDER = "order";
        /** 签到获得 */
        public static final String SIGN = "sign";
        /** 活动获得 */
        public static final String ACTIVITY = "activity";
        /** 退款退还 */
        public static final String REFUND = "refund";
        /** 系统调整 */
        public static final String SYSTEM = "system";
        /** 兑换消费 */
        public static final String EXCHANGE = "exchange";
    }

    /**
     * 判断是否为获得积分
     */
    public boolean isEarnPoints() {
        return PointsType.EARN.equals(this.pointsType);
    }

    /**
     * 判断是否为消费积分
     */
    public boolean isConsumePoints() {
        return PointsType.CONSUME.equals(this.pointsType);
    }

    /**
     * 判断是否已过期
     */
    public boolean isExpired() {
        return expireTime != null && expireTime.isBefore(LocalDateTime.now());
    }
}