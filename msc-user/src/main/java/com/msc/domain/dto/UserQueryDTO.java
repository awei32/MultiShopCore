package com.msc.domain.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户查询DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserQueryDTO {
    
    /**
     * 用户ID
     */
    private Long id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 性别:0-未知,1-男,2-女
     */
    private Integer gender;
    
    /**
     * 用户状态:0-禁用,1-正常,2-锁定
     */
    private Integer status;
    
    /**
     * 是否验证:0-未验证,1-已验证
     */
    private Integer verified;
    
    /**
     * 注册来源
     */
    private String registerSource;
    
    /**
     * 会员等级
     */
    private String memberLevel;
    
    /**
     * 创建时间开始
     */
    private LocalDateTime createTimeStart;
    
    /**
     * 创建时间结束
     */
    private LocalDateTime createTimeEnd;
    
    /**
     * 最后登录时间开始
     */
    private LocalDateTime lastLoginTimeStart;
    
    /**
     * 最后登录时间结束
     */
    private LocalDateTime lastLoginTimeEnd;
    
    /**
     * 省份
     */
    private String province;
    
    /**
     * 城市
     */
    private String city;
    
    /**
     * 排序字段
     */
    private String orderBy;
    
    /**
     * 排序方向:asc,desc
     */
    private String orderDirection;
    
    /**
     * 页码
     */
    private Integer pageNum;
    
    /**
     * 页大小
     */
    private Integer pageSize;
}