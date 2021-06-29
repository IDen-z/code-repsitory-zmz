package com.zmz.easyExcel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDataTest {

    @ExcelProperty(value = "用户编号", index = 0)
    private int uid;
    @ExcelProperty(value = "用户名称", index = 1)
    private String username;

}
