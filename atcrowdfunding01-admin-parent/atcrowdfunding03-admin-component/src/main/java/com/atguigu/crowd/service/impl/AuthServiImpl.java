package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.Auth;
import com.atguigu.crowd.entity.AuthExample;
import com.atguigu.crowd.mapper.AuthMapper;
import com.atguigu.crowd.service.api.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author linzihao
 * @create 2022-10-12-14:50
 */
@Service
public class AuthServiImpl implements AuthService {

    @Autowired
    private AuthMapper authMapper;

    @Override
    public List<Auth> getAll() {
        return authMapper.selectByExample(new AuthExample());
    }

    @Override
    public List<Integer> getAssignedAuthIdByRoleId(Integer roleId) {
        return authMapper.selectAssignedAuthIdByRoleId(roleId);
    }

    @Override
    public void saveRoleAuthRelationship(Map<String, List<Integer>> map) {

        //1.获取roleId的值
        List<Integer> roleIdList = map.get("roleId");
        Integer integer = roleIdList.get(0);

        //2.删除旧的关联数据
        authMapper.deleteOldRelationship(integer);

        //3.获取authidlist
        List<Integer> authIdArray = map.get("authIdArray");

        //4.判断authidlist是否有效
        if (authIdArray != null && authIdArray.size() > 0){
            authMapper.insertNewRelationship(integer,authIdArray);
        }
    }

    @Override
    public List<String> getAssignedAuthNameByAdminId(Integer adminId) {
        return authMapper.selectAssignedAuthNameByAdminId(adminId);
    }
}
