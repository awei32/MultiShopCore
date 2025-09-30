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
 * 管理员用户管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/user/admin/")
@RequiredArgsConstructor
@Validated
@Tag(name = "管理员用户管理", description = "管理员用户管理相关接口")
public class AdminUserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取用户信息")
    public Result<UserVO> getUserById(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        UserVO userVO = userService.getUserById(id);
        return Result.success(userVO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新用户基本信息")
    public Result<Void> updateUser(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        userUpdateDTO.setId(id);
        userService.updateUserInfo(userUpdateDTO);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "更新用户状态")
    public Result<Void> updateUserStatus(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestParam Integer status) {
        userService.updateUserStatus(id, status);
        return Result.success();
    }

//    @GetMapping("/list")
//    @Operation(summary = "分页查询用户列表")
//    public Result<Page<UserVO>> getUserList(
//            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
//            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
//            @Parameter(description = "用户名") @RequestParam(required = false) String username,
//            @Parameter(description = "邮箱") @RequestParam(required = false) String email,
//            @Parameter(description = "手机号") @RequestParam(required = false) String phone,
//            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
//        Page<UserVO> userPage = userService.getUserList(page, size, username, email, phone, status);
//        return Result.success(userPage);
//    }

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
        Map<String, Object> statistics = (Map<String, Object>) userService.getUserStatistics();
        return Result.success(statistics);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户账户")
    public Result<Void> deleteAccount(
            @Parameter(description = "用户ID") @PathVariable Long id,@Parameter(description = "密码") @PathVariable String password) {
        userService.deleteAccount(id, password);
        return Result.success();
    }
}