package com.msc.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户第三方授权实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@TableName("user_oauth")
public class UserOauth {
    
    /**
     * 绑定ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    
    /**
     * 第三方类型:wechat,alipay,qq,weibo
     */
    @TableField("oauth_type")
    private String oauthType;
    
    /**
     * 第三方用户ID
     */
    @TableField("oauth_id")
    private String oauthId;
    
    /**
     * 第三方用户名
     */
    @TableField("oauth_name")
    private String oauthName;
    
    /**
     * 第三方头像
     */
    @TableField("oauth_avatar")
    private String oauthAvatar;
    
    /**
     * 访问令牌
     */
    @TableField("access_token")
    private String accessToken;
    
    /**
     * 刷新令牌
     */
    @TableField("refresh_token")
    private String refreshToken;
    
    /**
     * 令牌过期时间(秒)
     */
    @TableField("expires_in")
    private Integer expiresIn;
    
    /**
     * 开放平台统一ID
     */
    @TableField("union_id")
    private String unionId;
    
    /**
     * 额外信息(JSON格式)
     */
    @TableField("extra_info")
    private String extraInfo;
    
    /**
     * 绑定时间
     */
    @TableField(value = "bind_time", fill = FieldFill.INSERT)
    private LocalDateTime bindTime;
    
    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /**
     * 自定义构造方法
     */
    public UserOauth(Long userId, String oauthType, String oauthId) {
        this.userId = userId;
        this.oauthType = oauthType;
        this.oauthId = oauthId;
        this.bindTime = LocalDateTime.now();
    }
    
    /**
     * 第三方类型常量
     */
    public static final class OauthType {
        /** 微信 */
        public static final String WECHAT = "wechat";
        /** 支付宝 */
        public static final String ALIPAY = "alipay";
        /** QQ */
        public static final String QQ = "qq";
        /** 微博 */
        public static final String WEIBO = "weibo";
    }
    
    /**
     * 更新令牌信息
     */
    public void updateToken(String accessToken, String refreshToken, Integer expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 检查令牌是否过期
     */
    public boolean isTokenExpired() {
        if (expiresIn == null || bindTime == null) {
            return false;
        }
        LocalDateTime expireTime = bindTime.plusSeconds(expiresIn);
        return LocalDateTime.now().isAfter(expireTime);
    }
    
    /**
     * 更新额外信息
     */
    public void updateExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
        this.updateTime = LocalDateTime.now();
    }
}