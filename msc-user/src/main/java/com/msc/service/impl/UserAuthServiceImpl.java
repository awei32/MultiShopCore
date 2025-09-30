package com.msc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.msc.common.util.JwtUtil;
import com.msc.domain.dto.UserLoginDTO;
import com.msc.domain.dto.UserRegisterDTO;
import com.msc.domain.entity.User;
import com.msc.domain.entity.UserLoginLog;
import com.msc.domain.entity.UserPreference;
import com.msc.domain.entity.UserProfile;
import com.msc.domain.vo.UserVO;
import com.msc.mapper.UserLoginLogMapper;
import com.msc.mapper.UserMapper;
import com.msc.mapper.UserPreferenceMapper;
import com.msc.mapper.UserProfileMapper;
import com.msc.service.UserAuthService;
import com.msc.service.VerifyCodeService;
import com.msc.util.PasswordUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static com.msc.domain.enums.LoginTypeEnum.*;

/**
 * 用户认证服务实现类
 * 
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final UserPreferenceMapper userPreferenceMapper;
    private final UserLoginLogMapper userLoginLogMapper;
    private final VerifyCodeService verifyCodeService;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String TOKEN_BLACKLIST_PREFIX = "token_blacklist:";
    private static final String USER_TOKEN_CACHE_PREFIX = "user_token:";
    private static final long TOKEN_CACHE_EXPIRE = 24; // 小时
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");



    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO register(UserRegisterDTO registerDTO) {
        // 验证输入参数
        validateRegisterDTO(registerDTO);

        // 验证验证码
        if (!verifyCodeService.verifyCode(getAccountFromRegisterDTO(registerDTO), 
                                         registerDTO.getVerifyCode(), REGISTER.getCode())) {
            throw new RuntimeException("验证码错误或已过期");
        }

        // 检查用户名是否已存在
        if (!checkUsernameAvailable(registerDTO.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (StringUtils.hasText(registerDTO.getEmail()) && !checkEmailAvailable(registerDTO.getEmail())) {
            throw new RuntimeException("邮箱已被注册");
        }

        // 检查手机号是否已存在
        if (StringUtils.hasText(registerDTO.getPhone()) && !checkPhoneAvailable(registerDTO.getPhone())) {
            throw new RuntimeException("手机号已被注册");
        }

        // 创建用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(PasswordUtil.encode(registerDTO.getPassword()));
        user.setEmail(registerDTO.getEmail());
        user.setPhone(registerDTO.getPhone());
        user.setNickname(registerDTO.getNickname());
        user.setGender(registerDTO.getGender());
        user.setBirthday(registerDTO.getBirthday());
        user.setRegisterSource(registerDTO.getRegisterSource());
        user.setRegisterIp(getClientIp());
        
        userMapper.insert(user);

        // 创建用户详细资料
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(user.getId());
        userProfileMapper.insert(userProfile);

        // 创建用户偏好设置
        UserPreference userPreference = new UserPreference(user.getId());
        userPreferenceMapper.insert(userPreference);

        // 记录登录日志
        recordLoginLog(user.getId(), "web", "Browser", REGISTER.getCode());

        // 转换为VO并返回
        UserVO userVO = convertToUserVO(user);
        
        // 生成Token
        String accessToken = jwtUtil.generateToken(user.getId(), user.getUsername());
        String refreshToken = jwtUtil.generateToken(user.getId(), user.getUsername());
        
        // 将Token存储到Redis缓存中
        cacheUserToken(user.getId(), accessToken);
        
        // 注意：实际项目中Token应该通过响应头或其他方式返回，这里仅作演示
        log.info("Generated tokens for user: {}", user.getUsername());

        log.info("User registered successfully: {}", user.getUsername());
        return userVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO login(UserLoginDTO loginDTO) {
        // 验证输入参数
        validateLoginDTO(loginDTO);

        // 根据登录类型验证
        User user = null;
        if ("password".equals(loginDTO.getLoginType())) {
            user = loginWithPassword(loginDTO);
        } else if ("sms".equals(loginDTO.getLoginType())) {
            user = loginWithSms(loginDTO);
        } else {
            throw new RuntimeException("不支持的登录类型");
        }

        if (user == null) {
            throw new RuntimeException("登录失败");
        }

        // 检查用户状态
        if (Integer.valueOf(0).equals(user.getStatus())) {
            throw new RuntimeException("账号已被禁用");
        }

        // 更新登录信息
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(getClientIp());
        userMapper.updateById(user);

        // 记录登录日志
        recordLoginLog(user.getId(), "web", "Browser", PASSWORD.getCode());

        // 转换为VO并返回
        UserVO userVO = convertToUserVO(user);
        
        // 生成Token
        String accessToken = jwtUtil.generateToken(user.getId(), user.getUsername());
        String refreshToken = jwtUtil.generateToken(user.getId(), user.getUsername());
        
        // 将Token存储到Redis缓存中
        cacheUserToken(user.getId(), accessToken);
        
        // 注意：实际项目中Token应该通过响应头或其他方式返回，这里仅作演示
        log.info("Generated tokens for user: {}", user.getUsername());

        log.info("User logged in successfully: {}", user.getUsername());
        return userVO;
    }

    @Override
    public Boolean logout(Long userId, String token) {
        try {
            // 将Token加入黑名单
            if (!jwtUtil.isTokenExpired(token)) {
                Long remainingTime = jwtUtil.getExpirationDateFromToken(token).getTime() - System.currentTimeMillis();
                if (remainingTime > 0) {
                    String key = TOKEN_BLACKLIST_PREFIX + token;
                    redisTemplate.opsForValue().set(key, "1", remainingTime, TimeUnit.MILLISECONDS);
                }
            }

            // 清除用户Token缓存
            clearUserTokenCache(userId);

            log.info("User logged out successfully: {}", userId);
            return true;
        } catch (Exception e) {
            log.error("Logout failed for user: {}", userId, e);
            return false;
        }
    }

    @Override
    public String refreshToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException("刷新令牌无效或已过期");
        }

        Long userId = jwtUtil.getUserIdFromToken(refreshToken);
        String username = jwtUtil.getUsernameFromToken(refreshToken);

        // 验证用户是否存在且状态正常
        User user = userMapper.selectById(userId);
        if (user == null || Integer.valueOf(0).equals(user.getStatus())) {
            throw new RuntimeException("用户不存在或已被禁用");
        }

        // 生成新的访问Token
        String newToken = jwtUtil.generateToken(userId, username);
        
        // 更新Redis缓存
        cacheUserToken(userId, newToken);
        
        return newToken;
    }

    @Override
    public UserVO validateToken(String token) {
        if (!jwtUtil.validateToken(token)) {
            return null;
        }

        // 检查Token是否在黑名单中
        String key = TOKEN_BLACKLIST_PREFIX + token;
        if (redisTemplate.hasKey(key)) {
            return null;
        }

        Long userId = jwtUtil.getUserIdFromToken(token);
        
        // 首先尝试从Redis缓存获取用户信息
        UserVO cachedUser = getCachedUser(userId);
        if (cachedUser != null) {
            return cachedUser;
        }
        
        User user = userMapper.selectById(userId);
        
        if (user == null || Integer.valueOf(0).equals(user.getStatus())) {
            return null;
        }

        UserVO userVO = convertToUserVO(user);
        
        // 将用户信息缓存到Redis
        cacheUser(userVO);
        
        return userVO;
    }

    @Override
    public Boolean sendVerifyCode(String account, String type) {
        if (isPhoneNumber(account)) {
            return verifyCodeService.sendSmsCode(account, type);
        } else if (isEmail(account)) {
            return verifyCodeService.sendEmailCode(account, type);
        } else {
            throw new RuntimeException("账号格式不正确");
        }
    }

    @Override
    public Boolean verifyCode(String account, String code, String type) {
        return verifyCodeService.verifyCode(account, code, type);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean resetPassword(String account, String newPassword, String verifyCode) {
        // 验证验证码
        if (!verifyCodeService.verifyCode(account, verifyCode, "reset")) {
            throw new RuntimeException("验证码错误或已过期");
        }

        // 验证密码格式
        if (!PasswordUtil.isValidPassword(newPassword)) {
            throw new RuntimeException("密码格式不符合要求");
        }

        // 查找用户
        User user = findUserByAccount(account);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 更新密码
        user.setPassword(PasswordUtil.encode(newPassword));
        user.setUpdateTime(LocalDateTime.now());
        
        int result = userMapper.updateById(user);
        
        // 清除用户Token缓存
        clearUserTokenCache(user.getId());
        
        log.info("Password reset successfully for user: {}", user.getUsername());
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean changePassword(Long userId, String oldPassword, String newPassword) {
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
        
        // 清除用户Token缓存
        clearUserTokenCache(userId);
        
        log.info("Password changed successfully for user: {}", user.getUsername());
        return result > 0;
    }

    @Override
    public Boolean checkUsernameAvailable(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return userMapper.selectCount(wrapper) == 0;
    }

    @Override
    public Boolean checkEmailAvailable(String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email);
        return userMapper.selectCount(wrapper) == 0;
    }

    @Override
    public Boolean checkPhoneAvailable(String phone) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        return userMapper.selectCount(wrapper) == 0;
    }

    /**
     * 缓存用户Token
     * @param userId 用户ID
     * @param token JWT Token
     */
    private void cacheUserToken(Long userId, String token) {
        try {
            String key = USER_TOKEN_CACHE_PREFIX + userId;
            redisTemplate.opsForValue().set(key, token, TOKEN_CACHE_EXPIRE, TimeUnit.HOURS);
        } catch (Exception e) {
            log.error("Failed to cache user token for user: {}", userId, e);
        }
    }

    /**
     * 从缓存获取用户Token
     * @param userId 用户ID
     * @return JWT Token
     */
    private String getCachedUserToken(Long userId) {
        try {
            String key = USER_TOKEN_CACHE_PREFIX + userId;
            Object obj = redisTemplate.opsForValue().get(key);
            return obj instanceof String ? (String) obj : null;
        } catch (Exception e) {
            log.error("Failed to get cached user token for user: {}", userId, e);
            return null;
        }
    }

    /**
     * 缓存用户信息
     * @param userVO 用户信息
     */
    private void cacheUser(UserVO userVO) {
        try {
            String key = "user:" + userVO.getId();
            redisTemplate.opsForValue().set(key, userVO, TOKEN_CACHE_EXPIRE, TimeUnit.HOURS);
        } catch (Exception e) {
            log.error("Failed to cache user info for user: {}", userVO.getId(), e);
        }
    }

    /**
     * 从缓存获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    private UserVO getCachedUser(Long userId) {
        try {
            String key = "user:" + userId;
            Object obj = redisTemplate.opsForValue().get(key);
            return obj instanceof UserVO ? (UserVO) obj : null;
        } catch (Exception e) {
            log.error("Failed to get cached user info for user: {}", userId, e);
            return null;
        }
    }

    /**
     * 清除用户Token缓存
     * @param userId 用户ID
     */
    private void clearUserTokenCache(Long userId) {
        try {
            String tokenKey = USER_TOKEN_CACHE_PREFIX + userId;
            String userKey = "user:" + userId;
            redisTemplate.delete(tokenKey);
            redisTemplate.delete(userKey);
        } catch (Exception e) {
            log.error("Failed to clear user token cache for user: {}", userId, e);
        }
    }

    /**
     * 密码登录
     */
    private User loginWithPassword(UserLoginDTO loginDTO) {
        User user = findUserByAccount(loginDTO.getAccount());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (!PasswordUtil.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        return user;
    }

    /**
     * 短信验证码登录
     */
    private User loginWithSms(UserLoginDTO loginDTO) {
        if (!verifyCodeService.verifyCode(loginDTO.getAccount(), 
                                         loginDTO.getSmsCode(), "login")) {
            throw new RuntimeException("验证码错误或已过期");
        }

        User user = findUserByAccount(loginDTO.getAccount());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        return user;
    }

    /**
     * 根据账号查找用户
     */
    private User findUserByAccount(String account) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        
        if (isPhoneNumber(account)) {
            wrapper.eq(User::getPhone, account);
        } else if (isEmail(account)) {
            wrapper.eq(User::getEmail, account);
        } else {
            wrapper.eq(User::getUsername, account);
        }
        
        return userMapper.selectOne(wrapper);
    }

    /**
     * 记录登录日志
     */
    private void recordLoginLog(Long userId, String deviceType, String deviceInfo, String loginType) {
        UserLoginLog loginLog = UserLoginLog.builder()
                .userId(userId)
                .loginIp(getClientIp())
                .loginType(loginType)
                .loginStatus(1)
                .loginTime(LocalDateTime.now())
                .build();
        
        userLoginLogMapper.insert(loginLog);
    }

    /**
     * 转换为UserVO
     */
    private UserVO convertToUserVO(User user) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    /**
     * 验证注册DTO
     */
    private void validateRegisterDTO(UserRegisterDTO registerDTO) {
        if (!StringUtils.hasText(registerDTO.getUsername())) {
            throw new RuntimeException("用户名不能为空");
        }
        
        if (!StringUtils.hasText(registerDTO.getPassword())) {
            throw new RuntimeException("密码不能为空");
        }
        
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new RuntimeException("两次输入的密码不一致");
        }
        
        if (!PasswordUtil.isValidPassword(registerDTO.getPassword())) {
            throw new RuntimeException("密码格式不符合要求");
        }
        
        if (!StringUtils.hasText(registerDTO.getVerifyCode())) {
            throw new RuntimeException("验证码不能为空");
        }
    }

    /**
     * 验证登录DTO
     */
    private void validateLoginDTO(UserLoginDTO loginDTO) {
        if (!StringUtils.hasText(loginDTO.getAccount())) {
            throw new RuntimeException("账号不能为空");
        }
        
        if ("password".equals(loginDTO.getLoginType()) && !StringUtils.hasText(loginDTO.getPassword())) {
            throw new RuntimeException("密码不能为空");
        }
        
        if ("sms".equals(loginDTO.getLoginType()) && !StringUtils.hasText(loginDTO.getSmsCode())) {
            throw new RuntimeException("验证码不能为空");
        }
    }

    /**
     * 从注册DTO获取账号
     */
    private String getAccountFromRegisterDTO(UserRegisterDTO registerDTO) {
        if (StringUtils.hasText(registerDTO.getPhone())) {
            return registerDTO.getPhone();
        } else if (StringUtils.hasText(registerDTO.getEmail())) {
            return registerDTO.getEmail();
        } else {
            throw new RuntimeException("手机号或邮箱不能为空");
        }
    }

    /**
     * 判断是否为手机号
     */
    private boolean isPhoneNumber(String account) {
        return PHONE_PATTERN.matcher(account).matches();
    }

    /**
     * 判断是否为邮箱
     */
    private boolean isEmail(String account) {
        return EMAIL_PATTERN.matcher(account).matches();
    }

    /**
     * 获取客户端IP（简化实现）
     */
    private String getClientIp() {
        // TODO: 从HttpServletRequest中获取真实IP
        return "127.0.0.1";
    }
}