package com.greenwiz.bms.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum UserState {

    /**
     * 待審
     */
    PENDING_REVIEW(0),

    /**
     * 開通
     */
    APPROVED(1),

    /**
     * 封鎖
     */
    REJECTED(2)
    ;

    private final Integer value;

    @JsonValue
    public Integer getValue() {
        return value;
    }

    UserState(Integer value) {
        this.value = value;
    }
}
