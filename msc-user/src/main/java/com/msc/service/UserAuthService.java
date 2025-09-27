package com.msc.service;

import com.msc.domain.dto.UserLoginDTO;
import com.msc.domain.dto.UserRegisterDTO;
import com.msc.domain.vo.UserVO;

/**
 * 用户认证服务接口
 * 
 * @since 1.0.0
 */
public interface UserAuthService {

    /**
     * 用户注册
     * 
     * @param registerDTO 注册信息
     * @return 注册结果
     */
    UserVO register(UserRegisterDTO registerDTO);

    /**
     * 用户登录
     * 
     * @param loginDTO 登录信息
     * @return 登录结果（包含Token）
     */
    UserVO login(UserLoginDTO loginDTO);

    /**
     * 用户登出
     * 
     * @param userId 用户ID
     * @param token JWT Token
     * @return 登出结果
     */
    Boolean logout(Long userId, String token);

    /**
     * 刷新Token
     * 
     * @param refreshToken 刷新令牌
     * @return 新的Token信息
     */
    String refreshToken(String refreshToken);

    /**
     * 验证Token
     * 
     * @param token JWT Token
     * @return 用户信息
     */
    UserVO validateToken(String token);

    /**
     * 发送验证码
     * 
     * @param account 账号（手机号或邮箱）
     * @param type 验证码类型：register-注册，login-登录，reset-重置密码
     * @return 发送结果
     */
    Boolean sendVerifyCode(String account, String type);

    /**
     * 验证验证码
     * 
     * @param account 账号
     * @param code 验证码
     * @param type 验证码类型
     * @return 验证结果
     */
    Boolean verifyCode(String account, String code, String type);

    /**
     * 重置密码
     * 
     * @param account 账号
     * @param newPassword 新密码
     * @param verifyCode 验证码
     * @return 重置结果
     */
    Boolean resetPassword(String account, String newPassword, String verifyCode);

    /**
     * 修改密码
     * 
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 修改结果
     */
    Boolean changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 检查用户名是否可用
     * 
     * @param username 用户名
     * @return 是否可用
     */
    Boolean checkUsernameAvailable(String username);

    /**
     * 检查邮箱是否可用
     * 
     * @param email 邮箱
     * @return 是否可用
     */
    Boolean checkEmailAvailable(String email);

    /**
     * 检查手机号是否可用
     * 
     * @param phone 手机号
     * @return 是否可用
     */
    Boolean checkPhoneAvailable(String phone);
}