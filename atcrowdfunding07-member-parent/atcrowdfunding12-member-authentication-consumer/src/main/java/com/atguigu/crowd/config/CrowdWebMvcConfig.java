package com.atguigu.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author linzihao
 * @create 2022-10-20-22:34
 */
@Configuration
public class CrowdWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        //浏览器访问地址
        String urlPath = "/auth/member/to/reg/page";

        //目标试图的名称，用来拼接前后缀
        String viewName = "member-reg";

        //添加viewController
        registry.addViewController("/auth/member/to/reg/page").setViewName("member-reg");
        registry.addViewController("/auth/member/to/login/page").setViewName("member-login");
        registry.addViewController("/auth/member/to/center/page").setViewName("member-center");
        registry.addViewController("/member/my/crowd").setViewName("member-crowd");

    }
}
