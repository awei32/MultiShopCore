package com.msc.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户收货地址实体类
 * 
 * @author MultiShop Team
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@TableName("user_address")
public class UserAddress {
    
    /**
     * 地址ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    
    /**
     * 收货人姓名
     */
    @TableField("receiver_name")
    private String receiverName;
    
    /**
     * 收货人电话
     */
    @TableField("receiver_phone")
    private String receiverPhone;
    
    /**
     * 省份
     */
    @TableField("province")
    private String province;
    
    /**
     * 城市
     */
    @TableField("city")
    private String city;
    
    /**
     * 区县
     */
    @TableField("district")
    private String district;
    
    /**
     * 街道
     */
    @TableField("street")
    private String street;
    
    /**
     * 详细地址
     */
    @TableField("detail_address")
    private String detailAddress;
    
    /**
     * 邮政编码
     */
    @TableField("postal_code")
    private String postalCode;
    
    /**
     * 地址标签:家,公司,学校等
     */
    @TableField("address_tag")
    private String addressTag;
    
    /**
     * 经度
     */
    @TableField("longitude")
    private BigDecimal longitude;
    
    /**
     * 纬度
     */
    @TableField("latitude")
    private BigDecimal latitude;
    
    /**
     * 是否默认地址:0-否,1-是
     */
    @TableField("is_default")
    private Integer isDefault;
    
    /**
     * 是否删除:0-否,1-是
     */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
    
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /**
     * 自定义构造方法
     */
    public UserAddress(Long userId, String receiverName, String receiverPhone) {
        this.userId = userId;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.isDefault = 0;
        this.isDeleted = 0;
    }
    
    /**
     * 获取完整地址
     */
    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (province != null) sb.append(province);
        if (city != null) sb.append(city);
        if (district != null) sb.append(district);
        if (street != null) sb.append(street);
        if (detailAddress != null) sb.append(detailAddress);
        return sb.toString();
    }
}