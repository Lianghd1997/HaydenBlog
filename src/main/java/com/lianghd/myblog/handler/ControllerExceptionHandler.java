package com.lianghd.myblog.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

// 拦截异常
@ControllerAdvice   //拦截注解为@Controller的控制器
public class ControllerExceptionHandler {
    // 日志记录
    private final Logger logger =
            LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)  //级别为Exception异常都被拦截
    public ModelAndView exceptionHandler(HttpServletRequest request,Exception e) throws Exception {
        logger.error("Request URL: {}, Exception: {}", request.getRequestURL(),e.getMessage());

        // 根据状态码判断是否拦截(是否指定状态)
        if(AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null){
            throw e;
        }

        // 获取URL和异常信息Exception返回到error.html上
        ModelAndView mv = new ModelAndView();
        mv.addObject("url",request.getRequestURL());
        mv.addObject("exception",e);
        mv.setViewName("error/error");
        return mv;
    };
}
