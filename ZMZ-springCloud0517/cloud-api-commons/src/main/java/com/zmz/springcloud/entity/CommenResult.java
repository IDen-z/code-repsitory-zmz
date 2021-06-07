package com.zmz.springcloud.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommenResult<T> {
    /*定义一个两个参数的构造器*/
    public CommenResult(Integer code, String message) {
        this(code,message,null);
    }


    private Integer code;
    private String message;
    /*泛型T就规定了，你是什么类型我就返回什么类型的result*/
    private T data;

}


