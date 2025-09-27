package com.msc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msc.domain.entity.UserOauth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户第三方授权数据访问层
 * 
 * @since 1.0.0
 */
@Mapper
public interface UserOauthMapper extends BaseMapper<UserOauth> {

    /**
     * 根据用户ID查询第三方授权信息
     * 
     * @param userId 用户ID
     * @return 第三方授权信息列表
     */
    @Select("SELECT * FROM user_oauth WHERE user_id = #{userId} AND bound = 1")
    List<UserOauth> findByUserId(@Param("userId") Long userId);

    /**
     * 根据平台和OpenID查询授权信息
     * 
     * @param platform 第三方平台
     * @param openId 第三方用户ID
     * @return 第三方授权信息
     */
    @Select("SELECT * FROM user_oauth WHERE platform = #{platform} AND open_id = #{openId}")
    UserOauth findByPlatformAndOpenId(@Param("platform") String platform, @Param("openId") String openId);

    /**
     * 根据平台和UnionID查询授权信息
     * 
     * @param platform 第三方平台
     * @param unionId 第三方统一用户ID
     * @return 第三方授权信息
     */
    @Select("SELECT * FROM user_oauth WHERE platform = #{platform} AND union_id = #{unionId}")
    UserOauth findByPlatformAndUnionId(@Param("platform") String platform, @Param("unionId") String unionId);

    /**
     * 根据用户ID和平台查询授权信息
     * 
     * @param userId 用户ID
     * @param platform 第三方平台
     * @return 第三方授权信息
     */
    @Select("SELECT * FROM user_oauth WHERE user_id = #{userId} AND platform = #{platform}")
    UserOauth findByUserIdAndPlatform(@Param("userId") Long userId, @Param("platform") String platform);

    /**
     * 更新Token信息
     * 
     * @param id 授权ID
     * @param accessToken 访问令牌
     * @param refreshToken 刷新令牌
     * @param expireTime 过期时间
     * @return 更新行数
     */
    @Update("UPDATE user_oauth SET access_token = #{accessToken}, refresh_token = #{refreshToken}, " +
            "token_expire_time = #{expireTime}, update_time = NOW() WHERE id = #{id}")
    int updateToken(@Param("id") Long id, 
                   @Param("accessToken") String accessToken, 
                   @Param("refreshToken") String refreshToken, 
                   @Param("expireTime") LocalDateTime expireTime);

    /**
     * 绑定第三方账号
     * 
     * @param id 授权ID
     * @param userId 用户ID
     * @return 更新行数
     */
    @Update("UPDATE user_oauth SET user_id = #{userId}, bound = 1, bind_time = NOW(), update_time = NOW() WHERE id = #{id}")
    int bindUser(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 解绑第三方账号
     * 
     * @param userId 用户ID
     * @param platform 第三方平台
     * @return 更新行数
     */
    @Update("UPDATE user_oauth SET bound = 0, unbind_time = NOW(), update_time = NOW() " +
            "WHERE user_id = #{userId} AND platform = #{platform}")
    int unbindUser(@Param("userId") Long userId, @Param("platform") String platform);

    /**
     * 查询即将过期的Token
     * 
     * @param expireTime 过期时间阈值
     * @return 即将过期的授权信息列表
     */
    @Select("SELECT * FROM user_oauth WHERE token_expire_time <= #{expireTime} AND bound = 1")
    List<UserOauth> findExpiringSoon(@Param("expireTime") LocalDateTime expireTime);

    /**
     * 统计平台绑定用户数
     * 
     * @param platform 第三方平台
     * @return 绑定用户数
     */
    @Select("SELECT COUNT(DISTINCT user_id) FROM user_oauth WHERE platform = #{platform} AND bound = 1")
    Long countBoundUsersByPlatform(@Param("platform") String platform);
}