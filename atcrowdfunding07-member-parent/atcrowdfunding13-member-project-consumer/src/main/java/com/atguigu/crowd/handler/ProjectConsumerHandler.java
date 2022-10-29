package com.atguigu.crowd.handler;

import com.atguigu.crowd.api.MySQLRemoteService;
import com.atguigu.crowd.config.OSSProperties;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.vo.*;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author linzihao
 * @create 2022-10-23-16:46
 */
@Controller
public class ProjectConsumerHandler {

    @Autowired
    private OSSProperties ossProperties;

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/get/project/detail/{projectId}")
    public String getProjectDetail(@PathVariable("projectId") Integer projectId, Model model){

        ResultEntity<DetailProjectVO> detailProjectVORemote = mySQLRemoteService.getDetailProjectVORemote(projectId);

        if (ResultEntity.SUCCESS.equals(detailProjectVORemote.getResult())){
            DetailProjectVO detailProjectVO = detailProjectVORemote.getData();
            model.addAttribute("detailProjectVO",detailProjectVO);
        }

        return "project-show-detail";

    }

    @RequestMapping("/create/confirm")
    public String saveConfirm(HttpSession session, MemberConfirmInfoVO memberConfirmInfoVO, ModelMap modelMap){

        //1.从session域中获取ProjectVO对象
        ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);

        //2.如果projectVO为空
        if (projectVO == null){

            throw new RuntimeException(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);

        }

        //3.把memberConfirmInfoVO的数据往projectVO里存
        projectVO.setMemberConfirmInfoVO(memberConfirmInfoVO);

        //4.获取session域中当前登录的用户
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        Integer memberId = memberLoginVO.getId();

        //5.调用远程方法保存projectVO
        ResultEntity<String> saveResultEntity = mySQLRemoteService.saveProjectVORemote(projectVO,memberId);

        //6.判断远程的保存是否成功
        String result = saveResultEntity.getResult();
        if (ResultEntity.FAILED.equals(result)){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,saveResultEntity.getMessage());
            return "project-confirm";
        }

        //9.删除临时的projectVO
        session.removeAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);

        //8.保存成功跳转到最后成功的页面
        return "redirect:http://localhost/project/create/success";
    }

    @ResponseBody
    @RequestMapping("/create/save/return.json")
    public ResultEntity<String> saveReturn(ReturnVO returnVO, HttpSession session){

        try {
            //1.从Session中获取之前缓存的ProjectVO对象
            ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);

            //2.判断projectVO对象是否为空
            if (projectVO == null){
                return ResultEntity.failed(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
            }

            //3.从projectVO对象中获取回报信息的集合
            List<ReturnVO> returnVOList = projectVO.getReturnVOList();

            //4.判断是否有效
            if (returnVOList == null || returnVOList.size() == 0){

                //对returnVOList进行初始化
                returnVOList = new ArrayList<>();
                //为了以后能正常使用这个值，设置到projectVO对象中
                projectVO.setReturnVOList(returnVOList);

            }

            //5.将收集了表单数据的returnVO存入到returnVOlist
            returnVOList.add(returnVO);

            //6.把projectVO及时存回session中，保证redis中的更新
            session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT,projectVO);

            //7.所有操作完成返回success
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    //javdscript代码中formData.append("returnPicture",file);
    //returnPicture是请求参数的名字
    //file是请求参数的值，就是要上传的文件
    @ResponseBody
    @RequestMapping("/create/upload/return/picture.json")
    public ResultEntity<String> uploadReturnPicture(
            //接受用户上传的文件
            @RequestParam("returnPicture") MultipartFile returnPicture) throws IOException {

        ResultEntity<String> uploadReturnPictureResultEntity = CrowdUtil.uploadFileToOss(
                ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                returnPicture.getInputStream(),
                ossProperties.getBucketName(),
                ossProperties.getBucketDomain(),
                returnPicture.getOriginalFilename());

        //返回上传结果
        return uploadReturnPictureResultEntity;

    }

    @RequestMapping("/create/project/information")
    public String saveProjectBasicInfo(

            //1.接收除了图片以外的其他普通数据
            ProjectVO projectVO,

            //2.接受上传的头图
            MultipartFile headerPicture,

            //3.接收详情图
            List<MultipartFile> detailPictureList,

            //4.用来将收集了一部分数据的ProjectVO数据存入session域
            HttpSession Session,

            //5.携带错误信息
            ModelMap modelMap
    ) throws IOException {

        //一.完成头图的上传
        //1.判断
        boolean headerPictureEmpty = headerPicture.isEmpty();

        //2.如果头图为空返回表单并显示错误信息
        if (headerPictureEmpty) {

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_HEADER_PIC_EMPTY);

            return "project-launch";
        }

        //3.如果用户确实上传了有内容的文件则执行上传
        ResultEntity<String> uploadHeaderResultEntity = CrowdUtil.uploadFileToOss(
                ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                headerPicture.getInputStream(),
                ossProperties.getBucketName(),
                ossProperties.getBucketDomain(),
                headerPicture.getOriginalFilename());

        String result = uploadHeaderResultEntity.getResult();

        // 4.判断是否成功
        if (ResultEntity.SUCCESS.equals(result)) {

            //5.从返回的数据获取图片访问地址
            String headerPicturePath = uploadHeaderResultEntity.getData();

            //6.存入ProjectVO
            projectVO.setHeaderPicturePath(headerPicturePath);
        } else {

            //7.如果失败则返回表单并显示错误信息
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_HEADER_UPLOAD_FAILED);

            return "project-launch";

        }
        //二.上传详情图片
        //1.创建一个用来存放详情图片路径的集合
        List<String> detailPicturePathList = new ArrayList<>();

        //2.检查detailPictureList是否有效
        if(detailPictureList == null || detailPictureList.size() == 0){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAILE_PIC_EMPTY);

            return "project-launch";
        }

        //3.遍历detailPictureList
        for( MultipartFile detailPicture :detailPictureList){

            //4.当前的detailPicture是否为空
            if (detailPicture.isEmpty()){
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAILE_PIC_EMPTY);
                //检测到详情图片中单个文件为空也是回去回显错误信息
                return "project-launch";
            }

            //5.执行上传
            ResultEntity<String> detailPictureUploadResultEntity = CrowdUtil.uploadFileToOss(
                    ossProperties.getEndPoint(),
                    ossProperties.getAccessKeyId(),
                    ossProperties.getAccessKeySecret(),
                    detailPicture.getInputStream(),
                    ossProperties.getBucketName(),
                    ossProperties.getBucketDomain(),
                    detailPicture.getOriginalFilename());

            //6.检查上传结果
            if (ResultEntity.SUCCESS.equals(detailPictureUploadResultEntity.getResult())) {

                String path = detailPictureUploadResultEntity.getData();

                //7.收集刚刚上传的图片的访问路径
                detailPicturePathList.add(path);

            }else {
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAILE_PIC_UPLOAD_FAILED);

                return "project-launch";
            }
        }
        //8.将详情图片路径集合存入ProjectVO
        projectVO.setDetailPicturePathList(detailPicturePathList);

        //三.后续操作
        //1.将ProjectVO对象存入session域
        Session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT,projectVO);

        //2.以完整的路径前往下一个收集汇报信息的页面
        return "redirect:http://localhost/project/return/info/page";
    }

}