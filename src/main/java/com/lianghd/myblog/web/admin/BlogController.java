package com.lianghd.myblog.web.admin;

import com.lianghd.myblog.po.Blog;
import com.lianghd.myblog.po.Tag;
import com.lianghd.myblog.po.User;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT="admin/blogs-input";
    private static final String LIST="admin/blogs";
    private static final String REDIRECT_LIST="redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    // 初始化 Type 和 Tag
    private void setTypeAndTag(Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
    }


    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 3, sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blogQuery, Model model){
        model.addAttribute("page", blogService.listBlog(pageable,blogQuery));

        // 初始化分类
        model.addAttribute("types", typeService.listType());

        return LIST;
    }

    // 查询返回只更新表格，不更新整个blogs页面
    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 3, sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blogQuery, Model model){
        model.addAttribute("page", blogService.listBlog(pageable,blogQuery));
        // 返回blogs页面里的blogList片段
        return "admin/blogs :: blogList";
    }

    @GetMapping("/blogs/input")
    public String input(Model model){
        // 初始化标签 和 分类
        setTypeAndTag(model);

        // blog-input 新增和修改共用导致点击“新增”时 无type值传入

        model.addAttribute("blog", new Blog());
        return INPUT;
    }

    // 新增
    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes,
                       HttpSession session){

        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));   //typeId
        blog.setTags(tagService.listTags(blog.getTagIds()));  //tagIds

        Blog b;
        // 避免修改重置createTime
        if (blog.getId() == null){
            b = blogService.saveBlog(blog);
        } else{
            b = blogService.updateBlog(blog,blog.getId());
        }

        if (b == null){
            attributes.addFlashAttribute("message","操作失败");
        }else{
            attributes.addFlashAttribute("message","操作成功");
        }
        return REDIRECT_LIST;
    }

    // 修改
    @GetMapping("/blogs/{id}/input")
    public String editinput(@PathVariable Long id, Model model){
        // 初始化标签 和 分类
        model.addAttribute("tags",tagService.listTag());
        model.addAttribute("types", typeService.listType());

        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);
        return INPUT;
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message","删除成功");
        return REDIRECT_LIST;
    }
}
