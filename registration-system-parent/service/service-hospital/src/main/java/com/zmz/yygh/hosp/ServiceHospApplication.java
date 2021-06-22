package com.zmz.yygh.hosp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//为什么加componentScan，是因为要用到common服务下的serviceUtil，虽然在pom文件中引入了依赖
//但是为了保证com.zmz.yygh.common.config在启动时也被扫描进来，需要加这个注解
@ComponentScan("com.zmz")
public class ServiceHospApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceHospApplication.class, args);
    }

}
