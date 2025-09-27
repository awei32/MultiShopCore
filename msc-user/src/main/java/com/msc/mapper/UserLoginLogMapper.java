package com.msc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msc.domain.entity.UserLoginLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户登录日志数据访问层
 * 
 * @since 1.0.0
 */
@Mapper
public interface UserLoginLogMapper extends BaseMapper<UserLoginLog> {

    /**
     * 分页查询用户登录日志
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @return 登录日志分页列表
     */
    @Select("SELECT * FROM user_login_log WHERE user_id = #{userId} ORDER BY create_time DESC")
    IPage<UserLoginLog> selectByUserId(Page<UserLoginLog> page, @Param("userId") Long userId);

    /**
     * 查询用户最近的登录记录
     * 
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 登录记录列表
     */
    @Select("SELECT * FROM user_login_log WHERE user_id = #{userId} ORDER BY create_time DESC LIMIT #{limit}")
    List<UserLoginLog> findRecentByUserId(@Param("userId") Long userId, @Param("limit") Integer limit);

    /**
     * 根据IP地址查询登录记录
     * 
     * @param loginIp 登录IP
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 登录记录列表
     */
    @Select("SELECT * FROM user_login_log WHERE login_ip = #{loginIp} AND create_time BETWEEN #{startTime} AND #{endTime} ORDER BY create_time DESC")
    List<UserLoginLog> findByIpAndTime(@Param("loginIp") String loginIp, 
                                      @Param("startTime") LocalDateTime startTime, 
                                      @Param("endTime") LocalDateTime endTime);

    /**
     * 统计用户指定时间范围内的登录次数
     * 
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 登录次数
     */
    @Select("SELECT COUNT(*) FROM user_login_log WHERE user_id = #{userId} AND create_time BETWEEN #{startTime} AND #{endTime}")
    Long countByUserIdAndTime(@Param("userId") Long userId, 
                             @Param("startTime") LocalDateTime startTime, 
                             @Param("endTime") LocalDateTime endTime);

    /**
     * 查询用户最后一次登录记录
     * 
     * @param userId 用户ID
     * @return 最后一次登录记录
     */
    @Select("SELECT * FROM user_login_log WHERE user_id = #{userId} ORDER BY create_time DESC LIMIT 1")
    UserLoginLog findLastByUserId(@Param("userId") Long userId);

    /**
     * 根据设备类型统计登录次数
     * 
     * @param deviceType 设备类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 登录次数
     */
    @Select("SELECT COUNT(*) FROM user_login_log WHERE device_type = #{deviceType} AND create_time BETWEEN #{startTime} AND #{endTime}")
    Long countByDeviceTypeAndTime(@Param("deviceType") String deviceType, 
                                 @Param("startTime") LocalDateTime startTime, 
                                 @Param("endTime") LocalDateTime endTime);
}