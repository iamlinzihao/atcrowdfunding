package com.atguigu.crowd.handler;

import com.atguigu.crowd.api.MySQLRemoteService;
import com.atguigu.crowd.api.RedisRemoteService;
import com.atguigu.crowd.config.ShortMessageProperties;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.vo.MemberLoginVO;
import com.atguigu.crowd.entity.vo.MemberVO;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author linzihao
 * @create 2022-10-21-8:33
 */
@Controller
public class MemberHandler {

    @Autowired
    private ShortMessageProperties shortMessageProperties;

    @Autowired
    private RedisRemoteService redisRemoteService;

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/auth/member/logout")
    public String logout(HttpSession session){

        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping("/auth/member/do/login")
    public String login(
            @RequestParam("loginacct") String loginacct,
            @RequestParam("userpswd") String userpswd,
            ModelMap modelMap,
            HttpSession session){

        //1.远程接口查找用户
        ResultEntity<MemberPO> resultEntity = mySQLRemoteService.getMemberPOByLoginAcctRemote(loginacct);

        if (ResultEntity.FAILED.equals(resultEntity.getResult())){

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,resultEntity.getMessage());

            return "member-login";
        }

        MemberPO memberPO = resultEntity.getData();

        if (memberPO == null){

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_LOGIN_FAILED);

            return "member-login";

        }

        //2.比较用户密码
        String userpswdDataBase = memberPO.getUserpswd();

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        boolean matchResult = bCryptPasswordEncoder.matches(userpswd,userpswdDataBase);

        if (!matchResult){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_LOGIN_FAILED);

            return "member-login";
        }

        //3.创建MemberLoginVO存入session域
        MemberLoginVO loginVO = new MemberLoginVO(memberPO.getId(), memberPO.getUsername(), memberPO.getEmail());
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER,loginVO);

        return "redirect:http://localhost/auth/member/to/center/page";

    }

    @RequestMapping("/auth/do/member/register")
    public String register(MemberVO memberVO, ModelMap modelMap) {

        //1.调用Redis模块获取验证码的功能
        //①获取手机号
        String phoneNum = memberVO.getPhoneNum();

        //②拼Redis中的key
        String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;

        //③读取Redis中的value
        ResultEntity<String> value = redisRemoteService.getRedisStringValueByKey(key);

        //④检查查询操作是否有效（是否查询成功和查询结果不是null）
        String result = value.getResult();
        if (ResultEntity.FAILED.equals(result)) {

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, value.getMessage());

            return "member-reg";

        }

        String code = value.getData();

        if (code == null) {

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_NOT_EXIST);

            return "member-reg";
        }

        //⑤如果能查询到，比较表单验证码和Redis验证码
        String formCode = memberVO.getCode();

        if (!Objects.equals(code, formCode)) {

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_NOT_INVALID);

            return "member-reg";
        }

        //⑥如果一致，删除Redis验证码
        redisRemoteService.removeRedisKeyRemote(key);

        //⑦执行密码加密
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String userpswd = memberVO.getUserpswd();
        String encode = bCryptPasswordEncoder.encode(userpswd);
        memberVO.setUserpswd(encode);

        //2.调用MySQL模块保存用户功能
        //①创建空的PO对象
        MemberPO memberPO = new MemberPO();

        //②复制对象属性
        BeanUtils.copyProperties(memberVO, memberPO);

        //③调用远程方法
        ResultEntity<String> saveMemberResultEntity = mySQLRemoteService.saveMemberPo(memberPO);

        if (ResultEntity.FAILED.equals(saveMemberResultEntity.getResult())) {

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,saveMemberResultEntity.getMessage());

            return "member-reg";
        }

        return "redirect:/auth/member/to/login/page";

    }

    @ResponseBody
    @RequestMapping("/auth/member/send/short/message.json")
    public ResultEntity<String> sendMessage(@RequestParam("phoneNum") String phoneNum) {

        //1.发送验证码到该手机
        ResultEntity<String> stringResultEntity = CrowdUtil.sendCodeByShortMessage(

                shortMessageProperties.getMethod(),
                shortMessageProperties.getPath(),
                shortMessageProperties.getHost(), phoneNum,
                shortMessageProperties.getAppcode(),
                shortMessageProperties.getSign(),
                shortMessageProperties.getSkin()
        );
        //2.判断发送结果
        if (ResultEntity.SUCCESS.equals(stringResultEntity.getResult())) {
            //3.如果成功存入Redis
            //发送的验证码
            String code = stringResultEntity.getData();

            String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;

            ResultEntity<String> saveCodeResultEntity = redisRemoteService.setRedisKeyValueRemoteWithTimeout(key, code, 15, TimeUnit.MINUTES);

            //判断redis存入是否成功
            if (ResultEntity.SUCCESS.equals(saveCodeResultEntity.getResult())) {
                //成功生成结果集并返回
                return ResultEntity.successWithoutData();
            } else {
                //失败直接返回结果集

                return saveCodeResultEntity;
            }
        } else {
            return stringResultEntity;
        }
    }

}
