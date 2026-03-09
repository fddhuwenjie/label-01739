package com.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.order.entity.User;
import com.order.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    public User findByInstanceAndSchool(String instanceName, String schoolName) {
        return getOne(new LambdaQueryWrapper<User>()
                .eq(User::getInstanceName, instanceName)
                .eq(User::getSchoolName, schoolName));
    }

    public User login(String instanceName, String schoolName, String password) {
        User user = findByInstanceAndSchool(instanceName, schoolName);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public User register(String instanceName, String schoolName, String password) {
        User existing = findByInstanceAndSchool(instanceName, schoolName);
        if (existing != null) {
            return null;
        }
        User user = new User();
        user.setInstanceName(instanceName);
        user.setSchoolName(schoolName);
        user.setPassword(password);
        user.setRole(0);
        save(user);
        return user;
    }
}
