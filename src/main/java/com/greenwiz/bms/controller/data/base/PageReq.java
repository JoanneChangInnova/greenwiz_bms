package com.greenwiz.bms.controller.data.base;

import lombok.Data;

@Data
public class PageReq {
    private int page;
    private int size;
    private String sortBy = "id";
    private String direction = "asc";

    public int getPage() {
        return page-1;
    }
}
