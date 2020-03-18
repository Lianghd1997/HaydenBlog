package com.lianghd.myblog.service;

import com.lianghd.myblog.dao.UserRepository;
import com.lianghd.myblog.po.User;
import com.lianghd.myblog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// 接口实现
@Service
public class UserServiceImpl implements UserService {

    @Autowired  // 注入资源
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {
        User user = userRepository.findByUsernameAndAndPassword(username,
                MD5Utils.code(password));
        return user;
    }
}
