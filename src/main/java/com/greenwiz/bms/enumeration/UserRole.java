package com.greenwiz.bms.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
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

    @JsonCreator
    public static UserRole fromValue(Object value) {
        if (value instanceof Integer) {
            Integer intValue = (Integer) value;
            for (UserRole role : UserRole.values()) {
                if (role.getValue().equals(intValue)) {
                    return role;
                }
            }
        } else if (value instanceof String) {
            Integer intValue = Integer.parseInt((String) value);
            for (UserRole role : UserRole.values()) {
                if (role.getValue().equals(intValue)) {
                    return role;
                }
            }
        }
        throw new IllegalArgumentException("找不到對應的角色值: " + value);
    }
}
