package com.lianghd.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutmeShowController {

    @GetMapping("/aboutme")
    private String aboutme(){
        return "aboutme";
    }
}
