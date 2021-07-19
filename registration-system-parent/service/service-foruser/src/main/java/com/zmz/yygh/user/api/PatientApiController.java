package com.zmz.yygh.user.api;

import com.zmz.yygh.common.result.Result;
import com.zmz.yygh.common.util.AuthContextHolder;
import com.zmz.yygh.user.service.PatientService;
import com.zmzyygh.model.user.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/user/patient")
public class PatientApiController {

    @Autowired
    private PatientService patientService;

    /**
     * @Description: 获取就诊人列表，最多三个不用分页
     * @Author: Zhu Mengze
     * @Date: 2021/7/16 17:05
     */
    @GetMapping("auth/findAll")
    public Result findAll(HttpServletRequest request) {
        //HttpServletRequest对象代表客户端的请求，当客户端通过HTTP协议访问服务器时，HTTP请求头中的所有信息都封装在这个对象中，开发人员通过这个对象的相关方法，即可以获得客户的这些信息。
        List<Patient> patientList = patientService.findAllByUserId(AuthContextHolder.getUserId(request));
        return Result.ok(patientList);

    }


}
