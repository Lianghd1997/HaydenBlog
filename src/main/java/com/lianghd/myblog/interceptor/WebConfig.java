package com.lianghd.myblog.interceptor;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 控制器过滤设置
@Configuration  // 有效的配置类
public class WebConfig implements WebMvcConfigurer {

    // 过滤掉admin/blogs-manage和admin/blogs-input
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin")
                .excludePathPatterns("/admin/login");

    }
}
