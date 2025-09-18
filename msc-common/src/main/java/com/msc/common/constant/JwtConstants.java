package com.msc.common.constant;

/**
 * JWT相关常量
 */
public class JwtConstants {

    /**
     * JWT token前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * JWT token请求头名称
     */
    public static final String TOKEN_HEADER = "Authorization";

    /**
     * 用户ID请求头
     */
    public static final String USER_ID_HEADER = "X-User-Id";

    /**
     * 用户名请求头
     */
    public static final String USERNAME_HEADER = "X-Username";

    /**
     * JWT载荷中的用户ID字段
     */
    public static final String JWT_PAYLOAD_USER_ID = "userId";

    /**
     * JWT载荷中的用户名字段
     */
    public static final String JWT_PAYLOAD_USERNAME = "username";

    /**
     * JWT载荷中的角色字段
     */
    public static final String JWT_PAYLOAD_ROLES = "roles";

    /**
     * JWT载荷中的权限字段
     */
    public static final String JWT_PAYLOAD_AUTHORITIES = "authorities";
}