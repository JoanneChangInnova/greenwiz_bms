package com.greenwiz.bms.controller.data.user;

import lombok.Data;

@Data
public class ParentData {
    private int id;
    private String name;

    public ParentData(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
