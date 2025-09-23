package com.msc.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户地址DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAddressDTO {
    
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
     * 省份代码
     */
    private String provinceCode;
    
    /**
     * 省份名称
     */
    private String provinceName;
    
    /**
     * 城市代码
     */
    private String cityCode;
    
    /**
     * 城市名称
     */
    private String cityName;
    
    /**
     * 区县代码
     */
    private String districtCode;
    
    /**
     * 区县名称
     */
    private String districtName;
    
    /**
     * 详细地址
     */
    private String detailAddress;
    
    /**
     * 邮政编码
     */
    private String postalCode;
    
    /**
     * 是否默认地址:0-否,1-是
     */
    private Integer isDefault;
    
    /**
     * 地址标签
     */
    private String addressTag;
    
    /**
     * 自定义构造方法
     */
    public UserAddressDTO(Long userId, String receiverName, String receiverPhone, String detailAddress) {
        this.userId = userId;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.detailAddress = detailAddress;
        this.isDefault = 0;
    }
}