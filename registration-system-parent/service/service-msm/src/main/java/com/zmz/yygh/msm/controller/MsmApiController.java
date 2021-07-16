package com.zmz.yygh.msm.controller;

import com.zmz.yygh.common.result.Result;
import com.zmz.yygh.msm.service.MsmService;
import com.zmz.yygh.msm.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/msm")
public class MsmApiController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/send/{phoneNum}")
    public Result sendCode(@PathVariable("phoneNum") String phoneNum) {
        //先查redis里有没有缓存过，有的话就直接返回
        //手机号作为key，验证码作为value
        String code = redisTemplate.opsForValue().get(phoneNum);
        if (!StringUtils.isEmpty(code)) {
            return Result.ok(code);
        } else {
            //redis中没有
            //生成验证码，整合短信服务进行发送
            //发送成功后，将验证码放在redis中并设置过期时间

            //利用工具类生成6位的随机数
            code = RandomUtil.getSixBitRandom();
            Boolean isSend = msmService.sendMessage(phoneNum, code);
            if (isSend){
                //第三个参数是数量，第四个参数是单位
                redisTemplate.opsForValue().set(phoneNum,code,2,TimeUnit.MINUTES);
                return Result.ok(code);
            }else {
                return Result.fail().message("短信验证码发送失败");
            }


        }

    }


}
