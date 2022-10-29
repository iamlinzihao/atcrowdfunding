package com.atguigu.crowd.filter;

import com.atguigu.crowd.constant.AccessPassResources;
import com.atguigu.crowd.constant.CrowdConstant;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author linzihao
 * @create 2022-10-22-12:50
 */
@Component
public class CrowdAccessFilter extends ZuulFilter {

    @Override
    public boolean shouldFilter() {

        //1.获取RequestContext对象
        RequestContext currentContext = RequestContext.getCurrentContext();

        //2.获取Request对象（框架底层是通过TreadLocal从当前线程中事先绑定的Request对象）
        HttpServletRequest request = currentContext.getRequest();

        //3.获取ServletPath
        String servletPath = request.getServletPath();

        //4.判断当前servletPath是否对应可以直接放行的特定功能
        boolean containsResult = AccessPassResources.PASS_RES_SET.contains(servletPath);

        if (containsResult){
            //如果当前请求是可以直接放行的特定功能请求返回false
            return false;
        }

        //5.判断当前请求是否为静态资源
        return !AccessPassResources.judgeCurrentServletPathWetherStaticRecource(servletPath);
    }

    @Override
    public Object run() throws ZuulException {

        //1.获取当前请求
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();

        //2.获取当前请求的session对象
        HttpSession session = request.getSession();

        //3.尝试从session域中获取已登录对象
        Object loginMember = session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);

        if (loginMember == null){

            //4.将提示消息存入session
            session.setAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_ACCESS_FORBIDDEN);

            //5.获取response对象
            HttpServletResponse response = currentContext.getResponse();

            try {
                //6.重定向到登陆页面
                response.sendRedirect("/auth/member/to/login/page");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    @Override
    public String filterType() {
        //返回pre表示在目标微服务之前过滤
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }
}
