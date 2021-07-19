package com.zmz.yygh.common.util;

import com.zmz.yygh.common.helper.JwtHelper;

import javax.servlet.http.HttpServletRequest;

/**
* @Description: 获取当前用户信息工具类
* @Author: Zhu Mengze
* @Date: 2021/7/16 10:49
*/
public class AuthContextHolder {

    /**
    * @Description: 获取用户ID
    * @Author: Zhu Mengze
    * @Date: 2021/7/16 10:53
    */
    public static Long getUserId(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("token");
        Long userId = JwtHelper.getUserId(token);
        return userId;
    }
    /**
    * @Description: 获取用户名
    * @Author: Zhu Mengze
    * @Date: 2021/7/16 10:53
    */
    public static String getUserName(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("token");
        String userName = JwtHelper.getUserName(token);
        return userName;
    }


}
