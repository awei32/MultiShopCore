package com.msc.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户基础信息实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@TableName("user")
public class User {
    
    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 用户名
     */
    @TableField("username")
    private String username;
    
    /**
     * 密码(加密)
     */
    @TableField("password")
    private String password;
    
    /**
     * 密码盐值
     */
    @TableField("salt")
    private String salt;
    
    /**
     * 邮箱
     */
    @TableField("email")
    private String email;
    
    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;
    
    /**
     * 头像URL
     */
    @TableField("avatar")
    private String avatar;
    
    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;
    
    /**
     * 性别:0-未知,1-男,2-女
     */
    @TableField("gender")
    private Integer gender;
    
    /**
     * 生日
     */
    @TableField("birthday")
    private LocalDate birthday;
    
    /**
     * 状态:0-禁用,1-正常,2-锁定
     */
    @TableField("status")
    private Integer status;
    
    /**
     * 会员等级:1-普通,2-银牌,3-金牌,4-钻石
     */
    @TableField("member_level")
    private Integer memberLevel;
    
    /**
     * 累计消费金额
     */
    @TableField("total_amount")
    private BigDecimal totalAmount;
    
    /**
     * 累计积分
     */
    @TableField("total_points")
    private Integer totalPoints;
    
    /**
     * 可用积分
     */
    @TableField("available_points")
    private Integer availablePoints;
    
    /**
     * 注册来源:web,app,wechat,alipay
     */
    @TableField("register_source")
    private String registerSource;
    
    /**
     * 注册IP
     */
    @TableField("register_ip")
    private String registerIp;
    
    /**
     * 最后登录时间
     */
    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;
    
    /**
     * 最后登录IP
     */
    @TableField("last_login_ip")
    private String lastLoginIp;
    
    /**
     * 登录次数
     */
    @TableField("login_count")
    private Integer loginCount;
    
    /**
     * 是否实名认证:0-否,1-是
     */
    @TableField("is_verified")
    private Integer isVerified;
    
    /**
     * 是否删除:0-否,1-是
     */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
    
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
     * 版本号(乐观锁)
     */
    @Version
    @TableField("version")
    private Integer version;
    
    /**
     * 自定义构造方法用于创建新用户
     */
    public User(String username, String password, String salt) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.status = 1;
        this.memberLevel = 1;
        this.totalAmount = BigDecimal.ZERO;
        this.totalPoints = 0;
        this.availablePoints = 0;
        this.loginCount = 0;
        this.isVerified = 0;
        this.isDeleted = 0;
        this.version = 0;
    }

    // 常量定义
    public static final class Status {
        public static final int DISABLED = 0;
        public static final int NORMAL = 1;
        public static final int LOCKED = 2;
    }

    public static final class Gender {
        public static final int UNKNOWN = 0;
        public static final int MALE = 1;
        public static final int FEMALE = 2;
    }

    public static final class MemberLevel {
        public static final int NORMAL = 1;
        public static final int SILVER = 2;
        public static final int GOLD = 3;
        public static final int DIAMOND = 4;
    }

    public static final class RegisterSource {
        public static final String WEB = "web";
        public static final String APP = "app";
        public static final String WECHAT = "wechat";
        public static final String ALIPAY = "alipay";
    }
}