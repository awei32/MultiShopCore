package com.msc.domain.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

import com.msc.domain.enums.RegisterSourceEnum;

/**
 * 用户注册DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterDTO {
    
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    private String username;
    
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    private String password;
    
    /**
     * 确认密码
     */
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
    
    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;
    
    /**
     * 手机号
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    /**
     * 昵称
     */
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String nickname;
    
    /**
     * 性别:0-未知,1-男,2-女
     */
    @Min(value = 0, message = "性别值不正确")
    @Max(value = 2, message = "性别值不正确")
    private Integer gender;
    
    /**
     * 生日
     */
    @Past(message = "生日必须是过去的日期")
    private LocalDate birthday;
    
    /**
     * 注册来源:web,app,wechat,alipay
     */
    private String registerSource = "web";
    
    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    @Size(min = 4, max = 6, message = "验证码长度不正确")
    private String verifyCode;
    
    /**
     * 邀请码
     */
    @Size(max = 20, message = "邀请码长度不能超过20个字符")
    private String inviteCode;
    
    /**
     * 是否同意用户协议:0-否,1-是
     */
    @NotNull(message = "必须选择是否同意用户协议")
    @Min(value = 0, message = "协议同意状态值不正确")
    @Max(value = 1, message = "协议同意状态值不正确")
    private Integer agreeTerms;
    
    // 自定义构造方法
    public UserRegisterDTO(String username, String password, String email, String phone) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }
}