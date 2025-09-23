package com.msc.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户收货地址实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAddress {
    
    /**
     * 地址ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 收货人姓名
     */
    private String receiverName;
    
    /**
     * 收货人电话
     */
    private String receiverPhone;
    
    /**
     * 省份
     */
    private String province;
    
    /**
     * 城市
     */
    private String city;
    
    /**
     * 区县
     */
    private String district;
    
    /**
     * 街道
     */
    private String street;
    
    /**
     * 详细地址
     */
    private String detailAddress;
    
    /**
     * 邮政编码
     */
    private String postalCode;
    
    /**
     * 地址标签:家,公司,学校等
     */
    private String addressTag;
    
    /**
     * 经度
     */
    private BigDecimal longitude;
    
    /**
     * 纬度
     */
    private BigDecimal latitude;
    
    /**
     * 是否默认地址:0-否,1-是
     */
    private Integer defaultAddress;
    
    /**
     * 是否删除:0-否,1-是
     */
    private Integer deleted;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 自定义构造方法
     */
    public UserAddress(Long userId, String receiverName, String receiverPhone) {
        this.userId = userId;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.defaultAddress = 0;
        this.deleted = 0;
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