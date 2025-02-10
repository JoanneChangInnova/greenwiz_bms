package com.greenwiz.bms.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;

public enum State {
    /**
     * 狀態：離線 (0)。
     */
    OFFLINE(0),

    /**
     * 狀態：在線 (1)。
     */
    ONLINE(1);

    private final Integer value;

    @JsonValue
    public Integer getValue() {
        return value;
    }

    State(Integer value) {
        this.value = value;
    }
}
