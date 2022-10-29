package com.atguigu.crowd.handler;

import com.atguigu.crowd.api.MySQLRemoteService;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.vo.PortalTypeVO;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author linzihao
 * @create 2022-10-20-16:26
 */
@Controller
public class PortalHandler {

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/")
    public String showPortalPage(Model model){

        //1.调用远程方法获取首页要显示的数据
        ResultEntity<List<PortalTypeVO>> portalTypeVOEntity = mySQLRemoteService.getPortalTypeProjectDataRemote();

        //2.判断是否为空
        String result = portalTypeVOEntity.getResult();
        if(ResultEntity.SUCCESS.equals(result)){

            //3.获取查询数据
            List<PortalTypeVO> portalTypeVOList = portalTypeVOEntity.getData();

            //4.存入模型
            model.addAttribute(CrowdConstant.ATTR_NAME_PORTAL_DATA,portalTypeVOList);

        }
        return "portal";

    }

}
