package com.msc.service.impl;

import com.msc.service.VerifyCodeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 验证码服务实现类
 * 
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VerifyCodeServiceImpl implements VerifyCodeService {

    private final StringRedisTemplate redisTemplate;

    private static final String CODE_PREFIX = "verify_code:";
    private static final String LIMIT_PREFIX = "verify_limit:";
    private static final int DEFAULT_CODE_LENGTH = 6;
    private static final int DEFAULT_EXPIRE_MINUTES = 5;
    private static final int SEND_LIMIT_MINUTES = 1;
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");



    @Override
    public Boolean sendSmsCode(String phone, String type) {
        if (!isValidPhone(phone)) {
            log.warn("Invalid phone number: {}", phone);
            return false;
        }

        if (!checkSendLimit(phone, type)) {
            log.warn("Send frequency limit exceeded for phone: {}", phone);
            return false;
        }

        String code = generateCode(DEFAULT_CODE_LENGTH);
        cacheCode(phone, code, type, DEFAULT_EXPIRE_MINUTES);
        
        // TODO: 集成短信服务提供商（阿里云、腾讯云等）
        log.info("SMS code sent to {}: {}", phone, code);
        
        // 设置发送频率限制
        setSendLimit(phone, type);
        
        return true;
    }

    @Override
    public Boolean sendEmailCode(String email, String type) {
        if (!isValidEmail(email)) {
            log.warn("Invalid email address: {}", email);
            return false;
        }

        if (!checkSendLimit(email, type)) {
            log.warn("Send frequency limit exceeded for email: {}", email);
            return false;
        }

        String code = generateCode(DEFAULT_CODE_LENGTH);
        cacheCode(email, code, type, DEFAULT_EXPIRE_MINUTES);
        
        // TODO: 集成邮件服务
        log.info("Email code sent to {}: {}", email, code);
        
        // 设置发送频率限制
        setSendLimit(email, type);
        
        return true;
    }

    @Override
    public Boolean verifyCode(String account, String code, String type) {
        if (account == null || code == null || type == null) {
            return false;
        }

        String cachedCode = getCachedCode(account, type);
        if (cachedCode == null) {
            log.warn("Verify code not found or expired for account: {}", account);
            return false;
        }

        boolean isValid = code.equals(cachedCode);
        if (isValid) {
            // 验证成功后删除验证码
            removeCachedCode(account, type);
            log.info("Verify code validated successfully for account: {}", account);
        } else {
            log.warn("Invalid verify code for account: {}", account);
        }

        return isValid;
    }

    @Override
    public String generateCode(int length) {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10));
        }
        
        return code.toString();
    }

    @Override
    public void cacheCode(String account, String code, String type, int expireMinutes) {
        String key = buildCodeKey(account, type);
        redisTemplate.opsForValue().set(key, code, expireMinutes, TimeUnit.MINUTES);
        log.debug("Cached verify code for account: {}, type: {}", account, type);
    }

    @Override
    public String getCachedCode(String account, String type) {
        String key = buildCodeKey(account, type);
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void removeCachedCode(String account, String type) {
        String key = buildCodeKey(account, type);
        redisTemplate.delete(key);
        log.debug("Removed cached verify code for account: {}, type: {}", account, type);
    }

    @Override
    public Boolean checkSendLimit(String account, String type) {
        String key = buildLimitKey(account, type);
        String value = redisTemplate.opsForValue().get(key);
        return value == null;
    }

    /**
     * 设置发送频率限制
     * 
     * @param account 账号
     * @param type 验证码类型
     */
    private void setSendLimit(String account, String type) {
        String key = buildLimitKey(account, type);
        redisTemplate.opsForValue().set(key, "1", SEND_LIMIT_MINUTES, TimeUnit.MINUTES);
    }

    /**
     * 构建验证码缓存键
     * 
     * @param account 账号
     * @param type 验证码类型
     * @return 缓存键
     */
    private String buildCodeKey(String account, String type) {
        return CODE_PREFIX + type + ":" + account;
    }

    /**
     * 构建频率限制缓存键
     * 
     * @param account 账号
     * @param type 验证码类型
     * @return 缓存键
     */
    private String buildLimitKey(String account, String type) {
        return LIMIT_PREFIX + type + ":" + account;
    }

    /**
     * 验证手机号格式
     * 
     * @param phone 手机号
     * @return 是否有效
     */
    private boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * 验证邮箱格式
     * 
     * @param email 邮箱
     * @return 是否有效
     */
    private boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
}