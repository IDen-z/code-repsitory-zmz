package com.zmz.yygh.cmn.controller;

import com.zmz.yygh.cmn.service.DictService;
import com.zmz.yygh.common.result.Result;
import com.zmzyygh.model.cmn.Dict;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/admin/cmn/dict")
@CrossOrigin
public class DictController {

    @Autowired
    private DictService dictService;

    /**
     * @Description: 根据数字id查询出子数据的id的列表
     * @Author: Zhu Mengze
     * @Date: 2021/6/28 8:50
     */
    @GetMapping("/findChildData/{id}")
    public Result<List<Dict>> findChildData(@PathVariable("id") Long id) {
        List<Dict> res = dictService.findChildData(id);
        return Result.ok(res);
    }


    /**
    * @Description: 导出数据字典数据接口
    * @Author: Zhu Mengze
    * @Date: 2021/6/28 14:41
    */
    @GetMapping(value = "/exportData")
    public void exportDict(HttpServletResponse httpServletResponse){
        dictService.exportDictData(httpServletResponse);
    }

    /**
    * @Description: 上传excel数据进入数据字典
    * @Author: Zhu Mengze
    * @Date: 2021/6/28 16:35
    */
    @PostMapping("importData")
    public Result importData(MultipartFile file){
        dictService.importDictData(file);
        return Result.ok();
    }



}
