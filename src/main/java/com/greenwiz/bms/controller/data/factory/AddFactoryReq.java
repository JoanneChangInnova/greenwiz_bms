package com.greenwiz.bms.controller.data.factory;

import com.greenwiz.bms.controller.data.base.RequestJson;
import com.greenwiz.bms.enumeration.Country;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class AddFactoryReq extends RequestJson {

    /**
     * 工廠名稱
     */
    @NotBlank(message = "name不能為空")
    @Size(max = 100, message = "name長度不能超過100個字符")
    private String name;

    /**
     * 時區
     * 預設值 +00:00
     */
    @NotBlank(message = "utc_offset不能為空")
    @Pattern(regexp = "^[+-]\\d{2}:\\d{2}$", message = "時區偏移量格式必須為 +HH:mm 或 -HH:mm")
    @Size(max = 6, message = "utc_offset長度不能超過6個字符")
    private String utcOffset;


    /**
     * 最大用電量
     * 預設值 100
     */
    @NotNull(message = "max_kwh不能為空")
    @Min(value = 0, message = "max_kwh不能小於0")
    @Max(value = 999999, message = "max_kwh不能大於999999")
    private Float maxKwh;

    /**
     * 監控週期（分鐘）
     * 預設值 15
     */
    @NotNull(message = "monitor_period_minute不能為空")
    @Min(value = 1, message = "監控週期不能小於1分鐘")
    private Short monitorPeriodMinute;

    /**
     * 國家名稱（ISO 3166-1三位字母代碼）
     */
    private Country country;

    /**
     * 工廠地址
     */
    @Size(max = 1024, message = "address長度不能超過1024個字符")
    private String address;

    /**
     * 經度
     */
    @Digits(integer = 3, fraction = 6, message = "longitude格式必須為xxx.xxxxxx")
    @DecimalMin(value = "-180.000000", message = "longitude不能小於-180")
    @DecimalMax(value = "180.000000", message = "longitude不能大於180")
    private BigDecimal longitude = BigDecimal.ZERO;

    /**
     * 緯度
     */
    @Digits(integer = 2, fraction = 6, message = "latitude格式必須為xx.xxxxxx")
    @DecimalMin(value = "-90.000000", message = "latitude不能小於-90")
    @DecimalMax(value = "90.000000", message = "latitude不能大於90")
    private BigDecimal latitude = BigDecimal.ZERO;

    /**
     * 工廠說明
     */
    @Size(max = 2048, message = "comment長度不能超過2048個字符")
    private String comment;

    /**
     * Factory綁定的kraken，可選多筆，由Kraken /listKrakenData取得清單
     */
    private List<Long> iotDeviceIds;

    /**
     * Factory綁定的使用者，可選多筆，由User /listAllCustomers取得清單
     */
    private List<Long> userIds;
}
