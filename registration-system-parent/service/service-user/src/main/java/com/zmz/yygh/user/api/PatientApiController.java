package com.zmz.yygh.user.api;

import com.zmz.yygh.common.result.Result;
import com.zmz.yygh.common.util.AuthContextHolder;
import com.zmz.yygh.user.service.PatientService;
import com.zmzyygh.model.user.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/user/patient")
public class PatientApiController {

    @Autowired
    private PatientService patientService;

    /**
     * @Description: 获取就诊人列表，就诊人是最多三个不用分页
     * @Author: Zhu Mengze
     * @Date: 2021/7/16 17:05
     */
    @GetMapping("auth/findAll")
    public Result findAll(HttpServletRequest request) {
        //HttpServletRequest对象代表客户端的请求，当客户端通过HTTP协议访问服务器时，HTTP请求头中的所有信息都封装在这个对象中，开发人员通过这个对象的相关方法，即可以获得客户的这些信息。
        List<Patient> patientList = patientService.findAllByUserId(AuthContextHolder.getUserId(request));
        return Result.ok(patientList);

    }

    /**
    * @Description: 根据ID获取就诊人信息(列表查出来以后，直接可以有ID)
    * @Author: Zhu Mengze
    * @Date: 2021/7/19 10:14
    */
    //根据id获取就诊人信息
    @GetMapping("auth/get/{id}")
    public Result getPatient(@PathVariable Long id) {
        Patient patient = patientService.getPatientId(id);
        return Result.ok(patient);
    }

    //添加就诊人
    @PostMapping("auth/save")
    public Result savePatient(@RequestBody Patient patient,HttpServletRequest request) {
        //获取当前登录用户id
        Long userId = AuthContextHolder.getUserId(request);
        patient.setUserId(userId);
        patientService.save(patient);
        return Result.ok();
    }

    //修改就诊人
    @PostMapping("auth/update")
    public Result updatePatient(@RequestBody Patient patient) {
        patientService.updateById(patient);
        return Result.ok();
    }

    //删除就诊人
    @DeleteMapping("auth/remove/{id}")
    public Result removePatient(@PathVariable Long id) {
        patientService.removeById(id);
        return Result.ok();
    }

    /**
    * @Description: 根据ID获取就诊人信息
    * @Author: Zhu Mengze
    * @Date: 2021/8/11 9:35
    */
    @GetMapping("inner/get/{id}")
    public Patient getPatientOrder(
            @PathVariable("id") Long id) {
        return patientService.getById(id);
    }





}
