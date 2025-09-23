package com.msc.domain.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户消息实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMessage {
    
    /**
     * 消息ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 消息类型:1-系统消息,2-订单消息,3-活动消息,4-客服消息
     */
    private Integer messageType;
    
    /**
     * 消息标题
     */
    private String title;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 消息来源
     */
    private String source;
    
    /**
     * 关联ID(订单ID、活动ID等)
     */
    private Long relatedId;
    
    /**
     * 跳转链接
     */
    private String jumpUrl;
    
    /**
     * 消息图标
     */
    private String icon;
    
    /**
     * 是否已读:0-未读,1-已读
     */
    private Integer read;
    
    /**
     * 阅读时间
     */
    private LocalDateTime readTime;
    
    /**
     * 是否删除:0-否,1-是
     */
    private Integer deleted;
    
    /**
     * 删除时间
     */
    private LocalDateTime deleteTime;
    
    /**
     * 发送时间
     */
    private LocalDateTime sendTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    // 自定义构造方法
    public UserMessage(Long userId, Integer messageType, String title, String content) {
        this.userId = userId;
        this.messageType = messageType;
        this.title = title;
        this.content = content;
        this.read = 0;
        this.deleted = 0;
        this.sendTime = LocalDateTime.now();
    }
    
    /**
     * 消息类型枚举
     */
    public enum MessageType {
        SYSTEM(1, "系统消息"),
        ORDER(2, "订单消息"),
        ACTIVITY(3, "活动消息"),
        CUSTOMER_SERVICE(4, "客服消息");
        
        private final Integer code;
        private final String desc;
        
        MessageType(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        
        public Integer getCode() {
            return code;
        }
        
        public String getDesc() {
            return desc;
        }
        
        public static MessageType getByCode(Integer code) {
            for (MessageType type : values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
            return null;
        }
    }
    
    /**
     * 标记为已读
     */
    public void markAsRead() {
        this.read = 1;
        this.readTime = LocalDateTime.now();
    }
    
    /**
     * 标记为删除
     */
    public void markAsDeleted() {
        this.deleted = 1;
        this.deleteTime = LocalDateTime.now();
    }
}