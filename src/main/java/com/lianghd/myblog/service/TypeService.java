package com.lianghd.myblog.service;

import com.lianghd.myblog.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {


    Type saveType(Type type);                   // 新增

    Type getType(Long id);                      // 获取单个

    Type getTypeByName(String name);            // 判断新增分类是否重复

    Page<Type> listType(Pageable pageable);     // 获取多个

    Type updateType(Long id, Type type);        // 修改

    void deleteType(Long id);                   // 删除

    List<Type> listType();

    // 首页Type的Top展示
    List<Type> listTypeTop(Integer size);
}
