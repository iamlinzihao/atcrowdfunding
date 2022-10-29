package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author linzihao
 * @create 2022-10-06-23:29
 */
@Controller
public class TestHandler {
    private Logger logger = LoggerFactory.getLogger(TestHandler.class);

    @Autowired
    private AdminService adminService;

    @RequestMapping("/test/ssm.html")
    public String testSsm(ModelMap modelMap ,HttpServletRequest request){

        boolean judgeRequestType = CrowdUtil.judgeRequestType(request);

        logger.info("judgeRequestType=" + judgeRequestType);

        List<Admin> adminList = adminService.getAll();

        modelMap.addAttribute("adminList",adminList);

        // String a = null;
        //
        // System.out.println(a.length());
        System.out.println(10/0);


        return "target";
    }
    @ResponseBody
    @RequestMapping("/send/array1.do")
    public String testReceiveArrayOne(@RequestParam("array[]") List<Integer> array){
        // 接收参数时需要在参数名后面加[]
        for (Integer number : array){
            System.out.println(number);
        }
        return "success";
    }

    @ResponseBody
    @RequestMapping("send/array3.do")
    public String testReceiveArrayThree(@RequestBody List<Integer> array){
        for (Integer number : array) {
            logger.info("number"+number);
        }
        return "success";
    }

}
