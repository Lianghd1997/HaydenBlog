package com.lianghd.myblog.po;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "t_tag")
public class Tag {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    // 实体间关系
    @ManyToMany(mappedBy = "tags") //Blog和Tag 多对多
    private List<Blog> blogs = new ArrayList<>();

    public Tag() {
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
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
