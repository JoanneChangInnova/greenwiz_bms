package com.greenwiz.bms.controller.data.user.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.AssertTrue;
@Data
@GroupSequence({RequestJson.class, RequestJson.LastCheck.class})
public class RequestJson {
    @AssertTrue(groups = {LastCheck.class})
    @JsonIgnore
    public final Boolean get_validate() {
        validate();

        return true;
    }

    /** <pre>
     * 應用自己實做 驗證.
     * 如果 實做的驗證有依賴其他欄位的驗證結果. 必須在 class level 使用(假設為 <b>ReqSubClass.class</b>)
     * &#64;GroupSequence({<b>ReqSubClass.class</b>, RequestJson.LastCheck.class}) 來確保 validate() 在其他驗證都執行成功後才執行
     *
     *
     */
    protected void validate() throws RuntimeException {
        // default empty
    }

    public static interface LastCheck {

    }

    public static interface FirstCheck {

    }
}
