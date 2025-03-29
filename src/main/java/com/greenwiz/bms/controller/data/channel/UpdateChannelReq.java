package com.greenwiz.bms.controller.data.channel;

import com.greenwiz.bms.controller.data.base.RequestJson;
import com.greenwiz.bms.enumeration.DeviceType;
import com.greenwiz.bms.enumeration.State;
import com.greenwiz.bms.utils.ValidationUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateChannelReq extends RequestJson {
    @NotNull(message = "kraken ID不能為空")
    private Long iotDeviceId;

    @NotNull(message = "工廠ID不能為空")
    private Long factoryId;

    @NotBlank(message = "名稱不能為空")
    private String name;

    @NotBlank(message = "通道代號不能為空")
    private String channelName;

    @NotNull(message = "設備類型不能為空")
    private DeviceType deviceType;

    @NotBlank(message = "設備型號不能為空")
    private String deviceModel;

    private String functionMode;

    @NotNull(message = "是否為總用電通道不能為空")
    private Boolean statisticsInOv;

    @NotNull(message = "狀態不能為空")
    private State state;

    private String description;

    @Override
    protected void validate() throws RuntimeException {
        ValidationUtils.validateChannelName(channelName);
        ValidationUtils.validateDeviceModel(deviceType, deviceModel);
        ValidationUtils.validateFunctionMode(deviceModel, functionMode);
    }
}