package com.zmz.yygh.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zmz.yygh.common.result.Result;
import com.zmz.yygh.user.service.UserInfoService;
import com.zmzyygh.model.hosp.HospitalSet;
import com.zmzyygh.model.user.UserInfo;
import com.zmzyygh.vo.user.UserInfoQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * @Description: 用户列表（条件查询（如果用条件查询，那么给一个VO是最合理的，
     * 这样无论是什么条件都封装在了一个方法里）带分页）
     * @Author: Zhu Mengze
     * @Date: 2021/7/19 14:39
     */
    @GetMapping("{page}/{limit}")
    public Result list(@PathVariable Long page,
                       @PathVariable Long limit,
                       UserInfoQueryVo userInfoQueryVo) {

        Page<UserInfo> pageModel = userInfoService.selectPage(page, limit, userInfoQueryVo);
        return Result.ok(pageModel);
    }


}
