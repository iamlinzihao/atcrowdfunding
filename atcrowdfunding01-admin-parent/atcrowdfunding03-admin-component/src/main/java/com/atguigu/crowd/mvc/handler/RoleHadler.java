package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.RoleService;
import com.atguigu.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author linzihao
 * @create 2022-10-10-12:21
 */
@RestController
// @Controller
public class RoleHadler {

    @Autowired
    private RoleService roleService;

    //@ResponseBody
    @RequestMapping("/role/remove/by/role/id/array.json")
    public ResultEntity<String> removeByRoleIdArray(@RequestBody List<Integer> roleIdList){

        roleService.removeRole(roleIdList);

        return ResultEntity.successWithoutData();

    }

    //@ResponseBody
    @RequestMapping("/role/update.json")
    public ResultEntity<String> updateRole(Role role){

        roleService.updateRole(role);

        return ResultEntity.successWithoutData();
    }

    //@ResponseBody
    @RequestMapping("/role/save.json")
    public ResultEntity<String> saveRole(Role role){

        roleService.saveRole(role);

        return ResultEntity.successWithoutData();
    }

    //@ResponseBody
    @PreAuthorize("hasRole('部长')")
    @RequestMapping("/role/get/page/info.json")
    public ResultEntity<PageInfo<Role>> getPageInfo(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "keyword", defaultValue = "") String keyword) {

        // 调用service获取分页数据
        PageInfo<Role> pageInfo = roleService.getPageInfo(pageNum, pageSize, keyword);

        return ResultEntity.successWithData(pageInfo);
    }
}


