package com.zmz.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmz.yygh.common.exception.YyghException;
import com.zmz.yygh.common.helper.JwtHelper;
import com.zmz.yygh.common.result.ResultCodeEnum;
import com.zmz.yygh.user.mapper.UserInfoMapper;
import com.zmz.yygh.user.service.PatientService;
import com.zmz.yygh.user.service.UserInfoService;
import com.zmzyygh.enums.AuthStatusEnum;
import com.zmzyygh.model.user.Patient;
import com.zmzyygh.model.user.UserInfo;
import com.zmzyygh.vo.user.LoginVo;
import com.zmzyygh.vo.user.UserAuthVo;
import com.zmzyygh.vo.user.UserInfoQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private PatientService patientService;

    /**
     * @Description: 手机号登陆
     * @Author: Zhu Mengze
     * @Date: 2021/7/13 9:47
     */
    @Override
    public Map<String, Object> login(LoginVo loginVo) {
        String phoneNum = loginVo.getPhone();
        String verifyCode = loginVo.getCode();
        //判断手机号和验证码是否为空
        if (StringUtils.isEmpty(phoneNum)) {
            //手机号不正确
            throw new YyghException(ResultCodeEnum.LOGIN_MOBLE_ERROR);
        }
        if (StringUtils.isEmpty(verifyCode)) {
            //验证码错误
            throw new YyghException(ResultCodeEnum.CODE_ERROR);
        }

        //校验验证码
        String codeFromRedis = redisTemplate.opsForValue().get(phoneNum);
        if (!verifyCode.equals(codeFromRedis)) {
            throw new YyghException(ResultCodeEnum.CODE_ERROR);
        }

        //绑定手机号码,
        //如果loginVo中的openid不为空
        //说明是从微信扫码过来的，要绑定手机号
        UserInfo userInfo = null;
        if (!StringUtils.isEmpty(loginVo.getOpenid())) {
            userInfo = this.getByOpenid(loginVo.getOpenid());
            if (Objects.nonNull(userInfo)) {
                userInfo.setPhone(loginVo.getPhone());
                this.updateById(userInfo);
            } else {
                throw new YyghException(ResultCodeEnum.DATA_ERROR);
            }
        }
        //为空说明不是从手机扫码过来的
        //是手机直接登录的
        if (Objects.isNull(userInfo)) {
            //判断是否是第一次登录，如果是，则进行注册操作
            QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("phone", phoneNum);
            userInfo = baseMapper.selectOne(queryWrapper);
            if (Objects.isNull(userInfo)) {
                //未注册，执行注册操作

                //这里如果有问题？事务注解？
                UserInfo newUser = UserInfo.builder().phone(phoneNum).status(1).name("").build();
                baseMapper.insert(newUser);
                userInfo = newUser;
            }
        }

        //登录成功返回登录信息
        if (userInfo.getStatus().equals(0)) {
            //用户状态校验
            throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
        Map<String, Object> map = new HashMap<>();
        String name = userInfo.getName();
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getPhone();
        }
        map.put("name", name);
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("token", token);
        return map;
    }

    /**
     * @Description: 根据微信openid获取用户信息
     * @Author: Zhu Mengze
     * @Date: 2021/7/15 18:45
     */
    @Override
    public UserInfo getByOpenid(String openid) {
        return baseMapper.selectOne(new QueryWrapper<UserInfo>().eq("openid", openid));
    }

    @Override
    public void userAuth(UserAuthVo userAuthVo, Long userId) {
        //根据用户id查找
        UserInfo userInfo = baseMapper.selectById(userId);
        if (Objects.isNull(userInfo)) {
            log.error("查找{}用户失败", userId);
            throw new YyghException(ResultCodeEnum.LOGIN_MOBLE_ERROR);
        }
        userInfo.setAuthStatus(AuthStatusEnum.AUTH_RUN.getStatus());
        userInfo.setCertificatesNo(userAuthVo.getCertificatesNo());
        userInfo.setCertificatesType(userAuthVo.getCertificatesType());
        userInfo.setCertificatesUrl(userAuthVo.getCertificatesUrl());
        userInfo.setName(userAuthVo.getName());
        //更新认证状态
        baseMapper.updateById(userInfo);

    }

    @Override
    public Page<UserInfo> selectPage(Long page, Long limit, UserInfoQueryVo userInfoQueryVo) {
        Page<UserInfo> userInfoPage = new Page<>(page, limit);
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        if (Objects.nonNull(userInfoQueryVo)) {
            String name = userInfoQueryVo.getKeyword();
            Integer authStatus = userInfoQueryVo.getAuthStatus();
            Integer status = userInfoQueryVo.getStatus();
            String createTimeBegin = userInfoQueryVo.getCreateTimeBegin(); //开始时间
            String createTimeEnd = userInfoQueryVo.getCreateTimeEnd(); //结束时间
            if (!StringUtils.isEmpty(name)) {
                queryWrapper.like("name", name);
            }
            if (!StringUtils.isEmpty(authStatus)) {
                queryWrapper.eq("auth_status", authStatus);
            }
            if (!StringUtils.isEmpty(status)) {
                queryWrapper.eq("status", status);
            }
            if (!StringUtils.isEmpty(createTimeBegin)) {
                queryWrapper.ge("create_time", createTimeBegin);
            }
            if (!StringUtils.isEmpty(createTimeEnd)) {
                queryWrapper.le("create_time", createTimeEnd);
            }

        }
        Page<UserInfo> infoPage = baseMapper.selectPage(userInfoPage, queryWrapper);
        //编号变成对应值封装
        infoPage.getRecords().forEach(item -> {
            this.packageUserInfo(item);
        });
        return infoPage;
    }

    @Override
    public void lock(Long userId, Integer status) {
        if (status == 0 || status == 1) {
            UserInfo userInfo = baseMapper.selectById(userId);
            userInfo.setStatus(status);
            baseMapper.updateById(userInfo);
        }
    }

    @Override
    public Map<String, Object> show(Long userId) {
        UserInfo userInfo = baseMapper.selectById(userId);
        if (Objects.isNull(userInfo)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        UserInfo packageUserInfo = this.packageUserInfo(userInfo);
        Map<String, Object> map = new HashMap<>();
        map.put("userInfo", packageUserInfo);
        //根据userid查询就诊人信息
        List<Patient> patientList = patientService.findAllByUserId(userId);
        map.put("patientList", patientList);
        return map;
    }

    //认证审批  2通过  -1不通过
    @Override
    public void approval(Long userId, Integer authStatus) {
        if (authStatus == 2 || authStatus == -1) {
            UserInfo userInfo = baseMapper.selectById(userId);
            userInfo.setAuthStatus(authStatus);
            baseMapper.updateById(userInfo);
        }
    }


    //编号变成对应值封装
    private UserInfo packageUserInfo(UserInfo userInfo) {
        //处理认证状态编码
        userInfo.getParam().put("authStatusString", AuthStatusEnum.getStatusNameByStatus(userInfo.getAuthStatus()));
        //处理用户状态 0  1
        String statusString = userInfo.getStatus() == 0 ? "锁定" : "正常";
        userInfo.getParam().put("statusString", statusString);
        return userInfo;
    }


}


