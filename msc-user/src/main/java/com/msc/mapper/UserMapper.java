package com.msc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msc.domain.entity.User;
import com.msc.domain.dto.UserQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户数据访问层
 * 
 * @since 1.0.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return 用户信息
     */
    @Select("SELECT * FROM user WHERE username = #{username} AND is_deleted = 0")
    User findByUsername(@Param("username") String username);

    /**
     * 根据邮箱查询用户
     * 
     * @param email 邮箱
     * @return 用户信息
     */
    @Select("SELECT * FROM user WHERE email = #{email} AND is_deleted = 0")
    User findByEmail(@Param("email") String email);

    /**
     * 根据手机号查询用户
     * 
     * @param phone 手机号
     * @return 用户信息
     */
    @Select("SELECT * FROM user WHERE phone = #{phone} AND is_deleted = 0")
    User findByPhone(@Param("phone") String phone);

    /**
     * 根据账号查询用户(用户名/邮箱/手机号)
     * 
     * @param account 账号
     * @return 用户信息
     */
    @Select("SELECT * FROM user WHERE (username = #{account} OR email = #{account} OR phone = #{account}) AND is_deleted = 0")
    User findByAccount(@Param("account") String account);

    /**
     * 更新最后登录信息
     * 
     * @param userId 用户ID
     * @param loginTime 登录时间
     * @param loginIp 登录IP
     * @return 更新行数
     */
    @Update("UPDATE user SET last_login_time = #{loginTime}, last_login_ip = #{loginIp}, login_count = login_count + 1 WHERE id = #{userId}")
    int updateLastLoginInfo(@Param("userId") Long userId, 
                           @Param("loginTime") LocalDateTime loginTime, 
                           @Param("loginIp") String loginIp);

    /**
     * 更新用户状态
     * 
     * @param userId 用户ID
     * @param status 状态
     * @return 更新行数
     */
    @Update("UPDATE user SET status = #{status} WHERE id = #{userId}")
    int updateStatus(@Param("userId") Long userId, @Param("status") Integer status);

    /**
     * 更新用户积分
     * 
     * @param userId 用户ID
     * @param totalPoints 总积分
     * @param availablePoints 可用积分
     * @return 更新行数
     */
    @Update("UPDATE user SET total_points = #{totalPoints}, available_points = #{availablePoints} WHERE id = #{userId}")
    int updatePoints(@Param("userId") Long userId, 
                    @Param("totalPoints") Integer totalPoints, 
                    @Param("availablePoints") Integer availablePoints);

    /**
     * 分页查询用户列表
     * 
     * @param page 分页参数
     * @param queryDTO 查询条件
     * @return 用户分页列表
     */
    IPage<User> selectUserPage(Page<User> page, @Param("query") UserQueryDTO queryDTO);

    /**
     * 根据会员等级查询用户数量
     * 
     * @param memberLevel 会员等级
     * @return 用户数量
     */
    @Select("SELECT COUNT(*) FROM user WHERE member_level = #{memberLevel} AND is_deleted = 0")
    Long countByMemberLevel(@Param("memberLevel") Integer memberLevel);

    /**
     * 根据注册来源查询用户数量
     * 
     * @param registerSource 注册来源
     * @return 用户数量
     */
    @Select("SELECT COUNT(*) FROM user WHERE register_source = #{registerSource} AND is_deleted = 0")
    Long countByRegisterSource(@Param("registerSource") String registerSource);

    /**
     * 查询指定时间范围内注册的用户
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 用户列表
     */
    @Select("SELECT * FROM user WHERE create_time BETWEEN #{startTime} AND #{endTime} AND is_deleted = 0 ORDER BY create_time DESC")
    List<User> findByCreateTimeBetween(@Param("startTime") LocalDateTime startTime, 
                                      @Param("endTime") LocalDateTime endTime);

    /**
     * 查询活跃用户(最近30天内登录过的用户)
     * 
     * @param days 天数
     * @return 用户列表
     */
    @Select("SELECT * FROM user WHERE last_login_time >= DATE_SUB(NOW(), INTERVAL #{days} DAY) AND is_deleted = 0")
    List<User> findActiveUsers(@Param("days") Integer days);
}