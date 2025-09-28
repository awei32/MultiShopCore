package com.msc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msc.domain.dto.UserUpdateDTO;
import com.msc.domain.entity.User;
import com.msc.domain.entity.UserAddress;
import com.msc.domain.entity.UserProfile;
import com.msc.domain.vo.UserVO;
import com.msc.mapper.UserAddressMapper;
import com.msc.mapper.UserMapper;
import com.msc.mapper.UserProfileMapper;
import com.msc.service.UserService;
import com.msc.service.VerifyCodeService;
import com.msc.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户信息管理服务实现类
 * 
 * @since 1.0.0
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private VerifyCodeService verifyCodeService;

    @Override
    public UserVO getUserById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return null;
        }
        return convertToUserVO(user);
    }

    @Override
    public UserVO getUserByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            return null;
        }
        return convertToUserVO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUserInfo(UserUpdateDTO updateDTO) {
        User user = userMapper.selectById(updateDTO.getId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 更新基本信息
        if (StringUtils.hasText(updateDTO.getNickname())) {
            user.setNickname(updateDTO.getNickname());
        }
        if (updateDTO.getGender() != null) {
            user.setGender(updateDTO.getGender());
        }
        if (updateDTO.getBirthday() != null) {
            user.setBirthday(updateDTO.getBirthday());
        }
        if (StringUtils.hasText(updateDTO.getAvatar())) {
            user.setAvatar(updateDTO.getAvatar());
        }

        user.setUpdateTime(LocalDateTime.now());
        int result = userMapper.updateById(user);

        // 更新详细资料
        if (hasProfileInfo(updateDTO)) {
            updateUserProfileInfo(updateDTO);
        }

        log.info("User info updated successfully: {}", userId);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateAvatar(Long userId, String avatarUrl) {
        if (!StringUtils.hasText(avatarUrl)) {
            throw new RuntimeException("头像URL不能为空");
        }

        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, userId)
               .set(User::getAvatar, avatarUrl)
               .set(User::getUpdateTime, LocalDateTime.now());

        int result = userMapper.update(null, wrapper);
        log.info("User avatar updated successfully: {}", userId);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUserStatus(Long userId, Integer status) {
        if (status == null || (status < 0 || status > 2)) {
            throw new RuntimeException("状态值不正确");
        }

        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, userId)
               .set(User::getStatus, status)
               .set(User::getUpdateTime, LocalDateTime.now());

        int result = userMapper.update(null, wrapper);
        log.info("User status updated successfully: {} -> {}", userId, status);
        return result > 0;
    }

    @Override
    public UserProfile getUserProfile(Long userId) {
        LambdaQueryWrapper<UserProfile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserProfile::getUserId, userId);
        return userProfileMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUserProfile(Long userId, UserProfile profile) {
        profile.setUserId(userId);
        profile.setUpdateTime(LocalDateTime.now());

        LambdaQueryWrapper<UserProfile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserProfile::getUserId, userId);
        UserProfile existingProfile = userProfileMapper.selectOne(wrapper);

        int result;
        if (existingProfile != null) {
            profile.setId(existingProfile.getId());
            result = userProfileMapper.updateById(profile);
        } else {
            result = userProfileMapper.insert(profile);
        }

        log.info("User profile updated successfully: {}", userId);
        return result > 0;
    }

    @Override
    public List<UserAddress> getUserAddresses(Long userId) {
        LambdaQueryWrapper<UserAddress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAddress::getUserId, userId)
               .eq(UserAddress::getDeleted, 0)
               .orderByDesc(UserAddress::getDefaultAddress)
               .orderByDesc(UserAddress::getCreateTime);
        return userAddressMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addUserAddress(Long userId, UserAddress address) {
        // 检查地址数量限制
        long count = userAddressMapper.countByUserId(userId);
        if (count >= 20) {
            throw new RuntimeException("收货地址数量不能超过20个");
        }

        address.setUserId(userId);
        address.setDeleted(0);
        address.setCreateTime(LocalDateTime.now());
        address.setUpdateTime(LocalDateTime.now());

        // 如果是第一个地址或设置为默认地址，则设为默认
        if (count == 0 || Integer.valueOf(1).equals(address.getDefaultAddress())) {
            // 取消其他默认地址
            LambdaUpdateWrapper<UserAddress> defaultWrapper = new LambdaUpdateWrapper<>();
            defaultWrapper.eq(UserAddress::getUserId, userId)
                         .set(UserAddress::getDefaultAddress, 0);
            userAddressMapper.update(null, defaultWrapper);
            address.setDefaultAddress(1);
        } else {
            address.setDefaultAddress(0);
        }

        int result = userAddressMapper.insert(address);
        log.info("User address added successfully: {}", userId);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUserAddress(Long userId, UserAddress address) {
        // 验证地址是否属于该用户
        UserAddress existingAddress = userAddressMapper.selectById(address.getId());
        if (existingAddress == null || !userId.equals(existingAddress.getUserId())) {
            throw new RuntimeException("地址不存在或无权限修改");
        }

        address.setUserId(userId);
        address.setUpdateTime(LocalDateTime.now());

        // 如果设置为默认地址，取消其他默认地址
        if (Integer.valueOf(1).equals(address.getDefaultAddress())) {
            LambdaUpdateWrapper<UserAddress> defaultWrapper = new LambdaUpdateWrapper<>();
            defaultWrapper.eq(UserAddress::getUserId, userId)
                         .set(UserAddress::getDefaultAddress, 0);
            userAddressMapper.update(null, defaultWrapper);
        }

        int result = userAddressMapper.updateById(address);
        log.info("User address updated successfully: {}", userId);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteUserAddress(Long userId, Long addressId) {
        // 验证地址是否属于该用户
        UserAddress address = userAddressMapper.selectById(addressId);
        if (address == null || !userId.equals(address.getUserId())) {
            throw new RuntimeException("地址不存在或无权限删除");
        }

        // 软删除
        LambdaUpdateWrapper<UserAddress> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserAddress::getId, addressId)
               .set(UserAddress::getDeleted, 1)
               .set(UserAddress::getUpdateTime, LocalDateTime.now());

        int result = userAddressMapper.update(null, wrapper);
        log.info("User address deleted successfully: {}", addressId);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean setDefaultAddress(Long userId, Long addressId) {
        // 验证地址是否属于该用户
        UserAddress address = userAddressMapper.selectById(addressId);
        if (address == null || !userId.equals(address.getUserId()) || Integer.valueOf(1).equals(address.getDeleted())) {
            throw new RuntimeException("地址不存在或无权限设置");
        }

        // 取消所有默认地址
        LambdaUpdateWrapper<UserAddress> defaultWrapper = new LambdaUpdateWrapper<>();
        defaultWrapper.eq(UserAddress::getUserId, userId)
                     .set(UserAddress::getDefaultAddress, 0);
        userAddressMapper.update(null, defaultWrapper);

        // 设置新的默认地址
        LambdaUpdateWrapper<UserAddress> setDefaultWrapper = new LambdaUpdateWrapper<>();
        setDefaultWrapper.eq(UserAddress::getId, addressId)
                        .set(UserAddress::getDefaultAddress, 1);
        userAddressMapper.update(null, setDefaultWrapper);

        log.info("Default address set successfully: {} -> {}", userId, addressId);
        return true;
    }

    @Override
    public UserAddress getDefaultAddress(Long userId) {
        return userAddressMapper.findDefaultByUserId(userId);
    }

    @Override
    public Page<UserVO> getUserList(Integer page, Integer size, String keyword, 
                                   Integer status, String memberLevel) {
        Page<User> userPage = new Page<>(page, size);
        
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        
        // 关键词搜索
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(User::getUsername, keyword)
                           .or().like(User::getNickname, keyword)
                           .or().like(User::getEmail, keyword)
                           .or().like(User::getPhone, keyword));
        }
        
        // 状态筛选
        if (status != null) {
            wrapper.eq(User::getStatus, status);
        }
        
        // 会员等级筛选
        if (StringUtils.hasText(memberLevel)) {
            wrapper.eq(User::getMemberLevel, memberLevel);
        }
        
        wrapper.orderByDesc(User::getCreateTime);
        
        Page<User> result = userMapper.selectPage(userPage, wrapper);
        
        // 转换为VO
        Page<UserVO> voPage = new Page<>();
        BeanUtils.copyProperties(result, voPage);
        voPage.setRecords(result.getRecords().stream()
                               .map(this::convertToUserVO)
                               .toList());
        
        return voPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchUpdateUserStatus(List<Long> userIds, Integer status) {
        if (userIds == null || userIds.isEmpty()) {
            throw new RuntimeException("用户ID列表不能为空");
        }
        
        if (status == null || (status < 0 || status > 2)) {
            throw new RuntimeException("状态值不正确");
        }

        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(User::getId, userIds)
               .set(User::getStatus, status)
               .set(User::getUpdateTime, LocalDateTime.now());

        int result = userMapper.update(null, wrapper);
        log.info("Batch update user status successfully: {} users -> {}", userIds.size(), status);
        return result > 0;
    }

    @Override
    public Map<String, Object> getUserStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // 用户总数统计
        LambdaQueryWrapper<User> totalWrapper = new LambdaQueryWrapper<>();
        statistics.put("totalUsers", userMapper.selectCount(totalWrapper));
        
        // 正常用户统计
        LambdaQueryWrapper<User> normalWrapper = new LambdaQueryWrapper<>();
        normalWrapper.eq(User::getStatus, 1);
        statistics.put("normalUsers", userMapper.selectCount(normalWrapper));
        
        // 禁用用户统计
        LambdaQueryWrapper<User> disabledWrapper = new LambdaQueryWrapper<>();
        disabledWrapper.eq(User::getStatus, 0);
        statistics.put("disabledUsers", userMapper.selectCount(disabledWrapper));
        
        // 锁定用户统计
        LambdaQueryWrapper<User> lockedWrapper = new LambdaQueryWrapper<>();
        lockedWrapper.eq(User::getStatus, 2);
        statistics.put("lockedUsers", userMapper.selectCount(lockedWrapper));
        
        return statistics;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean realNameVerification(Long userId, String realName, String idCard) {
        if (!StringUtils.hasText(realName) || !StringUtils.hasText(idCard)) {
            throw new RuntimeException("真实姓名和身份证号不能为空");
        }

        // 验证身份证号格式
        if (!isValidIdCard(idCard)) {
            throw new RuntimeException("身份证号格式不正确");
        }

        // 检查身份证号是否已被使用
        LambdaQueryWrapper<UserProfile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserProfile::getIdCard, idCard)
               .ne(UserProfile::getUserId, userId);
        if (userProfileMapper.selectCount(wrapper) > 0) {
            throw new RuntimeException("该身份证号已被其他用户使用");
        }

        // 更新用户认证信息
        UserProfile profile = getUserProfile(userId);
        if (profile == null) {
            profile = new UserProfile();
            profile.setUserId(userId);
        }
        
 // 更新用户资料
        profile.setRealName(realName);
        profile.setIdCard(idCard);
        profile.setUpdateTime(LocalDateTime.now());
        
        // 更新用户认证状态
        LambdaUpdateWrapper<User> userWrapper = new LambdaUpdateWrapper<>();
        userWrapper.eq(User::getId, userId)
                   .set(User::getIsVerified, 1)
                   .set(User::getUpdateTime, LocalDateTime.now());
        userMapper.update(null, userWrapper);

        // 保存或更新用户详细资料
        if (profile.getId() != null) {
            userProfileMapper.updateById(profile);
        } else {
            userProfileMapper.insert(profile);
        }

        log.info("Real name verification completed: {}", userId);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean bindPhone(Long userId, String phone, String verifyCode) {
        // 验证验证码
        if (!verifyCodeService.verifyCode(phone, verifyCode, "bind")) {
            throw new RuntimeException("验证码错误或已过期");
        }

        // 检查手机号是否已被使用
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone)
               .ne(User::getId, userId);
        if (userMapper.selectCount(wrapper) > 0) {
            throw new RuntimeException("该手机号已被其他用户使用");
        }

        // 更新用户手机号
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, userId)
                     .set(User::getPhone, phone)
                     .set(User::getUpdateTime, LocalDateTime.now());

        int result = userMapper.update(null, updateWrapper);
        log.info("Phone bound successfully: {} -> {}", userId, phone);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean bindEmail(Long userId, String email, String verifyCode) {
        // 验证验证码
        if (!verifyCodeService.verifyCode(email, verifyCode, "bind")) {
            throw new RuntimeException("验证码错误或已过期");
        }

        // 检查邮箱是否已被使用
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email)
               .ne(User::getId, userId);
        if (userMapper.selectCount(wrapper) > 0) {
            throw new RuntimeException("该邮箱已被其他用户使用");
        }

        // 更新用户邮箱
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, userId)
                     .set(User::getEmail, email)
                     .set(User::getUpdateTime, LocalDateTime.now());

        int result = userMapper.update(null, updateWrapper);
        log.info("Email bound successfully: {} -> {}", userId, email);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteAccount(Long userId, String password) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证密码
        if (!PasswordUtil.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 软删除用户（将状态设为已删除）
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, userId)
               .set(User::getStatus, -1) // -1表示已删除
               .set(User::getUpdateTime, LocalDateTime.now());

        int result = userMapper.update(null, wrapper);
        log.info("User account deleted: {}", userId);
        return result > 0;
    }

    /**
     * 转换为UserVO
     */
    private UserVO convertToUserVO(User user) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        
        // 设置性别描述
        if (user.getGender() != null) {
            switch (user.getGender()) {
                case 1 -> userVO.setGenderDesc("男");
                case 2 -> userVO.setGenderDesc("女");
                default -> userVO.setGenderDesc("未知");
            }
        }
        
        // 设置状态描述
        if (user.getStatus() != null) {
            switch (user.getStatus()) {
                case 0 -> userVO.setStatusDesc("禁用");
                case 1 -> userVO.setStatusDesc("正常");
                case 2 -> userVO.setStatusDesc("锁定");
                default -> userVO.setStatusDesc("未知");
            }
        }
        
        // 设置注册来源描述
        if (StringUtils.hasText(user.getRegisterSource())) {
            switch (user.getRegisterSource()) {
                case "web" -> userVO.setRegisterSourceDesc("网页");
                case "app" -> userVO.setRegisterSourceDesc("手机应用");
                case "wechat" -> userVO.setRegisterSourceDesc("微信");
                case "alipay" -> userVO.setRegisterSourceDesc("支付宝");
                case "qq" -> userVO.setRegisterSourceDesc("QQ");
                default -> userVO.setRegisterSourceDesc(user.getRegisterSource());
            }
        }
        
        // 设置认证状态描述
        if (user.getIsVerified() != null) {
            userVO.setVerifiedDesc(Integer.valueOf(1).equals(user.getIsVerified()) ? "已认证" : "未认证");
        }
        
        // 手机号脱敏
        if (StringUtils.hasText(user.getPhone())) {
            userVO.setPhone(maskPhone(user.getPhone()));
        }
        
        return userVO;
    }

    /**
     * 检查是否有详细资料信息需要更新
     */
    private boolean hasProfileInfo(UserUpdateDTO updateDTO) {
        return StringUtils.hasText(updateDTO.getBio()) ||
               StringUtils.hasText(updateDTO.getProvince()) ||
               StringUtils.hasText(updateDTO.getCity()) ||
               StringUtils.hasText(updateDTO.getAddress());
    }

    /**
     * 更新用户详细资料信息
     */
    private void updateUserProfileInfo(UserUpdateDTO updateDTO) {
        UserProfile profile = getUserProfile(updateDTO.getId());
        if (profile == null) {
            profile = new UserProfile();
            profile.setUserId(updateDTO.getId());
        }

        if (StringUtils.hasText(updateDTO.getBio())) {
            profile.setBio(updateDTO.getBio());
        }
        if (StringUtils.hasText(updateDTO.getProvince())) {
            profile.setProvince(updateDTO.getProvince());
        }
        if (StringUtils.hasText(updateDTO.getCity())) {
            profile.setCity(updateDTO.getCity());
        }
        if (StringUtils.hasText(updateDTO.getAddress())) {
            profile.setAddress(updateDTO.getAddress());
        }

        profile.setUpdateTime(LocalDateTime.now());

        if (profile.getId() != null) {
            userProfileMapper.updateById(profile);
        } else {
            userProfileMapper.insert(profile);
        }
    }

    /**
     * 验证身份证号格式
     */
    private boolean isValidIdCard(String idCard) {
        if (idCard == null || idCard.length() != 18) {
            return false;
        }
        
        // 简单的身份证号格式验证
        return idCard.matches("^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$");
    }

    /**
     * 手机号脱敏
     */
    private String maskPhone(String phone) {
        if (phone == null || phone.length() != 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }
}