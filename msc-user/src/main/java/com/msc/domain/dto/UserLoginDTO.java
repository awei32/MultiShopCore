package com.msc.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

import com.msc.domain.enums.LoginTypeEnum;

/**
 * 用户登录DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginDTO {
    
    /**
     * 登录类型: password/sms/wechat/alipay
     */
    @NotBlank(message = "登录类型不能为空")
    private String loginType;
    
    /**
     * 登录账号(用户名/邮箱/手机号)
     */
    @NotBlank(message = "登录账号不能为空")
    @Size(min = 3, max = 50, message = "登录账号长度必须在3-50个字符之间")
    private String account;
    
    /**
     * 密码 (password登录时必填)
     */
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    private String password;
    
    /**
     * 短信验证码 (sms登录时必填)
     */
    private String smsCode;
    
    /**
     * 图形验证码
     */
    private String captcha;
    
    /**
     * 验证码key
     */
    private String captchaKey;
    
    /**
     * 是否记住登录状态
     */
    private Boolean rememberMe;
    
    // 自定义构造方法
    public UserLoginDTO(String account, String password, String loginType) {
        this.account = account;
        this.password = password;
        this.loginType = loginType;
    }
}