package com.greenwiz.bms.controller.data.base;

import lombok.Data;

@Data
public class PageReq {
    private int page = 0;
    private int size = 10;
    private String sortBy = "id";
    private String direction = "asc";
}
