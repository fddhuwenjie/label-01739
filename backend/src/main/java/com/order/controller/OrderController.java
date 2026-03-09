package com.order.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.order.common.Result;
import com.order.dto.OrderQueryDTO;
import com.order.entity.Order;
import com.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Value("${file.upload-path}")
    private String uploadPath;

    @PostMapping("/submit")
    public Result<Order> submitOrder(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String database,
            @RequestParam(required = false) String frontend,
            @RequestParam(required = false) String frontendFramework,
            @RequestParam(required = false) String crawler,
            @RequestParam(required = false) String bigData,
            @RequestParam(required = false) String machineLearning,
            @RequestParam(required = false) String terminal,
            @RequestParam(required = false) String myNo,
            @RequestParam Long userId,
            @RequestParam(required = false) String instanceName,
            @RequestParam(required = false) String schoolName,
            @RequestParam(required = false) String devDate,
            @RequestParam(required = false) String requirement,
            @RequestParam(required = false, defaultValue = "0") Integer status,
            @RequestParam(required = false) MultipartFile file) throws IOException {

        Order order = new Order();
        order.setOrderNo(orderService.generateOrderNo());
        order.setTitle(title);
        order.setLanguage(language);
        order.setDatabase(database);
        order.setFrontend(frontend);
        order.setFrontendFramework(frontendFramework);
        order.setCrawler(crawler);
        order.setBigData(bigData);
        order.setMachineLearning(machineLearning);
        order.setTerminal(terminal);
        order.setMyNo(myNo);
        order.setUserId(userId);
        order.setInstanceName(instanceName);
        order.setSchoolName(schoolName);
        if (devDate != null && !devDate.isEmpty()) {
            order.setDevDate(LocalDate.parse(devDate));
        }
        order.setRequirement(requirement);
        order.setStatus(status);
        order.setSettled(0);

        if (file != null && !file.isEmpty()) {
            Path dir = Paths.get(uploadPath).toAbsolutePath();
            Files.createDirectories(dir);
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path targetPath = dir.resolve(fileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            order.setAttachmentPath(fileName);
            order.setAttachmentName(file.getOriginalFilename());
            log.info("订单附件上传: fileName={}", fileName);
        }

        orderService.save(order);
        log.info("订单提交成功: orderNo={}, userId={}, title={}, status={}", order.getOrderNo(), userId, title, status);
        return Result.success(order);
    }

    @GetMapping("/list")
    public Result<Page<Order>> listOrders(OrderQueryDTO dto) {
        return Result.success(orderService.queryOrders(dto));
    }

    @GetMapping("/{id}")
    public Result<Order> getOrder(@PathVariable Long id) {
        return Result.success(orderService.getById(id));
    }


    @PutMapping("/update")
    public Result<Boolean> updateOrder(@RequestBody Order order) {
        log.info("更新订单: orderId={}", order.getId());
        return Result.success(orderService.updateById(order));
    }

    @PostMapping("/updateWithFile")
    public Result<Boolean> updateOrderWithFile(
            @RequestParam Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String requirement,
            @RequestParam(required = false) String myNo,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String database,
            @RequestParam(required = false) String frontend,
            @RequestParam(required = false) String frontendFramework,
            @RequestParam(required = false) String terminal,
            @RequestParam(required = false) String devDate,
            @RequestParam(required = false) MultipartFile file) throws IOException {
        
        Order order = orderService.getById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        
        order.setTitle(title);
        order.setRequirement(requirement);
        order.setMyNo(myNo);
        order.setLanguage(language);
        order.setDatabase(database);
        order.setFrontend(frontend);
        order.setFrontendFramework(frontendFramework);
        order.setTerminal(terminal);
        if (devDate != null && !devDate.isEmpty()) {
            order.setDevDate(LocalDate.parse(devDate));
        }
        
        if (file != null && !file.isEmpty()) {
            Path dir = Paths.get(uploadPath).toAbsolutePath();
            Files.createDirectories(dir);
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path targetPath = dir.resolve(fileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            order.setAttachmentPath(fileName);
            order.setAttachmentName(file.getOriginalFilename());
            log.info("订单附件更新: orderId={}, fileName={}", id, fileName);
        }
        
        log.info("订单编辑保存: orderId={}, orderNo={}", id, order.getOrderNo());
        return Result.success(orderService.updateById(order));
    }

    @PostMapping("/uploadModify")
    public Result<Boolean> uploadModify(
            @RequestParam Long id,
            @RequestParam MultipartFile file) throws IOException {
        
        Order order = orderService.getById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        
        Path dir = Paths.get(uploadPath).toAbsolutePath();
        Files.createDirectories(dir);
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path targetPath = dir.resolve(fileName);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        order.setAttachmentPath(fileName);
        order.setAttachmentName(file.getOriginalFilename());
        
        log.info("上传修改文件: orderId={}, orderNo={}, fileName={}", id, order.getOrderNo(), fileName);
        return Result.success(orderService.updateById(order));
    }

    @PostMapping("/status/{id}")
    public Result<Boolean> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        return Result.success(orderService.updateStatus(id, params.get("status")));
    }

    @PostMapping("/price/{id}")
    public Result<Boolean> updatePrice(@PathVariable Long id, @RequestBody Map<String, BigDecimal> params) {
        return Result.success(orderService.updatePrice(id, params.get("price")));
    }

    @PostMapping("/settle/{id}")
    public Result<Boolean> updateSettled(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        return Result.success(orderService.updateSettled(id, params.get("settled")));
    }

    @GetMapping("/balance/{userId}")
    public Result<BigDecimal> getBalance(@PathVariable Long userId) {
        return Result.success(orderService.getBalance(userId));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteOrder(@PathVariable Long id) {
        log.info("删除订单: orderId={}", id);
        return Result.success(orderService.removeById(id));
    }
}
