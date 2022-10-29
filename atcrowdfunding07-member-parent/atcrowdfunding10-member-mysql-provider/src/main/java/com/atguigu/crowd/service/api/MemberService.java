package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.po.MemberPO;

/**
 * @author linzihao
 * @create 2022-10-18-12:08
 */
public interface MemberService {
    MemberPO getMemberPOByLoginAcct(String loginAcct);

    void saveMemberPo(MemberPO memberPO);
}
