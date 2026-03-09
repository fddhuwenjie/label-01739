package com.order.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.order.dto.OrderQueryDTO;
import com.order.entity.Order;
import com.order.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> {
    private static final AtomicLong orderSeq = new AtomicLong(System.currentTimeMillis());

    public String generateOrderNo() {
        return "ORD" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) 
               + String.format("%04d", orderSeq.incrementAndGet() % 10000);
    }

    public Page<Order> queryOrders(OrderQueryDTO dto) {
        Page<Order> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dto.getUserId() != null, Order::getUserId, dto.getUserId())
               .like(StrUtil.isNotBlank(dto.getMyNo()), Order::getMyNo, dto.getMyNo())
               .like(StrUtil.isNotBlank(dto.getOrderNo()), Order::getOrderNo, dto.getOrderNo())
               .like(StrUtil.isNotBlank(dto.getTitle()), Order::getTitle, dto.getTitle())
               .eq(StrUtil.isNotBlank(dto.getLanguage()), Order::getLanguage, dto.getLanguage())
               .eq(dto.getStatus() != null, Order::getStatus, dto.getStatus())
               .eq(dto.getSettled() != null, Order::getSettled, dto.getSettled())
               .orderByDesc(Order::getCreateTime);
        return page(page, wrapper);
    }

    public boolean updateStatus(Long orderId, Integer status) {
        Order order = getById(orderId);
        if (order == null) {
            log.warn("更新状态失败，订单不存在: orderId={}", orderId);
            return false;
        }
        int oldStatus = order.getStatus();
        order.setStatus(status);
        boolean result = updateById(order);
        log.info("订单状态更新: orderId={}, orderNo={}, {} -> {}, result={}", 
                orderId, order.getOrderNo(), oldStatus, status, result);
        return result;
    }

    public boolean updatePrice(Long orderId, BigDecimal price) {
        Order order = getById(orderId);
        if (order == null) {
            log.warn("更新价格失败，订单不存在: orderId={}", orderId);
            return false;
        }
        BigDecimal oldPrice = order.getTotalPrice();
        order.setTotalPrice(price);
        boolean result = updateById(order);
        log.info("订单价格更新: orderId={}, orderNo={}, {} -> {}, result={}", 
                orderId, order.getOrderNo(), oldPrice, price, result);
        return result;
    }

    public boolean updateSettled(Long orderId, Integer settled) {
        Order order = getById(orderId);
        if (order == null) {
            log.warn("更新结算状态失败，订单不存在: orderId={}", orderId);
            return false;
        }
        int oldSettled = order.getSettled();
        order.setSettled(settled);
        boolean result = updateById(order);
        log.info("订单结算状态更新: orderId={}, orderNo={}, {} -> {}, price={}, result={}", 
                orderId, order.getOrderNo(), oldSettled, settled, order.getTotalPrice(), result);
        return result;
    }

    public BigDecimal getBalance(Long userId) {
        BigDecimal balance = baseMapper.getSettledBalance(userId);
        log.debug("查询用户余额: userId={}, balance={}", userId, balance);
        return balance;
    }
}
