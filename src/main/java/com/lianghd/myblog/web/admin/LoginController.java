package com.lianghd.myblog.web.admin;

import com.lianghd.myblog.po.User;
import com.lianghd.myblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

// 登录的web控制器
@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping     //get方法请求时访问login页面
    public String loginPage(){
        return "admin/login";
    }

    // 登录及验证
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes){
        User user = userService.checkUser(username,password);
        if(user != null){
            user.setPassword(null);     // 密码设置为空
            session.setAttribute("user",user);
            return "admin/index";
        }else {
            attributes.addFlashAttribute("message","用户名或密码错误");
            // 重定向路径
            return "redirect:/admin";
        }
    }

    // 登出并清空信息
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");    // 清空
        return "redirect:/admin";
    }
}
