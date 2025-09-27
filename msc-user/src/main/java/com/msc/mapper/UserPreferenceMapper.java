package com.msc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msc.domain.entity.UserPreference;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 用户偏好设置数据访问层
 * 
 * @since 1.0.0
 */
@Mapper
public interface UserPreferenceMapper extends BaseMapper<UserPreference> {

    /**
     * 根据用户ID查询偏好设置
     * 
     * @param userId 用户ID
     * @return 用户偏好设置
     */
    @Select("SELECT * FROM user_preference WHERE user_id = #{userId}")
    UserPreference findByUserId(@Param("userId") Long userId);

    /**
     * 更新通知设置
     * 
     * @param userId 用户ID
     * @param emailNotification 邮件通知
     * @param smsNotification 短信通知
     * @param pushNotification 推送通知
     * @param marketingNotification 营销通知
     * @return 更新行数
     */
    @Update("UPDATE user_preference SET email_notification = #{emailNotification}, " +
            "sms_notification = #{smsNotification}, push_notification = #{pushNotification}, " +
            "marketing_notification = #{marketingNotification}, update_time = NOW() WHERE user_id = #{userId}")
    int updateNotificationSettings(@Param("userId") Long userId,
                                  @Param("emailNotification") Integer emailNotification,
                                  @Param("smsNotification") Integer smsNotification,
                                  @Param("pushNotification") Integer pushNotification,
                                  @Param("marketingNotification") Integer marketingNotification);

    /**
     * 更新隐私设置
     * 
     * @param userId 用户ID
     * @param privacyLevel 隐私级别
     * @param showOnlineStatus 显示在线状态
     * @param allowSearch 允许搜索
     * @return 更新行数
     */
    @Update("UPDATE user_preference SET privacy_level = #{privacyLevel}, " +
            "show_online_status = #{showOnlineStatus}, allow_search = #{allowSearch}, " +
            "update_time = NOW() WHERE user_id = #{userId}")
    int updatePrivacySettings(@Param("userId") Long userId,
                             @Param("privacyLevel") String privacyLevel,
                             @Param("showOnlineStatus") Integer showOnlineStatus,
                             @Param("allowSearch") Integer allowSearch);

    /**
     * 更新主题设置
     * 
     * @param userId 用户ID
     * @param theme 主题
     * @return 更新行数
     */
    @Update("UPDATE user_preference SET theme = #{theme}, update_time = NOW() WHERE user_id = #{userId}")
    int updateTheme(@Param("userId") Long userId, @Param("theme") String theme);

    /**
     * 更新语言和地区设置
     * 
     * @param userId 用户ID
     * @param language 语言
     * @param timezone 时区
     * @param currency 货币
     * @return 更新行数
     */
    @Update("UPDATE user_preference SET language = #{language}, timezone = #{timezone}, " +
            "currency = #{currency}, update_time = NOW() WHERE user_id = #{userId}")
    int updateLocaleSettings(@Param("userId") Long userId,
                            @Param("language") String language,
                            @Param("timezone") String timezone,
                            @Param("currency") String currency);

    /**
     * 更新默认收货地址
     * 
     * @param userId 用户ID
     * @param defaultAddressId 默认地址ID
     * @return 更新行数
     */
    @Update("UPDATE user_preference SET default_address_id = #{defaultAddressId}, update_time = NOW() WHERE user_id = #{userId}")
    int updateDefaultAddress(@Param("userId") Long userId, @Param("defaultAddressId") Long defaultAddressId);

    /**
     * 更新购物偏好
     * 
     * @param userId 用户ID
     * @param preferredCategories 偏好分类
     * @param preferredBrands 偏好品牌
     * @param priceRange 价格范围
     * @param shoppingHabits 购物习惯
     * @return 更新行数
     */
    @Update("UPDATE user_preference SET preferred_categories = #{preferredCategories}, " +
            "preferred_brands = #{preferredBrands}, price_range = #{priceRange}, " +
            "shopping_habits = #{shoppingHabits}, update_time = NOW() WHERE user_id = #{userId}")
    int updateShoppingPreferences(@Param("userId") Long userId,
                                 @Param("preferredCategories") String preferredCategories,
                                 @Param("preferredBrands") String preferredBrands,
                                 @Param("priceRange") String priceRange,
                                 @Param("shoppingHabits") String shoppingHabits);

    /**
     * 查询接收邮件通知的用户
     * 
     * @return 用户ID列表
     */
    @Select("SELECT user_id FROM user_preference WHERE email_notification = 1")
    List<Long> findUsersWithEmailNotification();

    /**
     * 查询接收短信通知的用户
     * 
     * @return 用户ID列表
     */
    @Select("SELECT user_id FROM user_preference WHERE sms_notification = 1")
    List<Long> findUsersWithSmsNotification();

    /**
     * 查询接收推送通知的用户
     * 
     * @return 用户ID列表
     */
    @Select("SELECT user_id FROM user_preference WHERE push_notification = 1")
    List<Long> findUsersWithPushNotification();

    /**
     * 查询接收营销通知的用户
     * 
     * @return 用户ID列表
     */
    @Select("SELECT user_id FROM user_preference WHERE marketing_notification = 1")
    List<Long> findUsersWithMarketingNotification();
}