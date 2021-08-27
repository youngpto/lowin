package com.young.lowin.config;

import com.young.lowin.interceptor.LowInInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created with IntelliJ IDEA.
 * Description: 配置请求拦截
 * <pre>
 *     拦截所有请求
 * </pre>
 * Author: young
 * Date: 2021-08-26
 * Time: 1:35
 */
@Configuration
@ComponentScan(basePackages = "com.young.lowin")
@RequiredArgsConstructor
public class LowInSpringWebConfig implements WebMvcConfigurer {

    private final LowInInterceptor lowInInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(lowInInterceptor).addPathPatterns("/**");
    }
}
