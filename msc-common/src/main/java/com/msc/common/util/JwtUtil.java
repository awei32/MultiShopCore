package com.msc.common.util;

import cn.hutool.core.util.StrUtil;
import com.msc.common.constant.JwtConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret:mySecretKey123456789012345678901234567890}")
    private String secret;

    @Value("${jwt.expiration:86400}")
    private Long expiration;

    /**
     * 生成JWT密钥
     */
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成JWT token
     *
     * @param userId   用户ID
     * @param username 用户名
     * @return JWT token
     */
    public String generateToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtConstants.JWT_PAYLOAD_USER_ID, userId);
        claims.put(JwtConstants.JWT_PAYLOAD_USERNAME, username);
        return generateToken(claims);
    }

    /**
     * 生成JWT token
     *
     * @param claims 载荷信息
     * @return JWT token
     */
    public String generateToken(Map<String, Object> claims) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 从token中获取Claims
     *
     * @param token JWT token
     * @return Claims
     */
    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从token中获取用户ID
     *
     * @param token JWT token
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            Object userId = claims.get(JwtConstants.JWT_PAYLOAD_USER_ID);
            if (userId instanceof Integer) {
                return ((Integer) userId).longValue();
            } else if (userId instanceof Long) {
                return (Long) userId;
            }
        }
        return null;
    }

    /**
     * 从token中获取用户名
     *
     * @param token JWT token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? claims.get(JwtConstants.JWT_PAYLOAD_USERNAME, String.class) : null;
    }

    /**
     * 获取token的过期时间
     *
     * @param token JWT token
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? claims.getExpiration() : null;
    }

    /**
     * 判断token是否过期
     *
     * @param token JWT token
     * @return 是否过期
     */
    public Boolean isTokenExpired(String token) {
        if (StrUtil.isBlank(token)) {
            return true;
        }
        Date expiration = getExpirationDateFromToken(token);
        return expiration == null || expiration.before(new Date());
    }

    /**
     * 验证token是否有效
     *
     * @param token JWT token
     * @return 是否有效
     */
    public Boolean validateToken(String token) {
        if (StrUtil.isBlank(token)) {
            return false;
        }
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 刷新token
     *
     * @param token 原token
     * @return 新token
     */
    public String refreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            // 创建新的claims映射，避免修改原Claims对象
            Map<String, Object> newClaims = new HashMap<>();
            newClaims.put(JwtConstants.JWT_PAYLOAD_USER_ID, claims.get(JwtConstants.JWT_PAYLOAD_USER_ID));
            newClaims.put(JwtConstants.JWT_PAYLOAD_USERNAME, claims.get(JwtConstants.JWT_PAYLOAD_USERNAME));
            // 复制其他可能存在的claims
            if (claims.get(JwtConstants.JWT_PAYLOAD_ROLES) != null) {
                newClaims.put(JwtConstants.JWT_PAYLOAD_ROLES, claims.get(JwtConstants.JWT_PAYLOAD_ROLES));
            }
            if (claims.get(JwtConstants.JWT_PAYLOAD_AUTHORITIES) != null) {
                newClaims.put(JwtConstants.JWT_PAYLOAD_AUTHORITIES, claims.get(JwtConstants.JWT_PAYLOAD_AUTHORITIES));
            }
            return generateToken(newClaims);
        }
        return null;
    }
}