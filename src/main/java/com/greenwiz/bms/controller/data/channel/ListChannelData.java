package com.greenwiz.bms.controller.data.channel;

import com.greenwiz.bms.controller.data.user.CreateModifyUser;
import com.greenwiz.bms.entity.Channel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ListChannelData extends Channel {

    private CreateModifyUser createModifyUser;

}
