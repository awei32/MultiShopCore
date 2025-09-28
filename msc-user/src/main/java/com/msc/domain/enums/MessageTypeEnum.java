package com.msc.domain.enums;

/**
 * 消息类型枚举
 */
public enum MessageTypeEnum {
    SYSTEM(1, "系统消息"),
    ORDER(2, "订单消息"),
    ACTIVITY(3, "活动消息"),
    CUSTOMER_SERVICE(4, "客服消息");
    
    private final Integer code;
    private final String desc;
    
    MessageTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public static MessageTypeEnum getByCode(Integer code) {
        for (MessageTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}