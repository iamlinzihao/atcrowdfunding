package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.service.api.AdminService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * @author linzihao
 * @create 2022-10-08-18:51
 */
@Controller
public class AdminHandler {

    @Autowired
    private AdminService adminService;

    @RequestMapping("/admin/update.html")
    public String update(Admin admin,
                         @RequestParam("pageNum") Integer pageNum,
                         @RequestParam("keyword") String keyword
                         ){

        adminService.update(admin);

        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;

    }

    @RequestMapping("/admin/to/edit/page.html")
    public String toEditPage(
            @RequestParam("adminId") Integer adminId,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("keyword") Integer keyword,
            ModelMap modelMap
    ){

        //1.根据adminId查询Admin对象
        Admin admin = adminService.getAdminById(adminId);

        //2.将admin对象存入模型
        modelMap.addAttribute("admin",admin);

        return "admin-edit";
    }

    @PreAuthorize("hasAuthority('user:save')")
    @RequestMapping("/admin/save.html")
    public String save(Admin admin){

        adminService.saveAdmin(admin);

        return "redirect:/admin/get/page.html?pageNum="+Integer.MAX_VALUE;
    }

    @RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public String remove(
            @PathVariable("adminId") Integer adminId,
            @PathVariable("pageNum") Integer pageNum,
            @PathVariable("keyword") String keyword
    ){

        //执行删除
        adminService.remove(adminId);

        //页面跳转：回到分页页面
        //1.直接跳转，转发到adminpage无法显示数据
        // return "admin-page";

        //2.转发到admin/get/page.html，一旦刷新重复删除操作浪费性能
        // return "forward:/admin/get/page.html";

        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
    }

    @RequestMapping("/admin/get/page.html")
    public String getPageInfo(

            //使用@RequestParam注解default属性，指定默认值，请求中没有携带参数是使用默认值
            //默认值为空字符串，实现两种情况适配
            @RequestParam(value = "keyword",defaultValue = "") String keyword,

            //pagenum默认值为“1”
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,

            //pageSize默认值为“5”
            @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize,
            ModelMap modelMap

    ){

        //调用Service方法获取PageInfo
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);

        //将查的得PageInfo对象存入模型
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO,pageInfo);

        return "admin-page";
    }

    @RequestMapping("/admin/do/logout.html")
    public String doLogout(HttpSession session){

        //强制session失效
        session.invalidate();
        return "redirect:/admin/to/login/page.html";
    }

    @RequestMapping("/admin/do/login.html")
    public String doLogin(
            @RequestParam("loginAcct") String loginAcct,
            @RequestParam("userPswd") String userPswd,
            HttpSession session
    ){
        //调用Service方法执行检查登录
        //这个方法如果返回admin则表示登陆成功，否则抛异常
        Admin admin = adminService.getAdminByLoginAcct(loginAcct,userPswd);

        //将登陆成功的admin存入session域
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN,admin);
        return "redirect:/admin/to/main/page.html";
    }


}
