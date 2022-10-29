package com.atguigu.crowd.constant;

/**
 * @author linzihao
 * @create 2022-10-08-10:39
 */
public class CrowdConstant {
    public static final String ATTR_NAME_EXCEPTION = "exception";
    public static final String ATTR_NAME_LOGIN_ADMIN = "loginAdmin";
    public static final String ATTR_NAME_LOGIN_MEMBER = "loginMember";
    public static final String ATTR_NAME_MESSAGE = "message";
    public static final String ATTR_NAME_PAGE_INFO = "pageInfo";
    public static final String ATTR_NAME_TEMPLE_PROJECT = "tempProject";

    public static final String REDIS_CODE_PREFIX = "REDIS_CODE_PREFIX_";

    public static final String MESSAGE_LOGIN_FAILED = "抱歉！，账号密码错误，请重新输入！";
    public static final String MESSAGE_LOGIN_ACCT_ALREADY_IN_USE = "账号已被占用！";
    public static final String MESSAGE_ACCESS_FORBIDDEN = "请登录后访问！";
    public static final String MESSAGE_STRING_INVALIDATE = "字符串不合法！";
    public static final String MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE = "系统错误：登陆账号不唯一";
    public static final String MESSAGE_ACCESS_DENIED = "抱歉，您不能访问这个资源";
    public static final String MESSAGE_REG_PHONENUM_IS_NULL = "抱歉手机号不能为空！";
    public static final String MESSAGE_CODE_NOT_EXIST = "验证码已过期！请检查手机号是否正确或重新发送";
    public static final String MESSAGE_CODE_NOT_INVALID = "验证码不正确";
    public static final String MESSAGE_HEADER_UPLOAD_FAILED = "头图上传失败";
    public static final String MESSAGE_HEADER_PIC_EMPTY = "头图不可为空";
    public static final String MESSAGE_DETAILE_PIC_EMPTY = "详情图不可为空";
    public static final String MESSAGE_DETAILE_PIC_UPLOAD_FAILED = "详情图上传失败";
    public static final String MESSAGE_TEMPLE_PROJECT_MISSING = "临时对象丢失";
    public static final String ATTR_NAME_PORTAL_DATA = "portal_data";
}
