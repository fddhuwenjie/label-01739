package com.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.order.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.math.BigDecimal;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    @Select("SELECT COALESCE(SUM(total_price), 0) FROM sys_order WHERE user_id = #{userId} AND settled = 1")
    BigDecimal getSettledBalance(Long userId);
}
