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

/**
 * 用户认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
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
        return Result.success("退出成功", true);
    }

    @PostMapping("/refresh")
    public Result<String> refreshToken(@RequestParam String refreshToken) {
        String newAccessToken = userAuthService.refreshToken(refreshToken);
        return Result.success("Token刷新成功", newAccessToken);
    }

    // 删除 validateToken 接口，API文档中没有定义

    @PostMapping("/captcha/send")
    public Result<Boolean> sendVerifyCode(
            @RequestParam String target,
            @RequestParam String type,
            @RequestParam String scene) {
        boolean result = userAuthService.sendVerifyCode(target, scene);
        return Result.success("验证码发送成功", result);
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
        return Result.success("密码重置成功", true);
    }

    @PostMapping("/change-password")
    public Result<Boolean> changePassword(
            @RequestParam Long userId,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        userAuthService.changePassword(userId, oldPassword, newPassword);
        return Result.success("密码修改成功", true);
    }

    // 添加获取用户信息接口
    @GetMapping("/profile")
    public Result<UserVO> getUserProfile(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            UserVO userVO = userAuthService.validateToken(token);
            return Result.success("获取成功", userVO);
        }
        return Result.error("Token无效");
    }
}