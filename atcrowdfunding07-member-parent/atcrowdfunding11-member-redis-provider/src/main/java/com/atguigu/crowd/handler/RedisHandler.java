package com.atguigu.crowd.handler;

import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author linzihao
 * @create 2022-10-20-15:40
 */
@RestController
public class RedisHandler {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/set/redis/key/value/remote")
    ResultEntity<String> setRedisKeyValueRemote(
            @RequestParam("key") String key,
            @RequestParam("value") String value
    ){

        try{

            ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();

            opsForValue.set(key, value);

            return ResultEntity.successWithoutData();

        }catch (Exception e){

            e.printStackTrace();

            return ResultEntity.failed(e.getMessage());

        }

    };

    @RequestMapping("/set/redis/key/value/remote/with/timeout")
    ResultEntity<String> setRedisKeyValueRemoteWithTimeout(
            @RequestParam("key") String key,
            @RequestParam("value") String value,
            @RequestParam("time") long time,
            @RequestParam("timeUnit") TimeUnit timeUnit
    ){
        try{

            ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();

            opsForValue.set(key, value, time, timeUnit);

            return ResultEntity.successWithoutData();

        }catch (Exception e){

            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());

        }
    };

    @RequestMapping("/get/redis/string/value/by/key")
    ResultEntity<String> getRedisStringValueByKey(
            @RequestParam("key") String key
    ){
        try{

            ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();

            String s = opsForValue.get(key);

            return ResultEntity.successWithData(s);

        }catch (Exception e){

            e.printStackTrace();

            return ResultEntity.failed(e.getMessage());

        }
    };

    @RequestMapping("/remove/redis/key/remote")
    ResultEntity<String> removeRedisKeyRemote(
            @RequestParam("key") String key
    ){
        try{

            stringRedisTemplate.delete(key);

            return ResultEntity.successWithoutData();

        }catch (Exception e){

            e.printStackTrace();

            return ResultEntity.failed(e.getMessage());

        }
    };

}
