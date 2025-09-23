package com.msc.domain.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户偏好设置实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreference {
    
    /**
     * 偏好ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 语言偏好
     */
    private String language;
    
    /**
     * 时区
     */
    private String timezone;
    
    /**
     * 货币偏好
     */
    private String currency;
    
    /**
     * 主题偏好:light,dark,auto
     */
    private String theme;
    
    /**
     * 是否接收邮件通知:0-否,1-是
     */
    private Integer emailNotification;
    
    /**
     * 是否接收短信通知:0-否,1-是
     */
    private Integer smsNotification;
    
    /**
     * 是否接收推送通知:0-否,1-是
     */
    private Integer pushNotification;
    
    /**
     * 是否接收营销信息:0-否,1-是
     */
    private Integer marketingNotification;
    
    /**
     * 隐私设置:public,friends,private
     */
    private String privacyLevel;
    
    /**
     * 是否显示在线状态:0-否,1-是
     */
    private Integer showOnlineStatus;
    
    /**
     * 是否允许搜索:0-否,1-是
     */
    private Integer allowSearch;
    
    /**
     * 默认收货地址ID
     */
    private Long defaultAddressId;
    
    /**
     * 偏好商品分类(JSON格式)
     */
    private String preferredCategories;
    
    /**
     * 偏好品牌(JSON格式)
     */
    private String preferredBrands;
    
    /**
     * 价格区间偏好(JSON格式)
     */
    private String priceRange;
    
    /**
     * 购物习惯(JSON格式)
     */
    private String shoppingHabits;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    // 自定义构造方法
    public UserPreference(Long userId) {
        this.userId = userId;
        // 设置默认值
        this.language = "zh-CN";
        this.timezone = "Asia/Shanghai";
        this.currency = "CNY";
        this.theme = "light";
        this.emailNotification = 1;
        this.smsNotification = 1;
        this.pushNotification = 1;
        this.marketingNotification = 0;
        this.privacyLevel = "public";
        this.showOnlineStatus = 1;
        this.allowSearch = 1;
    }
    
    /**
     * 主题枚举
     */
    public enum Theme {
        LIGHT("light", "浅色主题"),
        DARK("dark", "深色主题"),
        AUTO("auto", "自动主题");
        
        private final String code;
        private final String desc;
        
        Theme(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        
        public String getCode() {
            return code;
        }
        
        public String getDesc() {
            return desc;
        }
        
        public static Theme getByCode(String code) {
            for (Theme theme : values()) {
                if (theme.getCode().equals(code)) {
                    return theme;
                }
            }
            return null;
        }
    }
    
    /**
     * 隐私级别枚举
     */
    public enum PrivacyLevel {
        PUBLIC("public", "公开"),
        FRIENDS("friends", "好友可见"),
        PRIVATE("private", "私密");
        
        private final String code;
        private final String desc;
        
        PrivacyLevel(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        
        public String getCode() {
            return code;
        }
        
        public String getDesc() {
            return desc;
        }
        
        public static PrivacyLevel getByCode(String code) {
            for (PrivacyLevel level : values()) {
                if (level.getCode().equals(code)) {
                    return level;
                }
            }
            return null;
        }
    }
    
    /**
     * 更新通知设置
     */
    public void updateNotificationSettings(Integer email, Integer sms, Integer push, Integer marketing) {
        this.emailNotification = email;
        this.smsNotification = sms;
        this.pushNotification = push;
        this.marketingNotification = marketing;
    }
    
    /**
     * 更新隐私设置
     */
    public void updatePrivacySettings(String privacyLevel, Integer showOnlineStatus, Integer allowSearch) {
        this.privacyLevel = privacyLevel;
        this.showOnlineStatus = showOnlineStatus;
        this.allowSearch = allowSearch;
    }
}