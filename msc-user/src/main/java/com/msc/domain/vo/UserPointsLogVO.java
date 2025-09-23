package com.msc.domain.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户积分日志VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPointsLogVO {
    
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
     * 变动类型:earn,consume,expire,refund
     */
    private String changeType;
    
    /**
     * 变动类型描述
     */
    private String changeTypeDesc;
    
    /**
     * 变动积分(正数为增加，负数为减少)
     */
    private Integer changePoints;
    
    /**
     * 变动后积分余额
     */
    private Integer afterPoints;
    
    /**
     * 积分来源:order,sign,invite,activity,admin,refund
     */
    private String source;
    
    /**
     * 积分来源描述
     */
    private String sourceDesc;
    
    /**
     * 关联订单ID
     */
    private Long orderId;
    
    /**
     * 关联订单号
     */
    private String orderNo;
    
    /**
     * 关联金额
     */
    private BigDecimal relatedAmount;
    
    /**
     * 变动原因
     */
    private String reason;
    
    /**
     * 积分有效期
     */
    private LocalDateTime expireTime;
    
    /**
     * 操作人ID
     */
    private Long operatorId;
    
    /**
     * 操作人姓名
     */
    private String operatorName;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}