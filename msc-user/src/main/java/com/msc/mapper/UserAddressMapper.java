package com.msc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msc.domain.entity.UserAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 用户收货地址数据访问层
 * 
 * @since 1.0.0
 */
@Mapper
public interface UserAddressMapper extends BaseMapper<UserAddress> {

    /**
     * 根据用户ID查询收货地址列表
     * 
     * @param userId 用户ID
     * @return 收货地址列表
     */
    @Select("SELECT * FROM user_address WHERE user_id = #{userId} AND is_deleted = 0 ORDER BY is_default DESC, create_time DESC")
    List<UserAddress> findByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询默认收货地址
     * 
     * @param userId 用户ID
     * @return 默认收货地址
     */
    @Select("SELECT * FROM user_address WHERE user_id = #{userId} AND is_default = 1 AND is_deleted = 0")
    UserAddress findDefaultByUserId(@Param("userId") Long userId);

    /**
     * 取消用户的所有默认地址
     * 
     * @param userId 用户ID
     * @return 更新行数
     */
    @Update("UPDATE user_address SET is_default = 0 WHERE user_id = #{userId}")
    int cancelDefaultByUserId(@Param("userId") Long userId);

    /**
     * 设置默认地址
     * 
     * @param addressId 地址ID
     * @param userId 用户ID
     * @return 更新行数
     */
    @Update("UPDATE user_address SET is_default = 1 WHERE id = #{addressId} AND user_id = #{userId}")
    int setDefault(@Param("addressId") Long addressId, @Param("userId") Long userId);

    /**
     * 统计用户地址数量
     * 
     * @param userId 用户ID
     * @return 地址数量
     */
    @Select("SELECT COUNT(*) FROM user_address WHERE user_id = #{userId} AND is_deleted = 0")
    Long countByUserId(@Param("userId") Long userId);
}