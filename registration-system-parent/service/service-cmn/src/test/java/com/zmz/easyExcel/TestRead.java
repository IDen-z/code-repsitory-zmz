package com.zmz.easyExcel;

import com.alibaba.excel.EasyExcel;

public class TestRead {

    public static void main(String[] args) {
        String fileName="D:\\writeTest\\test.xlsx";
        EasyExcel.read(fileName,UserDataTest.class,new ExcelListener()).sheet("Sheet1").doRead();

    }


}
