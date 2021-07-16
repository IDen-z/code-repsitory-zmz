package com.zmz.yygh.user.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zmz.yygh.common.exception.YyghException;
import com.zmz.yygh.common.helper.JwtHelper;
import com.zmz.yygh.common.result.Result;
import com.zmz.yygh.common.result.ResultCodeEnum;
import com.zmz.yygh.user.config.ConstantPropertiesUtil;
import com.zmz.yygh.user.service.UserInfoService;
import com.zmz.yygh.user.utils.HttpClientUtils;
import com.zmzyygh.model.user.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
//为了让页面跳转
@RequestMapping("/api/ucenter/wx")
@Slf4j
public class WeixinApiController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * @Description: 生成微信扫描二维码
     * @Author: Zhu Mengze
     * @Date: 2021/7/15 9:01
     */
    @GetMapping("/getLoginParam")
    @ResponseBody
    public Result genQrConnect(HttpSession session) throws UnsupportedEncodingException {
        Map<String, Object> map = new HashMap<>();
        map.put("appid", ConstantPropertiesUtil.WX_OPEN_APP_ID);
        map.put("redirectUri", URLEncoder.encode(ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL, "UTF-8"));
        map.put("scope", "snsapi_login");
        map.put("state", System.currentTimeMillis() + "");
        return Result.ok(map);
    }

    /**
     * @Description: 微信二维码扫描确认以后的回调函数
     * 回调地址：http://localhost:8160/api/ucenter/wx/callback?code=0210eJFa1n28oB0eBZIa1kaLsa30eJFK&state=1626316552134
     * @Author: Zhu Mengze
     * @Date: 2021/7/15 9:46
     */
    @GetMapping("/callback")
    public String callback(String code, String state) {
        if (StringUtils.isEmpty(state) || StringUtils.isEmpty(code)) {
            log.error("非法回调请求");
            throw new YyghException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }
        //通过这个code请求微信给的固定地址，获取accesstoken并最终获得用户信息
        log.info("微信授权服务器回调方法执行。。。。。。");
        log.info("state = {},code={}", state, code);
        //这个%s 类似sql中的问号，表示占位符，需要传参数
        //利用string.format可以向里面设置占位符的具体参数
        StringBuffer baseAccessTokenUrl = new StringBuffer()
                .append("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&code=%s")
                .append("&grant_type=authorization_code");
        String accessTokenUrl = String.format(baseAccessTokenUrl.toString(),
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                code);
        String accessTokenInfo = null;
        try {
            accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            log.info("获取返回的accessToken = {}", accessTokenInfo);
        } catch (Exception e) {
            log.error("获取用户accessToken失败\n异常信息为", e);
            throw new YyghException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }
        JSONObject jsonObjectForAccessToken = JSON.parseObject(accessTokenInfo);

        if (jsonObjectForAccessToken.getString("errcode") != null) {
            log.error("获取access_token失败：" + jsonObjectForAccessToken.getString("errcode") + jsonObjectForAccessToken.getString("errmsg"));
            throw new YyghException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }
        //获取accesstoken成功则正常获取
        String accessToken = jsonObjectForAccessToken.getString("access_token");
        String openid = jsonObjectForAccessToken.getString("openid");
        //接下来同样向微信发送请求来获取userinfo
        String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                "?access_token=%s" +
                "&openid=%s";
        String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);
        String userInfoWx = null;
        try {
            userInfoWx = HttpClientUtils.get(userInfoUrl);
            log.info("获取用户信息成功 userInfo = {}",userInfoWx);
        } catch (Exception e) {
            log.error("获取用户信息userInfo失败\n异常信息为", e);
            throw new YyghException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        }
        JSONObject jsonObjectForUserInfo = JSON.parseObject(userInfoWx);
        if (jsonObjectForUserInfo.getString("errcode") != null) {
            log.error("获取access_token失败：" + jsonObjectForUserInfo.getString("errcode") + jsonObjectForUserInfo.getString("errmsg"));
            throw new YyghException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        }
        String nickname = jsonObjectForUserInfo.getString("nickname");
        String headimgurl = jsonObjectForUserInfo.getString("headimgurl");
        //此时已经获取到用户信息，应该把它保存到自己的数据库里
        UserInfo userInfo = userInfoService.getOne(new QueryWrapper<UserInfo>().eq("nick_name", nickname));
        if (Objects.isNull(userInfo)){
            //进行绑定手机号
            userInfo = UserInfo.builder()
                    .nickName(nickname)
                    .status(1)
                    .openid(openid)
                    .build();
            userInfo.setCreateTime(new Date());
            boolean isSaveUserInfo = userInfoService.save(userInfo);
            if (!isSaveUserInfo){
                log.info("userInfo插入数据库失败");
            }
        }
        //如果数据库里已经保存了信息，更新即可
        userInfo.setStatus(1);
        userInfo.setUpdateTime(new Date());
        userInfoService.updateById(userInfo);

        Map<String, Object> map = new HashMap<>();
        String name = userInfo.getName();
        if(StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }
        if(StringUtils.isEmpty(name)) {
            name = userInfo.getPhone();
        }
        map.put("name", name);
        //这个openid主要是为了前端
        //前端判断openid为空，就是绑定过了手机号，否则就是没有绑定过
        if(StringUtils.isEmpty(userInfo.getPhone())) {
            map.put("openid", userInfo.getOpenid());
        } else {
            map.put("openid", "");
        }
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("token", token);
        try {
            return "redirect:" + ConstantPropertiesUtil.YYGH_BASE_URL + "/weixin/callback?token="+map.get("token")+"&openid="+map.get("openid")+"&name="
                    +URLEncoder.encode((String) map.get("name"),"UTF-8");
        } catch (Exception e) {
            log.error("重定向失败\n异常信息为：",e);
            throw new YyghException(ResultCodeEnum.SERVICE_ERROR);
        }
    }

}
