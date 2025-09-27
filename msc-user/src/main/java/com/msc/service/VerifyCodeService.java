package com.msc.service;

/**
 * 验证码服务接口
 * 
 * @since 1.0.0
 */
public interface VerifyCodeService {

    /**
     * 发送短信验证码
     * 
     * @param phone 手机号
     * @param type 验证码类型
     * @return 发送结果
     */
    Boolean sendSmsCode(String phone, String type);

    /**
     * 发送邮箱验证码
     * 
     * @param email 邮箱
     * @param type 验证码类型
     * @return 发送结果
     */
    Boolean sendEmailCode(String email, String type);

    /**
     * 验证验证码
     * 
     * @param account 账号（手机号或邮箱）
     * @param code 验证码
     * @param type 验证码类型
     * @return 验证结果
     */
    Boolean verifyCode(String account, String code, String type);

    /**
     * 生成验证码
     * 
     * @param length 验证码长度
     * @return 验证码
     */
    String generateCode(int length);

    /**
     * 缓存验证码
     * 
     * @param account 账号
     * @param code 验证码
     * @param type 验证码类型
     * @param expireMinutes 过期时间（分钟）
     */
    void cacheCode(String account, String code, String type, int expireMinutes);

    /**
     * 获取缓存的验证码
     * 
     * @param account 账号
     * @param type 验证码类型
     * @return 验证码
     */
    String getCachedCode(String account, String type);

    /**
     * 删除缓存的验证码
     * 
     * @param account 账号
     * @param type 验证码类型
     */
    void removeCachedCode(String account, String type);

    /**
     * 检查发送频率限制
     * 
     * @param account 账号
     * @param type 验证码类型
     * @return 是否可以发送
     */
    Boolean checkSendLimit(String account, String type);
}