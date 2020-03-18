package com.lianghd.myblog.web.admin;

import com.lianghd.myblog.po.Type;
import com.lianghd.myblog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    // 前端页面构造的参数自动封装到pageable
    // 分页查询
    @GetMapping("/types")
    public String list(@PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.DESC)
                                   Pageable pageable, Model model){
        model.addAttribute("page",typeService.listType(pageable));
        return "admin/types";
    }

    @GetMapping("/types/input")
    public String input(Model model){
        model.addAttribute("type", new Type());
        return "admin/types-input";
    }

    // 分类新增及验证
    @PostMapping("/types")
    public String post(@Valid Type type, BindingResult result,
                       RedirectAttributes attributes){

        Type type1 = typeService.getTypeByName(type.getName());
        if (type1 != null){
            result.rejectValue("name","nameError","该分类已存在");
        }

        // 后端表单验证
        if(result.hasErrors()){
            return "admin/types-input";
        }

        Type t = typeService.saveType(type);
        if(t == null){
            attributes.addFlashAttribute("message","新增失败");
        }else{
            attributes.addFlashAttribute("message","新增成功");
        }
        return "redirect:/admin/types";
    }

    // 分类修改及验证
    @GetMapping("/types/{id}/input")
    public String editinput(@PathVariable Long id, Model model){
        model.addAttribute("type",typeService.getType(id));
        return "admin/types-input";
    }

    // 分类更新
    @PostMapping("/types/{id}")
    public String editpost(@Valid Type type, BindingResult result,
                           @PathVariable Long id, RedirectAttributes attributes){
        // 校验
        Type type1 = typeService.getTypeByName(type.getName());
        if (type1 != null){
            result.rejectValue("name","nameError","该分类已存在");
        }

        // 后端表单验证
        if(result.hasErrors()){
            return "admin/types-input";
        }

        Type t = typeService.updateType(id, type);
        if(t == null){
            attributes.addFlashAttribute("message","更新失败");
        }else{
            attributes.addFlashAttribute("message","更新成功");
        }
        return "redirect:/admin/types";
    }

    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        typeService.deleteType(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/types";
    }
}
