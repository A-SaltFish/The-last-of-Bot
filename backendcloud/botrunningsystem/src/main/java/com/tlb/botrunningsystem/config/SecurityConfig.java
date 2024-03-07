package com.tlb.botrunningsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        //放行所有OPTIONS选项
        return (web) -> web.ignoring().requestMatchers("/websocket/**").requestMatchers(HttpMethod.OPTIONS, "/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 关闭跨站请求防护：token的验证方式不需要开启csrf的防护
        http.csrf(AbstractHttpConfigurer::disable)
                // 不创建和使用session
                .sessionManagement(httpSecuritySessionManagementConfigurer
                        -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //权限控制，允许匿名访问的链接
                .authorizeRequests()
                .requestMatchers("/bot/add/").hasIpAddress("127.0.0.1")
                .requestMatchers(HttpMethod.OPTIONS).permitAll()
                //其他所有请求都需要登录认证
                .anyRequest().authenticated();

//        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}