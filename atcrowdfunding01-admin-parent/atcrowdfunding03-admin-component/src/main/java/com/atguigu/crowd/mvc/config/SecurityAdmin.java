package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

/**
 * 考虑到user对象仅仅包含账号密码，为了能获取原始的admin对象创建这个类对user进行扩展
 * @author linzihao
 * @create 2022-10-14-9:19
 */
public class SecurityAdmin extends User{

    Admin originalAdmin;

    public SecurityAdmin(

            //1.传入原始得admin
            Admin originalAdmin,

            //2.创建用户角色权限集合
            List<GrantedAuthority> authorities){

        //调用父类构造器
        super(originalAdmin.getLoginAcct(),originalAdmin.getUserPswd(),authorities);

        //给本类originaladmin赋值
        this.originalAdmin = originalAdmin;

        //将原始的admin对象中的密码擦除
        this.originalAdmin.setUserPswd(null);
    }

    //对外提供get方法
    public Admin getOriginalAdmin(){
        return originalAdmin;
    }

}
