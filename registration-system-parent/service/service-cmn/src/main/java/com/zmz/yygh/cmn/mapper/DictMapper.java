package com.zmz.yygh.cmn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zmzyygh.model.cmn.Dict;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface DictMapper extends BaseMapper<Dict> {



}
