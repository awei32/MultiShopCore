package com.msc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msc.domain.entity.UserPointsLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户积分记录数据访问层
 * 
 * @since 1.0.0
 */
@Mapper
public interface UserPointsLogMapper extends BaseMapper<UserPointsLog> {

    /**
     * 分页查询用户积分记录
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @return 积分记录分页列表
     */
    @Select("SELECT * FROM user_points_log WHERE user_id = #{userId} ORDER BY create_time DESC")
    IPage<UserPointsLog> selectByUserId(Page<UserPointsLog> page, @Param("userId") Long userId);

    /**
     * 根据用户ID和操作类型查询积分记录
     * 
     * @param userId 用户ID
     * @param operationType 操作类型
     * @return 积分记录列表
     */
    @Select("SELECT * FROM user_points_log WHERE user_id = #{userId} AND operation_type = #{operationType} ORDER BY create_time DESC")
    List<UserPointsLog> findByUserIdAndOperationType(@Param("userId") Long userId, 
                                                     @Param("operationType") Integer operationType);

    /**
     * 统计用户指定时间范围内的积分变化
     * 
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 积分变化总和
     */
    @Select("SELECT COALESCE(SUM(points_change), 0) FROM user_points_log WHERE user_id = #{userId} AND create_time BETWEEN #{startTime} AND #{endTime}")
    Integer sumPointsChangeByUserIdAndTime(@Param("userId") Long userId, 
                                          @Param("startTime") LocalDateTime startTime, 
                                          @Param("endTime") LocalDateTime endTime);

    /**
     * 统计用户总获得积分
     * 
     * @param userId 用户ID
     * @return 总获得积分
     */
    @Select("SELECT COALESCE(SUM(points_change), 0) FROM user_points_log WHERE user_id = #{userId} AND points_change > 0")
    Integer sumEarnedPointsByUserId(@Param("userId") Long userId);

    /**
     * 统计用户总消费积分
     * 
     * @param userId 用户ID
     * @return 总消费积分
     */
    @Select("SELECT COALESCE(SUM(ABS(points_change)), 0) FROM user_points_log WHERE user_id = #{userId} AND points_change < 0")
    Integer sumSpentPointsByUserId(@Param("userId") Long userId);

    /**
     * 查询用户最近的积分记录
     * 
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 积分记录列表
     */
    @Select("SELECT * FROM user_points_log WHERE user_id = #{userId} ORDER BY create_time DESC LIMIT #{limit}")
    List<UserPointsLog> findRecentByUserId(@Param("userId") Long userId, @Param("limit") Integer limit);
}