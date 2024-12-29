package com.greenwiz.bms.controller.data.base;

import lombok.Data;

import java.util.List;

@Data
public class LayuiTableResp<T> {

    private int code;        // 狀態碼，0 表示成功
    private String msg;      // 提示信息
    private long count;      // 總數據量
    private List<T> data;          // 當前頁數據

    // 靜態方法：快速創建成功響應
    public static <T> LayuiTableResp<T> success(long count, List<T> data) {
        LayuiTableResp<T> response = new LayuiTableResp<>();
        response.setCode(0);
        response.setMsg("操作成功");
        response.setCount(count);
        response.setData(data);
        return response;
    }

    // 靜態方法：快速創建失敗響應
    public static <T> LayuiTableResp<T> error(String msg) {
        LayuiTableResp<T> response = new LayuiTableResp<>();
        response.setCode(1); // 非 0 表示錯誤
        response.setMsg(msg);
        response.setCount(0);
        response.setData(null);
        return response;
    }
}