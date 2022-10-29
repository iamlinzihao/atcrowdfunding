package com.atguigu.crowd.test;

import com.atguigu.crowd.util.CrowdUtil;
import org.junit.Test;

/**
 * @author linzihao
 * @create 2022-10-08-16:45
 */
public class StringTest {

    @Test
    public void testMd5(){

        String source = "1275903512";

        String s = CrowdUtil.md5(source);
        System.out.println(s);
    }
}
