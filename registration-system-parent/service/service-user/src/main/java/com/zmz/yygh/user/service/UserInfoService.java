package com.zmz.yygh.user.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zmzyygh.model.user.UserInfo;
import com.zmzyygh.vo.user.LoginVo;
import com.zmzyygh.vo.user.UserAuthVo;
import com.zmzyygh.vo.user.UserInfoQueryVo;

import java.util.Map;

public interface UserInfoService extends IService<UserInfo> {
    Map<String, Object> login(LoginVo loginVo);


    UserInfo getByOpenid(String openid);


    void userAuth(UserAuthVo userAuthVo, Long userId);

    Page<UserInfo> selectPage(Long page, Long limit, UserInfoQueryVo userInfoQueryVo);

    void lock(Long userId, Integer status);

    Map<String, Object> show(Long userId);

    void approval(Long userId, Integer authStatus);
}
