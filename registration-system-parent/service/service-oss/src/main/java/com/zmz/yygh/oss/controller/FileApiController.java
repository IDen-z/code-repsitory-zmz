package com.zmz.yygh.oss.controller;

import com.zmz.yygh.common.result.Result;
import com.zmz.yygh.oss.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/oss/file")
public class FileApiController {

    @Autowired
    private FileService fileService;


    /**
    * @Description: 上传文件到阿里云oss
    * @Author: Zhu Mengze
    * @Date: 2021/7/16 9:46
    */
    @PostMapping("/fileUpload")
    public Result fileUpload(MultipartFile file) {
        //获取上传文件
        String url = fileService.upload(file);
        return Result.ok(url);
    }


}
