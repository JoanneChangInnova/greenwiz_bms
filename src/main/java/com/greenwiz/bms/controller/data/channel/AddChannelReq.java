package com.greenwiz.bms.controller.data.channel;

import com.greenwiz.bms.controller.data.base.RequestJson;
import com.greenwiz.bms.enumeration.DeviceType;
import com.greenwiz.bms.enumeration.State;
import com.greenwiz.bms.exception.BmsException;
import com.greenwiz.bms.utils.ValidationUtils;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AddChannelReq extends RequestJson {

    @NotNull(message = "kraken ID不能為空")
    private Long iotDeviceId;

    @NotBlank(message = "名稱不能為空")
    private String name;

    @NotBlank(message = "通道代號不能為空")
    private String channelName;

    @NotNull(message = "設備類型不能為空")
    private String deviceType;

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
        if (deviceType == null) {
            throw new BmsException("設備類型不能為空");
        }

        ValidationUtils.validateChannelName(channelName);
        //ValidationUtils.validateDeviceModel(deviceType, deviceModel);
        ValidationUtils.validateFunctionMode(deviceType, functionMode); // 因為方法接收 String
    }

}
