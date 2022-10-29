package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.po.MemberPOExample;
import com.atguigu.crowd.mapper.MemberPOMapper;
import com.atguigu.crowd.service.api.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author linzihao
 * @create 2022-10-18-12:09
 */
@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberPOMapper memberPOMapper;

    @Override
    public MemberPO getMemberPOByLoginAcct(String loginAcct) {

        MemberPOExample memberPOExample = new MemberPOExample();

        MemberPOExample.Criteria criteria = memberPOExample.createCriteria();

        criteria.andLoginacctEqualTo(loginAcct);

        List<MemberPO> memberPOS = memberPOMapper.selectByExample(memberPOExample);

        if (memberPOS == null || memberPOS.size() == 0){

            return null;

        }

        MemberPO memberPO = memberPOS.get(0);

        return memberPO;
    }

    @Transactional(
            propagation = Propagation.REQUIRES_NEW ,
            rollbackFor = Exception.class ,
            readOnly = false)
    @Override
    public void saveMemberPo(MemberPO memberPO) {

        memberPOMapper.insertSelective(memberPO);

    }
}
