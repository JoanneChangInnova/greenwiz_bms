package com.greenwiz.bms.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;

public enum KrakenState {
    /**
     * 狀態：庫存 (0)。
     */
    STOCKED(0),

    /**
     * 狀態：運行中 (1)。
     */
    RUNNING(1),

    /**
     * 狀態：斷線 (2)。
     */
    OFFLINE(2);

    private final Integer value;

    @JsonValue
    public Integer getValue() {
        return value;
    }

    KrakenState(Integer value) {
        this.value = value;
    }

    public static KrakenState fromValue(Integer value) {
        for (KrakenState state : KrakenState.values()) {
            if (state.getValue().equals(value)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Invalid state value: " + value);
    }
}
