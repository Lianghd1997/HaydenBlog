package com.lianghd.myblog.dao;

import com.lianghd.myblog.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

// 使用 JPA对数据库操作 （对象+主键类型）
public interface UserRepository extends JpaRepository<User,Long> {

    User findByUsernameAndAndPassword(String username, String password);

}
