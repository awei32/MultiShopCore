package com.msc.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户积分日志实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPointsLog {
    
    /**
     * 日志ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 积分变动类型:1-获得,2-消费,3-过期,4-退还
     */
    private Integer changeType;
    
    /**
     * 积分变动数量
     */
    private Integer changePoints;
    
    /**
     * 变动前积分
     */
    private Integer beforePoints;
    
    /**
     * 变动后积分
     */
    private Integer afterPoints;
    
    /**
     * 变动原因
     */
    private String reason;
    
    /**
     * 关联订单ID
     */
    private Long orderId;
    
    /**
     * 关联活动ID
     */
    private Long activityId;
    
    /**
     * 积分来源:order-订单,activity-活动,manual-手动,system-系统
     */
    private String source;
    
    /**
     * 过期时间
     */
    private LocalDateTime expireTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    // 自定义构造方法
    public UserPointsLog(Long userId, Integer changeType, Integer changePoints, 
                        Integer beforePoints, String reason) {
        this.userId = userId;
        this.changeType = changeType;
        this.changePoints = changePoints;
        this.beforePoints = beforePoints;
        this.afterPoints = beforePoints + (changeType == 1 ? changePoints : -changePoints);
        this.reason = reason;
    }
    
    /**
     * 积分变动类型枚举
     */
    public enum ChangeType {
        EARN(1, "获得"),
        CONSUME(2, "消费"),
        EXPIRE(3, "过期"),
        REFUND(4, "退还");
        
        private final Integer code;
        private final String desc;
        
        ChangeType(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        
        public Integer getCode() {
            return code;
        }
        
        public String getDesc() {
            return desc;
        }
        
        public static ChangeType getByCode(Integer code) {
            for (ChangeType type : values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
            return null;
        }
    }
    
    /**
     * 积分来源枚举
     */
    public enum Source {
        ORDER("order", "订单"),
        ACTIVITY("activity", "活动"),
        MANUAL("manual", "手动"),
        SYSTEM("system", "系统");
        
        private final String code;
        private final String desc;
        
        Source(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        
        public String getCode() {
            return code;
        }
        
        public String getDesc() {
            return desc;
        }
        
        public static Source getByCode(String code) {
            for (Source source : values()) {
                if (source.getCode().equals(code)) {
                    return source;
                }
            }
            return null;
        }
    }
}