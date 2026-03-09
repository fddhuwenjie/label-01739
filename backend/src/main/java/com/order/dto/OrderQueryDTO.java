package com.order.dto;

import lombok.Data;

@Data
public class OrderQueryDTO {
    private String myNo;
    private String orderNo;
    private String title;
    private String language;
    private Integer status;
    private Integer settled;
    private Long userId;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
