package com.zmz.yygh.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zmzyygh.model.user.UserInfo;
import com.zmzyygh.vo.user.LoginVo;
import com.zmzyygh.vo.user.UserAuthVo;

import java.util.Map;

public interface UserInfoService extends IService<UserInfo> {
    Map<String, Object> login(LoginVo loginVo);


    UserInfo getByOpenid(String openid);


    void userAuth(UserAuthVo userAuthVo, Long userId);
}
