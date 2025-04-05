package com.greenwiz.bms.controller.data.kraken;

import com.greenwiz.bms.controller.data.user.CreateModifyUser;
import com.greenwiz.bms.entity.Kraken;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ListKrakenData extends Kraken {

    private CreateModifyUser createModifyUser;

}
