package com.msc.controller;

import com.msc.common.result.Result;
import com.msc.domain.dto.UserUpdateDTO;
import com.msc.domain.entity.UserAddress;
import com.msc.domain.vo.UserVO;
import com.msc.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.util.List;

// 添加JWT相关导入
import com.msc.common.util.JwtUtil;
import com.msc.common.constant.JwtConstants;

/**
 * 用户信息管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Validated
@Tag(name = "用户信息管理", description = "用户信息管理相关接口")
public class UserProfileController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    /**
     * 从Authorization头中解析用户ID
     * @param authorization Authorization头
     * @return 用户ID
     */
    private Long getUserIdFromToken(String authorization) {
        if (authorization != null && authorization.startsWith(JwtConstants.TOKEN_PREFIX)) {
            String token = authorization.substring(JwtConstants.TOKEN_PREFIX.length());
            return jwtUtil.getUserIdFromToken(token);
        }
        throw new RuntimeException("无效的认证信息");
    }

    @GetMapping("/profile")
    @Operation(summary = "获取当前用户信息")
    public Result<UserVO> getUserProfile(@RequestHeader("Authorization") String authorization) {
        // 从token中解析用户ID
        Long userId = getUserIdFromToken(authorization);
        UserVO userVO = userService.getUserById(userId);
        return Result.success(userVO);
    }

    @PutMapping("/profile")
    @Operation(summary = "更新用户基本信息")
    public Result<Void> updateUserProfile(
            @RequestHeader("Authorization") String authorization,
            @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        // 从token中解析用户ID
        Long userId = getUserIdFromToken(authorization);
        userUpdateDTO.setId(userId);
        userService.updateUserInfo(userUpdateDTO);
        return Result.success();
    }

    @PostMapping("/avatar")
    @Operation(summary = "更新用户头像")
    public Result<Boolean> updateAvatar(
            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "头像文件") @RequestParam("file") MultipartFile file) {
        // 从token中解析用户ID
        Long userId = getUserIdFromToken(authorization);
        Boolean isSuccess = userService.updateAvatar(userId, file);
        return Result.success(isSuccess);
    }

    @PutMapping("/password")
    @Operation(summary = "修改密码")
    public Result<Void> updatePassword(
            @RequestHeader("Authorization") String authorization,
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword) {
        // 从token中解析用户ID
        Long userId = getUserIdFromToken(authorization);
        // 验证新密码和确认密码是否一致
        if (!newPassword.equals(confirmPassword)) {
            return Result.error("新密码和确认密码不一致");
        }
        // 实际应该调用服务层方法修改密码
        userService.updatePassword(userId, oldPassword, newPassword);
        return Result.success();
    }

    @GetMapping("/addresses")
    @Operation(summary = "获取用户收货地址列表")
    public Result<List<UserAddress>> getUserAddresses(
            @RequestHeader("Authorization") String authorization) {
        // 从token中解析用户ID
        Long userId = getUserIdFromToken(authorization);
        List<UserAddress> addresses = userService.getUserAddresses(userId);
        return Result.success(addresses);
    }

    @PostMapping("/addresses")
    @Operation(summary = "添加用户收货地址")
    public Result<Void> addUserAddress(
            @RequestHeader("Authorization") String authorization,
            @Valid @RequestBody UserAddress address) {
        // 从token中解析用户ID
        Long userId = getUserIdFromToken(authorization);
        address.setUserId(userId);
        userService.addUserAddress(userId, address);
        return Result.success();
    }

    @PutMapping("/addresses/{addressId}")
    @Operation(summary = "更新用户收货地址")
    public Result<Void> updateUserAddress(
            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "地址ID") @PathVariable Long addressId,
            @Valid @RequestBody UserAddress address) {
        // 从token中解析用户ID
        Long userId = getUserIdFromToken(authorization);
        address.setId(addressId);
        address.setUserId(userId);
        userService.updateUserAddress(userId, address);
        return Result.success();
    }

    @DeleteMapping("/addresses/{addressId}")
    @Operation(summary = "删除用户收货地址")
    public Result<Void> deleteUserAddress(
            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "地址ID") @PathVariable Long addressId) {
        // 从token中解析用户ID
        Long userId = getUserIdFromToken(authorization);
        userService.deleteUserAddress(userId, addressId);
        return Result.success();
    }

    @PutMapping("/addresses/{addressId}/default")
    @Operation(summary = "设置默认收货地址")
    public Result<Void> setDefaultAddress(
            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "地址ID") @PathVariable Long addressId) {
        // 从token中解析用户ID
        Long userId = getUserIdFromToken(authorization);
        userService.setDefaultAddress(userId, addressId);
        return Result.success();
    }

    @GetMapping("/addresses/default")
    @Operation(summary = "获取默认收货地址")
    public Result<UserAddress> getDefaultAddress(
            @RequestHeader("Authorization") String authorization) {
        // 从token中解析用户ID
        Long userId = getUserIdFromToken(authorization);
        UserAddress address = userService.getDefaultAddress(userId);
        return Result.success(address);
    }

    @PostMapping("/bind/phone")
    @Operation(summary = "绑定手机号")
    public Result<Void> bindPhone(
            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "手机号") @RequestParam String phone,
            @Parameter(description = "验证码") @RequestParam String code) {
        // 从token中解析用户ID
        Long userId = getUserIdFromToken(authorization);
        userService.bindPhone(userId, phone, code);
        return Result.success();
    }

    @PostMapping("/bind/email")
    @Operation(summary = "绑定邮箱")
    public Result<Void> bindEmail(
            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "邮箱") @RequestParam String email,
            @Parameter(description = "验证码") @RequestParam String code) {
        // 从token中解析用户ID
        Long userId = getUserIdFromToken(authorization);
        userService.bindEmail(userId, email, code);
        return Result.success();
    }

    @PostMapping("/realname/verify")
    @Operation(summary = "实名认证")
    public Result<Void> verifyUser(
            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "真实姓名") @RequestParam String realName,
            @Parameter(description = "身份证号") @RequestParam String idCard) {
        // 从token中解析用户ID
        Long userId = getUserIdFromToken(authorization);
        userService.verifyUser(userId, realName, idCard);
        return Result.success();
    }
}