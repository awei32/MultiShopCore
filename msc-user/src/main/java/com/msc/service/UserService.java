package com.msc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msc.domain.dto.UserUpdateDTO;
import com.msc.domain.entity.User;
import com.msc.domain.entity.UserAddress;
import com.msc.domain.entity.UserProfile;
import com.msc.domain.vo.UserVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 用户信息管理服务接口
 * 
 * @since 1.0.0
 */
public interface UserService {

    /**
     * 根据ID获取用户信息
     * 
     * @param userId 用户ID
     * @return 用户信息
     */
    UserVO getUserById(Long userId);

    /**
     * 更新用户基本信息
     * 
     * @param userUpdateDTO 用户更新信息
     * @return 是否更新成功
     */
    Boolean updateUserInfo(UserUpdateDTO userUpdateDTO);

    /**
     * 更新用户头像
     * 
     * @param userId    用户ID
     * @param file 文件
     * @return 是否更新成功
     */
    Boolean updateAvatar(Long userId, MultipartFile file);

    /**
     * 更新用户密码
     * 
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否更新成功
     */
    Boolean updatePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 更新用户状态
     * 
     * @param userId 用户ID
     * @param status 状态：0-禁用，1-正常，2-锁定
     * @return 是否更新成功
     */
    Boolean updateUserStatus(Long userId, Integer status);

    /**
     * 获取用户详细资料
     * 
     * @param userId 用户ID
     * @return 用户详细资料
     */
    UserProfile getUserProfile(Long userId);

    /**
     * 更新用户详细资料
     * 
     * @param userId  用户ID
     * @param profile 用户详细资料
     * @return 是否更新成功
     */
    Boolean updateUserProfile(Long userId, UserProfile profile);

    /**
     * 获取用户收货地址列表
     * 
     * @param userId 用户ID
     * @return 收货地址列表
     */
    List<UserAddress> getUserAddresses(Long userId);

    /**
     * 添加收货地址
     * 
     * @param userId  用户ID
     * @param address 收货地址
     * @return 是否添加成功
     */
    Boolean addUserAddress(Long userId, UserAddress address);

    /**
     * 更新收货地址
     * 
     * @param userId  用户ID
     * @param address 收货地址
     * @return 是否更新成功
     */
    Boolean updateUserAddress(Long userId, UserAddress address);

    /**
     * 删除收货地址
     * 
     * @param userId    用户ID
     * @param addressId 地址ID
     * @return 是否删除成功
     */
    Boolean deleteUserAddress(Long userId, Long addressId);

    /**
     * 设置默认收货地址
     *
     * @param userId    用户ID
     * @param addressId 地址ID
     * @return 是否设置成功
     */
    Boolean setDefaultAddress(Long userId, Long addressId);

    /**
     * 获取默认收货地址
     *
     * @param userId 用户ID
     * @return 默认收货地址
     */
    UserAddress getDefaultAddress(Long userId);

    /**
     * 分页查询用户列表
     * 
     * @param page        页码
     * @param size        每页大小
     * @param keyword     搜索关键词
     * @param status      用户状态
     * @param memberLevel 会员等级
     * @return 用户分页列表
     */
    Page<UserVO> getUserList(Integer page, Integer size, String keyword,
            Integer status, String memberLevel);

    /**
     * 批量更新用户状态
     * 
     * @param userIds 用户ID列表
     * @param status  状态
     * @return 是否更新成功
     */
    Boolean batchUpdateUserStatus(List<Long> userIds, Integer status);

    /**
     * 获取用户统计信息
     * 
     * @return 用户统计信息
     */
    Object getUserStatistics();

    /**
     * 绑定手机号
     * 
     * @param userId 用户ID
     * @param phone  手机号
     * @param code   验证码
     * @return 是否绑定成功
     */
    Boolean bindPhone(Long userId, String phone, String code);

    /**
     * 绑定邮箱
     * 
     * @param userId 用户ID
     * @param email  邮箱
     * @param code   验证码
     * @return 是否绑定成功
     */
    Boolean bindEmail(Long userId, String email, String code);

    /**
     * 实名认证
     * 
     * @param userId   用户ID
     * @param realName 真实姓名
     * @param idCard   身份证号
     * @return 是否认证成功
     */
    Boolean verifyUser(Long userId, String realName, String idCard);
}