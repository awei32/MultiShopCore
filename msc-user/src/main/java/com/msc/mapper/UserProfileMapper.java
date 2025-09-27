package com.msc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msc.domain.entity.UserProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 用户详细资料数据访问层
 * 
 * @since 1.0.0
 */
@Mapper
public interface UserProfileMapper extends BaseMapper<UserProfile> {

    /**
     * 根据用户ID查询用户详细资料
     * 
     * @param userId 用户ID
     * @return 用户详细资料
     */
    @Select("SELECT * FROM user_profile WHERE user_id = #{userId}")
    UserProfile findByUserId(@Param("userId") Long userId);

    /**
     * 根据身份证号查询用户资料
     * 
     * @param idCard 身份证号
     * @return 用户详细资料
     */
    @Select("SELECT * FROM user_profile WHERE id_card = #{idCard}")
    UserProfile findByIdCard(@Param("idCard") String idCard);

    /**
     * 根据真实姓名查询用户资料
     * 
     * @param realName 真实姓名
     * @return 用户详细资料列表
     */
    @Select("SELECT * FROM user_profile WHERE real_name = #{realName}")
    UserProfile findByRealName(@Param("realName") String realName);
}