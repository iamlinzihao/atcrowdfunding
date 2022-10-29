package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author linzihao
 * @create 2022-10-26-17:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String receiveName;

    private String phoneNum;

    private String address;

    private Integer memberId;

}
