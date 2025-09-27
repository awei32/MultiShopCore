package com.msc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msc.domain.entity.UserMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户消息数据访问层
 * 
 * @since 1.0.0
 */
@Mapper
public interface UserMessageMapper extends BaseMapper<UserMessage> {

    /**
     * 分页查询用户消息
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @return 消息分页列表
     */
    @Select("SELECT * FROM user_message WHERE user_id = #{userId} AND deleted = 0 ORDER BY send_time DESC")
    IPage<UserMessage> selectByUserId(Page<UserMessage> page, @Param("userId") Long userId);

    /**
     * 根据用户ID和消息类型查询消息
     * 
     * @param userId 用户ID
     * @param messageType 消息类型
     * @return 消息列表
     */
    @Select("SELECT * FROM user_message WHERE user_id = #{userId} AND message_type = #{messageType} AND deleted = 0 ORDER BY send_time DESC")
    List<UserMessage> findByUserIdAndType(@Param("userId") Long userId, @Param("messageType") Integer messageType);

    /**
     * 查询用户未读消息
     * 
     * @param userId 用户ID
     * @return 未读消息列表
     */
    @Select("SELECT * FROM user_message WHERE user_id = #{userId} AND read = 0 AND deleted = 0 ORDER BY send_time DESC")
    List<UserMessage> findUnreadByUserId(@Param("userId") Long userId);

    /**
     * 统计用户未读消息数量
     * 
     * @param userId 用户ID
     * @return 未读消息数量
     */
    @Select("SELECT COUNT(*) FROM user_message WHERE user_id = #{userId} AND read = 0 AND deleted = 0")
    Long countUnreadByUserId(@Param("userId") Long userId);

    /**
     * 根据消息类型统计用户未读消息数量
     * 
     * @param userId 用户ID
     * @param messageType 消息类型
     * @return 未读消息数量
     */
    @Select("SELECT COUNT(*) FROM user_message WHERE user_id = #{userId} AND message_type = #{messageType} AND read = 0 AND deleted = 0")
    Long countUnreadByUserIdAndType(@Param("userId") Long userId, @Param("messageType") Integer messageType);

    /**
     * 标记消息为已读
     * 
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 更新行数
     */
    @Update("UPDATE user_message SET read = 1, read_time = NOW() WHERE id = #{messageId} AND user_id = #{userId}")
    int markAsRead(@Param("messageId") Long messageId, @Param("userId") Long userId);

    /**
     * 批量标记消息为已读
     * 
     * @param userId 用户ID
     * @param messageType 消息类型（可选）
     * @return 更新行数
     */
    @Update("UPDATE user_message SET read = 1, read_time = NOW() WHERE user_id = #{userId} AND read = 0 " +
            "AND (#{messageType} IS NULL OR message_type = #{messageType})")
    int markAllAsRead(@Param("userId") Long userId, @Param("messageType") Integer messageType);

    /**
     * 软删除消息
     * 
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 更新行数
     */
    @Update("UPDATE user_message SET deleted = 1, delete_time = NOW() WHERE id = #{messageId} AND user_id = #{userId}")
    int softDelete(@Param("messageId") Long messageId, @Param("userId") Long userId);

    /**
     * 清理过期消息
     * 
     * @param expireTime 过期时间
     * @return 删除行数
     */
    @Update("DELETE FROM user_message WHERE send_time < #{expireTime}")
    int cleanExpiredMessages(@Param("expireTime") LocalDateTime expireTime);
}