package com.zmz.springcloud.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator myRouteLocator(RouteLocatorBuilder routeLocatorBuilder){
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        //http://news.baidu.com/guonei
        routes.route("path_route_3",predicateSpec -> predicateSpec.path("/guonei").uri("http://news.baidu.com/guonei")).build();
        return routes.build();
    }

}
