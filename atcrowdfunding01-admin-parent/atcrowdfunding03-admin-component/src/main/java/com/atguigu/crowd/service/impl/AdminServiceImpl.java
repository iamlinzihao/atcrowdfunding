package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.AdminExample;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.util.CrowdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author linzihao
 * @create 2022-10-06-20:43
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Admin getAdminByLoginAcct(String username) {

        AdminExample adminExample = new AdminExample();

        AdminExample.Criteria criteria = adminExample.createCriteria();

        criteria.andLoginAcctEqualTo(username);

        List<Admin> admins = adminMapper.selectByExample(adminExample);

        Admin admin = admins.get(0);

        return admin;
    }

    @Override
    public void saveAdmin(Admin admin) {

        //1.密码加密
        String userPswd = admin.getUserPswd();
        // String s = CrowdUtil.md5(userPswd);
        String encode = passwordEncoder.encode(userPswd);
        admin.setUserPswd(encode);

        //2.生成创建时间
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String creatTime = format.format(date);
        admin.setCreateTime(creatTime);

        try {
            adminMapper.insert(admin);
        } catch (Exception exception) {
            exception.printStackTrace();

            if (exception instanceof DuplicateKeyException){
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }

    @Override
    public List<Admin> getAll() {
        return adminMapper.selectByExample(new AdminExample());
    }

    @Override
    public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {

        //1.根据账号密码查找Admin对象
        //①创建example对象
        AdminExample adminExample = new AdminExample();

        //②创建Criteria
        AdminExample.Criteria criteria = adminExample.createCriteria();

        //③在Criteria对象中封装查询条件
        criteria.andLoginAcctEqualTo(loginAcct);

        //④调用AdminMapper的方法进行查询
        List<Admin> admins = adminMapper.selectByExample(adminExample);

        //2.判断admin是否为null
        if (admins == null || admins.size() == 0){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        if (admins.size() > 1){
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        }

        Admin admin = admins.get(0);
        //3.如果为空则抛异常
        if (admin == null){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        //4.如果Admin对象不为空则将密码从Admin中取出
        String userPswdFromDB = admin.getUserPswd();

        //5.将表单的明文密码加密
        String userPswdForm = CrowdUtil.md5(userPswd);

        //6.比较密码
        if (!Objects.equals(userPswdFromDB,userPswdForm)){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        //7.密码一致则返回Admin对象
        return admin;
    }

    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {

        //1.调用pagehelper静态方法开启分页
        //这里体现非侵入性设计原则，对原本代码不做修改
        PageHelper.startPage(pageNum,pageSize);

        //2.执行查询
        List<Admin> list = adminMapper.selectAdminByKeyword(keyword);

        //3.把结果封装到pageinfo并返回
        return new PageInfo<>(list);

    }

    @Override
    public void remove(Integer adminId) {

        //执行删除
        adminMapper.deleteByPrimaryKey(adminId);


    }

    @Override
    public Admin getAdminById(Integer adminId) {
        return adminMapper.selectByPrimaryKey(adminId);
    }

    @Override
    public void update(Admin admin) {

        //1.“Selective”，对于null字段不更新
        try {
            adminMapper.updateByPrimaryKeySelective(admin);
        } catch (Exception exception) {
            exception.printStackTrace();
            if (exception instanceof DuplicateKeyException){
                throw new LoginAcctAlreadyInUseForUpdateException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }

    @Override
    public void saveAdminRoleRelationShip(Integer adminId, List<Integer> roleIdList) {

        //1.根据adminId删除旧的关联关系数据
        adminMapper.deleteOldRelationShip(adminId);

        //2.根据roleIdList和adminId保存新的关联关系
        if (roleIdList != null && roleIdList.size() > 0){

            adminMapper.insertNewRelationShip(adminId, roleIdList);

        }

    }
}
