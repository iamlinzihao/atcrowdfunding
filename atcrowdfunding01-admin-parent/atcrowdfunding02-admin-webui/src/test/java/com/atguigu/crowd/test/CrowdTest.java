package com.atguigu.crowd.test;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.mapper.RoleMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.mapper.AdminMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author linzihao
 * @create 2022-10-06-8:35
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml","classpath:spring-persist-tx.xml"})
public class CrowdTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleMapper roleMapper;

    @Test
    public void testRoleSave(){

        for (int i = 0; i < 235; i++){

            roleMapper.insert(new Role(null,"role"+i));

        }
    }

    @Test
    public void testConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }

    @Test
    public void testInsertAdmin(){
        Admin admin = new Admin(null, "Rachel", "123456", "lin", "1275903512@qq.com", null);
        int i = adminMapper.insertSelective(admin);
        System.out.println("受影响行数" + i);
    }

    @Test
    public void testTx(){
        Admin admin = new Admin(null, "linzihao", "123456", "linzihao", "1275903512@qq.com", null);
        adminService.saveAdmin(admin);
    }

    @Test
    public void test(){
        for (int i = 0;i < 288; i++){
            adminMapper.insert(new Admin(null, "loginAcct" + i,"userPswd" + i,"userName" + i,"email" + i,null));
        }
    }

    @Test
    public void pswdEncoder(){

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        String s = bCryptPasswordEncoder.encode("123456");

        System.out.println(s);

    }

}
