package com.zmz.yygh.msm.service.impl;

import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.zmz.yygh.common.exception.YyghException;
import com.zmz.yygh.common.result.ResultCodeEnum;
import com.zmz.yygh.msm.service.MsmService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Set;

@Service
public class MsmServiceImpl implements MsmService {



    /**
     * @Description: 短信发送服务
     * @Author: Zhu Mengze
     * @Date: 2021/7/13 16:29
     */
    @Override
    public Boolean sendMessage(String phoneNum, String code) {
        if (StringUtils.isEmpty(phoneNum)) {
            throw new YyghException(ResultCodeEnum.LOGIN_MOBLE_ERROR);
        }
        this.send(phoneNum, code);

        return true;
    }


    /**
     * @Description: 容联云短信发送服务
     * code表示验证码，expireTime表示有效时间
     * @Author: Zhu Mengze
     * @Date: 2021/7/13 16:08
     */
    private void send(String phoneNum, String code) {
        //生产环境请求地址：app.cloopen.com
        String serverIp = "app.cloopen.com";
        //请求端口
        String serverPort = "8883";
        //主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN
        String accountSId = "8aaf07087a331dc7017a9e99aab62739";
        String accountToken = "af43249fd6724c699ae7fffb442ac2a1";
        //请使用管理控制台中已创建应用的APPID
        String appId = "8aaf07087a331dc7017a9e99abb1273f";
        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
        sdk.init(serverIp, serverPort);
        sdk.setAccount(accountSId, accountToken);
        sdk.setAppId(appId);
        sdk.setBodyType(BodyType.Type_JSON);
        String to = phoneNum;
        String templateId = "1";
        //设置过期时间，2分钟
        String expireTime = "2";
        String[] datas = {code, expireTime};
//        String subAppend = "1234";  //可选 扩展码，四位数字 0~9999
//        String reqId = "fadfafas";  //可选 第三方自定义消息id，最大支持32位英文数字，同账号下同一自然天内不允许重复
        //HashMap<String, Object> result = sdk.sendTemplateSMS(to,templateId,datas);
        HashMap<String, Object> result = sdk.sendTemplateSMS(to, templateId, datas);
        if ("000000".equals(result.get("statusCode"))) {
            //正常返回输出data包体信息（map）
            HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for (String key : keySet) {
                Object object = data.get(key);
                System.out.println(key + " = " + object);
            }
        } else {
            //异常返回输出错误码和错误信息
            System.out.println("错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
        }
    }


}
