package com.greenwiz.bms.controller.data.factory;

import com.greenwiz.bms.controller.data.base.RequestJson;
import com.greenwiz.bms.enumeration.Country;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class AddFactoryReq extends RequestJson {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "factory_uuid", nullable = false, unique = true)
//    private UUID factoryUuid;

    @NotBlank(message = "name不能為空")
    @Size(max = 100, message = "name長度不能超過100個字符")
    private String name;

    @NotBlank(message = "utc_offset不能為空")
    @Pattern(regexp = "^[+-]\\d{2}:\\d{2}$", message = "時區偏移量格式必須為 +HH:mm 或 -HH:mm")
    @Size(max = 6, message = "utc_offset長度不能超過6個字符")
    private String utcOffset;

    @NotNull(message = "max_kwh不能為空")
    @Min(value = 0, message = "max_kwh不能小於0")
    @Max(value = 999999, message = "max_kwh不能大於999999")
    private Float maxKwh;

    @NotNull(message = "monitor_period_minute不能為空")
    @Min(value = 1, message = "監控週期不能小於1分鐘")
    private Short monitorPeriodMinute;

    private Country country;

    @Size(max = 1024, message = "address長度不能超過1024個字符")
    private String address;

    @Digits(integer = 3, fraction = 6, message = "longitude格式必須為xxx.xxxxxx")
    @DecimalMin(value = "-180.000000", message = "longitude不能小於-180")
    @DecimalMax(value = "180.000000", message = "longitude不能大於180")
    private BigDecimal longitude = BigDecimal.ZERO;

    @Digits(integer = 2, fraction = 6, message = "latitude格式必須為xx.xxxxxx")
    @DecimalMin(value = "-90.000000", message = "latitude不能小於-90")
    @DecimalMax(value = "90.000000", message = "latitude不能大於90")
    private BigDecimal latitude = BigDecimal.ZERO;

    @Size(max = 2048, message = "comment長度不能超過2048個字符")
    private String comment;
}
