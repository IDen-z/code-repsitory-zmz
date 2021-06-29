package com.zmz.easyExcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

public class ExcelListener extends AnalysisEventListener<UserDataTest> {
    @Override
    public void invoke(UserDataTest userDataTest, AnalysisContext analysisContext) {
        System.err.println(userDataTest);
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.err.println("读取到的表头信息"+headMap);

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
