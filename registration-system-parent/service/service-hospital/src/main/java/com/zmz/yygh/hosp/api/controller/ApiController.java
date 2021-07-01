package com.zmz.yygh.hosp.api.controller;


import com.zmz.yygh.common.exception.YyghException;
import com.zmz.yygh.common.result.Result;
import com.zmz.yygh.common.result.ResultCodeEnum;
import com.zmz.yygh.common.util.HttpRequestHelper;
import com.zmz.yygh.common.util.HttpUtil;
import com.zmz.yygh.common.util.MD5;
import com.zmz.yygh.hosp.api.service.DepartmentService;
import com.zmz.yygh.hosp.api.service.HospitalService;
import com.zmz.yygh.hosp.service.HospitalSetService;
import com.zmzyygh.model.hosp.Hospital;
import com.zmzyygh.model.hosp.HospitalSet;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/hosp")
public class ApiController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSetService hospitalSetService;

    @Autowired
    private DepartmentService departmentService;


    /**
    * @Description: 查询医院信息接口
    * @Author: Zhu Mengze
    * @Date: 2021/7/1 10:17
    */
    @PostMapping("/hospital/show")
    public Result getHospital(HttpServletRequest request){
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(map);
        String hoscode =(String) paramMap.get("hoscode");

        //在保存之前要进行签名的校验
        String hospSign = (String) paramMap.get("sign");
        HospitalSet hospitalSet = hospitalSetService.getByHoscode((String) paramMap.get("hoscode"));
        if (!hospSign.equals(MD5.encrypt(hospitalSet.getSignKey()))) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //调用service方法
        Hospital hospital=hospitalService.getByHoscode(hoscode);


        return Result.ok(hospital);
    }





    /**
     * @Description: 上传医院的基本信息与规则信息
     * @Author: Zhu Mengze
     * @Date: 2021/6/30 10:58
     */
    @PostMapping("/saveHospital")
    public Result saveHosp(HttpServletRequest request) {
        //这里将请求过来的requuest中的map拿出来，这里面封装了hosp参数信息

        //这部分是将Map<String, String[]>转成Map<String, Object>
        //后面可以用工具类代替
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            resultMap.put(entry.getKey(), entry.getValue()[0]);
        }

        //在保存之前要进行签名的校验
        String hospSign = (String) resultMap.get("sign");
        HospitalSet hospitalSet = hospitalSetService.getByHoscode((String) resultMap.get("hoscode"));
        if (!hospSign.equals(MD5.encrypt(hospitalSet.getSignKey()))) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        //在传输过程中，图片格式由于base64编码的问题+号都被转化为了空格
        //这里要做处理转换回来
        String logoData = (String) resultMap.get("logoData");
        logoData = logoData.replaceAll(" ", "+");
        resultMap.put("logoData",logoData);

        hospitalService.saveHospotal(resultMap);
        return Result.ok();


    }


    /**
    * @Description: 上传科室接口
    * @Author: Zhu Mengze
    * @Date: 2021/7/1 14:22
    */
    @PostMapping("/saveDepartment")
    public Result saveDept(HttpServletRequest request){
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(map);
        String hoscode =(String) paramMap.get("hoscode");

        //在保存之前要进行签名的校验
        String hospSign = (String) paramMap.get("sign");
        HospitalSet hospitalSet = hospitalSetService.getByHoscode((String) paramMap.get("hoscode"));
        if (!hospSign.equals(MD5.encrypt(hospitalSet.getSignKey()))) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //校验通过继续操作，调用deptmentservice方法继续
        departmentService.saveDeptment(paramMap);
        return Result.ok();
    }





}
