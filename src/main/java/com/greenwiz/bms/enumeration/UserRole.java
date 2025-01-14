package com.greenwiz.bms.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum UserRole {

    ADMIN(0),

    AGENT(1),

    INSTALLER(2),

    CUSTOMER(3);

    private final Integer value;

    @JsonValue
    public Integer getValue() {
        return value;
    }

    UserRole(Integer value) {
        this.value = value;
    }
}
