package com.zmz.yygh.cmn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zmzyygh.model.cmn.Dict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface DictMapper extends BaseMapper<Dict> {

    //根据数字id查询出子数据的id的列表
    @Select("SELECT * FROM `dict` WHERE `parent_id`=#{id};")
    List<Dict> findChildData(Long id);



}
