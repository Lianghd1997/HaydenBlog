package com.lianghd.myblog.service;

import com.lianghd.myblog.NotFoundException;
import com.lianghd.myblog.dao.BlogRepository;
import com.lianghd.myblog.po.Blog;
import com.lianghd.myblog.po.Type;
import com.lianghd.myblog.util.MarkdownUtils;
import com.lianghd.myblog.util.MyBeanUtils;
import com.lianghd.myblog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        // 新增的 Blog id为空
        if (blog.getId() == null){
            // 时间，浏览次数初始化
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        } else{
            blog.setUpdateTime(new Date());
        }

        return blogRepository.save(blog);
    }

    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getOne(id);
    }

    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.getOne(id);
        if (blog == null){
            throw new NotFoundException("该博文不存在");
        }
        // 避免改变源数据库markdown文本
        Blog blog1 = new Blog();
        BeanUtils.copyProperties(blog,blog1);
        String content = blog1.getContent();
        blog1.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        // 浏览次数统计
        blogRepository.updateViews(id);
        return blog1;
    }

    @Transactional
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blogQuery) {

        // 标签 分类 是否推荐
        // 动态查询
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicates = new ArrayList<>();
                if (blogQuery.getTitle() != null && !"".equals(blogQuery.getTitle())){
                    predicates.add(criteriaBuilder.like(root.<String>get("title"), "%"+blogQuery.getTitle()+"%"));
                }
                if (blogQuery.getTypeId() != null){
                    predicates.add(criteriaBuilder.equal(root.<Type>get("type").get("id"),blogQuery.getTypeId()));
                }
                if (blogQuery.isRecommend()){
                    predicates.add(criteriaBuilder.equal(root.<Boolean>get("recommend"),blogQuery.isRecommend()));
                }
                // list转为数组array
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlogs(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Blog updateBlog(Blog blog, Long id) {
        Blog b = blogRepository.getOne(id);
        if (b == null){
            throw new NotFoundException("该博客不存在");
        }
        // 传递空属性值避免赋值
        BeanUtils.copyProperties(blog,b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"updateTime");
        Pageable pageable = PageRequest.of(0,size,sort);
        return blogRepository.findTop(pageable);
    }

    @Override
    public Page<Blog> searchBlog(String search_info, Pageable pageable) {
        return blogRepository.findBySearch(search_info, pageable);
    }

    @Override
    public Page<Blog> listBlogByTag(Pageable pageable, Long tagId) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                // 查表t_blog_tags
                Join join = root.join("tags");
                return criteriaBuilder.equal(join.get("id"),tagId);
            }
        },pageable);
    }

    @Override
    public Map<String, List<Blog>> archivesBlog() {
        List<String> years = blogRepository.findGroupYear();

        Map<String,List<Blog>> map = new HashMap<>();  // 分配内存
        for (String year : years){
            map.put(year,blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }
}
