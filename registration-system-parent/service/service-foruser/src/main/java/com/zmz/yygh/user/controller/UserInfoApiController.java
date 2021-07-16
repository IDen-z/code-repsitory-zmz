package com.zmz.yygh.user.controller;

import com.zmz.yygh.common.result.Result;
import com.zmz.yygh.user.service.UserInfoService;
import com.zmzyygh.vo.user.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserInfoApiController {

    @Autowired
    private UserInfoService userInfoService;


    /**
     * @Description: 用户手机号登录接口
     * @Author: Zhu Mengze
     * @Date: 2021/7/13 9:32
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo) {
        //注意，登陆操作完成以后需要返回一些信息，在右上角显示头像和昵称
        //同时后续操作需要判断当前是否是登陆状态
        Map<String, Object> map = userInfoService.login(loginVo);
        return Result.ok(map);
    }


}
