package com.zmz.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmz.yygh.cmn.listener.DictListener;
import com.zmz.yygh.cmn.mapper.DictMapper;
import com.zmz.yygh.cmn.service.DictService;
import com.zmz.yygh.common.exception.YyghException;
import com.zmz.yygh.common.result.ResultCodeEnum;
import com.zmzyygh.model.cmn.Dict;
import com.zmzyygh.vo.cmn.DictEeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Autowired
    private DictMapper dictMapper;

    @Autowired
    private DictListener dictListener;

    @Override
    @Cacheable(value = "dict", keyGenerator = "keyGenerator")
    public List<Dict> findChildData(Long id) {
        List<Dict> childDataList = dictMapper.findChildData(id);
        if (!ObjectUtils.isEmpty(childDataList)) {
            for (Dict dict : childDataList) {
                dict.setHasChildren(this.hasChildren(dict.getId()));
            }
        }
        return childDataList;
    }

    /**
     * @Description: 导出数据字典方法实现
     * 这里的HttpServletResponse是响应给客户端浏览器之前的接口
     * @Author: Zhu Mengze
     * @Date: 2021/6/28 14:45
     */
    @Override
    public void exportDictData(HttpServletResponse httpServletResponse) {
        try {
            httpServletResponse.setContentType("application/vnd.ms-excel");
            httpServletResponse.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("数据字典", "UTF-8");
            httpServletResponse.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            //查出所有的数据库中数据字典信息
            List<Dict> dictList = baseMapper.selectList(null);
            //在此之前要把这个list变成VOlist
            List<DictEeVo> dictEeVoList = new ArrayList<>();
            //遍历后进行复制
            for (Dict dict : dictList) {
                DictEeVo dictEeVo = DictEeVo.builder().id(dict.getId())
                        .parentId(dict.getParentId())
                        .name(dict.getName())
                        .value(dict.getValue())
                        .dictCode(dict.getDictCode())
                        .build();
                dictEeVoList.add(dictEeVo);
            }
            //开始利用easyExcel进行写操作
            EasyExcel.write(httpServletResponse.getOutputStream(), DictEeVo.class).sheet("数据字典").doWrite(dictEeVoList);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    //这个表示当数据库发生插入操作，需要把缓存清除
    @CacheEvict(value = "dict", allEntries = true)
    public void importDictData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), DictEeVo.class, dictListener).sheet().doRead();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //获取DICTname
    @Override
    public String getNameByDictCodeAndValue(String dictCode, String value) {
        if (ObjectUtils.isEmpty(dictCode)) {
            //如果为空就根据value查询
            return this.getNameByValue(value);
        } else {
            //否则根据两个条件查询
            //首先根据dictcode查询ID
            //查出来的id作为parentID进行查询
            QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("dict_code", dictCode);
            Dict dictByCode = baseMapper.selectOne(queryWrapper);
            if (Objects.isNull(dictByCode)) {
                throw new YyghException(ResultCodeEnum.PARAM_ERROR);
            }
            Long pid = dictByCode.getId();
            Dict dict = baseMapper.selectOne(new QueryWrapper<Dict>()
                    .eq("parent_id", pid)
                    .eq("value",value));
            return dict.getName();
        }


    }

    @Override
    public String getNameByValue(String value) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("value", value);
        Dict dict = baseMapper.selectOne(queryWrapper);
        if (Objects.isNull(dict)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        return dict.getName();
    }


    /**
     * @Description: 工具方法：判断当前id下面是否有子节点
     * @Author: Zhu Mengze
     * @Date: 2021/6/28 9:10
     */
    private boolean hasChildren(Long id) {
//        List<Dict> res = this.findChildData(id);
//        return !ObjectUtils.isEmpty(res);
        //这里不能调用本类中的findChildData方法，因为该方法中有循环，你这里只需查询即可，加快速度
        return !ObjectUtils.isEmpty(dictMapper.findChildData(id));
    }


}
