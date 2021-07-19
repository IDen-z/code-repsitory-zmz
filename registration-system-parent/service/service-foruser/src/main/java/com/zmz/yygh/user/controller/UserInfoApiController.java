package com.zmz.yygh.user.controller;

import com.zmz.yygh.common.result.Result;
import com.zmz.yygh.common.util.AuthContextHolder;
import com.zmz.yygh.user.service.UserInfoService;
import com.zmzyygh.model.user.UserInfo;
import com.zmzyygh.vo.user.LoginVo;
import com.zmzyygh.vo.user.UserAuthVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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


    /**
    * @Description: 用户认证
    * @Author: Zhu Mengze
    * @Date: 2021/7/16 11:04
    */
    @PostMapping("/auth/userAuth")
    public Result userAuth(@RequestBody UserAuthVo userAuthVo, HttpServletRequest request){
        userInfoService.userAuth(userAuthVo, AuthContextHolder.getUserId(request));
        return Result.ok();
    }

    /**
    * @Description: 获取用户id信息接口
    * @Author: Zhu Mengze
    * @Date: 2021/7/16 14:29
    */
    //获取用户id信息接口
    @GetMapping("auth/getUserInfo")
    public Result getUserInfo(HttpServletRequest request){
        Long userId = AuthContextHolder.getUserId(request);
        UserInfo userInfo = userInfoService.getById(userId);
        return Result.ok(userInfo);
    }






}
