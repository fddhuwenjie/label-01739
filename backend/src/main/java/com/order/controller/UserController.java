package com.order.controller;

import cn.hutool.crypto.SecureUtil;
import com.order.common.Result;
import com.order.entity.User;
import com.order.service.UserService;
import com.order.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String instanceName = params.get("instanceName");
        String schoolName = params.get("schoolName");
        String password = params.get("password");
        // 前端传来的是MD5加密后的密码
        User user = userService.login(instanceName, schoolName, password);
        if (user != null) {
            String token = JwtUtil.generateToken(user.getId(), user.getInstanceName(), user.getRole());
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("id", user.getId());
            result.put("instanceName", user.getInstanceName());
            result.put("schoolName", user.getSchoolName());
            result.put("role", user.getRole());
            return Result.success(result);
        }
        return Result.error("用户名或密码错误");
    }

    @PostMapping("/register")
    public Result<Map<String, Object>> register(@RequestBody Map<String, String> params) {
        String instanceName = params.get("instanceName");
        String schoolName = params.get("schoolName");
        String password = params.get("password");
        // 前端传来的是MD5加密后的密码
        User user = userService.register(instanceName, schoolName, password);
        if (user != null) {
            String token = JwtUtil.generateToken(user.getId(), user.getInstanceName(), user.getRole());
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("id", user.getId());
            result.put("instanceName", user.getInstanceName());
            result.put("schoolName", user.getSchoolName());
            result.put("role", user.getRole());
            return Result.success(result);
        }
        return Result.error("该用户已存在");
    }

    @GetMapping("/find")
    public Result<User> findUser(@RequestParam String instanceName, @RequestParam String schoolName) {
        User user = userService.findByInstanceAndSchool(instanceName, schoolName);
        if (user != null) {
            user.setPassword(null);
            return Result.success(user);
        }
        return Result.error("用户不存在");
    }
}
