package com.zmz.yygh.client.cmn;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("service-cmn")
@RequestMapping("/admin/cmn/dict")
@Component
public interface DictFeignProvider {

    /**
     * @Description: 根据dictcode和value查询name
     * @Author: Zhu Mengze
     * @Date: 2021/7/5 9:32
     */
    @GetMapping(value = "/getName/{dictCode}/{value}")
    public String getNameByDictCodeAndValue(
            @PathVariable("dictCode") String dictCode,
            @PathVariable("value") String value
    );

    /**
     * @Description: 根据value查询name
     * @Author: Zhu Mengze
     * @Date: 2021/7/5 9:39
     */
    @GetMapping(value = "/getName/{value}")
    public String getNameByDictValue(@PathVariable("value") String value);


}
