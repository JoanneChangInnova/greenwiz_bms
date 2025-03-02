package com.greenwiz.bms.controller.data.channel;

import com.greenwiz.bms.controller.data.base.RequestJson;
import com.greenwiz.bms.enumeration.DeviceType;
import com.greenwiz.bms.enumeration.State;
import com.greenwiz.bms.utils.ValidationUtils;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddChannelReq extends RequestJson {

    private Long factoryId;

    @NotNull(message = "iotDeviceId 不能為空")
    private Long iotDeviceId;

    @NotNull(message = "Modbus 地址不能為空")
    @Min(value = 1, message = "Modbus 地址最小值為 1")
    @Max(value = 30, message = "Modbus 地址最大值為 30")
    private Integer addr;

    @NotBlank(message = "名稱不能為空")
    private String name;

    @NotBlank(message = "通道代號不能為空")
    private String channelName;

    @NotNull(message = "設備類型不能為空")
    private DeviceType deviceType;

    @NotBlank(message = "設備型號不能為空")
    private String deviceModel;

    @NotBlank(message = "功能模式不能為空")
    private String functionType;

    private String functionDetail;

    @NotNull(message = "是否為總用電通道不能為空")
    private Boolean statisticsInOv;

    @NotNull(message = "狀態不能為空")
    private State state;

    private String description;

    @Override
    protected void validate() throws RuntimeException {
        ValidationUtils.validateChannelName(channelName);
        ValidationUtils.validateDeviceModel(deviceType, deviceModel);
        ValidationUtils.validateFunctionType(deviceModel, functionType);
    }
}
