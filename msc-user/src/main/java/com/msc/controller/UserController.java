package com.msc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msc.common.result.Result;
import com.msc.domain.dto.UserUpdateDTO;
import com.msc.domain.entity.User;
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
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 用户信息管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
@Tag(name = "用户信息管理", description = "用户信息管理相关接口")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取用户信息")
    public Result<UserVO> getUserById(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        UserVO userVO = userService.getUserById(id);
        return Result.success(userVO);
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "根据用户名获取用户信息")
    public Result<UserVO> getUserByUsername(
            @Parameter(description = "用户名") @PathVariable String username) {
        UserVO userVO = userService.getUserByUsername(username);
        return Result.success(userVO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新用户基本信息")
    public Result<Void> updateUser(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        userUpdateDTO.setId(id);
        userService.updateUser(userUpdateDTO);
        return Result.success();
    }

    @PostMapping("/{id}/avatar")
    @Operation(summary = "更新用户头像")
    public Result<String> updateAvatar(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "头像文件") @RequestParam("file") MultipartFile file) {
        String avatarUrl = userService.updateAvatar(id, file);
        return Result.success(avatarUrl);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "更新用户状态")
    public Result<Void> updateUserStatus(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestParam Integer status) {
        userService.updateUserStatus(id, status);
        return Result.success();
    }

    @GetMapping("/{id}/addresses")
    @Operation(summary = "获取用户收货地址列表")
    public Result<List<UserAddress>> getUserAddresses(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        List<UserAddress> addresses = userService.getUserAddresses(id);
        return Result.success(addresses);
    }

    @PostMapping("/{id}/addresses")
    @Operation(summary = "添加用户收货地址")
    public Result<Void> addUserAddress(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Valid @RequestBody UserAddress address) {
        address.setUserId(id);
        userService.addUserAddress(address);
        return Result.success();
    }

    @PutMapping("/{id}/addresses/{addressId}")
    @Operation(summary = "更新用户收货地址")
    public Result<Void> updateUserAddress(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "地址ID") @PathVariable Long addressId,
            @Valid @RequestBody UserAddress address) {
        address.setId(addressId);
        address.setUserId(id);
        userService.updateUserAddress(address);
        return Result.success();
    }

    @DeleteMapping("/{id}/addresses/{addressId}")
    @Operation(summary = "删除用户收货地址")
    public Result<Void> deleteUserAddress(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "地址ID") @PathVariable Long addressId) {
        userService.deleteUserAddress(id, addressId);
        return Result.success();
    }

    @PutMapping("/{id}/addresses/{addressId}/default")
    @Operation(summary = "设置默认收货地址")
    public Result<Void> setDefaultAddress(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "地址ID") @PathVariable Long addressId) {
        userService.setDefaultAddress(id, addressId);
        return Result.success();
    }

    @GetMapping("/{id}/addresses/default")
    @Operation(summary = "获取默认收货地址")
    public Result<UserAddress> getDefaultAddress(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        UserAddress address = userService.getDefaultAddress(id);
        return Result.success(address);
    }

    @GetMapping("/list")
    @Operation(summary = "分页查询用户列表")
    public Result<Page<UserVO>> getUserList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "用户名") @RequestParam(required = false) String username,
            @Parameter(description = "邮箱") @RequestParam(required = false) String email,
            @Parameter(description = "手机号") @RequestParam(required = false) String phone,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        Page<UserVO> userPage = userService.getUserList(page, size, username, email, phone, status);
        return Result.success(userPage);
    }

    @PutMapping("/batch/status")
    @Operation(summary = "批量更新用户状态")
    public Result<Void> batchUpdateUserStatus(
            @Parameter(description = "用户ID列表") @RequestParam List<Long> userIds,
            @Parameter(description = "状态") @RequestParam Integer status) {
        userService.batchUpdateUserStatus(userIds, status);
        return Result.success();
    }

    @GetMapping("/statistics")
    @Operation(summary = "获取用户统计信息")
    public Result<Map<String, Object>> getUserStatistics() {
        Map<String, Object> statistics = userService.getUserStatistics();
        return Result.success(statistics);
    }

    @PostMapping("/{id}/verify")
    @Operation(summary = "实名认证")
    public Result<Void> verifyUser(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "真实姓名") @RequestParam String realName,
            @Parameter(description = "身份证号") @RequestParam String idCard) {
        userService.verifyUser(id, realName, idCard);
        return Result.success();
    }

    @PostMapping("/{id}/bind/phone")
    @Operation(summary = "绑定手机号")
    public Result<Void> bindPhone(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "手机号") @RequestParam String phone,
            @Parameter(description = "验证码") @RequestParam String code) {
        userService.bindPhone(id, phone, code);
        return Result.success();
    }

    @PostMapping("/{id}/bind/email")
    @Operation(summary = "绑定邮箱")
    public Result<Void> bindEmail(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "邮箱") @RequestParam String email,
            @Parameter(description = "验证码") @RequestParam String code) {
        userService.bindEmail(id, email, code);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户账户")
    public Result<Void> deleteUser(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }
}