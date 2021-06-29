package com.zmz.easyExcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;

import java.util.ArrayList;
import java.util.List;

public class TestWrite {

    public static void main(String[] args) {
        List<UserDataTest> list=new ArrayList<>();
        list.add(UserDataTest.builder().username("小妹").uid(2).build());
        list.add(UserDataTest.builder().username("张三").uid(4).build());

        String fileName="D:\\writeTest\\test.xlsx";
        //调用方法实现写操作
        EasyExcel.write(fileName,UserDataTest.class).sheet("Sheet1")
        .doWrite(list);
    }
}
