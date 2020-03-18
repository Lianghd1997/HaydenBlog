package com.lianghd.myblog.service;

import com.lianghd.myblog.po.User;

public interface UserService {

    // 用户登录 检查用户名和密码
    User checkUser(String username, String password);

}
