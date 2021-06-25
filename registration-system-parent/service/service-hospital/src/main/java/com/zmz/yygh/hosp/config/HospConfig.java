package com.zmz.yygh.hosp.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HospConfig {

    /**
    * @Description: 分页插件
    * @Author: Zhu Mengze
    * @Date: 2021/6/24 14:53
    */
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }

}
