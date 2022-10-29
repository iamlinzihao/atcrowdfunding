package com.atguigu.crowd.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author linzihao
 * @create 2022-10-21-8:51
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
@ConfigurationProperties(prefix = "short.message")
public class ShortMessageProperties {

    private String method;
    private String path;
    private String host;
    private String appcode;
    private String sign;
    private String skin;

}
