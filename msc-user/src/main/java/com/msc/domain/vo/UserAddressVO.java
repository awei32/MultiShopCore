package com.msc.domain.vo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户地址VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAddressVO {
    
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
     * 完整地址
     */
    private String fullAddress;
    
    /**
     * 邮政编码
     */
    private String postalCode;
    
    /**
     * 是否默认地址:0-否,1-是
     */
    private Integer isDefault;
    
    /**
     * 默认地址描述
     */
    private String defaultDesc;
    
    /**
     * 地址标签
     */
    private String addressTag;
    
    /**
     * 地址标签描述
     */
    private String addressTagDesc;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}