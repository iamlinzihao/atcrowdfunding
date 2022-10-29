package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.atguigu.crowd.constant.CrowdConstant.ATTR_NAME_EXCEPTION;

/**
 * @author linzihao
 * @create 2022-10-08-8:59
 */
//此注解表示当前类是一个基于注解的异常处理器类
@ControllerAdvice
public class CrowdExceptionResolver {
    //将一个具体的异常类型和方法关联起来

    @ExceptionHandler(value = Exception.class)
    public ModelAndView Exception(
            Exception exception,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String viewName = "admin-login";
        return commonResolve(viewName,exception,request,response);
    }

    @ExceptionHandler(value = LoginAcctAlreadyInUseForUpdateException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseForUpdateException(
            LoginAcctAlreadyInUseForUpdateException exception,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String viewName = "system-error";
        return commonResolve(viewName,exception,request,response);
    }

    @ExceptionHandler(value = LoginAcctAlreadyInUseException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseException(
            LoginAcctAlreadyInUseException exception,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String viewName = "admin-add";
        return commonResolve(viewName,exception,request,response);
    }

    @ExceptionHandler(value = LoginFailedException.class)
    public ModelAndView resolveLoginFailedException(
            LoginFailedException exception,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String viewName = "admin-login";
        return commonResolve(viewName,exception,request,response);
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ModelAndView resolveNullPointerException(
            NullPointerException exception,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String viewName = "system-error";
        return commonResolve(viewName,exception,request,response);
    }


    @ExceptionHandler(value = ArithmeticException.class)
    public ModelAndView resolveMathExcetion(
            ArithmeticException exception,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        String viewName = "system-error";
        return commonResolve(viewName,exception,request,response);
    }

    private ModelAndView commonResolve(
            //要去到的视图名称
            String viewName,
            //实际捕获到的异常
            Exception exception ,
            //当前请求对象
            HttpServletRequest request,
            //当前响应对象
            HttpServletResponse response) throws IOException {
        boolean judgeRequestType = CrowdUtil.judgeRequestType(request);
        //2.如果是Ajax请求
        if (judgeRequestType){

            //3.创建ResultEntity对象
            ResultEntity<Object> entity = ResultEntity.failed(exception.getMessage());

            //4.创建gson
            Gson gson = new Gson();

            //5.转换json为字符串
            String json = gson.toJson(entity);

            //6.将json字符串作为响应体返回给浏览器
            response.getWriter().write(json);

            return null;
        }

        //7.创建modelandview
        ModelAndView modelAndView = new ModelAndView();

        //8.将exception存入modelandview
        modelAndView.addObject(ATTR_NAME_EXCEPTION,exception);

        //9.设置对应的视图名称
        modelAndView.setViewName(viewName);

        //10.返回modelandview
        return modelAndView;
    }
}
