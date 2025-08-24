package com.example.shopping_cart.config;

import com.example.shopping_cart.interceptor.ProductServiceInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private ProductServiceInterceptor myInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("-----addInterceptors {} ", registry);
        registry.addInterceptor(myInterceptor)
                .addPathPatterns("/Admin/**"); // Only Apply these end point APIs
//                .excludePathPatterns("/auth/**"); // allow login/signup without token
    }
}
