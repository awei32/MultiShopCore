package com.msc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msc.domain.entity.UserMemberLevel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户会员等级配置数据访问层
 * 
 * @since 1.0.0
 */
@Mapper
public interface UserMemberLevelMapper extends BaseMapper<UserMemberLevel> {

    /**
     * 查询所有启用的会员等级
     * 
     * @return 会员等级列表
     */
    @Select("SELECT * FROM user_member_level WHERE status = 1 ORDER BY level_value ASC")
    List<UserMemberLevel> findAllEnabled();

    /**
     * 根据等级值查询会员等级
     * 
     * @param levelValue 等级值
     * @return 会员等级
     */
    @Select("SELECT * FROM user_member_level WHERE level_value = #{levelValue} AND status = 1")
    UserMemberLevel findByLevelValue(@Param("levelValue") Integer levelValue);

    /**
     * 根据消费金额查询对应的会员等级
     * 
     * @param totalAmount 总消费金额
     * @return 会员等级
     */
    @Select("SELECT * FROM user_member_level WHERE growth_value <= #{totalAmount} AND status = 1 ORDER BY growth_value DESC LIMIT 1")
    UserMemberLevel findByTotalAmount(@Param("totalAmount") BigDecimal totalAmount);

    /**
     * 根据积分查询对应的会员等级
     * 
     * @param totalPoints 总积分
     * @return 会员等级
     */
    @Select("SELECT * FROM user_member_level WHERE growth_value <= #{totalPoints} AND status = 1 ORDER BY growth_value DESC LIMIT 1")
    UserMemberLevel findByTotalPoints(@Param("totalPoints") Integer totalPoints);

    /**
     * 查询下一个等级
     * 
     * @param currentLevelValue 当前等级值
     * @return 下一个等级
     */
    @Select("SELECT * FROM user_member_level WHERE level_value > #{currentLevelValue} AND status = 1 ORDER BY level_value ASC LIMIT 1")
    UserMemberLevel findNextLevel(@Param("currentLevelValue") Integer currentLevelValue);

    /**
     * 查询最高等级
     * 
     * @return 最高等级
     */
    @Select("SELECT * FROM user_member_level WHERE status = 1 ORDER BY level_value DESC LIMIT 1")
    UserMemberLevel findMaxLevel();

    /**
     * 查询最低等级
     * 
     * @return 最低等级
     */
    @Select("SELECT * FROM user_member_level WHERE status = 1 ORDER BY level_value ASC LIMIT 1")
    UserMemberLevel findMinLevel();
}