package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author linzihao
 * @create 2022-10-21-16:16
 *
 * 封装的是表单的数据，和MemberPO封装的信息错开（有相同有重叠）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberVO {

    private String loginacct;

    private String userpswd;

    private String username;

    private String email;

    private String phoneNum;

    private String code;

}
