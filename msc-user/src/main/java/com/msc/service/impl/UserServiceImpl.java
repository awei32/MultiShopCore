package com.msc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msc.domain.dto.UserUpdateDTO;
import com.msc.domain.entity.User;
import com.msc.domain.entity.UserAddress;
import com.msc.domain.entity.UserProfile;
import com.msc.domain.enums.GenderEnum;
import com.msc.domain.enums.RegisterSourceEnum;
import com.msc.common.util.MinioUtil;
import com.msc.config.FileUploadConfig;
import com.msc.domain.vo.UserVO;
import com.msc.mapper.UserAddressMapper;
import com.msc.mapper.UserMapper;
import com.msc.mapper.UserProfileMapper;
import com.msc.service.UserService;
import com.msc.service.VerifyCodeService;
import com.msc.util.PasswordUtil;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
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
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final UserAddressMapper userAddressMapper;
    private final MinioUtil minioUtil;
    private final FileUploadConfig fileUploadConfig;
    private final VerifyCodeService verifyCodeService;

    @Override
    public UserVO getUserById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        return convertToUserVO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUserInfo(UserUpdateDTO userUpdateDTO) {
        // 更新用户基本信息
        User user = new User();
        user.setId(userUpdateDTO.getId());
        user.setNickname(userUpdateDTO.getNickname());
        user.setAvatar(userUpdateDTO.getAvatar());
        user.setGender(userUpdateDTO.getGender());
        user.setBirthday(userUpdateDTO.getBirthday());
        user.setUpdateTime(LocalDateTime.now());

        int result = userMapper.updateById(user);

        // 更新用户详细资料
        if (hasProfileInfo(userUpdateDTO)) {
            updateUserProfileInfo(userUpdateDTO);
        }

        log.info("User info updated successfully: {}", userUpdateDTO.getId());
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateAvatar(Long userId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }

        try(InputStream inputStream = file.getInputStream()) {
            // 上传文件到MinIO并获取URL
            String fileName = "avatar/" + userId + "/" + System.currentTimeMillis() + "-" + file.getOriginalFilename();
            String fileUrl = minioUtil.uploadFileAndGetUrl(fileName, inputStream);
            
            if (fileUrl == null) {
                throw new RuntimeException("文件上传失败");
            }

            // 更新用户头像
            LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(User::getId, userId)
                    .set(User::getAvatar, fileUrl)
                    .set(User::getUpdateTime, LocalDateTime.now());

            int result = userMapper.update(null, wrapper);
            log.info("Avatar updated successfully: {} -> {}", userId, fileUrl);
            return result > 0;
        } catch (Exception e) {
            log.error("Failed to update avatar for user: {}", userId, e);
            throw new RuntimeException("头像更新失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePassword(Long userId, String oldPassword, String newPassword) {
        // 验证新密码格式
        if (!PasswordUtil.isValidPassword(newPassword)) {
            throw new RuntimeException("新密码格式不符合要求");
        }

        // 查找用户
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证旧密码
        if (!PasswordUtil.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }

        // 更新密码
        user.setPassword(PasswordUtil.encode(newPassword));
        user.setUpdateTime(LocalDateTime.now());

        int result = userMapper.updateById(user);

        log.info("Password updated successfully for user: {}", userId);
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
                .eq(UserAddress::getIsDeleted, 0)
                .orderByDesc(UserAddress::getIsDefault)
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
        address.setCreateTime(LocalDateTime.now());
        address.setUpdateTime(LocalDateTime.now());

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

        int result = userAddressMapper.deleteById(addressId);
        log.info("User address deleted successfully: {}", addressId);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean setDefaultAddress(Long userId, Long addressId) {
        // 验证地址是否属于该用户
        UserAddress address = userAddressMapper.selectById(addressId);
        if (address == null || !userId.equals(address.getUserId())) {
            throw new RuntimeException("地址不存在或无权限设置默认地址");
        }

        // 取消之前的默认地址
        userAddressMapper.cancelDefaultByUserId(userId);

        // 设置新的默认地址
        int result = userAddressMapper.setDefault(addressId, userId);
        log.info("Default address set successfully: {} -> {}", userId, addressId);
        return result > 0;
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
        log.info("Batch update user status successfully: {} users -> status {}", userIds.size(), status);
        return result > 0;
    }

    @Override
    public Object getUserStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // 总用户数统计
        statistics.put("totalUsers", userMapper.selectCount(null));

        // 按状态统计
        LambdaQueryWrapper<User> activeWrapper = new LambdaQueryWrapper<>();
        activeWrapper.eq(User::getStatus, 1);
        statistics.put("activeUsers", userMapper.selectCount(activeWrapper));

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

    @Override
    public Boolean verifyUser(Long userId, String realName, String idCard) {
        return realNameVerification(userId, realName, idCard);
    }

     /**
      * 转换为UserVO
      */
     private UserVO convertToUserVO(User user) {
         UserVO userVO = new UserVO();
         BeanUtils.copyProperties(user, userVO);

         // 设置枚举描述
         if (user.getGender() != null) {
             GenderEnum genderEnum = GenderEnum.getByCode(user.getGender());
             if (genderEnum != null) {
                 userVO.setGenderDesc(genderEnum.getDesc());
             }
         }

         if (StringUtils.hasText(user.getRegisterSource())) {
             RegisterSourceEnum sourceEnum = RegisterSourceEnum.getByCode(user.getRegisterSource());
             if (sourceEnum != null) {
                 userVO.setRegisterSourceDesc(sourceEnum.getDesc());
             }
         }

        // 邮箱脱敏
        if (StringUtils.hasText(user.getEmail())) {
            userVO.setEmail(maskEmail(user.getEmail()));
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

        LambdaQueryWrapper<UserProfile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserProfile::getUserId, updateDTO.getId());
        UserProfile existingProfile = userProfileMapper.selectOne(wrapper);

        if (existingProfile != null) {
            profile.setId(existingProfile.getId());
            userProfileMapper.updateById(profile);
        } else {
            userProfileMapper.insert(profile);
        }
    }

    /**
     * 邮箱脱敏
     */
    private String maskEmail(String email) {
        if (!StringUtils.hasText(email) || !email.contains("@")) {
            return email;
        }
        String[] parts = email.split("@");
        String username = parts[0];
        if (username.length() <= 2) {
            return email;
        }
        return username.substring(0, 2) + "***@" + parts[1];
    }

    /**
     * 手机号脱敏
     */
    private String maskPhone(String phone) {
        if (!StringUtils.hasText(phone) || phone.length() < 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    /**
     * 验证身份证号格式
     */
    private boolean isValidIdCard(String idCard) {
        // 简单的身份证号格式验证，实际项目中应该使用更复杂的验证逻辑
        return idCard != null && (idCard.length() == 18 || idCard.length() == 15);
    }
}