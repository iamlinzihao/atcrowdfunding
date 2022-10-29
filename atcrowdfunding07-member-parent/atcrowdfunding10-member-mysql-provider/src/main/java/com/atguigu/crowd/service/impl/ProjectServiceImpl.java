package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.po.MemberConfirmInfoPO;
import com.atguigu.crowd.entity.po.MemberLaunchInfoPO;
import com.atguigu.crowd.entity.po.ProjectPO;
import com.atguigu.crowd.entity.po.ReturnPO;
import com.atguigu.crowd.entity.vo.*;
import com.atguigu.crowd.mapper.*;
import com.atguigu.crowd.service.api.ProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author linzihao
 * @create 2022-10-23-12:32
 */
@Transactional(readOnly = true)
@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private MemberLaunchInfoPOMapper memberLaunchInfoPOMapper;

    @Autowired
    private ReturnPOMapper returnPOMapper;

    @Autowired
    private MemberConfirmInfoPOMapper memberConfirmInfoPOMapper;

    @Autowired
    private ProjectPOMapper projectPOMapper;

    @Autowired
    private ProjectItemPicPOMapper projectItemPicPOMapper;



    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveProject(ProjectVO projectVO, Integer memberId) {

        //一.保存ProjectPO对象
        //1.创建空的projectPO对象
        ProjectPO projectPO = new ProjectPO();

        //2.复制到ProjectPO
        BeanUtils.copyProperties(projectVO,projectPO);

        //修复bug：memberId设置到projectPO里
        projectPO.setMemberid(memberId);

        //修复bug：生成创建时间存入
        String createdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        projectPO.setCreatedate(createdate);

        //修复bug：satus设置为0，表示即将开始
        projectPO.setStatus(0);

        //3.保存ProjectPO并获取自增的ProjectPO的id
        //需要到ProjectPOMapper.xml中设置  <insert id="insertSelective" useGeneratedKeys="true" keyProperty="id"。。。
        projectPOMapper.insertSelective(projectPO);

        //4.从projectPO中获取自增的id
        Integer projectId = projectPO.getId();

        //二.保存项目分类关联信息
        //1.从ProjectVO中获取typeIdList
        List<Integer> typeIdList = projectVO.getTypeIdList();
        projectPOMapper.insertTypeRelationShip(typeIdList,projectId);

        //三.保存项目标签关联信息
        List<Integer> tagIdList = projectVO.getTagIdList();
        projectPOMapper.insertTagRelationShip(tagIdList,projectId);

        //四.保存项目详情图片路径信息
        List<String> detailPicturePathList = projectVO.getDetailPicturePathList();
        projectItemPicPOMapper.insertPathList(projectId,detailPicturePathList);

        //五.保存项目发起人信息
        MemberLauchInfoVO memberLauchInfoVO = projectVO.getMemberLauchInfoVO();
        MemberLaunchInfoPO memberLaunchInfoPO = new MemberLaunchInfoPO();
        BeanUtils.copyProperties(memberLauchInfoVO,memberLaunchInfoPO);
        memberLaunchInfoPO.setMemberid(memberId);
        memberLaunchInfoPOMapper.insert(memberLaunchInfoPO);

        //六.保存回报信息
        List<ReturnVO> returnVOList = projectVO.getReturnVOList();

        List<ReturnPO> returnPOList = new ArrayList<>();

        for (ReturnVO returnVO :
                returnVOList) {
            ReturnPO returnPO = new ReturnPO();
            BeanUtils.copyProperties(returnVO,returnPO);
            returnPOList.add(returnPO);
        }

        returnPOMapper.insertReturnPOBatch(returnPOList,projectId);

        //七.保存项目确认信息
        MemberConfirmInfoVO memberConfirmInfoVO = projectVO.getMemberConfirmInfoVO();
        MemberConfirmInfoPO memberConfirmInfoPO = new MemberConfirmInfoPO();
        BeanUtils.copyProperties(memberConfirmInfoVO,memberConfirmInfoPO);
        memberConfirmInfoPO.setMemberid(memberId);
        memberConfirmInfoPOMapper.insert(memberConfirmInfoPO);

    }

    @Override
    public List<PortalTypeVO> getPortalTypeVO() {
        return projectPOMapper.selectPortalTypeVOList();
    }

    @Override
    public DetailProjectVO getDetailProjectVO(Integer projectId) {

        //1.获取detailProjectVO
        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(projectId);

        //2.获取status并确定statusText
        Integer status = detailProjectVO.getStatus();

        switch(status){
            case 0:
                detailProjectVO.setStatusText("审核中");
                break;
            case 1:
                detailProjectVO.setStatusText("众筹中");
                break;
            case 2:
                detailProjectVO.setStatusText("众筹成功");
                break;
            case 3:
                detailProjectVO.setStatusText("已关闭");
                break;
            default:
                break;
        }
        //3.根据deployDate计算lastDay
        //2020-10-15
        String deployDate = detailProjectVO.getDeployDate();

        //获取当前日期
        Date currentdate = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            //把众筹日期解析为Date
            Date deployDate1 = simpleDateFormat.parse(deployDate);

            long time = currentdate.getTime();

            long time1 = deployDate1.getTime();

            long pastDays = (time - time1)/1000/60/60/24;

            //使用总的天数减去过去的天数
            Integer totalday = detailProjectVO.getDay();

            Integer lastDay = (int)(totalday - pastDays);

            detailProjectVO.setLastDay(lastDay);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return detailProjectVO;
    }
}
