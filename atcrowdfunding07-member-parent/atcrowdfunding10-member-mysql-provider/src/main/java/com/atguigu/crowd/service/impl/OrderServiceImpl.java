package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.po.AddressPO;
import com.atguigu.crowd.entity.po.AddressPOExample;
import com.atguigu.crowd.entity.po.OrderPO;
import com.atguigu.crowd.entity.po.OrderProjectPO;
import com.atguigu.crowd.entity.vo.AddressVO;
import com.atguigu.crowd.entity.vo.OrderProjectVO;
import com.atguigu.crowd.entity.vo.OrderVO;
import com.atguigu.crowd.mapper.AddressPOMapper;
import com.atguigu.crowd.mapper.OrderPOMapper;
import com.atguigu.crowd.mapper.OrderProjectPOMapper;
import com.atguigu.crowd.service.api.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linzihao
 * @create 2022-10-26-14:28
 */
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderProjectPOMapper orderProjectPOMapper;

    @Autowired
    private OrderPOMapper orderPOMapper;

    @Autowired
    private AddressPOMapper addressPOMapper;

    Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId) {
        return orderProjectPOMapper.selectOrderProjectVO(returnId);
    }

    @Override
    public List<AddressVO> getAddressVOList(Integer memberId) {

        AddressPOExample addressPOExample = new AddressPOExample();
        addressPOExample.createCriteria().andMemberIdEqualTo(memberId);

        List<AddressPO> addressPOList = addressPOMapper.selectByExample(addressPOExample);

        List<AddressVO> addressVOList = new ArrayList<>();

        for (AddressPO addressPO :
                addressPOList) {
            AddressVO addressVO = new AddressVO();
            BeanUtils.copyProperties(addressPO,addressVO);
            addressVOList.add(addressVO);
        }

        return addressVOList;

    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveAddress(AddressVO addressVO) {

        AddressPO addressPO = new AddressPO();

        BeanUtils.copyProperties(addressVO,addressPO);

        addressPOMapper.insert(addressPO);

    }
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveOrder(OrderVO orderVO) {

        //保存OrderVO
        OrderPO orderPO = new OrderPO();
        BeanUtils.copyProperties(orderVO,orderPO);

        //保存OrderprojectPO
        OrderProjectPO orderProjectPO = new OrderProjectPO();
        BeanUtils.copyProperties(orderVO.getOrderProjectVO(),orderProjectPO);

        orderPOMapper.insert(orderPO);

        //保存完orderPO之后获取自增的orderid用于保存orderProjectPO
        //这个自动生成的主键是保存orderProjectVO要用到的外键
        Integer orderId = orderPO.getId();
        orderProjectPO.setOrderId(orderId);

        orderProjectPOMapper.insert(orderProjectPO);

    }
}
