package com.zmz.springcloud.config;


import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationContextConfig {

    @Bean
    @LoadBalanced //当你在客户端修改成集群地址以后，必须开启负载均衡注解
                    //否则客户端不知道去哪个provider提供服务，会报UnknownHostException
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }


}
