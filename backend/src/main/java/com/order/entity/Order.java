package com.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("sys_order")
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private String title;
    private String language;
    @TableField("`database`")
    private String database;
    private String frontend;
    private String frontendFramework;
    private String crawler;
    private String bigData;
    private String machineLearning;
    private String terminal;
    private String myNo;
    private Long userId;
    private String instanceName;
    private String schoolName;
    private LocalDate devDate;
    private String attachmentPath;
    private String attachmentName;
    private String requirement;
    private Integer status; // 0-待接单 1-已接单 2-开发中 3-开发完成 4-修改完成
    private BigDecimal totalPrice;
    private Integer settled; // 0-未结算 1-已结算
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
