package com.lianghd.myblog.web;

import com.lianghd.myblog.NotFoundException;
import com.lianghd.myblog.service.BlogService;
import com.lianghd.myblog.service.TagService;
import com.lianghd.myblog.service.TypeService;
import com.lianghd.myblog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    // 请求URL接受参数需要加 PathVariable
    @GetMapping("/")
    public String index(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC)Pageable pageable,
                        Model model){

        model.addAttribute("page",blogService.listBlogs(pageable));
        model.addAttribute("types",typeService.listTypeTop(5));
        model.addAttribute("tags",tagService.listTagTop(10));
        model.addAttribute("recommendBlogs",blogService.listRecommendBlogTop(5));

        return "index";
    }

    // 全局查询 title或content包含
    @PostMapping("/search")
    public String search(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC)Pageable pageable,
                         @RequestParam String search_info, Model model){
        // select * from t_blog where title like '%内容%'
        model.addAttribute("page",blogService.searchBlog("%"+search_info+"%", pageable));
        model.addAttribute("search_info",search_info);
        return "search";
    }


    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model){
        model.addAttribute("blog",blogService.getAndConvert(id));
        return "blog";
    }

    // 底部菜单最新博客
    @GetMapping("/footer/newblog")
    public String newblogs(Model model){
        model.addAttribute("newblogs",blogService.listRecommendBlogTop(3));
        return "_fragments :: newblogList";
    }
}
