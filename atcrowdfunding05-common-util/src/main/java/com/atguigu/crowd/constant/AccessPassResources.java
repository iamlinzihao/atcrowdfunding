package com.atguigu.crowd.constant;

import java.util.HashSet;
import java.util.Set;

/**
 * @author linzihao
 * @create 2022-10-22-12:03
 */
public class AccessPassResources {

    public static final Set<String> PASS_RES_SET = new HashSet<>();

    static {

        PASS_RES_SET.add("/");
        PASS_RES_SET.add("/auth/member/to/reg/page");
        PASS_RES_SET.add("/auth/member/to/login/page");
        PASS_RES_SET.add("/auth/member/do/login");
        PASS_RES_SET.add("/auth/member/logout");
        PASS_RES_SET.add("/auth/do/member/register");
        PASS_RES_SET.add("/auth/member/send/short/message.json");

    }

    public static final Set<String> STATIC_RES_SET = new HashSet<>();

    static {
        STATIC_RES_SET.add("bootstrap");
        STATIC_RES_SET.add("css");
        STATIC_RES_SET.add("fonts");
        STATIC_RES_SET.add("img");
        STATIC_RES_SET.add("jquery");
        STATIC_RES_SET.add("layer");
        STATIC_RES_SET.add("script");
        STATIC_RES_SET.add("ztree");
    }

    /**
     * 用于判断某个servletPath是否对应一个静态资源
     * @param servletPath
     * @return
     *      true:是静态资源
     *      false:不是静态资源
     */
    public static boolean judgeCurrentServletPathWetherStaticRecource(String servletPath){

        //1.排除字符串为空的情况
        if (servletPath == null || servletPath.length() == 0){
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }
        //2.用split切割字符串
        String[] split = servletPath.split("/");

        //3.切割后第一个斜杠的左边是空字符串，所以要取下标为一的元素
        String firstLevelPath = split[1];

        //4.判断是否存在集合
        return STATIC_RES_SET.contains(firstLevelPath);

    }

    // public static void main(String[] args) {
    //
    //     String servletPath = "/css/aaa/bbb/ccc";
    //     boolean b = judgeCurrentServletPathWetherStaticRecource(servletPath);
    //     System.out.println(b);
    //
    // }

}
