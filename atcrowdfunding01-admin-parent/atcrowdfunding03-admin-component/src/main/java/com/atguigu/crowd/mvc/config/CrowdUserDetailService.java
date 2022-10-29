package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.api.AuthService;
import com.atguigu.crowd.service.api.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linzihao
 * @create 2022-10-14-9:38
 */
@Component
public class CrowdUserDetailService implements UserDetailsService {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //1.根据账户名查询admin对象
        Admin admin = adminService.getAdminByLoginAcct(username);

        //2.获得adminId
        Integer id = admin.getId();

        //3.根据adminId查询角色信息
        List<Role> assignedRole = roleService.getAssignedRole(id);

        //4.根据adminId查询权限信息
        List<String> assignedAuthNameByAdminId = authService.getAssignedAuthNameByAdminId(id);

        //5.创建集合存储GrantedAuthority
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();

        //6.遍历角色集合
        for (Role role: assignedRole) {

            String roleName = "ROLE_" + role.getName();

            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(roleName);

            authorities.add(simpleGrantedAuthority);

        }

        //7.遍历权限集合存入信息
        for (String authName : assignedAuthNameByAdminId
             ) {

            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authName);

            authorities.add(simpleGrantedAuthority);

        }

        //8.封装SecurityAdmin
        SecurityAdmin securityAdmin = new SecurityAdmin(admin, authorities);

        return securityAdmin;
    }
}
