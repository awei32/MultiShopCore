package com.msc.domain.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.msc.domain.enums.ThemeEnum;
import com.msc.domain.enums.PrivacyLevelEnum;

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
    @TableId(value = "id", type = IdType.ASSIGN_ID)
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
}