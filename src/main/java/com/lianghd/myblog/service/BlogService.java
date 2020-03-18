package com.lianghd.myblog.service;

import com.lianghd.myblog.po.Blog;
import com.lianghd.myblog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {

    Blog saveBlog(Blog blog);

    Blog getBlog(Long id);

    // 前端展示
    Blog getAndConvert(Long id);

    // 添加查询参数（标签 分类 查询） Blog (查询条件封装)
    Page<Blog> listBlog(Pageable pageable, BlogQuery blogQuery);

    // 首页分页展示，无查询条件
    Page<Blog> listBlogs(Pageable pageable);

    Blog updateBlog(Blog blog, Long id);

    void deleteBlog(Long id);

    // 首页最新推荐
    List<Blog> listRecommendBlogTop(Integer size);

    // 全局查询
    Page<Blog> searchBlog(String search_info, Pageable pageable);

    // TagShow
    Page<Blog> listBlogByTag(Pageable pageable,Long tagId);

    // 归档（数据库查询数据到 Map集合）
    Map<String,List<Blog>> archivesBlog();

    Long countBlog();
}
