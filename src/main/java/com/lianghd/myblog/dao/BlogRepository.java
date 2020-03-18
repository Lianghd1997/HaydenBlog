package com.lianghd.myblog.dao;

import com.lianghd.myblog.po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// JpaSpecificationExecutor 组合查询接口
public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

    @Query("select b from t_blog b where b.recommend = true")
    List<Blog> findTop(Pageable pageable);

    // like查询第一个参数？1（search_info）
    @Query("select b from t_blog b where b.title like ?1 or b.content like ?1")
    Page<Blog> findBySearch(String search_info,Pageable pageable);

    // 浏览次数统计
    @Transactional
    @Modifying
    @Query("update t_blog b set b.views = b.views + 1 where b.id = ?1")
    int updateViews(Long id);

    // 年份查询
    @Query("select function('date_format',b.createTime,'%Y') as year from t_blog b group by function('date_format',b.createTime,'%Y') order by function('date_format',b.createTime,'%Y') desc ")
    List<String> findGroupYear();

    // 相应年份博客
    @Query("select b from t_blog b where function('date_format',b.createTime,'%Y') = ?1")
    List<Blog> findByYear(String year);

}
