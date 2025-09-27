package com.msc.controller;

import com.msc.common.result.Result;
import com.msc.domain.dto.UserLoginDTO;
import com.msc.domain.dto.UserRegisterDTO;
import com.msc.domain.vo.UserVO;
import com.msc.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 用户认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class UserAuthController {

    private final UserAuthService userAuthService;

    @PostMapping("/register")
    public Result<UserVO> register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        UserVO userVO = userAuthService.register(registerDTO);
        return Result.success("注册成功", userVO);
    }

    @PostMapping("/login")
    public Result<UserVO> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        UserVO userVO = userAuthService.login(loginDTO);
        return Result.success("登录成功", userVO);
    }

    @PostMapping("/logout")
    public Result<Boolean> logout(HttpServletRequest request, @RequestParam Long userId) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            userAuthService.logout(userId, token);
        }
        return Result.success("退出成功");
    }

    @PostMapping("/refresh")
    public Result<String> refreshToken(@RequestParam String refreshToken) {
        String newAccessToken = userAuthService.refreshToken(refreshToken);
        return Result.success("Token刷新成功", newAccessToken);
    }

    @GetMapping("/validate")
    public Result<UserVO> validateToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            UserVO userVO = userAuthService.validateToken(token);
            return Result.success("Token有效", userVO);
        }
        return Result.error("Token无效");
    }

    @PostMapping("/send-sms-code")
    public Result<Boolean> sendSmsCode(
            @RequestParam @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确") String phone,
            @RequestParam String type) {
        userAuthService.sendSmsVerifyCode(phone, type);
        return Result.success("验证码发送成功");
    }

    @PostMapping("/send-email-code")
    public Result<Boolean> sendEmailCode(
            @RequestParam @Email(message = "邮箱格式不正确") String email,
            @RequestParam String type) {
        userAuthService.sendEmailVerifyCode(email, type);
        return Result.success("验证码发送成功");
    }

    @PostMapping("/verify-code")
    public Result<Boolean> verifyCode(
            @RequestParam String account,
            @RequestParam String code,
            @RequestParam String type) {
        boolean isValid = userAuthService.verifyCode(account, code, type);
        return Result.success("验证成功", isValid);
    }

    @PostMapping("/reset-password")
    public Result<Boolean> resetPassword(
            @RequestParam String account,
            @RequestParam String newPassword,
            @RequestParam String code) {
        userAuthService.resetPassword(account, newPassword, code);
        return Result.success("密码重置成功");
    }

    @PostMapping("/change-password")
    public Result<Boolean> changePassword(
            @RequestParam Long userId,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        userAuthService.changePassword(userId, oldPassword, newPassword);
        return Result.success("密码修改成功");
    }

    @GetMapping("/check-username")
    public Result<Boolean> checkUsernameAvailable(@RequestParam String username) {
        boolean available = userAuthService.isUsernameAvailable(username);
        return Result.success("检查完成", available);
    }

    @GetMapping("/check-email")
    public Result<Boolean> checkEmailAvailable(@RequestParam String email) {
        boolean available = userAuthService.isEmailAvailable(email);
        return Result.success("检查完成", available);
    }

    @GetMapping("/check-phone")
    public Result<Boolean> checkPhoneAvailable(@RequestParam String phone) {
        boolean available = userAuthService.isPhoneAvailable(phone);
        return Result.success("检查完成", available);
    }
}