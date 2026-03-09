package com.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String instanceName;
    private String schoolName;
    private String password;
    private Integer role; // 0-学生 1-管理员
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
