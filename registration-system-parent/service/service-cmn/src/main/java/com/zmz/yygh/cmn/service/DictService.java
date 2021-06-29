package com.zmz.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zmzyygh.model.cmn.Dict;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;



public interface DictService extends IService<Dict> {
    List<Dict> findChildData(Long id);

    void exportDictData(HttpServletResponse httpServletResponse);

    void importDictData(MultipartFile file);
}
