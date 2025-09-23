package com.msc.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户密码DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPasswordDTO {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 旧密码
     */
    private String oldPassword;
    
    /**
     * 新密码
     */
    private String newPassword;
    
    /**
     * 确认新密码
     */
    private String confirmPassword;
    
    /**
     * 验证码
     */
    private String verifyCode;
    
    /**
     * 验证方式:email,phone
     */
    private String verifyType;
    
    /**
     * 重置密码token
     */
    private String resetToken;
    
    /**
     * 自定义构造方法
     */
    public UserPasswordDTO(Long userId, String oldPassword, String newPassword) {
        this.userId = userId;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}