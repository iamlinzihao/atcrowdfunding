package com.atguigu.crowd.handler;

import com.atguigu.crowd.api.MySQLRemoteService;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.vo.AddressVO;
import com.atguigu.crowd.entity.vo.MemberLoginVO;
import com.atguigu.crowd.entity.vo.OrderProjectVO;
import com.atguigu.crowd.util.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author linzihao
 * @create 2022-10-26-12:23
 */
@Controller
public class OrderHandler {

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    private Logger logger = LoggerFactory.getLogger(OrderHandler.class);

    @RequestMapping("/save/address")
    public String saveAddress(AddressVO addressVO, HttpSession session){

        //1.执行信息的保存
        ResultEntity<String> resultEntity = mySQLRemoteService.saveAddressRemote(addressVO);

        logger.debug("" + resultEntity.getResult());

        //2.从session中获取returnCount
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");
        Integer returnCount = orderProjectVO.getReturnCount();

        //3.重定向到订单确认页面（也就是本页）
        return "redirect:http://localhost/order/confirm/order/" + returnCount;

    }

    @RequestMapping("/confirm/order/{returnCount}")
    public String showConfirmOrderInfo(
            @PathVariable("returnCount") Integer returnCount,
            HttpSession session){

        //1.把接收到的回报数量存到session域中
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");
        orderProjectVO.setReturnCount(returnCount);
        session.setAttribute("orderProjectVO",orderProjectVO);

        //2.获取当前已登录的用户的id
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        Integer memberId = memberLoginVO.getId();

        //3.查询目前用户的收货地址
        ResultEntity<List<AddressVO>> resultEntity = mySQLRemoteService.getAddressVORemote(memberId);

        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())){
            List<AddressVO> addressVOList = resultEntity.getData();
            session.setAttribute("addressVOList", addressVOList);
        }

        return "confirm_order";

    }

    @RequestMapping("/confirm/return/info/{projectId}/{returnId}")
    public String showReturnConfirmInfo(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("returnId") Integer returnId,
            HttpSession session){

        ResultEntity<OrderProjectVO> resultEntity =
                mySQLRemoteService.getOrderProjectVORemote(projectId,returnId);

        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())){

            OrderProjectVO orderProjectVO = resultEntity.getData();

            //为了保证在后续操作后保持orderProjectVO数据，存入session域中
            session.setAttribute("orderProjectVO",orderProjectVO);

        }

        return "confirm_return";

    }

}
