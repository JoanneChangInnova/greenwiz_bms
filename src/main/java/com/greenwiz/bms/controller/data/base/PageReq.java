package com.greenwiz.bms.controller.data.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Data
public class PageReq {
    private int page;
    private int size;
    private String sortBy = "id";
    private String direction = "asc";

    @JsonIgnore
    private PageRequest pageable;


    public PageRequest getPageable() {
        Sort.Direction direction = Sort.Direction.fromString(getDirection());
        pageable = PageRequest.of(getPage(), getSize(), direction, getSortBy());
        return pageable;
    }


    public int getPage() {
        return page - 1;
    }
}
