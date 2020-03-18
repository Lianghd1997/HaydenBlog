package com.lianghd.myblog.po;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "t_type")
public class Type {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "请输入分类名称")
    private String name;

    // 实体间关系
    @OneToMany(mappedBy = "type")  //Blog和Type 多对一 One端关系被维护端
    private List<Blog> blogs = new ArrayList<>();

    public Type() {
    }

    /*getter and setter*/
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
